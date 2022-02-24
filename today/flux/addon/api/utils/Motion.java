package today.flux.addon.api.utils;




public class Motion {
     
    public double x, y, z = 0;

    public Motion(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
    public void setY(final double y) {
        this.y = y;
    }
    public void setX(final double y) {
        this.x = y;
    }
    
    public void setZ(final double y) {
        this.z = y;
    }
}
