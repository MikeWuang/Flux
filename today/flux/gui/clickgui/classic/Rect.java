package today.flux.gui.clickgui.classic;




public class Rect {
    private float x;
    private float y;
    private float width;
    private float height;

    public Rect(){

    }

    public Rect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getY() {
        return this.y;
    }

    
    public float getX() {
        return this.x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    public void setX(final float y) {
        this.x = y;
    }

	public float getWidth() {
		// TODO 自动生成的方法存根
		return width;
	}

	public float getHeight() {
		// TODO 自动生成的方法存根
		return height;
	}
    

    
}
