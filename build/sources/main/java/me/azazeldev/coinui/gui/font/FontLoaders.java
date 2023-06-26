package me.azazeldev.coinui.gui.font;

/*
 * Decompiled with CFR 0_132.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontLoaders {

    private static HashMap fonts = new HashMap();

    public static CFontRenderer arial16 = new CFontRenderer(FontLoaders.getArial(16), true, true);
    public static CFontRenderer arial18 = new CFontRenderer(FontLoaders.getArial(18), true, true);
    public static CFontRenderer arial20 = new CFontRenderer(FontLoaders.getArial(20), true, true);
    public static CFontRenderer arial22 = new CFontRenderer(FontLoaders.getArial(22), true, true);
    public static CFontRenderer arial24 = new CFontRenderer(FontLoaders.getArial(24), true, true);
    public static CFontRenderer arial16B = new CFontRenderer(FontLoaders.getArialBold(16), true, true);
    public static CFontRenderer arial20B = new CFontRenderer(FontLoaders.getRoboto(20), true, true);
    public static CFontRenderer roboto16 = new CFontRenderer(FontLoaders.getRoboto(16), true, true);
    private static Font getDefault(int size) {
        return new Font("default", 0, size);
    }


    private static Font getArial(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Arial.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getArialBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/ArialBold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getRoboto(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Roboto-Medium.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

}


