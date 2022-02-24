package today.flux.module;

import com.darkmagician6.eventapi.EventManager;


import net.minecraft.client.Minecraft;

public class SubModule {
     
    private String name, parentModName;
    
    private boolean isEnabled;
    public static Minecraft mc = Minecraft.getMinecraft();

    public SubModule(String name, String mainmodule) {
        this.name = name;
        this.parentModName = mainmodule;
    }

    public void onEnable() {
        EventManager.register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }
    public String getName() {
        return name;
    }
    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.isEnabled = true;
            this.onEnable();
        } else {
            this.isEnabled = false;
            this.onDisable();
        }
    }

	public boolean isEnabled() {
		// TODO 自动生成的方法存根
		return isEnabled;
	}
}
