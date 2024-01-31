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

        paintBackground(g2);
        paintBridge(g2);
        g2.setColor(Color.WHITE);
        g.drawImage(buffer, 0, 0, null);
    }

    private void paintBridge(Graphics g) {
        g.setColor(Pallete.Primary);
        int y_start = 400;
        int frence_y_offset = 520;
        // top_2
        for (int i = 0; i < 2; i++)
        {
            Graphicer.bresenhamLine(g, 0, y_start+=10, 600, frence_y_offset, 2);
            g.setColor(Pallete.Primary.brighter());
            frence_y_offset+=10;
        }

        //low_0
        g.setColor(Pallete.Primary);
        Graphicer.bresenhamLine(g, 0, 460, 600, 550, 2);

        //stick_loop
        int x_start = 11;
        y_start = 422;
        int x_start_bot = 9;
        for (int i = 0; i < 600; i+=30) {
            Graphicer.bresenhamLine(g, x_start + i, Graphicer.y_bresenham(i + 600), x_start_bot + i, Graphicer.y_bresenham(i + 1200), 2);
        }
        Graphicer.yCoordinates.clear();
    }

    private void paintBackground(Graphics g){
        for (int i = 0; i < 100; i++) {
            g.setColor(Pallete.getGradient(Pallete.background.darker(), Pallete.background, i));
            g.fillRect(0, 6 * i, 600, 6);
        }
    }
}