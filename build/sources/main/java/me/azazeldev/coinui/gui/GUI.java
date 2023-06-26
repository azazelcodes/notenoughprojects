package me.azazeldev.coinui.gui;

import me.azazeldev.coinui.Main;
import me.azazeldev.coinui.gui.elements.Element;
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
    private boolean drag = false;


    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;

    static float windowX = 180, windowY = 50;
    static float width = 620, height = 360;



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



        // Dragging by thvf0 (Yes VapeLiteGUI :D)

        if (isHovered(windowX, windowY, windowX + width, windowY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
            drag = true;
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
        Gui.drawRect((int) windowX, (int) windowY, (int) (windowX + width), (int) (windowY + 25), Theme.topColor.getRGB());

        // Categories
        // Category Line
        Gui.drawRect((int) windowX + 2, (int) windowY + 48, (int) (windowX + width)- 2, (int) (windowY + 50), Theme.categoryColor.getRGB());
        // Category Indicators
        RenderUtils.drawRoundedRect(windowX + 30, windowY + 28, windowX + 30 + 100, windowY + 58, 10, Theme.categoryColor.getRGB());
        RenderUtils.drawRoundedRect(windowX + 30 + 25 + 100, windowY + 28, windowX + 30 + 100 + 100 + 15, windowY + 58, 10, Theme.categoryColor.getRGB());
        // Category Cover
        Gui.drawRect((int) windowX + 20, (int) windowY + 50, (int) (windowX + width - 20), (int) (windowY + 100), Theme.bodyColor.getRGB());

        // Category Change Handling
        if (isHovered(windowX + 32, windowY + 30, windowX + 28 + 100, windowY + 68, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            Main.page = 1;
        }
        if (isHovered(windowX + 32 + 25 + 100, windowY + 30, windowX + 28 + 100 + 15 + 100, windowY + 68, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            Main.page = 2;
        }

        // Current Indicator
        if (Main.page == 1) {
            RenderUtils.drawRoundedRect(windowX + 32, windowY + 30, windowX + 28 + 100, windowY + 68, 10, Theme.bodyColor.getRGB());
        }
        if (Main.page == 2) {
            RenderUtils.drawRoundedRect(windowX + 32 + 25 + 100, windowY + 30, windowX + 28 + 100 + 15 + 100, windowY + 68, 10, Theme.bodyColor.getRGB());
        }

        // Elements
        if (Main.page == 1) {
            new Element().Toggle(windowX, windowY + 76, "Flipper", "Toggles NEC's flipper on or off.", true, false, mouseX, mouseY, "flipper");
            new Element().Toggle(windowX, windowY + 126, "Flip Sounds", "Whether to send a sound when receiving a new batch of flips.", true, false, mouseX, mouseY, "sounds");
            new Element().IntInput(windowX, windowY + 176, "Min. Profit", "How much profit is needed for a flip to show.", true, false, mouseX, mouseY, "minprofit", 0, 5000000, 100);
            new Element().IntInput(windowX, windowY + 276, "Min. Profit %", "Needed % of the item's cost as profit for a flip to show.", true, false, mouseX, mouseY, "minpercent", 0, 10, 1);
            new Element().IntInput(windowX, windowY + 226, "Min. Demand", "Minimum amount of sales needed for a flip to show.", true, false, mouseX, mouseY, "mindemand", 1, 10, 1);
        }
        if (Main.page == 2) {
            new Element().Toggle(windowX, windowY + 76, "One-Click Buy", "Buy listing with one click.", true, false, mouseX, mouseY, "oneclickbuy");
            new Element().Toggle(windowX, windowY + 126, "Page Flipper", "Find flips before the API refresh.", true, false, mouseX, mouseY, "pageflipper");
            new Element().Toggle(windowX, windowY + 176, "Auto Buy", "Automatically buys flips for you.", true, false, mouseX, mouseY, "autobuy");
            new Element().Toggle(windowX, windowY + 226, "Full AFK", "Buys flips and lists them on its own.", true, false, mouseX, mouseY, "fullafk");
            new Element().Toggle(windowX, windowY + 276, "High Ping Mode", "Tries to buy the 2nd best flip first.", true, false, mouseX, mouseY, "hpm");
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {

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

    // Handle user input
    @Override
    protected void actionPerformed(GuiButton button) {
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}