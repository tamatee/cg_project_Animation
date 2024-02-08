import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsSwing extends JPanel implements Runnable {

    protected Random random = new Random();

    protected double lastTime = System.currentTimeMillis();
    protected double currentTime, elapsedTime;
    protected double startTime = lastTime;
    protected double bridgeMove = 0;
    protected double poleMove = 0;
    protected double bridge_velocity = 75;
    protected double pole_velocity = 50;
    protected double person_velocity = 30;
    protected double personMove = 0;
    protected double comet_velocity = 10;
    protected double cometMove = 0;

    public void run() {
        while (true) {
            // time initiate
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            // bridge move
            bridgeMove += bridge_velocity * elapsedTime / 10000.0;
            poleMove += pole_velocity * elapsedTime / 3000.0;
            personMove += person_velocity * elapsedTime / 3000.0;
            if (Math.abs(startTime - currentTime) >= 10000) {
                bridge_velocity = 0;
                pole_velocity = 0;
            }
            else if(Math.abs(startTime - currentTime) >= 3000) {
                person_velocity = 0;
            }

            // Display
            repaint();
        }
    }

    public static void main(String[] args) {
        GraphicsSwing m = new GraphicsSwing();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Lonely night");
        f.setSize(800, 800);
        f.setBackground(Color.BLACK);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        (new Thread(m)).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage buffer = new BufferedImage(600, 600,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();

        paintBackground(g2);
        drawCloud(g2);
        drawComet(g2);
        drawStar(g2);
        drawElectricPole(g2);
        paintObject(g2);;
        paintBridge(g2);
        g2.setColor(Color.WHITE);
        g.drawImage(buffer, 0, 0, null);
    }

    private void paintObject(Graphics2D g)
    {

        g.translate(personMove, 0);
        int[] xPointTorso = {115, 150, 105, 140};
        int[] yPointTorso = {430, 435, 500, 505};

        int[] xPointHead = {121, 154, 116, 145};
        int[] yPointHead = {395, 400, 450, 457};
        g.rotate(0.06);
        Graphicer.drawEclipse(g, 163, 380, 35, 50, Pallete.Primary);
        Graphicer.drawEclipse(g, 163, 412, 37, 20, Pallete.Primary);
        g.rotate(-0.09);
        Graphicer.drawRect_Affine(g, xPointTorso, yPointTorso, Pallete.Primary);
        Graphicer.drawRect_Affine(g, xPointHead, yPointHead, Pallete.Primary);
        g.translate(-personMove, 0);
    }

    private void paintBridge(Graphics2D g) {
        g.setColor(Pallete.Primary);
        int y_start = 350;
        int frence_y_offset = 470;
        g.translate(0, bridgeMove);

        //Light_pole
        Graphicer.drawCircle(g, 495, 425, 15, Pallete.Primary);
        int x_poleBase[] = { 483, 505, 478, 510 };
        int y_poleBase[] = { 425, 425, 500, 520 };
        Graphicer.drawRect_Affine(g, x_poleBase, y_poleBase, Pallete.Primary);
        Graphicer.bresenhamLine(g, 492, 450, 490, 110, 8);
        Graphicer.bresenhamLine(g, 490, 105, 418, 130, 5);
        Graphicer.yCoordinates.clear();
        Graphicer.drawCircle(g, 494, 110, 5, Pallete.Primary);
        Graphicer.drawEclipse(g, 415, 135, 30, 15, Pallete.DownLight);

        // top_2
        g.setColor(Pallete.Primary);
        for (int i = 0; i < 2; i++) {
            Graphicer.bresenhamLine(g, 0, y_start += 10, 600, frence_y_offset, 5);
            frence_y_offset += 10;
        }

        // low_0
        g.setColor(Pallete.Primary);
        Graphicer.bresenhamLine(g, 0, 410, 600, 500, 5);

        // stick_loop
        int x_start = 11;
        y_start = 372;
        int x_start_bot = 9;
        for (int i = 0; i < 600; i += 30) {
            if (i % 180 == 0)
                Graphicer.bresenhamLine(g, x_start + i, Graphicer.y_bresenham(i + 600) - 30, x_start_bot + i,
                        Graphicer.y_bresenham(i + 1200) + 50, 6);
            else
                Graphicer.bresenhamLine(g, x_start + i, Graphicer.y_bresenham(i + 600), x_start_bot + i,
                        Graphicer.y_bresenham(i + 1200), 4);
        }
        Graphicer.yCoordinates.clear();

        // Bridge_pillar0
        int x_pillarB[] = { 0, 600, 0, 600 };
        int y_pillarB[] = { 435, 511, 900, 900 };

        Graphicer.drawRect_Affine(g, x_pillarB, y_pillarB, Pallete.Secondary);
        // Bridge_pillar1
        int x_pillar1[] = { 80, 140, 70, 135 };
        int y_pillar1[] = { 490, 500, 900, 900 };

        // Bridge_pillar2
        int x_pillar2[] = { 260, 320, 250, 310 };
        int y_pillar2[] = { 510, 515, 900, 900 };

        // Bridge_pillar3
        int x_pillar3[] = { 417, 461, 415, 460 };
        int y_pillar3[] = { 530, 535, 900, 900 };

        int x_pillar[][] = { x_pillar1, x_pillar2, x_pillar3 };
        int y_pillar[][] = { y_pillar1, y_pillar2, y_pillar3 };

        for (int i = 0; i < y_pillar.length; i++) {
            Graphicer.drawRect_Affine(g, x_pillar[i], y_pillar[i], Pallete.Primary);
        }
        // Bridge_base floor
        int x_baseBridge[] = { 0, 600, 0, 600 };
        int y_baseBridge[] = { 435, 511, 490, 550 };
        Graphicer.drawRect_Affine(g, x_baseBridge, y_baseBridge, Pallete.Primary);
    }

    private void drawComet(Graphics g) {
        g.setColor(Pallete.getShade(Pallete.highlight, 15));
        for (int i = 0; i < 200; i += 10) {
            int[] x = { 245, 240, 450 + i };
            int[] y = { 280, 285, 210 - (int) (i * 0.5) };
            Graphicer.drawPolygon(g, x, y);
        }
    }

    private void drawCloud(Graphics g) {
        int[] x1 = { 300, 330, 350, 360 };
        int[] y1 = { 400, 375, 340, 325 };
        int[] x2 = { 440, 450, 470, 530 };
        int[] y2 = { 500, 470, 460, 470 };
        int[] posX = { -60, 100, 70, -120, -330, 40, 50, 200 };
        int[] posY = { 20, -40, -60, 50, -200, -100, -70, -20 };
        for (int k = 0; k < posX.length; k++) {
            x1 = transfromArray(x1, +(posX[k]));
            y1 = transfromArray(y1, +(posY[k]));
            x2 = transfromArray(x2, +(posX[k]));
            y2 = transfromArray(y2, +(posY[k]));
            for (int i = 0; i < x1.length; i++)
                Graphicer.drawCloudLine(g, x1[i], y1[i], x2[i], y2[i], 15);
        }
    }

    private int[] transfromArray(int[] a, int value) {
        int[] newA = new int[a.length];
        for (int i = 0; i < a.length; i++)
            newA[i] = a[i] + value;
        return newA;
    }

    private void drawStar(Graphics g) {
        int[] x = { 20, 70, 110, 65, 135, 280, 330, 385, 8, 25, 15,
                50, 360, 370, 410, 440, 445, 460, 550, 540, 580,
                560, 550, 570, 580 };
        int[] y = { 40, 20, 40, 82, 45, 30, 10, 35, 300, 320, 380,
                440, 420, 430, 330, 410, 420, 460, 380, 390, 400,
                310, 40, 20, 60 };
        for (int i = 0; i < x.length; i++)
            Graphicer.drawCircle(g, x[i], y[i], 2, Pallete.highlight);
    }

    private void drawElectricPole(Graphics2D g) {
        g.translate(0, poleMove);
        g.setColor(Pallete.Primary);
        Graphicer.bresenhamLine(g, 235, 390, 221, 520, 3);
        Graphicer.bresenhamLine(g, 220, 400, 250, 405, 3);
        Graphicer.bresenhamLine(g, 220, 410, 250, 415, 3);
        Graphicer.bezierCurve(g, 220, 400, 340, 400, 450, 420, 600, 320);
        Graphicer.bezierCurve(g, 250, 405, 340, 410, 450, 430, 600, 350);
        Graphicer.bezierCurve(g, 250, 415, 340, 420, 450, 440, 600, 380);
        Graphicer.yCoordinates.clear();
    }

    private void paintBackground(Graphics g) {
        for (int i = 0; i < 100; i++) {
            g.setColor(Pallete.getGradient(Pallete.background.darker(), Pallete.background, i));
            g.fillRect(0, 6 * i, 600, 6);
        }
    }
}