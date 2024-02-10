import java.awt.Color;

public class Pallete {
    static final Color Primary = Color.decode("#0f1a2e");
    static final Color Secondary = Color.decode("#0e334e");
    static final Color Accent = Color.decode("#933a39");
    static final Color DownLight = Color.decode("#dc9261");
    static final Color background = Color.decode("#1e4159");
    static final Color highlight = Color.decode("#4389d3");

    static Color getShade(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    static Color getGradient(Color color1, Color color2, double value) {
        // from color1 to color2 by percent
        int result_red = (int)(color1.getRed() + ((value / 100) * (color2.getRed() - color1.getRed())));
        int result_green = (int)(color1.getGreen() + ((value / 100) * (color2.getGreen() - color1.getGreen())));
        int result_blue = (int)(color1.getBlue() + ((value / 100) * (color2.getBlue() - color1.getBlue())));

        // convert rgb to hex
        return Color.decode(String.format("#%02x%02x%02x", result_red, result_green, result_blue));
    }
}
