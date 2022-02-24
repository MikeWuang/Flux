/*
 * CFR 0.151
 *
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package today.flux.gui.clickgui.skeet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.flux.Flux;
import today.flux.config.preset.PresetManager;
import today.flux.gui.AbstractGuiScreen;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.clickgui.skeet.component.impl.sub.button.ButtonComponentImpl;
import today.flux.gui.clickgui.skeet.component.impl.sub.checkBox.CheckBoxTextComponent;
import today.flux.gui.clickgui.skeet.component.impl.sub.color.ColorPickerTextComponent;
import today.flux.gui.clickgui.skeet.component.impl.sub.comboBox.ComboBoxTextComponent;
import today.flux.gui.clickgui.skeet.component.impl.sub.key.KeyBindComponent;
import today.flux.gui.clickgui.skeet.component.impl.sub.slider.SliderTextComponent;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.fontRenderer.FontUtils;
import today.flux.gui.other.SavePresetScreen;
import today.flux.gui.clickgui.skeet.component.Component;
import today.flux.gui.clickgui.skeet.component.TabComponent;
import today.flux.gui.clickgui.skeet.component.impl.GroupBoxComponent;
import today.flux.gui.clickgui.skeet.component.impl.ScrollListComponent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.implement.Render.CGUI;
import today.flux.module.value.*;
import today.flux.utility.TimeHelper;

import java.awt.*;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SkeetClickGUI extends AbstractGuiScreen {
    public static BooleanValue paimon = new BooleanValue("ClickGui", "Paimon", false);

    public static final FontUtils ICONS_RENDERER;
    public static final FontUtils GROUP_BOX_HEADER_RENDERER;
    public static final FontUtils FONT_RENDERER;
    public static final FontUtils KEYBIND_FONT_RENDERER;
    private static final ResourceLocation BACKGROUND_IMAGE;
    private static final char[] ICONS;
    private static final int TAB_SELECTOR_HEIGHT;
    public static double alpha;
    public static boolean open;
    private final Component rootComponent;
    private final Component tabSelectorComponent;
    public double targetAlpha;
    public boolean closed;

    private boolean dragging;
    private float prevX;
    private float prevY;
    private int selectorIndex;
    private TabComponent selectedTab;
    private TimeHelper timeHelper = new TimeHelper();

    //For config
    private ScrollListComponent configListComponent;
    private String selectedConfig = "_NONE";
    public static boolean needUpdateValue = false;

    static {
        GROUP_BOX_HEADER_RENDERER = FontManager.tahoma11bold;
        BACKGROUND_IMAGE = new ResourceLocation("flux/skoot.png");
        ICONS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'm'};
        TAB_SELECTOR_HEIGHT = 321 / ICONS.length;
        ICONS_RENDERER = FontManager.icon40;
        FONT_RENDERER = FontManager.tahoma11;
        KEYBIND_FONT_RENDERER = FontManager.tahoma9;
    }

    //dat rainbow
    public float hue = 0;

    public SkeetClickGUI() {
        this.rootComponent = new Component(null, 0.0f, 0.0f, 406f, 350.0f) {
            @Override
            public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {

                if (SkeetClickGUI.this.dragging) {
                    this.setX(Math.max(0.0f, Math.min((float) lockedResolution.getWidth() - this.getWidth(), (float) mouseX - SkeetClickGUI.this.prevX)));
                    this.setY(Math.max(0.0f, Math.min((float) lockedResolution.getHeight() - this.getHeight(), (float) mouseY - SkeetClickGUI.this.prevY)));
                }

                float borderX = this.getX();
                float borderY = this.getY();
                float width = this.getWidth();
                float height = this.getHeight();

                Gui.drawRect(borderX, borderY, borderX + width, borderY + height, SkeetClickGUI.getColor(0x10110E));
                Gui.drawRect(borderX + 0.5f, borderY + 0.5f, borderX + width - 0.5f, borderY + height - 0.5f, SkeetClickGUI.getColor(0x373A3A));
                Gui.drawRect(borderX + 1.0f, borderY + 1.0f, borderX + width - 1.0f, borderY + height - 1.0f, SkeetClickGUI.getColor(0x232323));
                Gui.drawRect(borderX + 3.0f, borderY + 3.0f, borderX + width - 3.0f, borderY + height - 3.0f, SkeetClickGUI.getColor(0x2F2F2F));

                float left = borderX + 3.5f;
                float top = borderY + 3.5f;
                float right = borderX + width - 3.5f;
                float bottom = borderY + height - 3.5f;

                Gui.drawRect(left, top, right, bottom, SkeetClickGUI.getColor(0x151515));

                if (alpha > 20.0) {
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    SkeetUtils.startScissorBox(lockedResolution, (int) left, (int) top, (int) (right - left), (int) (bottom - top));
                    SkeetUtils.drawImage(left, top, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, BACKGROUND_IMAGE);
                    SkeetUtils.drawImage(left + 325.0f, top + 1.0f, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, BACKGROUND_IMAGE);
                    SkeetUtils.drawImage(left + 1.0f, top + 275.0f, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, BACKGROUND_IMAGE);
                    SkeetUtils.drawImage(left + 326.0f, top + 276.0f, 325.0f, 275.0f, 1.0f, 1.0f, 1.0f, BACKGROUND_IMAGE);
                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
                }

                float xDif = (right - left) / 2.0f;

                if (hue > 255.0F) {
                    hue = 0.0F;
                }

                float h = hue;
                float h2 = hue + 85.0F;
                float h3 = hue + 170.0F;
                if (h > 255.0F) {
                    h = 0.0F;
                }

                if (h2 > 255.0F) {
                    h2 -= 255.0F;
                }

                if (h3 > 255.0F) {
                    h3 -= 255.0F;
                }

                Color a = Color.getHSBColor(h / 255.0F, 0.4F, 1.0F);
                Color b = Color.getHSBColor(h2 / 255.0F, 0.4F, 1.0F);
                Color c = Color.getHSBColor(h3 / 255.0F, 0.4F, 1.0F);
                int color1 = a.getRGB();
                int color2 = b.getRGB();
                int color3 = c.getRGB();
                hue = hue + 0.1f;

                if (alpha > 70) {
                    SkeetUtils.drawGradientRect(left += 0.5f, top += 0.5f, left + xDif, top + 1.5f - 0.5f, true, color1, color2);
                    SkeetUtils.drawGradientRect(left + xDif, top, right -= 0.5f, top + 1.5f - 0.5f, true, color2, color3);
                }

                if (alpha >= 70.0) {
                    Gui.drawRect(left, top + 1.5f - 1.0f, right, top + 1.5f - 0.5f, 0x70000000);
                }

                for (Component child : this.children) {
                    if (child instanceof TabComponent && SkeetClickGUI.this.selectedTab != child) continue;
                    child.drawComponent(lockedResolution, mouseX, mouseY);
                }

                if (timeHelper.delay(35)) {
                    if(photoIndex + 1 > 52) {
                        photoIndex = 0;
                    } else {
                        ++photoIndex;
                    }
                    timeHelper.reset();
                }
                if(SkeetClickGUI.paimon.getValue())
                RenderUtil.drawImage(new ResourceLocation("flux/paimon/paimon_00" + (photoIndex < 10 ? "0" + photoIndex : photoIndex)  + ".png"), this.getX() + this.getWidth() + 5, this.getY(), 99f, 116f, (float) alpha / 255f);
            }

            @Override
            public void onKeyPress(int keyCode) {
                for (Component child : this.children) {
                    if (child instanceof TabComponent && SkeetClickGUI.this.selectedTab != child) continue;
                    child.onKeyPress(keyCode);
                }
            }

            @Override
            public void onMouseClick(int mouseX, int mouseY, int button) {
                for (Component child : this.children) {
                    if (child instanceof TabComponent && SkeetClickGUI.this.selectedTab != child) continue;
                    child.onMouseClick(mouseX, mouseY, button);
                }

                if (button == 0 && RenderUtil.isHovering(mouseX, mouseY, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + 14)) {
                    SkeetClickGUI.this.dragging = true;
                    SkeetClickGUI.this.prevX = (float) mouseX - this.getX();
                    SkeetClickGUI.this.prevY = (float) mouseY - this.getY();
                    return;
                }

                if (button == 0 && this.isHovered(mouseX, mouseY)) {
                    for (Component child : this.getChildren()) {
                        if (child instanceof TabComponent && SkeetClickGUI.this.selectedTab == child) {
                            for (Component grandChild : child.getChildren()) {
                                if (!grandChild.isHovered(mouseX, mouseY)) continue;
                                return;
                            }
                            continue;
                        }
                        if (child instanceof TabComponent || !child.isHovered(mouseX, mouseY)) continue;
                        return;
                    }
                }
            }

            @Override
            public void onMouseRelease(int button) {
                super.onMouseRelease(button);
                SkeetClickGUI.this.dragging = false;
            }

            @Override
            public void onMouseScroll(int mouseX, int mouseY, int value) {
                for (Component child : this.children) {
                    if (child instanceof TabComponent && SkeetClickGUI.this.selectedTab != child) continue;
                    child.onMouseScroll(mouseX, mouseY, value);
                }
            }
        };

        for(Category c : Category.values()) {
            TabComponent categoryTab = new TabComponent(this.rootComponent, c.name(), 51.5f, 5.0f, 350f, 341.5f) {
                @Override
                public void setupChildren() {
                    if (c == Category.Global) {
                        GroupBoxComponent groupBoxComponent = new GroupBoxComponent(this, c.name(), 0.0f, 0.0f, 160, 320.0f);
                        this.addChild(groupBoxComponent);
                        for (Value v : ValueManager.getValueByModName(c.name())) {
                            Component component = null;
                            if (v instanceof BooleanValue) {
                                BooleanValue booleanProperty = (BooleanValue) v;
                                component = new CheckBoxTextComponent(groupBoxComponent, booleanProperty.getKey(), booleanProperty::getValue, booleanProperty::setValue);
                            } else if (v instanceof FloatValue) {
                                FloatValue doubleProperty = (FloatValue) v;
                                component = new SliderTextComponent(groupBoxComponent, doubleProperty.getKey(), doubleProperty::getValueState, doubleProperty::setValueState, doubleProperty::getDMin, doubleProperty::getDMax, doubleProperty::getDIncrement);
                            } else if (v instanceof ModeValue) {
                                ModeValue enumProperty = (ModeValue) v;
                                component = new ComboBoxTextComponent(groupBoxComponent, v.getKey(), enumProperty::getCurVal, enumProperty::getValues, enumProperty::setCurVal);
                            } else if (v instanceof ColorValue) {
                                ColorValue colorValue = (ColorValue) v;
                                component = new ColorPickerTextComponent(groupBoxComponent, v.getKey(), colorValue::getColorInt, colorValue::setValueInt);
                            }
                            if (component == null) continue;
                            groupBoxComponent.addChild(component);
                        }

                        GroupBoxComponent configsGroupBox = new GroupBoxComponent(this, "Configs", 170f, 8.0f, 165f, 320f);

                        configListComponent = new ScrollListComponent(configsGroupBox, () -> new ArrayList<>(PresetManager.presets), selected -> selectedConfig = selected, 159f, 236f);
                        configsGroupBox.addChild(configListComponent);

                        int buttonHeight = 15;
                        configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Load", button -> {
                            if(!selectedConfig.equals("_NONE")) {
                                PresetManager.loadPreset(selectedConfig);
                            }
                        }, 159f, buttonHeight));

                        configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Save", button -> mc.displayGuiScreen(new SavePresetScreen(Flux.INSTANCE.getSkeetClickGUI())), 159f, buttonHeight));

                        configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Refresh", button -> SkeetClickGUI.needUpdateValue = true, 159f, buttonHeight));

                        configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Delete", button -> {
                            if(!selectedConfig.equals("_NONE") && !selectedConfig.startsWith("#")) {
                                Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo((result, id) -> {
                                    if (result) {
                                        PresetManager.deletePreset(selectedConfig);
                                        needUpdateValue = true;
                                    }
                                    Minecraft.getMinecraft().displayGuiScreen(Flux.INSTANCE.getSkeetClickGUI());
                                }, "Are you sure to remove the preset named " + EnumChatFormatting.YELLOW + selectedConfig + EnumChatFormatting.RESET + " ?", "", 0));
                            }
                        }, 159f, buttonHeight));

                        this.addChild(configsGroupBox);
                    } else {
                        List<Module> modulesInCategory = Flux.INSTANCE.getModuleManager().getModulesByCategory(c);
                        for (Module module : modulesInCategory) {
                            GroupBoxComponent groupBoxComponent = new GroupBoxComponent(this, module.getName(), 0.0f, 0.0f, 105f, 6.0f);
                            if (!module.cantToggle) {
                                CheckBoxTextComponent enabledButton = new CheckBoxTextComponent(groupBoxComponent, "Enabled", module::isEnabled, module::setEnabled);
                                enabledButton.addChild(new KeyBindComponent(enabledButton, module::getBind, module::setBind, 52.0f, -1f));
                                groupBoxComponent.addChild(enabledButton);
                            }
                            this.addChild(groupBoxComponent);
                            for (Value v : ValueManager.getValueByModName(module.getName())) {
                                Component component = null;
                                if (v instanceof BooleanValue) {
                                    BooleanValue booleanProperty = (BooleanValue) v;
                                    component = new CheckBoxTextComponent(groupBoxComponent, booleanProperty.getKey(), booleanProperty::getValue, booleanProperty::setValue);
                                } else if (v instanceof FloatValue) {
                                    FloatValue doubleProperty = (FloatValue) v;
                                    component = new SliderTextComponent(groupBoxComponent, doubleProperty.getKey(), doubleProperty::getValueState, doubleProperty::setValueState, doubleProperty::getDMin, doubleProperty::getDMax, doubleProperty::getDIncrement);
                                } else if (v instanceof ModeValue) {
                                    ModeValue enumProperty = (ModeValue) v;
                                    component = new ComboBoxTextComponent(groupBoxComponent, v.getKey(), enumProperty::getCurVal, enumProperty::getValues, enumProperty::setCurVal);
                                } else if (v instanceof ColorValue) {
                                    ColorValue colorProperty = (ColorValue) v;
                                    component = new ColorPickerTextComponent(groupBoxComponent, colorProperty.getKey(), colorProperty::getColorInt, colorProperty::setValueInt);
                                }

                                if (component == null) continue;
                                groupBoxComponent.addChild(component);
                            }
                        }
                    }
                    this.getChildren().sort(Comparator.comparingDouble(Component::getHeight).reversed());
                }
            };
            this.rootComponent.addChild(categoryTab);
        }

        this.selectedTab = (TabComponent) this.rootComponent.getChildren().get(this.selectorIndex);
        this.tabSelectorComponent = new Component(this.rootComponent, 3.5f, 5.0f, 48.0f, 341.5f) {
            private float selectorY;

            @Override
            public void onMouseClick(int mouseX, int mouseY, int button) {
                float mouseYOffset;
                if (this.isHovered(mouseX, mouseY) && (mouseYOffset = (float) mouseY - SkeetClickGUI.this.tabSelectorComponent.getY() - 10.0f) > 0.0f && mouseYOffset < SkeetClickGUI.this.tabSelectorComponent.getHeight() - 10.0f) {
                    SkeetClickGUI.this.selectorIndex = Math.min(ICONS.length - 1, (int) (mouseYOffset / (float) TAB_SELECTOR_HEIGHT));
                    SkeetClickGUI.this.selectedTab = (TabComponent) SkeetClickGUI.this.rootComponent.getChildren().get(SkeetClickGUI.this.selectorIndex);
                }
            }

            @Override
            public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
                this.selectorY = SkeetUtils.smoothAnimation(this.selectorY, SkeetClickGUI.this.selectorIndex * TAB_SELECTOR_HEIGHT + 10, 30, 0.3f);
                float x = this.getX();
                float y = this.getY();
                float width = this.getWidth();
                float height = this.getHeight();
                int innerColor = SkeetClickGUI.getColor(394758);
                int outerColor = SkeetClickGUI.getColor(0x202020);
                Gui.drawRect(x, y, x + width, y + this.selectorY, SkeetClickGUI.getColor(789516));
                Gui.drawRect((x + width - 1.0f), y, (x + width), y + this.selectorY, innerColor);
                Gui.drawRect((x + width - 0.5f), y, (x + width), y + this.selectorY, outerColor);
                Gui.drawRect(x, y + this.selectorY - 1.0f, (x + width - 0.5f), y + this.selectorY, innerColor);
                Gui.drawRect(x, y + this.selectorY - 0.5f, (x + width), y + this.selectorY, outerColor);
                Gui.drawRect(x, y + this.selectorY + TAB_SELECTOR_HEIGHT, (x + width), (y + height), SkeetClickGUI.getColor(789516));
                Gui.drawRect((x + width - 1.0f), y + this.selectorY + TAB_SELECTOR_HEIGHT, (x + width), (y + height), innerColor);
                Gui.drawRect((x + width - 0.5f), y + this.selectorY + TAB_SELECTOR_HEIGHT, (x + width), (y + height), outerColor);
                Gui.drawRect(x, y + this.selectorY + TAB_SELECTOR_HEIGHT, (x + width - 0.5f), y + this.selectorY + TAB_SELECTOR_HEIGHT + 1.0f, innerColor);
                Gui.drawRect(x, y + this.selectorY + TAB_SELECTOR_HEIGHT, (x + width), y + this.selectorY + TAB_SELECTOR_HEIGHT + 0.5f, outerColor);
                if (SkeetClickGUI.shouldRenderText()) {
                    int i = 0;
                    while (i < ICONS.length) {
                        String c = String.valueOf(ICONS[i]);
                        ICONS_RENDERER.drawString(c, x + 24.0f - ICONS_RENDERER.getStringWidth(c) / 2.0f - 1.0f, y + 10.0f + (float) (i * TAB_SELECTOR_HEIGHT) + (float) TAB_SELECTOR_HEIGHT / 2.0f - ICONS_RENDERER.getHeight(c) / 2.0f, SkeetClickGUI.getColor(i == SkeetClickGUI.this.selectorIndex ? 0xFFFFFF : 0x808080));
                        ++i;
                    }
                }
            }
        };
        this.rootComponent.addChild(this.tabSelectorComponent);
    }

    public static double getAlpha() {
        return alpha;
    }

    public static int getColor() {
        return SkeetClickGUI.getColor(CGUI.skeetGUiColours.getColorInt());
    }

    public static boolean shouldRenderText() {
        return alpha > 20.0;
    }

    private static boolean isVisible() {
        return open || alpha > 0.0;
    }

    public static int getColor(int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = (int) alpha;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            if (open) {
                this.targetAlpha = 0.0;
                open = false;
                this.dragging = false;
            }
        } else {
            this.rootComponent.onKeyPress(keyCode);
        }
    }

    public int photoIndex = 0;
    @Override
    public void drawScr(int mouseX, int mouseY, float partialTicks) {
        this.scale = 2f;
        if (!open && alpha == 0.0 && !this.closed) {
            mc.displayGuiScreen(null);
            return;
        }

        if (SkeetClickGUI.isVisible()) {
            alpha = SkeetUtils.linearAnimation(alpha, this.targetAlpha, 10.0);
            this.rootComponent.drawComponent(new LockedResolution((int) curWidth, (int) curHeight), mouseX, mouseY);
            this.onMouseScroll(mouseX, mouseY, Mouse.getDWheel());
        }

        GlStateManager.translate(0, 0, 5f);
        final float xx = mouseX - 0.5f;
        final float yy = mouseY - 0.5f;
        FontManager.icon18.drawString("p", xx + 0.6f - 1, yy + 0.3f, 0xFF000000);
        FontManager.icon18.drawString("p", xx - 1, yy, CGUI.cursorColours.getColorInt());
        GlStateManager.translate(0, 0, -5f);
    }

    public void initGui() {
        if (rootComponent.getX() < 5 && rootComponent.getY() < 5) {
           ScaledResolution sr = new ScaledResolution(mc);
           rootComponent.setX((sr.getScaledWidth() - rootComponent.getWidth()) / 2);
           rootComponent.setY((sr.getScaledHeight() - rootComponent.getHeight()) / 2);
        }
        timeHelper.reset();
        needUpdateValue = true;

        try {
            int min = org.lwjgl.input.Cursor.getMinCursorSize();
            IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
            org.lwjgl.input.Cursor emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
            Mouse.setNativeCursor(emptyCursor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int mouseButton) {
        if (SkeetClickGUI.isVisible()) {
            this.rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int state) {
        if (SkeetClickGUI.isVisible()) {
            this.rootComponent.onMouseRelease(state);
        }
    }

    public void onMouseScroll(int mouseX, int mouseY, int value) {
        this.rootComponent.onMouseScroll(mouseX, mouseY, value);
    }

    public void updateValue(String valGroup, String valKey, Object oldVal, Object val) {
        this.rootComponent.updateValue(valGroup, valKey, oldVal, val);
    }

    @Override
    public void onGuiClosed() {
        try {
            Mouse.setNativeCursor(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

