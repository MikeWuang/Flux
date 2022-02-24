package me.yarukon.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class GlyphPage {
    private int imgSize;
    private int maxFontHeight = -1;
    private Font font;
    private boolean antiAliasing;
    private boolean fractionalMetrics;
    private HashMap<Character, Glyph> glyphCharacterMap = new HashMap<>();
    public int texID;

    private BufferedImage bufferedImage;

    public GlyphPage(Font font, boolean antiAliasing, boolean fractionalMetrics) {
        this.font = font;
        this.antiAliasing = antiAliasing;
        this.fractionalMetrics = fractionalMetrics;
    }

    public void generateGlyphPage(char[] chars, boolean allChars) {
        // Calculate glyphPageSize
        double maxWidth = -1;
        double maxHeight = -1;

        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, antiAliasing, fractionalMetrics);
        if (allChars) {
            this.imgSize = 8192;
        } else {
            for (char ch : chars) {
                Rectangle2D bounds = font.getStringBounds(Character.toString(ch), fontRenderContext);

                if (maxWidth < bounds.getWidth()) maxWidth = bounds.getWidth();
                if (maxHeight < bounds.getHeight()) maxHeight = bounds.getHeight();
            }

            // Leave some additional space
            maxWidth += 2;
            maxHeight += 2;

            imgSize = (int) Math.ceil(Math.max(
                    Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.length) / maxWidth),
                    Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.length) / maxHeight))
                    * Math.max(maxWidth, maxHeight)) + 1;
        }


        bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

        g.setFont(font);
        // Set Color to Transparent
        g.setColor(new Color(255, 255, 255, 0));
        // Set the image background to transparent
        g.fillRect(0, 0, imgSize, imgSize);

        g.setColor(Color.white);

        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        FontMetrics fontMetrics = g.getFontMetrics();

        int currentCharHeight = 0;
        int posX = 0;
        int posY = 1;

        for (char ch : chars) {
            Glyph glyph = new Glyph();

            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);

            glyph.width = bounds.getBounds().width + 8; // Leave some additional space
            glyph.height = bounds.getBounds().height;

            if (posX + glyph.width >= imgSize) {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }

            glyph.x = posX;
            glyph.y = posY;

            if (glyph.height > maxFontHeight) maxFontHeight = glyph.height;

            if (glyph.height > currentCharHeight) currentCharHeight = glyph.height;

            g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());

            posX += glyph.width;

            glyphCharacterMap.put(ch, glyph);
        }

        try {
            this.texID = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), bufferedImage, true, !allChars);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void bindTexture() {
        GlStateManager.bindTexture(this.texID);
    }

    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }

    public float drawChar(char ch, float x, float y) {
        Glyph glyph = glyphCharacterMap.get(ch);

        if (glyph == null) throw new IllegalArgumentException("'" + ch + "' wasn't found");

        float pageX = glyph.x / (float) imgSize;
        float pageY = glyph.y / (float) imgSize;

        float pageWidth = glyph.width / (float) imgSize;
        float pageHeight = glyph.height / (float) imgSize;

        float width = glyph.width;
        float height = glyph.height;

        GL11.glBegin(GL11.GL_TRIANGLES);

        GL11.glTexCoord2f(pageX + pageWidth, pageY);
        GL11.glVertex2f(x + width, y);

        GL11.glTexCoord2f(pageX, pageY);
        GL11.glVertex2f(x, y);

        GL11.glTexCoord2f(pageX, pageY + pageHeight);
        GL11.glVertex2f(x, y + height);

        GL11.glTexCoord2f(pageX, pageY + pageHeight);
        GL11.glVertex2f(x, y + height);

        GL11.glTexCoord2f(pageX + pageWidth, pageY + pageHeight);
        GL11.glVertex2f(x + width, y + height);

        GL11.glTexCoord2f(pageX + pageWidth, pageY);
        GL11.glVertex2f(x + width, y);

        GL11.glEnd();

        return width - 8;
    }

    public float getWidth(char ch) {
        return glyphCharacterMap.get(ch).width;
    }

    public int getMaxFontHeight() {
        return maxFontHeight;
    }

    public boolean isAntiAliasingEnabled() {
        return antiAliasing;
    }

    public boolean isFractionalMetricsEnabled() {
        return fractionalMetrics;
    }

    static class Glyph {
        private int x;
        private int y;
        private int width;
        private int height;

        Glyph(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        Glyph() {
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
