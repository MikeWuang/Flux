package today.flux.module;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.soterdev.SoterObfuscator;
import net.minecraft.client.gui.FontRenderer;
import sun.misc.Unsafe;
import today.flux.event.RespawnEvent;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.module.implement.Combat.*;
import today.flux.module.implement.Ghost.*;
import today.flux.module.implement.Misc.*;
import today.flux.module.implement.Movement.*;
import today.flux.module.implement.Player.*;
import today.flux.module.implement.Render.*;
import today.flux.module.implement.World.*;
import today.flux.utility.ChatUtils;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    public static List<Module> apiModules = new ArrayList<>();
    private static final ArrayList<Module> mL = new ArrayList<>();

    public static AntiPrick antiPrick = new AntiPrick();
    public static KillAura killAuraMod = new KillAura();
    public static NoRender noRenderMod = new NoRender();
    public static XRay xRayMod = new XRay();
    public static FullBright fullBrightMod = new FullBright();
    public static Phase phaseMod = new Phase();
    public static Chams chamsMod = new Chams();
    public static Ambience ambienceMod = new Ambience();
    public static AntiBots antiBotsMod = new AntiBots();
    public static MurderMystery murderMysteryMod = new MurderMystery();
    public static Scaffold scaffoldMod = new Scaffold();
    public static NoSlow noSlowMod = new NoSlow();
    public static ItemESP itemESPMod = new ItemESP();
    public static BetterSword betterSwordMod = new BetterSword();
    public static InvCleaner invCleanerMod = new InvCleaner();
    public static Hud hudMod = new Hud();
    public static FastUse fastUseMod = new FastUse();
    public static Criticals criticalsMod = new Criticals();
    public static AutoArmor autoArmorMod = new AutoArmor();
    public static Regen regenMod = new Regen();
    public static AutoPot autoPotMod = new AutoPot();
    public static Blink blinkMod = new Blink();
    public static NameTags nameTagsMod = new NameTags();
    public static LongJump longJumpMod = new LongJump();
    public static AutoClicker autoClickerMod = new AutoClicker();
    public static FastPlace fastPlaceMod = new FastPlace();
    public static Fly flyMod = new Fly();
    public static Freecam freecamMod = new Freecam();
    public static InvMove invMoveMod = new InvMove();
    public static Jesus jesusMod = new Jesus();
    public static NoRotate noRotateMod = new NoRotate();
    public static ESP espMod = new ESP();
    public static Speed speedMod = new Speed();
    public static Sprint sprintMod = new Sprint();
    public static Step stepMod = new Step();
    public static ChestESP chestESPMod = new ChestESP();
    public static Tracers tracersMod = new Tracers();
    public static Velocity velocityMod = new Velocity();
    public static Derp derpMod = new Derp();
    public static ChestStealer chestStealerMod = new ChestStealer();
    public static FastBreak fastBreakMod = new FastBreak();
    public static AirJump airJumpMod = new AirJump();
    public static NoFov noFovMod = new NoFov();
    public static NoFall noFallMod = new NoFall();
    public static Zoot zootMod = new Zoot();
    public static SafeWalk safeWalkMod = new SafeWalk();
    public static ViewClip viewClipMod = new ViewClip();
    public static NoHurtCam noHurtCamMod = new NoHurtCam();
    public static Strafe strafeMod = new Strafe();
    public static Nuker nukerMod = new Nuker();
    public static AntiVoid antiVoidMod = new AntiVoid();
    public static Sneak sneakMod = new Sneak();
    public static NameProtect nameProtectMod = new NameProtect();
    public static Ghost ghostMod = new Ghost();
    public static Reach reachMod = new Reach();
    public static Panic panicMod = new Panic();
    public static BreakAura breakAuraMod = new BreakAura();
    public static BowAimbot bowAimbotMod = new BowAimbot();
    public static CGUI clickGUIMod = new CGUI();
    public static FPSHurtCam fpsHurtCamMod = new FPSHurtCam();
    public static PullBack pullBackMod = new PullBack();
    public static MiddleClickFriend middleClickFriendMod = new MiddleClickFriend();
    public static AimAssist AimAssistMod = new AimAssist();
    public static AntiInvis antiInvisMod = new AntiInvis();
    public static TriggerBot triggerBotMod = new TriggerBot();
    public static HitBox hitBoxMod = new HitBox();
    public static AutoEat autoEatMod = new AutoEat();
    public static Aimbot aimbotMod = new Aimbot();
    public static DamageParticle damageParticleMod = new DamageParticle();
    public static ItemPhysics itemPhysicsMod = new ItemPhysics();
    public static Fastbow fastbowMod = new Fastbow();
    public static MotionBlur motionBlurMod = new MotionBlur();
    public static Spider spiderMod = new Spider();
    public static Projectile projectileMod = new Projectile();
    public static PortalMenu portalmenuMod = new PortalMenu();
    public static AutoL autoLMod = new AutoL();
    public static Wings wingMod = new Wings();
    public static AutoGG autoGGMod = new AutoGG();
    public static Eagle eagleMod = new Eagle();
    public static Module debugMod = new Module("Debug", Category.Misc, false);
    public static TargetStrafe targetStrafeModule = new TargetStrafe();
    public static HackerDetector hackdetectionModule = new HackerDetector();
    public static ZombieCrisis zombiecrisismodule = new ZombieCrisis();
    public static AutoTools autoToolsMod = new AutoTools();
    public static ChatBypass chatBypassMod = new ChatBypass();
    public static BlockAnimation animationMod = new BlockAnimation();
    public static Skeletal skeletalMod = new Skeletal();
    public static LightningLocator lightningLocatorMod = new LightningLocator();
    public static ObsidianRemover obsidianRemoverMod = new ObsidianRemover();
    public static Pendant pendantMod = new Pendant();
    public static BlockESP blockEspMod = new BlockESP();
    public static RotationAnimation rotationAnimationMod = new RotationAnimation();
    public static ChestAura chestAuraMod = new ChestAura();
    public static AutoHead autoHeadMod = new AutoHead();
    public static StaffAnalyser staffAnalyserMod = new StaffAnalyser();
    public static AutoRod autoRodMod = new AutoRod();
    public static Refill refillMod = new Refill();
    public static Timer timerMod = new Timer();
    public static BowLongJump bowLongJumpMod = new BowLongJump();
    public static RacistHat racistHatMod = new RacistHat();
    public static HudWindowMod hudWindowMod = new HudWindowMod();
    public static EnchantEffect enchantEffectMod = new EnchantEffect();
    public static Module ircMod = new Module("IRC", Category.World, false);
    public static Disabler disabler = new Disabler();
    public static AntiExploit antiExploitMod = new AntiExploit();

    public ModuleManager() {
        registerMod();
        mL.sort(Comparator.comparing(Module::getName));
    }

    public List<Module> getModulesRender(Object font) {
        List<Module> modules = null;
        modules = getModList().stream().filter(module -> !module.isHide() && module.isEnabled() && (!Hud.renderRenderCategory.getValue() || module.getCategory() != Category.Render)).collect(Collectors.toList());

        if (font instanceof FontUtils) {
            FontUtils nFont = (FontUtils) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return Float.compare(width1, width2);
            });
        } else if (font instanceof FontRenderer) {
            FontRenderer nFont = (FontRenderer) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return Float.compare(width1, width2);
            });
        }

        Collections.reverse(modules);
        return modules;
    }

    public List<Module> getModulesRenderWithAnimation(Object font) {
        List<Module> modules = null;
        modules = new ArrayList<>(getModList());

        if (font instanceof FontUtils) {
            FontUtils nFont = (FontUtils) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return -Float.compare(width1, width2);
            });
        } else if (font instanceof FontRenderer) {
            FontRenderer nFont = (FontRenderer) font;
            modules.sort((m1, m2) -> {
                String modText1 = m1.getDisplayText();
                String modText2 = m2.getDisplayText();
                float width1 = nFont.getStringWidth(modText1);
                float width2 = nFont.getStringWidth(modText2);
                return -Float.compare(width1, width2);
            });
        }

        return modules;
    }

    @SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
    public void registerMod() {
        mL.add(ircMod);
        mL.add(antiPrick);
        mL.add(noRenderMod);
        mL.add(fastUseMod);
        mL.add(speedMod);
        mL.add(sprintMod);
        mL.add(stepMod);
        mL.add(chestESPMod);
        mL.add(pendantMod);
        mL.add(tracersMod);
        // mL.add(voidJumpMod);
        mL.add(velocityMod);
        mL.add(autoHeadMod);
        mL.add(derpMod);
        mL.add(autoToolsMod);
        mL.add(chatBypassMod);
        mL.add(staffAnalyserMod);
        mL.add(autoRodMod);
        mL.add(refillMod);
        mL.add(bowLongJumpMod);
        mL.add(enchantEffectMod);
        // Client Check (Way 3)
        try {
      

            mL.add(criticalsMod);
            mL.add(autoArmorMod);
            mL.add(chestAuraMod);
            mL.add(regenMod);
            mL.add(autoPotMod);
            mL.add(obsidianRemoverMod);
            mL.add(hudMod);
            mL.add(blinkMod);
            mL.add(nameTagsMod);
            mL.add(lightningLocatorMod);
            mL.add(longJumpMod);
            mL.add(autoClickerMod);
            mL.add(fastPlaceMod);
            mL.add(flyMod);
            mL.add(freecamMod);
            mL.add(fullBrightMod);
            mL.add(invMoveMod);
            mL.add(jesusMod);
            mL.add(noRotateMod);
            mL.add(noSlowMod);
            mL.add(espMod);
            mL.add(killAuraMod);
            mL.add(chestStealerMod);
            mL.add(rotationAnimationMod);
            mL.add(antiBotsMod);
            mL.add(scaffoldMod);
            mL.add(invCleanerMod);
            mL.add(fastBreakMod);
            mL.add(airJumpMod);
            mL.add(noFovMod);
        } catch (Throwable e) {
            new Color(25123, 12351, 61231);
        }
        mL.add(noFallMod);
        mL.add(zootMod);
        mL.add(safeWalkMod);
        mL.add(viewClipMod);
        mL.add(noHurtCamMod);
        mL.add(strafeMod);
        mL.add(nukerMod);
        mL.add(antiVoidMod);
        mL.add(sneakMod);
        mL.add(nameProtectMod);
        mL.add(ghostMod);
        mL.add(reachMod);
        mL.add(panicMod);
        mL.add(breakAuraMod);
        mL.add(bowAimbotMod);
        mL.add(clickGUIMod);
        mL.add(fpsHurtCamMod);
        mL.add(pullBackMod);
        mL.add(middleClickFriendMod);
        mL.add(AimAssistMod);
        mL.add(antiInvisMod);
        mL.add(triggerBotMod);
        mL.add(xRayMod);
        mL.add(murderMysteryMod);
        mL.add(phaseMod);
        mL.add(chamsMod);
        mL.add(ambienceMod);
        mL.add(itemESPMod);
        mL.add(betterSwordMod);
        mL.add(hitBoxMod);
        mL.add(autoEatMod);
        mL.add(aimbotMod);
        mL.add(damageParticleMod);
        mL.add(itemPhysicsMod);
        mL.add(fastbowMod);
        mL.add(motionBlurMod);
        mL.add(spiderMod);
        mL.add(projectileMod);
        mL.add(portalmenuMod);
        mL.add(wingMod);
        mL.add(autoLMod);
        mL.add(autoGGMod);
        mL.add(eagleMod);
        mL.add(targetStrafeModule);
        mL.add(hackdetectionModule);
        mL.add(zombiecrisismodule);
        mL.add(animationMod);
        mL.add(skeletalMod);
        mL.add(blockEspMod);
        mL.add(timerMod);
        mL.add(hudWindowMod);
        mL.add(racistHatMod);
        mL.add(disabler);
        mL.add(antiExploitMod);
    }

    public Module getModuleByName(String theMod) {
        for (Module mod : getModList()) {
            if (mod.getName().equalsIgnoreCase(theMod)) {
                return mod;
            }
        }
        return null;
    }

    public Module getModuleByClass(Class<? extends Module> theMod) {
        for (Module mod : getModList()) {
            if (mod.getClass().equals(theMod)) {
                return mod;
            }
        }
        return null;
    }

    public List<Module> getModulesByCategory(Category category) {
        return getModList().stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }

    public static void registerModule(Module module) {
        mL.add(module);
    }

    public static ArrayList<Module> getModList() {
        ArrayList<Module> allModules = new ArrayList<>(mL);
        allModules.addAll(apiModules);
        return allModules;
    }

    @EventTarget
    public void onRespawn(RespawnEvent e) {
        if (Module.a == 1) {
            ChatUtils.debug("Reloading Modules");
            for (Module module : getModList()) {
                if (module == ModuleManager.antiBotsMod || module == ModuleManager.autoGGMod) continue;
                module.update();
            }
        }
    }
}
