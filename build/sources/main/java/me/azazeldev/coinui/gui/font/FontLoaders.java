package me.azazeldev.coinui.gui.font;

/*
 * Decompiled with CFR 0_132.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontLoaders {

    public static CFontRenderer arial18 = new CFontRenderer(FontLoaders.getArial(18), true, true);
    public static CFontRenderer arial24 = new CFontRenderer(FontLoaders.getArial(24), true, true);
    public static CFontRenderer roboto16 = new CFontRenderer(FontLoaders.getRoboto(), true, true);


    private static Font getArial(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Arial.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }

    private static Font getRoboto() {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Roboto-Medium.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, 16);
        } catch (Exception ex) {
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, 16);
        }
        return font;
    }

}


