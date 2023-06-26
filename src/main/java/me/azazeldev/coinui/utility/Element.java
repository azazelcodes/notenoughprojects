package me.azazeldev.coinui.utility;

import me.azazeldev.coinui.Main;
import me.azazeldev.coinui.gui.Theme;
import me.azazeldev.coinui.gui.font.FontLoaders;
import org.lwjgl.input.Mouse;

import java.util.List;


public class Element {
    public void Empty(Float posX, Float posY, String title, String description, boolean centeredX, boolean centeredY) {
        posX = centeredX ? posX + 135 : posX;
        posY = centeredY ? posY + 22.5f : posY;

        RenderUtils.drawRoundedRect(posX, posY, posX + 350, posY + 45, 10, Theme.elementColor.getRGB());
        RenderUtils.drawRoundedRect(posX + 280, posY, posX + 350, posY + 45, 10, Theme.overlayColor.getRGB());
        FontLoaders.arial24.drawString(title, posX + 50, posY + 16, Theme.highlightColorB.getRGB());

        int lineHeight = 0;
        int lines = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 120);
        for (String e : splits) {
            if (lines >= 3) {
                break;
            }
            FontLoaders.arial18.drawString(e, posX + 50 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 10 + lineHeight, Theme.descriptionColor.getRGB());
            lineHeight += 9;
            lines++;
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

        int lineHeight = 0;

        List<String> splits = FontLoaders.arial18.wrapWords(description, 120);
        for (String e : splits.subList(0, Math.min(3, splits.size()))) {
            FontLoaders.arial18.drawString(e, posX + 50 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 10 + lineHeight, Theme.descriptionColor.getRGB());
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
        int valueWidth = FontLoaders.arial24.getStringWidth(formattedValue);
        FontLoaders.arial18.drawString(formattedValue, posX + 335 - valueWidth, posY + 35, Theme.descriptionColor.getRGB());

        // Draw description
        int lineHeight = 0;
        int lines = 0;
        List<String> wrappedDescription = FontLoaders.arial18.wrapWords(description, 95);
        for (String line : wrappedDescription) {
            if (lines < 3) {
                FontLoaders.arial18.drawString(line, posX + 50 + FontLoaders.arial24.getStringWidth(title) + 15, posY + 10 + lineHeight, Theme.descriptionColor.getRGB());
                lineHeight += 9;
                lines++;
            } else {
                break;
            }
        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }
}
