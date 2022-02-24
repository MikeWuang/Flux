package today.flux.module.value;

import com.darkmagician6.eventapi.EventManager;

import today.flux.gui.clickgui.skeet.EventChangeValue;
import today.flux.utility.SmoothAnimationTimer;

public class FloatValue extends Value {
	
	SmoothAnimationTimer animationTimer = new SmoothAnimationTimer(1);
	
    
    private float min, max, increment;

    
    private String unit;

    public boolean anotherShit;

    public FloatValue(String group, String key, float value, float min, float max, float increment, boolean fromAPI) {
        this.group = group;
        this.key = key;
        this.value = value;

        this.min = min;
        this.max = max;
        this.increment = increment;
        this.anotherShit = false;
        if (!fromAPI) ValueManager.addValue(this);
    }

    public FloatValue(String group, String key, float value, float min, float max, float increment) {
        this(group, key, value, min, max, increment, false);
    }

    public FloatValue(String group, String key, float value, float min, float max, float increment, String unit) {
        this(group, key, value, min, max, increment);
        this.unit = unit;
    }

    public Float getValue() {
        return (Float) this.value;
    }

    public void setValue(float value) {
        if (value < min) {
            value = min;
        }

        if (value > max) {
            value = max;
        }

        EventManager.call(new EventChangeValue(this.group, this.key, this.value, value));
        this.value = value;
    }

	public float getValueState() {return this.getValue();}
	public void setValueState(float val) {this.setValue(val);}
    public float getDMin() {return min;}
    public float getDMax() {return max;}
    public float getDIncrement() {return increment;}

	public SmoothAnimationTimer getAnimationTimer() {
		// TODO 自动生成的方法存根
		return animationTimer;
	}
	public float getMin() {
		// TODO 自动生成的方法存根
		return min;
	}
	public float getMax() {
		// TODO 自动生成的方法存根
		return max;
	}

	public String getUnit() {
		// TODO 自动生成的方法存根
		return unit;
	}

	public float getIncrement() {
		// TODO 自动生成的方法存根
		return increment;
	}
}
