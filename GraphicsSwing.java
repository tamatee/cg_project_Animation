import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicsSwing extends JPanel {

    protected Random random = new Random();

    public static void main(String[] args) {
        GraphicsSwing m = new GraphicsSwing();

        JFrame f = new JFrame();
        f.add(m);
        f.setTitle("Lonely night");
        f.setSize(600, 600);
        f.setBackground(Color.BLACK);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage buffer = new BufferedImage(600, 600,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();

        for (int i = 0; i < 100; i++) {
            g2.setColor(Pallete.getGradient(Pallete.background.darker(), Pallete.background, i));
            g2.fillRect(0, 6 * i, 600, 6);
        }

        g2.setColor(Color.WHITE);
        drawCloud(g2);
        g.drawImage(buffer, 0, 0, null);
    }

    private void drawCloud(Graphics g) {
        g.setColor(Pallete.getShade(Pallete.highlight, 10));
        for (int i = 0; i < 200; i += 10) {
            int[] x = {300, 500, 520};
            int[] y = {400, 400, 400};
            Graphicer.drawPolygon(g, x, y);
        }
    }
}