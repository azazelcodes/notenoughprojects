package me.azazeldev.coinui.utility;

import me.azazeldev.coinui.Main;
import me.azazeldev.coinui.gui.Theme;
import me.azazeldev.coinui.gui.font.FontLoaders;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Element {
    int lastRememberedKeycode;

    public void Empty(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY) {
        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        RenderUtils.drawRoundedRect(posX, posY, posX + 350, posY + 45, 10, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + 280, posY, posX + 350, posY + 45, 10, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + 50, posY + 16, Theme.highlightColorB.getRGB());

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 120);
        for (String e : splits.subList(0, Math.min(3, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 50 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 10 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
        }
    }

    public static void Toggle(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName) {
        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        boolean isHovered = isHovered(posX + 303, posY + 11.25f, posX + 327, posY + 31.5f, mouseX, mouseY);
        boolean configValue = Config.getConfig().get(configValueName).getAsBoolean();
        boolean justClicked = Main.justClicked;

        if (isHovered && Mouse.isButtonDown(0) && !justClicked) {
            Main.justClicked = true;
            Config.write(configValueName, Config.gson.toJsonTree(!configValue));
        }

        if (isHovered && !Mouse.isButtonDown(0) && justClicked) {
            Main.justClicked = false;
        }

        RenderUtils.drawRoundedRect(posX, posY, posX + 350, posY + 45, 10, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + 280, posY, posX + 350, posY + 45, 10, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + 50, posY + 16, Theme.highlightColorB.getRGB());

        int highlightColor = configValue ? Theme.highlightColorD.getRGB() : Theme.highlightColorA.getRGB();

        RenderUtils.drawCircle(posX + 303, posY + 21.25f, 10, highlightColor);
        RenderUtils.drawCircle(posX + 327, posY + 21.25f, 10, highlightColor);
        RenderUtils.drawRect(posX + 303, posY + 11f, posX + 327, posY + 31.75f, highlightColor);

        RenderUtils.drawCircle(posX + 280 + (configValue ? 23f : 47f), posY + 21f, 7.5f, Theme.highlightColorB.getRGB());

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 120);
        for (String e : splits.subList(0, Math.min(3, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 50 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 10 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
        }
    }

    public void StrInput(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName, char lastTypedChar, int lastTypedKeycode, boolean onlyAlphabetical, String[] exclusions) {
        String currentValue = Config.getConfig().get(configValueName).getAsString();

        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        RenderUtils.drawRoundedRect(posX, posY, posX + 350, posY + 45, 10, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + 225, posY, posX + 350, posY + 45, 10, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + 10, posY + 16, Theme.highlightColorB.getRGB());

        // Draw Input
        RenderUtils.drawRoundedRect(posX + 250, posY + 11.875f, posX + 330, posY + 34.875f, 1, Theme.categoryColor.getRGB());

        List<String> exclusionList = Arrays.asList(exclusions);

        // Handle new chars
        // https://minecraft.fandom.com/wiki/Key_codes#Keyboard_codes CHECK THIS BEFORE DOING ANYTHING
        if (lastTypedKeycode == 29) {
            lastRememberedKeycode = 29;
        } else if (lastTypedKeycode == 47 && lastRememberedKeycode == 29) {
            paste();
        } else if (lastTypedKeycode == 46 && lastRememberedKeycode == 29) {
            copy();
        } else if (lastTypedKeycode != -1) {
            lastRememberedKeycode = -1;
        }

        if (lastTypedKeycode != -1 && lastTypedKeycode != 184 && lastTypedKeycode != 56 && lastTypedKeycode != 29 && lastTypedKeycode != 24) {
            System.out.println("Keycode: "+lastTypedKeycode);
            if (onlyAlphabetical) {
                if (Character.isAlphabetic(lastTypedChar) || exclusionList.contains(Character.toString(lastTypedChar))) {
                    Config.write(configValueName, Config.gson.toJsonTree(currentValue + lastTypedChar));
                }
            } else if (!exclusionList.contains(Character.toString(lastTypedChar))) {
                Config.write(configValueName, Config.gson.toJsonTree(currentValue + lastTypedChar));
            }
        }

        currentValue = Config.getConfig().get(configValueName).getAsString();
        if (lastTypedKeycode == 14 && !currentValue.equals("")) {
            Config.write(configValueName, Config.gson.toJsonTree(currentValue.substring(0, currentValue.length() - 1)));
        }

        if (lastTypedKeycode == 211 && !currentValue.equals("")) {
            Config.write(configValueName, Config.gson.toJsonTree(""));
        }

        // Draw current value
        currentValue = Config.getConfig().get(configValueName).getAsString();
        if (!currentValue.equals("")) {
            String shownValue = currentValue;
            System.out.println(shownValue);
            System.out.println(""+(FontLoaders.arial18.getStringWidth(shownValue) + posX));
            System.out.println(""+(posX + 100));
            while (FontLoaders.arial18.getStringWidth(shownValue) + posX > posX + 70) {
                System.out.println(shownValue);
                if (isHovered(posX + 255, posY + 11, posX + 325, posY + 32.75f, mouseX, mouseY)) {
                    shownValue = shownValue.substring(0, shownValue.length() - 1);
                } else {
                    shownValue = shownValue.substring(1);
                }
            }
            FontLoaders.arial18.drawString(shownValue, posX + 255, posY + 18.875f, Theme.descriptionColor.getRGB());
        } else {
            FontLoaders.arial18.drawString("Enter "+title+"!", posX + 255, posY + 18.875f, Theme.descriptionColor.getRGB());
        }

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 160);
        for (String e : splits.subList(0, Math.min(4, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 10 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 5 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
        }
    }
    public void IntInput(float posX, float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName, int min, int max, int step) {
        max = max + 1;

        // Adjust position
        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        // Draw background rectangles
        RenderUtils.drawRoundedRect(posX, posY, posX + 350, posY + 45, 10, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + 230, posY, posX + 350, posY + 45, 10, Theme.overlayColor.getRGB());

        // Draw title
        FontLoaders.arial24.drawString(title, posX + 50, posY + 16, Theme.highlightColorB.getRGB());

        // Draw bar
        float barStartX = posX + 253;
        float barEndX = posX + 327;
        float barCenterY = posY + 21.25f;
        RenderUtils.drawCircle(barStartX, barCenterY, 10, Theme.highlightColorA.getRGB());
        RenderUtils.drawCircle(barEndX, barCenterY, 10, Theme.highlightColorA.getRGB());
        RenderUtils.drawRect(barStartX, posY + 11f, barEndX, posY + 31.75f, Theme.highlightColorA.getRGB());

        // Calculate slider position
        float sliderPos = (posX + 245) + (((Config.getConfig().get(configValueName).getAsInt() - min) / (float) (max - min)) * 88f);

        // Draw slider
        float sliderRadius = 2.5f;
        RenderUtils.drawCircle(sliderPos, posY + 9.5f, sliderRadius, Theme.highlightColorB.getRGB());
        RenderUtils.drawCircle(sliderPos, posY + 37f, sliderRadius, Theme.highlightColorB.getRGB());
        RenderUtils.drawRect((sliderPos - sliderRadius), posY + 9.5f, (sliderPos + 2.25f), posY + 37f, Theme.highlightColorA.getRGB());

        int newValue = (int) ((((sliderPos + (mouseX - sliderPos)) - (posX + 245)) / (88f / (max - min))) * step) + min;
        // Update config value on click
        if (isHovered(posX + 240, posY, posX + 350, posY + 45, mouseX, mouseY) && Mouse.isButtonDown(0) && newValue < max) {
            Config.write(configValueName, Config.gson.toJsonTree(newValue));
        }

        // Draw current value
        int currentValue = Config.getConfig().get(configValueName).getAsInt();
        String formattedValue = MainUtils.formatNumber(currentValue);
        int valueWidth = FontLoaders.arial18.getStringWidth(formattedValue);
        FontLoaders.arial18.drawString(formattedValue, posX + 335 - valueWidth, posY + 35, Theme.descriptionColor.getRGB());

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 120);
        for (String e : splits.subList(0, Math.min(3, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 50 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 10 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    private void copy() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String text = "value";
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
    }

    private void paste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor = DataFlavor.stringFlavor;
        if (clipboard.isDataFlavorAvailable(flavor)) {
            try {
                String text = (String) clipboard.getData(flavor);
                System.out.println(text);
            } catch (UnsupportedFlavorException | IOException e) {
                System.out.println(e);
            }
        }
    }
}
