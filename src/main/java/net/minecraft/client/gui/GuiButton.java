package net.minecraft.client.gui;

import dev.faiths.Faiths;
import dev.faiths.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

import org.lwjgl.opengl.GL11;

import static dev.faiths.utils.render.RenderUtils.drawOutLineRect;
import static net.minecraft.client.renderer.GlStateManager.resetColor;

public class GuiButton extends Gui {
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;

    protected float alpha;

    public GuiButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this
     * button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    public void drawOutLineRect(final float x, final float y, final float width, final float height,
            final float size, final int internalColor, final int borderColor) {
        RenderUtils.drawRect(x, y, width, height, internalColor);

        RenderUtils.drawRect(x, y, size, height, borderColor);

        RenderUtils.drawRect(x, y - size, width + size, size, borderColor);

        RenderUtils.drawRect(x + width, y, size, height, borderColor);

        RenderUtils.drawRect(x, y + height - size, width, size, borderColor);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            hovered = (mouseX >= xPosition && mouseY >= yPosition &&
                    mouseX < xPosition + width && mouseY < yPosition + height);
            final float deltaTime = Faiths.delta;

            if (enabled && hovered) {
                alpha += 0.3F * deltaTime;
                if (alpha >= 210)
                    alpha = 210;
            } else {
                alpha -= 0.3F * deltaTime;
                if (alpha <= 120)
                    alpha = 120;
            }

            final Color hoveredColor = hovered ? new Color(148, 63, 137) : new Color(38, 36, 35);
            final Color textColor = new Color(148, 63, 137);
            if (enabled) {
                drawOutLineRect(xPosition, yPosition,
                        width, height,
                        0.5F, new Color(26, 23, 22).getRGB(), hoveredColor.getRGB());

                mc.getTextureManager().bindTexture(buttonTextures);
                mouseDragged(mc, mouseX, mouseY);

                final FontRenderer fontRenderer = mc.fontRendererObj;
                fontRenderer.drawStringWithShadow(displayString,
                        (float) ((xPosition + width / 2) - fontRenderer.getStringWidth(displayString) / 2),
                        yPosition + (height - 5) / 2F, hovered ? textColor.getRGB() : -1);
            } else {
                drawOutLineRect(xPosition, yPosition,
                        width, height,
                        0.5F, new Color(26, 23, 22, 150).getRGB(), new Color(26, 23, 22, 150).getRGB());

                mc.getTextureManager().bindTexture(buttonTextures);
                mouseDragged(mc, mouseX, mouseY);

                final FontRenderer fontRenderer = mc.fontRendererObj;
                fontRenderer.drawStringWithShadow("§o" + displayString,
                        (float) ((xPosition + width / 2) - fontRenderer.getStringWidth(displayString) / 2),
                        yPosition + (height - 5) / 2F, Color.gray.getRGB());
            }
            resetColor();
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of
     * MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of
     * MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of
     * MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public int getButtonHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
