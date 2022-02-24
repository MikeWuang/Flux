package today.flux.addon.api.event.events;



import today.flux.addon.api.event.AddonEvent;

public class EventMove extends AddonEvent {
     
    private double x, y, z;
     
    private boolean onGround, safeWalk;

	public boolean isSafeWalk() {
		// TODO 自动生成的方法存根
		return safeWalk;
	}

	public double getZ() {
		// TODO 自动生成的方法存根
		return z;
	}
	public double getY() {
		// TODO 自动生成的方法存根
		return y;
	}
	public double getX() {
		// TODO 自动生成的方法存根
		return x;
	}

	public boolean isOnGround() {
		// TODO 自动生成的方法存根
		return onGround;
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
    
	public void setSafeWalk(boolean safeWalk2) {
		// TODO 自动生成的方法存根
		safeWalk=safeWalk2;
	}

	public void setOnGround(boolean onGround2) {
		// TODO 自动生成的方法存根
		onGround=onGround2;
	}
}
