import java.awt.Color;
import java.awt.Graphics;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Graphicer extends GraphicsSwing {
    static Random random = new Random(1);
    static List<Integer> yCoordinates = new ArrayList<>();

    static Integer y_bresenham(int i) {
        return yCoordinates.get(i);
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
            yCoordinates.add(y); // Store the y-coordinate

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
                            + Math.pow(t, 3) * y4),
                    1);
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
        int[] xTopPoly = { x1, x2, x1 };
        int[] yTopPoly = { y1, y1, y2 };

        int[] xBottomPoly = { x1, x2, x2 };
        int[] yBottomPoly = { y2, y1, y2 };
        Polygon topPoly = new Polygon(xTopPoly, yTopPoly, 3);
        Polygon bottomPoly = new Polygon(xBottomPoly, yBottomPoly, 3);

        g.setColor(color);
        g.fillPolygon(topPoly);
        g.fillPolygon(bottomPoly);
    }

    static void drawRect_Affine(Graphics g, int[] x, int[] y, Color color) {
        int[] xTopPoly = { x[0], x[1], x[2] };
        int[] yTopPoly = { y[0], y[1], y[2] };

        int[] xBottomPoly = { x[2], x[1], x[3] };
        int[] yBottomPoly = { y[2], y[1], y[3] };
        Polygon topPoly = new Polygon(xTopPoly, yTopPoly, 3);
        Polygon bottomPoly = new Polygon(xBottomPoly, yBottomPoly, 3);

        g.setColor(color);
        g.fillPolygon(topPoly);
        g.fillPolygon(bottomPoly);
    }

    static void drawEclipse(Graphics g, int centerX, int centerY, int width, int height, Color color) {
        g.setColor(color);
        int ellipseWidth = width / 2;
        int ellipseHeight = height / 2;

        int x = (width - ellipseWidth) / 2;
        int y = (height - ellipseHeight) / 2;

        int sides = 600; // Adjust the number of sides for smoother ellipse
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            xPoints[i] = (int) (centerX + x + ellipseWidth * Math.cos(angle));
            yPoints[i] = (int) (centerY + y + ellipseHeight * Math.sin(angle));
        }

        g.fillPolygon(xPoints, yPoints, sides);
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

    static void fourSide(Graphics g, int centerX, int centerY, int radius, Color color) {
        int sides = 4;
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

    static void drawCloudLine(Graphics g, int x1, int y1, int x2, int y2, int radius, Color color) {
        for (double j = 0; j <= 1; j += 0.05) {
            int dx = (int) ((x2 - x1) * j);
            int dy = (int) ((y2 - y1) * j);
            Graphicer.drawCircle(g, x1 + dx, y1 + dy, 15,
                    Pallete.getShade(color, 4));
        }
    }

    static void drawLightShade(Graphics g, int x1, int y1, int x2, int y2, int radius) {
        for (double j = 0; j <= 1; j += 0.02) {
            int dx = (int) ((x2 - x1) * j);
            int dy = (int) ((y2 - y1) * j);
            fourSide(g, x1 + dx, y1 + dy, 10,
                    Pallete.getShade(Pallete.Accent, 5));
        }
    }

    static void drawLightShade_1(Graphics g, int x1, int y1, int x2, int y2, int radius) {
        for (double j = 0; j <= 1; j += 0.02) {
            int dx = (int) ((x2 - x1) * j);
            int dy = (int) ((y2 - y1) * j);
            Graphicer.drawCircle(g, x1 + dx, y1 + dy, 4,
                    Pallete.getShade(Pallete.Accent, 20));
        }
    }

    static void plot(Graphics g, int x, int y, int size) {
        g.fillRect(x, y, size, size);
    }
}