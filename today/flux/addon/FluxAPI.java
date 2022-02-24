package today.flux.addon;


import today.flux.addon.api.command.AddonCommandManager;
import today.flux.addon.api.module.AddonModuleManager;
import today.flux.addon.api.value.AddonValueManager;
import today.flux.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FluxAPI {
    
    private final AddonManager addonManager;
    
    private final AddonModuleManager moduleManager;
    
    private final AddonValueManager valueManager;
    
    private final AddonEventManager eventManager;
    
    private final AddonCommandManager commandManager;

    public static FluxAPI FLUX_API;

    public FluxAPI() {
        FLUX_API = this;

        this.loadAddons();

        this.eventManager = new AddonEventManager();
        this.addonManager = new AddonManager();
        this.valueManager = new AddonValueManager();
        this.addonManager.loadAllAPI();

        this.moduleManager = new AddonModuleManager();
        this.commandManager = new AddonCommandManager();
    }

    public void saveAddons() {
        try {
            final FileWriter fileWriter = new FileWriter(Config.ROOT_DIR + "/enabledAddons.txt");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String enabledAddon : AddonManager.getEnabledAddonsName()) {
                bufferedWriter.write(enabledAddon + "\n");
            }
            bufferedWriter.close();
        } catch (Exception ex) {
        }
    }

    public boolean loadAddons() {
        try {
            AddonManager.getEnabledAddonsName().clear();
            String line = null;
            final FileReader fileReader = new FileReader(Config.ROOT_DIR + "/enabledAddons.txt");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                AddonManager.getEnabledAddonsName().add(line);
            }
            bufferedReader.close();
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

	public AddonModuleManager getModuleManager() {
		// TODO 自动生成的方法存根
		return moduleManager;
	}

	public AddonManager getAddonManager() {
		// TODO 自动生成的方法存根
		return addonManager;
	}

	public AddonCommandManager getCommandManager() {
		// TODO 自动生成的方法存根
		return commandManager;
	}

	public AddonValueManager getValueManager() {
		// TODO 自动生成的方法存根
		return valueManager;
	}
}
