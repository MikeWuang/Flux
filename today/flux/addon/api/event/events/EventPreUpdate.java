package today.flux.addon.api.event.events;



import today.flux.addon.api.event.AddonEvent;

public class EventPreUpdate extends AddonEvent {
     
    private double x, y, z;
     
    private float yaw, pitch;
     
    private boolean onGround;
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }
    
    public double getX() {
        return this.x;
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
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
}
