package dev.faiths.ui.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BaseFontRender;
import net.optifine.util.FontUtils;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class FontManager {
    private static final HashMap<String, Font> loadedFonts = new HashMap<>();
    public static HashMap<String, BaseFontRender> fonts = new HashMap<>();

    public static BaseFontRender getFont(String name) {
        return fonts.get(name.toLowerCase(Locale.ROOT));
    }

    public static CustomFont sf40;
    public static CustomFont sf24;
    public static CustomFont sf20;
    public static CustomFont sf18;
    public static CustomFont sf15;
    public static CustomFont sf14;
    public static CustomFont sf12;
    public static CustomFont bold20;
    public static CustomFont bold19;
    public static CustomFont bold18;
    public static CustomFont bold15;
    public static CustomFont bold14;
    public static CustomFont bold13;
    public static CustomFont comfortaa20;
    public static CustomFont comfortaa18;
    public static CustomFont robotolight20;
    public static CustomFont robotolight18;
    public static CustomFont robotoregular20;
    public static CustomFont robotoregular18;
    public static CustomFont robotoregular15;

    public static void init() {
        sf40 = new CustomFont(getFont("SFUI.ttf", 40));
        sf24 = new CustomFont(getFont("SFUI.ttf", 24));
        sf20 = new CustomFont(getFont("SFUI.ttf", 20));
        sf18 = new CustomFont(getFont("SFUI.ttf", 18));
        sf15 = new CustomFont(getFont("SFUI.ttf", 15));
        sf14 = new CustomFont(getFont("SFUI.ttf", 14));
        bold20 = new CustomFont(getFont("Bold.ttf", 20));
        bold19 = new CustomFont(getFont("Bold.ttf", 19));
        bold18 = new CustomFont(getFont("Bold.ttf", 18));
        bold15 = new CustomFont(getFont("Bold.ttf", 15));
        bold14 = new CustomFont(getFont("Bold.ttf", 14));
        bold13 = new CustomFont(getFont("Bold.ttf", 13));
        comfortaa20 = new CustomFont(getFont("Comfortaa.ttf", 20));
        comfortaa18 = new CustomFont(getFont("Comfortaa.ttf", 18));
        robotolight20 = new CustomFont(getFont("RobotoLight.ttf", 20));
        robotolight18 = new CustomFont(getFont("RobotoLight.ttf", 18));
        robotoregular20 = new CustomFont(getFont("RobotoRegular.ttf", 20));
        robotoregular18 = new CustomFont(getFont("RobotoRegular.ttf", 18));
        robotoregular15 = new CustomFont(getFont("RobotoRegular.ttf", 15));
        fonts.put("minecraft", Minecraft.getMinecraft().fontRendererObj);
    }

    public static Font getFont(String fontName, float size) {
        return getFont(fontName, Font.PLAIN, size);
    }

    public static Font getFont(String fontName, int type, float size) {
        Font font;

        try {
            InputStream is = FontUtils.class.getResourceAsStream("/assets/minecraft/client/font/" + fontName);
            font = Font.createFont(type, Objects.requireNonNull(is));
            font = font.deriveFont(type, size);
        } catch (Exception ex) {
            System.out.println("Error while loading font " + fontName + " - " + size + "!");
            font = new Font("Arial", type, 0).deriveFont(size);
        }

        return font;
    }

}
