package today.flux;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.EnumChatFormatting;
import today.flux.addon.FluxAPI;
import today.flux.config.Cloud;
import today.flux.config.Config;
import today.flux.config.preset.PresetManager;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.UpdateEvent;
import today.flux.gui.NewClickGUI;
import today.flux.gui.clickgui.classic.ClickGUI;
import today.flux.gui.clickgui.skeet.EventChangeValue;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.gui.hud.window.HudWindow;
import today.flux.gui.hud.window.HudWindowManager;

import today.flux.module.CommandManager;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.Render.Hud;
import today.flux.module.implement.Render.HudWindowMod;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ModeValue;
import today.flux.module.value.ValueManager;
import today.flux.utility.*;
import today.flux.utility.shader.shaders.ShaderBlob;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Flux {
    public static Color rainbow = new Color(0, 0, 0, 0);
    public static boolean DEBUG_MODE = false;
    public static boolean AUTO_RECONNECT;
    public static final Flux INSTANCE = new Flux();

    public static final String NAME = "Flux";
    public static final float VERSION = 39F;
    public static final String CREDIT = "Flux Client";
    public static final String BETA = "";
    public static final Map<String, Boolean> supportedServer = new HashMap<String, Boolean>() {{
        put("a.polars.cc", true);
    }};
    public static String status = EnumChatFormatting.YELLOW + "(Checking)";


    public static FontRenderer font;
    public boolean directConnectToHypixel;
    public String serverHostName = null;

    
    private CommandManager commandManager;
    
    
    public ModuleManager moduleManager;
    
    private FriendManager friendManager;
    
    private ValueManager valueManager;
    
    private PresetManager presetManager;
    
    private Config config;
    
    public FluxAPI api;
    
    
    private ClickGUI clickGUI;
    
    
    private NewClickGUI newClickGUI;
    
    
    private SkeetClickGUI skeetClickGUI;
    
    public Cloud cloud;
    


    // TODO: Refactor
    public ServerData lastServer;
    public static String[] currentAlt;

    // Global Settings
    public static ModeValue GTheme = new ModeValue("Global", "Global Theme", "Dark", "Dark", "Light");

    public static BooleanValue teams = new BooleanValue("Global", "Teams", true);
    public static BooleanValue friends = new BooleanValue("Global", "Friends", true);
    public static BooleanValue mobfriends = new BooleanValue("Global", "Mob Friends", true);

    public static BooleanValue ToggleSound = new BooleanValue("Global", "Toggle Sound", true);
    public static BooleanValue Cape = new BooleanValue("Global", "Cape", true);
    public static BooleanValue RealBobbing = new BooleanValue("Global", "Real Bobbing", false);
    public static BooleanValue voidFlickFix = new BooleanValue("Global", "Void Flick Fix", true);
    public static BooleanValue asyncScreenshot = new BooleanValue("Global", "ASyn ScreenShot", true);
    public static BooleanValue dontCloseChat = new BooleanValue("Global", "Keep Chat", true);
    public static BooleanValue memFix = new BooleanValue("Global", "Memory Fix", true);
    public static BooleanValue mouseDelay = new BooleanValue("Global", "Mouse Delay Fix", true);
    public static BooleanValue noCommand = new BooleanValue("Global", "No Command", false);
    public static BooleanValue everythingBlock = new BooleanValue("Global", "All Item Block", false);
    public static BooleanValue noobfname = new BooleanValue("Global", "No Obf Name", false);
    public static BooleanValue background = new BooleanValue("Global", "No Blob", false);
    public ShaderBlob blobShader;

    public float lastTPS = 20.0f;
    public long keepAliveTime = 0, curPing;
    public boolean receivedPacket = false;

    
    
    public HudWindowManager hudWindowMgr;
    public static long playTimeStart = 0;

    
    public void start() {
        Hud.blurIntensity.anotherShit = true;

        // Initialize
        this.cloud = new Cloud();
        this.cloud.sync();




        this.friendManager = new FriendManager();
        this.commandManager = new CommandManager();
        this.valueManager = new ValueManager();

        hudWindowMgr=new HudWindowManager();
        this.presetManager = new PresetManager();
        newClickGUI=new NewClickGUI();
        clickGUI=new ClickGUI();
        this.config = new Config();
        // Enable default modules
        ModuleManager.hudMod.enable();

        // disable annoying modules
        ModuleManager.blinkMod.disable();
        ModuleManager.speedMod.disable();
        ModuleManager.flyMod.disable();
        ModuleManager.killAuraMod.disable();
        ModuleManager.nukerMod.disable();
        ModuleManager.regenMod.disable();
        ModuleManager.phaseMod.disable();

        // register anti-bot
        EventManager.register(ModuleManager.antiBotsMod);
        // register win count
        EventManager.register(ModuleManager.autoGGMod);

        EventManager.register(moduleManager);
        // register events
        EventManager.register(new ServerUtils());
        EventManager.register(this);


    }

    public void onShutdown() {
        config.saveConfig();
    }

    @EventTarget
    public void onPre(UpdateEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiChat) && ServerUtils.INSTANCE.isOnHypixel() && receivedPacket && mc.thePlayer.ticksExisted % 20 == 0) {
            receivedPacket = false;
            mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/" + KillAura.getRandomDoubleInRange(10E10, 10E20)));
            keepAliveTime = System.currentTimeMillis();
        }

        for (HudWindow window : HudWindowManager.windows) {
            switch (window.windowID) {
                case "SessionInfo":
                    if (HudWindowMod.sessionInfo.getValue() && window.hide) {
                        window.show();
                    } else if (!HudWindowMod.sessionInfo.getValue() && !window.hide) {
                        window.hide();
                    }
                    break;
                case "PlayerInventory":
                    if (HudWindowMod.plyInventory.getValue() && window.hide) {
                        window.show();
                    } else if (!HudWindowMod.plyInventory.getValue() && !window.hide) {
                        window.hide();
                    }
                    break;
                case "Scoreboard":
                    if (HudWindowMod.scoreboard.getValue() && window.hide) {
                        window.show();
                    } else if (!HudWindowMod.scoreboard.getValue() && !window.hide) {
                        window.hide();
                    }
                    break;
                case "Radar":
                    if (HudWindowMod.radar.getValue() && window.hide) {
                        window.show();
                    } else if (!HudWindowMod.radar.getValue() && !window.hide) {
                        window.hide();
                    }
                    break;
            }
        }
    }

    private static ArrayList<Long> times = new ArrayList<Long>();
    private TimeHelper tpsTimer = new TimeHelper();
    public static TimeHelper laggTimer = new TimeHelper();

    @EventTarget
    public void onRPacket(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof S02PacketChat)) {
            laggTimer.reset();
        }

        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            times.add(Math.max(1000, tpsTimer.getElapsedTime()));
            long timesAdded = 0;
            if (times.size() > 5) {
                times.remove(0);
            }
            for (long l : times) {
                timesAdded += l;
            }
            long roundedTps = timesAdded / times.size();
            lastTPS = (float) ((20.0 / roundedTps) * 1000.0);
            tpsTimer.reset();
        }

        if (System.currentTimeMillis() - keepAliveTime > 3000) {
            curPing = -1;
            receivedPacket = true;
        }

        if (event.getPacket() instanceof S3APacketTabComplete) {
            S3APacketTabComplete packet = (S3APacketTabComplete) event.getPacket();
            if (packet.func_149630_c().length > 500) {
                PlayerUtils.tellPlayer("Received a large packet, cancelled for preventing client crash.");
                event.setCancelled(true);
            } else if (packet.func_149630_c().length == 0) {
                curPing = System.currentTimeMillis() - keepAliveTime;
                receivedPacket = true;
            }
        }
    }

    // For skoot ui color picker
    @EventTarget
    public void onChangeValue(EventChangeValue evt) {
        if (this.getSkeetClickGUI() != null) {
            this.getSkeetClickGUI().updateValue(evt.valKey, evt.valName, evt.oldVal, evt.val);
        }
    }
	public SkeetClickGUI getSkeetClickGUI() {
		// TODO 自动生成的方法存根
		return skeetClickGUI;
	}
	public NewClickGUI getNewClickGUI() {
		// TODO 自动生成的方法存根
		return newClickGUI;
	}

	public ClickGUI getClickGUI() {
		// TODO 自动生成的方法存根
		return clickGUI;
	}

	public Config getConfig() {
		// TODO 自动生成的方法存根
		return config;
	}

	public void setSkeetClickGUI(SkeetClickGUI skeetClickGUI2) {
		// TODO 自动生成的方法存根
		skeetClickGUI=skeetClickGUI2;
	}

	public ModuleManager getModuleManager() {
		// TODO 自动生成的方法存根
		return moduleManager;
	}

	public FriendManager getFriendManager() {
		// TODO 自动生成的方法存根
		return friendManager;
	}

	public HudWindowManager getHudWindowMgr() {
		// TODO 自动生成的方法存根
		return hudWindowMgr;
	}

	public CommandManager getCommandManager() {
		// TODO 自动生成的方法存根
		return commandManager;
	}

	public void setClickGUI(ClickGUI clickGUI2) {
		// TODO 自动生成的方法存根
		clickGUI=clickGUI2;
	}

	public void setNewClickGUI(NewClickGUI newClickGUI2) {
		// TODO 自动生成的方法存根
		newClickGUI=newClickGUI2;
	}
}