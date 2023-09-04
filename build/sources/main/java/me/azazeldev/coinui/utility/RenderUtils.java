package me.azazeldev.coinui.utility;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void circle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
//        GL11.glEnable(GL_POLYGON_SMOOTH);

        glColor4f(red, green, blue, alpha);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1F);
        glBegin(GL_POLYGON);
        for(int i = 0; i <= 360; i++)
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1F, 1F, 1F, 1F);
//        GL11.glDisable(GL_POLYGON_SMOOTH);

    }

    public static void arc(final float x, final float y, final float start, final float end, final float radius,
                           final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
                                  final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        var15.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w * 1.001f;
                final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float) Math.cos(i * Math.PI / 180.0) * w;
            final float ldy = (float) Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(float x1, float y1, float x2, float y2, int color) {
        Gui.drawRect((int) x1, (int) y1, (int) x2, (int) y2, color);
    }

    public static void drawRoundedRect(float x1, float y1, float x2, float y2, final int round, final int color) {
        x1 += (float) (round / 2.0f + 0.5);
        y1 += (float) (round / 2.0f + 0.5);
        x2 -= (float) (round / 2.0f + 0.5);
        y2 -= (float) (round / 2.0f + 0.5);
        Gui.drawRect((int) x1, (int) y1, (int) x2, (int) y2, color);
        circle(x2 - round / 2.0f, y1 + round / 2.0f, round, color);
        circle(x1 + round / 2.0f, y2 - round / 2.0f, round, color);
        circle(x1 + round / 2.0f, y1 + round / 2.0f, round, color);
        circle(x2 - round / 2.0f, y2 - round / 2.0f, round, color);
        Gui.drawRect((int) (x1 - round / 2.0f - 0.5f), (int) (y1 + round / 2.0f), (int) x2, (int) (y2 - round / 2.0f),
                color);
        Gui.drawRect((int) x1, (int) (y1 + round / 2.0f), (int) (x2 + round / 2.0f + 0.5f), (int) (y2 - round / 2.0f),
                color);
        Gui.drawRect((int) (x1 + round / 2.0f), (int) (y1 - round / 2.0f - 0.5f), (int) (x2 - round / 2.0f),
                (int) (y2 - round / 2.0f), color);
        Gui.drawRect((int) (x1 + round / 2.0f), (int) y1, (int) (x2 - round / 2.0f), (int) (y2 + round / 2.0f + 0.5f),
                color);
    }

    public static void initAA() throws LWJGLException {
        // Anti-Aliasing
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

}

