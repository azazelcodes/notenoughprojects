package me.azazeldev.coinui.gui;

import me.azazeldev.coinui.Main;
import me.azazeldev.coinui.gui.font.FontLoaders;
import me.azazeldev.coinui.modules.Category;
import me.azazeldev.coinui.modules.Module;
import me.azazeldev.coinui.modules.Modules;
import me.azazeldev.coinui.utility.Element;
import me.azazeldev.coinui.utility.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import net.minecraftforge.common.MinecraftForge;

public class GUI extends GuiScreen {



    // Constructor
    public GUI() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    private boolean close = false;
    private boolean closed;


    private float dragX, dragY;


    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;

    static float windowX = 180, windowY = 50;
    static float width = 620, height = 360;

    int lastTypedKeycode;
    char lastTypedChar;
    char emptyChar;



    // Initialize the GUI
    @Override
    public void initGui() {
        super.initGui();
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;
    }

    public float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 8));
    }

    // Draw the GUI
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawWorldBackground(0);
        ScaledResolution sr = new ScaledResolution(mc);
        float outro = smoothTrans(this.outro, lastOutro);

        if (mc.theWorld != null) {
            if (mc.currentScreen == null) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(outro, outro, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            }

            // Animation
            percent = smoothTrans(this.percent, lastPercent);
            percent2 = smoothTrans(this.percent2, lastPercent2);

            if (percent > 0.98) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(percent, percent, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            } else if (percent2 <= 1) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(percent2, percent2, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            }

            if (percent <= 1.5 && close) {
                percent = smoothTrans(this.percent, 2);
                percent2 = smoothTrans(this.percent2, 2);
            }

            if (percent >= 1.4 && close) {
                percent = 1.5f;
                closed = true;
                mc.currentScreen = null;
            }
        }


        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
        } else {
            dragX = 0;
            dragY = 0;
        }

        try {
            RenderUtils.initAA();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        // Window
        RenderUtils.drawRoundedRect(windowX, windowY, windowX + width, windowY + height, 20, Theme.bodyColor.getRGB());
        Gui.drawRect((int) windowX, (int) windowY, (int) (windowX + width), (int) (windowY + 20), Theme.topColor.getRGB());

        // Categories
        // Category Line
        Gui.drawRect((int) windowX + 2, (int) windowY + 48, (int) (windowX + width)- 2, (int) (windowY + 50), Theme.categoryColor.getRGB());
        // Category Indicators
        int cats = 0;
        for (Category cat : Category.values()) {
            RenderUtils.drawRoundedRect(windowX + 30  + (cats*125), windowY + 28, windowX + 30 + 100 + (cats*115), windowY + 58, 10, Theme.categoryColor.getRGB());

            // Category Switch Handling
            if (isHovered(windowX + 32  + (cats*125), windowY + 30, windowX + 28 + 100 + (cats*115), windowY + 68, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                Main.page = cats;
            }
            // Category Cover (we draw this multiple times to cover up the current page indication)
            Gui.drawRect((int) windowX + 20, (int) windowY + 50, (int) (windowX + width - 20), (int) (windowY + 100), Theme.bodyColor.getRGB());
            // Current Indication
            if (Main.page == cats) {
                RenderUtils.drawRoundedRect(windowX + 32  + (cats*125), windowY + 30, windowX + 28 + 100 + (cats*115), windowY + 68, 10, Theme.bodyColor.getRGB());
            }

            char[] charArray = cat.toString().toLowerCase().toCharArray();
            boolean foundSpace = true;

            for (int i = 0; i < charArray.length; i++) {
                if (Character.isLetter(charArray[i])) {
                    if (foundSpace) charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                } else {
                    foundSpace = true;
                }
            }
            FontLoaders.arial24.drawString(String.valueOf(charArray), windowX + 30  + (cats*125) + (FontLoaders.arial24.getStringWidth(String.valueOf(charArray)) / 2), windowY + 32, Theme.descriptionColor.getRGB());

            cats++;
        }

        // Elements
        int mods = 0;
        for (Module module : Modules.modules) {
            if (Category.valueOf(String.valueOf(module.category)).ordinal() == Main.page) {
                if (module.type.toString() == "TOGGLE") {
                    new Element().Toggle(windowX, windowY + 76 + (76*mods), module.name, module.description, true, false, mouseX, mouseY, module.internalID);
                }
                if (module.type.toString() == "TEXT") {
                    new Element().StrInput(windowX, windowY + 76 + (76*mods), module.name, module.description, true, false, mouseX, mouseY, module.internalID, lastTypedChar, lastTypedKeycode, false, new String[]{" ", "\"", "\\"});
                }
                /*if (module.type.toString() == "INTEGER") {
                    // TODO: Figure out how to set the step, min & max in the module code!
                    //new Element().IntInput(windowX, windowY + 76 + (76*mods), module.name, module.description, true, false, mouseX, mouseY, module.internalID, );
                }*/
                mods++;
                lastTypedKeycode = -1;
                lastTypedChar = emptyChar;
            }
        }
    }

    // Handle user input
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        lastTypedKeycode = keyCode;
        lastTypedChar = typedChar;

        if (!closed && keyCode == Keyboard.KEY_ESCAPE) {
            close = true;
            mc.mouseHelper.grabMouseCursor();
            mc.inGameHasFocus = true;
            return;
        }


        if (close) {
            windowX = 400;
            windowY = 205;
            this.mc.displayGuiScreen(null);
        }

        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void actionPerformed(GuiButton button) {
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}