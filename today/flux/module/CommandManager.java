package today.flux.module;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.BooleanUtils;
import sun.misc.Unsafe;
import today.flux.Flux;
import today.flux.addon.FluxAPI;
import today.flux.event.ChatSendEvent;

import today.flux.module.implement.Command.*;
import today.flux.utility.ChatUtils;
import today.flux.utility.NumberUtils;
import today.flux.module.value.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class CommandManager {
    private final List<Command> cmd = new ArrayList<>();
    public static final String PREFIX = ".";

    public CommandManager() {
        resister();
        cmd.sort(comparing(Command::getCmdName));
    }

    @SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
    public void resister() {
        try {
           

            cmd.add(new BindCmd());
            cmd.add(new FriendCmd());
            cmd.add(new HelpCmd());
            cmd.add(new PDCmd());
            cmd.add(new AddAltCmd());
            cmd.add(new ToggleCmd());
            cmd.add(new VClipCmd());
            cmd.add(new EnchantCmd());
            cmd.add(new HClipCmd());
            cmd.add(new SoundFixCmd());
            cmd.add(new NamePCmd());
            cmd.add(new Say());
            cmd.add(new ResetGuiCmd());
            cmd.add(new Hide());
            cmd.add(new PresetCmd());
            cmd.add(new ResetRecord());
            cmd.add(new IGNCmd());
            cmd.add(new ScaleCommand());
            cmd.add(new AddonCmd());
            cmd.add(new WatermarkCmd());
        } catch (Throwable e) {

        }

        EventManager.register(this);
    }

    @EventTarget
    public void onChatSend(ChatSendEvent event) {
        try {
        	if (Flux.noCommand.getValueState()) return;
        	
            String message = event.message;
            if (!message.startsWith(PREFIX))
                return;

            event.setCancelled(true);

            message = message.substring(1); //remove prefix

            final String commandName = message.split("( )+")[0];
            message = message.substring(commandName.length());

            final String[] args = Arrays.stream(message.split("( )+")).filter(i -> i != null && !i.equals("") && !i.equals(" ")).toArray(String[]::new);

            final Command cmd = getCommandByName(commandName);

            if (cmd != null) {
                try {
                    cmd.execute(args);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }

            final List<Value> values = ValueManager.getValueByModName(commandName);

            if (values.size() > 0) {
                if (args.length <= 0) {
                    sendCurrentValues(values);
                    return;
                }

                if (args.length <= 1) {
                    ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Invalid argument. " + EnumChatFormatting.WHITE + "Usage: .<module> " + EnumChatFormatting.GRAY + ":" + EnumChatFormatting.WHITE + " .<module> <setting> <value>");
                    return;
                }

                if (args.length <= 2) {
                    final String key = args[0].replace("-", " ");
                    final String val = args[1];

                    //Invalid Key
                    if (values.stream().noneMatch(v -> v.getKey().equalsIgnoreCase(key))) {
                        ChatUtils.debug("Invalid Key (" + key + ")");
                        sendCurrentValues(values);
                        return;
                    }

                    final Value value = values.stream().filter(v -> v.getKey().equalsIgnoreCase(key)).collect(Collectors.toList()).get(0);

                    if (value instanceof ModeValue) {
                        //Invalid Value
                        if (Arrays.stream(((ModeValue) value).getModes()).noneMatch(s -> s.equalsIgnoreCase(val))) {
                            ChatUtils.sendMessageToPlayer(EnumChatFormatting.GOLD + "Available Modes: ");

                            String output = "";
                            for (String mode : ((ModeValue) value).getModes())
                                output += EnumChatFormatting.GREEN + mode + EnumChatFormatting.WHITE + ", ";

                            ChatUtils.sendMessageToPlayer(output.substring(0, output.length() - 2));
                            return;
                        }

                        //Valid
                        ((ModeValue) value).setValue(val);

                        ChatUtils.sendMessageToPlayer(value.getKey() + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
                        return;
                    }

                    if (value instanceof FloatValue) {
                        //Invalid Value
                        if (!NumberUtils.isNumber(val)) {
                            ChatUtils.sendMessageToPlayer("§cInvalid format (§r" + val + "§c)");
                            return;
                        }

                        final float set = Float.parseFloat(val);

                        if (set < ((FloatValue) value).getMin() || set > ((FloatValue) value).getMax()) {
                            ChatUtils.sendMessageToPlayer("§cOut of range §r[Min: §a" + ((FloatValue) value).getMin() + "§r, Max: §a" + ((FloatValue) value).getMax() + "§r]");
                            return;
                        }

                        //Valid
                        ((FloatValue) value).setValue(set);
                        ChatUtils.sendMessageToPlayer(value.getKey() + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
                        return;
                    }

                    if (value instanceof BooleanValue) {
                        //Invalid Value
                        if (BooleanUtils.toBooleanObject(val) == null) {
                            ChatUtils.sendMessageToPlayer("§cInvalid format (§r" + val + "§c)");
                            return;
                        }

                        //Valid
                        value.setValue(BooleanUtils.toBoolean(val));
                        ChatUtils.sendMessageToPlayer(value.getKey() + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
                        return;
                    }
                    return;
                }
                return;
            }

            ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Unknown command. Type " + PREFIX + "help for help.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCurrentValues(List<Value> values) {
        ChatUtils.sendMessageToPlayer(EnumChatFormatting.GOLD + "Current Settings: ");

        for (Value value : values) {
            final String name = value.getKey().replace(" ", "-");

            ChatUtils.sendMessageToPlayer(name + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
        }
    }

    public Command getCommandByName(final String name) {
        try {
            return getCommands().stream().filter(item -> item.getCmdName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>(cmd);
        commands.addAll(FluxAPI.FLUX_API.getCommandManager().getAddonCommands());
        return commands;
    }
}
