package today.flux.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;


import today.flux.Flux;
import today.flux.utility.ChatUtils;


public class PreUpdateEvent extends EventCancellable {
     
    public double x, y, z;
    public float yaw;
    public float pitch;
     
    public boolean onGround;
    private boolean modified;

    public PreUpdateEvent(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.y = y;
        this.z = z;
        this.x = x;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.modified = true;
    }

    public void setPitch(float pitch) {
        if (Math.abs(pitch) > 90) {
            ChatUtils.debug("WARNING: PITCH IS " + pitch);
            if (Flux.DEBUG_MODE) {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.pitch = pitch;
        this.modified = true;
    }

    public void setRotation(float[] rotation) {
        setYaw(rotation[0]);
        setPitch(rotation[1]);
        this.modified = true;
    }
    public void setY(final double y) {
        this.y = y;
    }
    public void setX(final double y) {
        this.x = y;
    }
    
    public void setZ(final double y) {
        this.z = y;
    }
    public float getYaw() {
        return yaw;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public float getPitch() {
        return pitch;
    }

    public boolean isModified() {
        return modified;
    }

	public boolean isOnGround() {
		// TODO 自动生成的方法存根
		return onGround;
	}

	public void setOnGround(boolean onGround2) {
		// TODO 自动生成的方法存根
		onGround=onGround2;
	}

}
