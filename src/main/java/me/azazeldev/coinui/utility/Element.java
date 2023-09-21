package me.azazeldev.coinui.utility;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        RenderUtils.drawRoundedRect(posX + 225, posY, posX + 350, posY + 45, 10, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + 10, posY + 16, Theme.highlightColorB.getRGB());

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 160);
        for (String e : splits.subList(0, Math.min(4, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 10 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 5 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
        }
    }

    public static void Toggle(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName) {
        if (Config.getConfig().get(configValueName) == null) {
            System.out.println("GUI ERROR: "+configValueName+" does not exist in the user's config. Try deleting the config file in %appdata%/.minecraft/NotEnoughCoins/");
        }

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

        float objectLength = 350;
        float objectHeight = 45;
        int objectRoundness = 10;
        float overlayLength = 70;
        int overlayRoundness = 10;
        float titleXoffset = 10;
        float titleYoffset = 16;

        RenderUtils.drawRoundedRect(posX, posY, posX + objectLength, posY + objectHeight, objectRoundness, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + (objectLength-overlayLength), posY, posX + objectLength, posY + objectHeight, overlayRoundness, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + titleXoffset, posY + titleYoffset, Theme.highlightColorB.getRGB());

        int highlightColor = configValue ? Theme.highlightColorD.getRGB() : Theme.highlightColorA.getRGB();

        RenderUtils.drawCircle(posX + 303, posY + 21.25f, 10, highlightColor);
        RenderUtils.drawCircle(posX + 327, posY + 21.25f, 10, highlightColor);
        RenderUtils.drawRect(posX + 303, posY + 11f, posX + 327, posY + 31.75f, highlightColor);

        RenderUtils.drawCircle(posX + 280 + (configValue ? 23f : 47f), posY + 21f, 7.5f, Theme.highlightColorB.getRGB());

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 160);
        for (String e : splits.subList(0, Math.min(4, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 10 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 5 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
        }
    }

    int marqueeLength = 0;
    public void TextInput(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName, char lastTypedChar, int lastTypedKeycode, boolean onlyAlphabetical, String[] exclusions) {
        if (Config.getConfig().get(configValueName) == null) {
            System.out.println("GUI ERROR: "+configValueName+" does not exist in the user's config. Try deleting the config file in %appdata%/.minecraft/NotEnoughCoins/");
        }

        String currentValue = Config.getConfig().get(configValueName).getAsString();

        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        float objectLength = 350;
        float objectHeight = 45;
        int objectRoundness = 10;
        float overlayLength = 125;
        int overlayRoundness = 10;
        float titleXoffset = 10;
        float titleYoffset = 16;

        RenderUtils.drawRoundedRect(posX, posY, posX + objectLength, posY + objectHeight, objectRoundness, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + (objectLength-overlayLength), posY, posX + objectLength, posY + objectHeight, overlayRoundness, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + titleXoffset, posY + titleYoffset, Theme.highlightColorB.getRGB());

        // Draw Input
        // TODO: Have not done y variables yet cuz im confusion
        float startOffset = 25;
        float inputLength = 80;
        RenderUtils.drawRoundedRect(posX + ((objectLength-overlayLength)+startOffset), posY + 11.875f, posX + (((objectLength-overlayLength)+startOffset)+inputLength), posY + 34.875f, 1, Theme.categoryColor.getRGB());

        List<String> exclusionList = Arrays.asList(exclusions);

        // Handle new chwars
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

        if (lastTypedKeycode != -1 && lastTypedKeycode != 184 && lastTypedKeycode != 56 && lastTypedKeycode != 29 && lastTypedKeycode != 14) {
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
            while (FontLoaders.arial18.getStringWidth(shownValue) + posX > posX + 70) {
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
    public void IntegerInput(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName, char lastTypedChar, int lastTypedKeycode) {
        if (Config.getConfig().get(configValueName) == null) {
            System.out.println("GUI ERROR: "+configValueName+" does not exist in the user's config. Try deleting the config file in %appdata%/.minecraft/NotEnoughCoins/");
        }
        float min = (float) Config.getConfig().get(configValueName).getAsJsonObject().get("min").getAsDouble();
        float max = (float) Config.getConfig().get(configValueName).getAsJsonObject().get("max").getAsDouble();
        int step = Config.getConfig().get(configValueName).getAsJsonObject().get("step").getAsInt();

        double currentValue = Math.round(Config.getConfig().get(configValueName).getAsJsonObject().get("value").getAsDouble());

        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        float objectLength = 350;
        float objectHeight = 45;
        int objectRoundness = 10;
        float overlayLength = 125;
        int overlayRoundness = 10;
        float titleXoffset = 10;
        float titleYoffset = 16;

        RenderUtils.drawRoundedRect(posX, posY, posX + objectLength, posY + objectHeight, objectRoundness, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + (objectLength-overlayLength), posY, posX + objectLength, posY + objectHeight, overlayRoundness, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + titleXoffset, posY + titleYoffset, Theme.highlightColorB.getRGB());

        // Draw Input
        // TODO: Have not done y variables yet cuz im confusion
        float startOffset = 25;
        float inputLength = 80;
        RenderUtils.drawRoundedRect(posX + ((objectLength-overlayLength)+startOffset), posY + 11.875f, posX + (((objectLength-overlayLength)+startOffset)+inputLength), posY + 34.875f, 1, Theme.categoryColor.getRGB());

        // Handle new chwars
        // https://minecraft.fandom.com/wiki/Key_codes#Keyboard_codes CHECK THIS BEFORE DOING ANYTHING
        if (lastTypedKeycode == 29) {
            lastRememberedKeycode = 29;
        } else if (lastTypedKeycode != -1) {
            lastRememberedKeycode = -1;
        }

        if (lastTypedKeycode != -1 && lastTypedKeycode != 184 && lastTypedKeycode != 56 && lastTypedKeycode != 29 && lastTypedKeycode != 14) {
            if (Character.isDigit(lastTypedChar) && Integer.parseInt(Double.toString(currentValue)+ lastTypedChar) < max) {
                Config.write(configValueName, Config.gson.toJsonTree(currentValue + lastTypedChar));
            }
        }
        currentValue = Math.round(Config.getConfig().get(configValueName).getAsJsonObject().get("value").getAsDouble());
        if (lastTypedKeycode == 14 && !Double.toString(currentValue).equals("")) {
            Config.write(configValueName, new Gson().fromJson("{\"value\":"+Double.toString(currentValue).substring(0, Double.toString(currentValue).length() - 1)+",\"min\":"+min+",\"max\":"+max+",\"step\":"+step+"}", JsonObject.class));
        }

        if (lastTypedKeycode == 211 && !Double.toString(currentValue).equals("")) {
            Config.write(configValueName, new Gson().fromJson("{\"value\":0,\"min\":"+min+",\"max\":"+max+",\"step\":"+step+"}", JsonObject.class));
        }


        // Draw current value
        currentValue = Math.round(Config.getConfig().get(configValueName).getAsJsonObject().get("value").getAsDouble());
        if (currentValue < min) {
            currentValue = min;
        }
        if (currentValue != min) {
            String shownValue = Double.toString(currentValue);
            while (FontLoaders.arial18.getStringWidth(shownValue) + posX > posX + 70) {
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
    public void Slider(float posX, float posY, String title, String description, boolean centeredX, boolean centeredY, int mouseX, int mouseY, String configValueName) {
        if (Config.getConfig().get(configValueName) == null) {
            System.out.println("GUI ERROR: "+configValueName+" does not exist in the user's config. Try deleting the config file in %appdata%/.minecraft/NotEnoughCoins/");
        }
        float min = (float) Config.getConfig().get(configValueName).getAsJsonObject().get("min").getAsDouble();
        float max = (float) Config.getConfig().get(configValueName).getAsJsonObject().get("max").getAsDouble();
        int step = Config.getConfig().get(configValueName).getAsJsonObject().get("step").getAsInt();

        // Adjust position
        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        float objectLength = 350;
        float objectHeight = 45;
        int objectRoundness = 10;
        float overlayLength = 125;
        int overlayRoundness = 10;
        float titleXoffset = 10;
        float titleYoffset = 16;

        RenderUtils.drawRoundedRect(posX, posY, posX + objectLength, posY + objectHeight, objectRoundness, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + (objectLength-overlayLength), posY, posX + objectLength, posY + objectHeight, overlayRoundness, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + titleXoffset, posY + titleYoffset, Theme.highlightColorB.getRGB());

        // Draw bar
        float barStartX = posX + 253;
        float barEndX = posX + 327;
        float barCenterY = posY + 21.25f;
        RenderUtils.drawCircle(barStartX-1, barCenterY, 10, Theme.highlightColorA.getRGB());
        RenderUtils.drawCircle(barEndX-1, barCenterY, 10, Theme.highlightColorA.getRGB());
        RenderUtils.drawRect(barStartX, posY + 11f, barEndX, posY + 31.75f, Theme.highlightColorA.getRGB());

        // Calculate slider position
        float sliderPos = (posX + 245f) + ((((int) Config.getConfig().get(configValueName).getAsJsonObject().get("value").getAsDouble() - min) / (max - min)) * 94f);

        // Draw slider
        float sliderRadius = 2.5f;
        RenderUtils.drawCircle(sliderPos, posY + 9.5f, sliderRadius, Theme.highlightColorB.getRGB());
        RenderUtils.drawCircle(sliderPos, posY + 37f, sliderRadius, Theme.highlightColorB.getRGB());
        RenderUtils.drawRect((sliderPos - sliderRadius), posY + 9.5f, (sliderPos + 2.25f), posY + 37f, Theme.highlightColorA.getRGB());

        double newValue = (((sliderPos + (mouseX - sliderPos) - (posX + 244f)) / (94f / (max - min))) * step + min);
        // Update config value on click
        if (isHovered(posX + 244f, posY, posX + 338f, posY + 45, mouseX, mouseY) && Mouse.isButtonDown(0) && newValue < max) {
            Config.write(configValueName, new Gson().fromJson("{\"value\":"+newValue+",\"min\":"+min+",\"max\":"+max+",\"step\":"+step+"}", JsonObject.class));
        }

        // Draw current value
        double currentValue = Math.round(Config.getConfig().get(configValueName).getAsJsonObject().get("value").getAsDouble());
        String formattedValue = MainUtils.formatNumber(currentValue);
        int valueWidth = FontLoaders.arial18.getStringWidth(formattedValue);
        FontLoaders.arial18.drawString(formattedValue, posX + 335 - valueWidth, posY + 35, Theme.descriptionColor.getRGB());

        // Draw description
        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 160);
        for (String e : splits.subList(0, Math.min(4, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 10 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 5 + lineHeight, Theme.descriptionColor.getRGB());
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
