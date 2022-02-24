package today.flux.addon.api.command;

import com.soterdev.SoterObfuscator;

import today.flux.addon.FluxAPI;
import today.flux.addon.api.FluxAddon;
import today.flux.module.Command;
import today.flux.utility.ChatUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddonCommandManager {
    
    public List<Command> addonCommands = new ArrayList<>();

    public AddonCommandManager() {
        for (FluxAddon fluxAddon : FluxAPI.FLUX_API.getAddonManager().getEnabledFluxAddonList()) {
            for (AddonCommand command : fluxAddon.getCommands()) {
                try {
                    addonCommands.add(command.getNativeCommand());
                    ChatUtils.sendOutputMessage(String.format("[Flux API] \2472[Command] \247a[%s] \2477Loaded!", command.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                    ChatUtils.sendErrorToPlayer(String.format("[Flux API] \2472[Command] \247a[%s] \2477Error: %s", command.getName(), e.getMessage()));
                }
            }
        }
    }

	public Collection<? extends Command> getAddonCommands() {
		// TODO 自动生成的方法存根
		return addonCommands;
	}
}
