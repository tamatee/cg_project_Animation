import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Graphicer extends GraphicsSwing{
    static Random random = new Random(1);

    static void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;

        if (dy > dx) {
            int temp = dx;
            dx = dy;
            dy = temp;
            isSwap = true;
        }
        int D = 2 * dy - dx;
        int x = x1;
        int y = y1;

        for (int i = 1; i < dx; i++) {
            plot(g, x, y);
            if (D >= 0) {
                if (isSwap)
                    x += sx;
                else
                    y += sy;
                D -= 2 * dx;
            }
            if (isSwap)
                y += sy;
            else
                x += sx;
            D += 2 * dy;
        }
    }

    static void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2, int size) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;

        if (dy > dx) {
            int temp = dx;
            dx = dy;
            dy = temp;
            isSwap = true;
        }
        int D = 2 * dy - dx;
        int x = x1;
        int y = y1;

        for (int i = 1; i < dx; i++) {
            plot(g, x, y, size);
            if (D >= 0) {
                if (isSwap)
                    x += sx;
                else
                    y += sy;
                D -= 2 * dx;
            }
            if (isSwap)
                y += sy;
            else
                x += sx;
            D += 2 * dy;
        }
    }

    static void bezierCurve(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        for (int i = 0; i < 1000; i++) {
            float t = i / 1000.0f;
            plot(
                    g,
                    (int) (Math.pow(1 - t, 3) * x1
                            + 3 * t * Math.pow(1 - t, 2) * x2
                            + 3 * t * t * (1 - t) * x3
                            + Math.pow(t, 3) * x4),
                    (int) (Math.pow(1 - t, 3) * y1
                            + 3 * t * Math.pow(1 - t, 2) * y2
                            + 3 * t * t * (1 - t) * y3
                            + Math.pow(t, 3) * y4));
        }
    }

    static BufferedImage floodFill(BufferedImage m, int x, int y, Color targetColor, Color replacementColor) {
        Queue<NodeCoordinate> q = new LinkedList<>();
        coloredPlot(m.getGraphics(), x, y, replacementColor);
        q.add(new NodeCoordinate(x, y));
        while (!q.isEmpty()) {
            NodeCoordinate cur = q.poll();
            try {
                // South
                if (m.getRGB(cur.getX(), cur.getY() + 1) == targetColor.getRGB()) {
                    coloredPlot(m.getGraphics(), cur.getX(), cur.getY() + 1, replacementColor);
                    q.add(new NodeCoordinate(cur.getX(), cur.getY() + 1));
                }
                // North
                if (m.getRGB(cur.getX(), cur.getY() - 1) == targetColor.getRGB()) {
                    coloredPlot(m.getGraphics(), cur.getX(), cur.getY() - 1, replacementColor);
                    q.add(new NodeCoordinate(cur.getX(), cur.getY() - 1));
                }
                // West
                if (m.getRGB(cur.getX() - 1, cur.getY()) == targetColor.getRGB()) {
                    coloredPlot(m.getGraphics(), cur.getX() - 1, cur.getY(), replacementColor);
                    q.add(new NodeCoordinate(cur.getX() - 1, cur.getY()));
                }
                // East
                if (m.getRGB(cur.getX() + 1, cur.getY()) == targetColor.getRGB()) {
                    coloredPlot(m.getGraphics(), cur.getX() + 1, cur.getY(), replacementColor);
                    q.add(new NodeCoordinate(cur.getX() + 1, cur.getY()));
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
        return m;
    }

    static void coloredPlot(Graphics g, int x, int y, Color c) {
        g.setColor(c);
        g.fillRect(x, y, 1, 1);
        g.setColor(Color.BLACK);
    }

    static class NodeCoordinate {
        private int x;
        private int y;

        NodeCoordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    static void drawPolygon(Graphics g, int[] x, int[] y) {
        Polygon poly = new Polygon(x, y, x.length);
        g.fillPolygon(poly);
    }

    static void drawRect(Graphics g, int x1, int y1, int x2, int y2, Color color) {
        int[] xTopPoly = { x1, x2, x1};
        int[] yTopPoly = { y1, y1, y2};

        int[] xBottomPoly = {x1, x2, x2};
        int[] yBottomPoly = {y2, y1, y2};
        Polygon topPoly = new Polygon(xTopPoly, yTopPoly, 3);
        Polygon bottomPoly = new Polygon(xBottomPoly, yBottomPoly, 3);

        g.setColor(color);
        g.fillPolygon(topPoly);
        g.fillPolygon(bottomPoly);
    }

    static void drawCircle(Graphics g, int centerX, int centerY, int radius, Color color) {
        int sides = 360;
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {
            double theta = 2 * Math.PI * i / sides;
            xPoints[i] = (int) (centerX + radius * Math.cos(theta));
            yPoints[i] = (int) (centerY + radius * Math.sin(theta));
        }

        Polygon circle = new Polygon(xPoints, yPoints, sides);
        g.setColor(color);
        g.fillPolygon(circle);
    }

    static void FireworksLayer(Graphics2D g, int centerX, int centerY, int line, int min, int max, int offset, Color color, int width) {
        int[] len = new int[line];

        for (int i = 0; i < line; i++) {
            int size = random.nextInt(min, max);
            len[i] = size;
        }

        g.setColor(color);
        for (int j = 0; j < len.length; j += offset) {
            bresenhamLine(
                    g, centerX + offset, centerY + offset,
                    (int) (centerX + len[j] * Math.cos(2 * Math.PI * j / len[j])),
                    (int) (centerY + len[j] * Math.sin(2 * Math.PI * j / len[j])),
                    width);
        }
    }

    static void plot(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    static void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x, y, size, size);
    }
}