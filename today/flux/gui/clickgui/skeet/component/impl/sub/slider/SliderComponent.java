package today.flux.gui.clickgui.skeet.component.impl.sub.slider;

import java.math.BigDecimal;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import today.flux.gui.clickgui.skeet.SkeetUtils;
import today.flux.gui.clickgui.skeet.LockedResolution;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.gui.clickgui.skeet.component.ButtonComponent;
import today.flux.gui.clickgui.skeet.component.Component;
import today.flux.gui.clickgui.skeet.component.PredicateComponent;

public abstract class SliderComponent extends ButtonComponent implements PredicateComponent {
    private boolean sliding;

    public SliderComponent(Component parent, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
    }

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        float x = this.getX();
        float y = this.getY();
        
        float width = this.getWidth();
        float height = this.getHeight();
        
        float min = this.getMin();
        float max = this.getMax();
        float fValue = this.getValue();

        boolean hovered = this.isHovered(mouseX, mouseY);

        if (this.sliding) {
            if (mouseX >= x - 0.5f && mouseY >= y - 0.5f && mouseX <= x + width + 0.5f && mouseY <= y + height + 0.5f) {
                this.setValue(MathHelper.clamp_float(this.roundToIncrement((mouseX - x) * (max - min) / (width - 1.0f) + min), min, max));
            } else {
                this.sliding = false;
            }
        }

        float sliderPercentage = (fValue - min) / (max - min);
        String valueString;

        if (fValue % 1.0f != 0.0f) {
            valueString = new BigDecimal(fValue).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
        } else {
            valueString = new BigDecimal(fValue).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }

        Gui.drawRect(x, y, x + width, y + height, SkeetClickGUI.getColor(855309));
        SkeetUtils.drawGradientRect(x + 0.5f, y + 0.5f, x + width - 0.5f, y + height - 0.5f, false, SkeetClickGUI.getColor(hovered ? SkeetUtils.darker(0x494949, 1.4f) : 0x494949), SkeetClickGUI.getColor(hovered ? SkeetUtils.darker(0x303030, 1.4f) : 0x303030));
        SkeetUtils.drawGradientRect(x + 0.5f, y + 0.5f,  (x + width * sliderPercentage - 0.5f), y + height - 0.5f, false, SkeetClickGUI.getColor(), SkeetUtils.darker(SkeetClickGUI.getColor(), 0.8f));
        if (SkeetClickGUI.shouldRenderText()) {
            float stringWidth = SkeetClickGUI.GROUP_BOX_HEADER_RENDERER.getStringWidth(valueString);
            float xx = x + width * sliderPercentage - stringWidth / 2.0f;
            if (SkeetClickGUI.getAlpha() > 120.0) {
                SkeetClickGUI.GROUP_BOX_HEADER_RENDERER.drawOutlinedString(valueString, xx, y + height / 2.0f - 2f, SkeetClickGUI.getColor(0xFFFFFF), 0x78000000);
            } else {
                SkeetClickGUI.GROUP_BOX_HEADER_RENDERER.drawString(valueString, xx, y + height / 2.0f - 2f, SkeetClickGUI.getColor(0xFFFFFF));
            }
        }
    }

    @Override
    public void onPress(int mouseButton) {
        if (!this.sliding && mouseButton == 0) {
            this.sliding = true;
        }
    }

    @Override
    public void onMouseRelease(int button) {
        this.sliding = false;
    }

    private float roundToIncrement(float value) {
        double inc = this.getIncrement();
        double halfOfInc = inc / 2.0;
        double floored = StrictMath.floor(value / inc) * inc;

        if (value >= floored + halfOfInc) {
            return new BigDecimal(StrictMath.ceil(value / inc) * inc).setScale(2, 4).floatValue();
        }

        return new BigDecimal(floored).setScale(2, 4).floatValue();
    }

    public abstract float getValue();

    public abstract void setValue(float value);

    public abstract float getMin();

    public abstract float getMax();

    public abstract float getIncrement();
}

