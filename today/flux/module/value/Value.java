package today.flux.module.value;



public class Value {
    
    protected String group;
    
    protected String key;
    protected Object value;

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

	public String getKey() {
		// TODO 自动生成的方法存根
		return key;
	}

	public String getGroup() {
		// TODO 自动生成的方法存根
		return group;
	}
	
}
