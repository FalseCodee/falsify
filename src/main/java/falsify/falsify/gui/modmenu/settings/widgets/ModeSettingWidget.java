package falsify.falsify.gui.modmenu.settings.widgets;

import falsify.falsify.Falsify;
import falsify.falsify.gui.utils.Clickable;
import falsify.falsify.gui.modmenu.primitives.Panel;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.RenderUtils;
import falsify.falsify.utils.fonts.FontRenderer;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class ModeSettingWidget extends SettingWidget<ModeSetting> {

    private final DropdownBox dropdownBox;
    public ModeSettingWidget(ModeSetting setting, Panel panel, double x, double y, double width) {
        super(setting, panel, x, y, width, 25);
        this.dropdownBox = new DropdownBox(setting, x+width-65, y+height/2-10);
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        isActive = dropdownBox.handleClick(x, y, button);
        return isActive;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.dropdownBox.setY(y+height/2-10);
        FontRenderer fr = Falsify.fontRenderer;
        pushStackToPosition(context.getMatrices());
        RenderUtils.fillXGradient(context, 50, (int) height+1, (int) width, (int) height+2, 0, panel.getTheme().primaryColor().darker().getRGB());
        context.getMatrices().push();
        context.getMatrices().scale(1.2f, 1.2f, 1);
        fr.drawString(context, setting.getName(), 5F, (float) height/2-fr.getStringHeight(setting.getName())*1.2f/2, panel.getTheme().primaryTextColor(), true);
        context.getMatrices().pop();

        context.getMatrices().pop();
        dropdownBox.render(context, mouseX, mouseY, delta);
    }

    public class DropdownBox extends Clickable {

        private final ModeSetting modeSetting;
        private final ArrayList<Clickable> modes = new ArrayList<>();
        private boolean extended = false;
        public DropdownBox(ModeSetting modeSetting, double x, double y) {
            super(x, y, 60, 20);
            this.modeSetting = modeSetting;
            modeSetting.getValue().forEach(this::addMode);
        }

        private void addMode(String mode) {
            Clickable modeClickable = new Clickable.ButtonBuilder()
                    .pos(DropdownBox.this.x, DropdownBox.this.y+DropdownBox.this.height*(modes.size()+1))
                    .dimensions(width, height)
                    .onClick((instance, x1, y1, button) -> {
                        if(instance.isHovering(x1, y1)) {
                            modeSetting.setIndex(modeSetting.getValue().indexOf(mode));
                            return true;
                        }
                        return false;
                    })
                    .onRender((instance, context, mouseX, mouseY, delta) -> {
                        Color color = panel.getTheme().secondaryColor();
                        if(modeSetting.getMode().equals(mode)) color = color.brighter();
                        instance.pushStackToPosition(context.getMatrices());
                        context.getMatrices().translate(0,0,0.03);
                        context.fill(0,0, (int) instance.getWidth(), (int) instance.getHeight(), color.getRGB());
                        Falsify.fontRenderer.drawCenteredString(context, mode, (float) (instance.getWidth()/2f), (float) (instance.getHeight()/2f-Falsify.fontRenderer.getStringHeight(mode)/2f), panel.getTheme().primaryTextColor(), true);
                        context.getMatrices().pop();
                    }).build();
            modes.add(modeClickable);
        }

        @Override
        public boolean handleClick(double x, double y, int button) {
            if(isHovering(x, y)) {
                this.extended = !this.extended;
                return true;
            }
            else if(extended) {
                for (Clickable clickable : modes) {
                    if (clickable.handleClick(x, y, button)) return true;
                }
            }
            return false;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            FontRenderer fr = Falsify.fontRenderer;
            setPositions();
            DropdownBox.this.pushStackToPosition(context.getMatrices());
            context.fill(0,0, (int) width, (int) height, panel.getTheme().secondaryColor().getRGB());
            String message = (extended) ? "Select Mode" : modeSetting.getMode();
            fr.drawCenteredString(context, message, (float) (DropdownBox.this.width/2f), (float) (DropdownBox.this.height/2f-fr.getStringHeight(message)/2f), panel.getTheme().primaryTextColor(), true);
            context.getMatrices().pop();

            if(extended) modes.forEach(clickable -> clickable.render(context, mouseX, mouseY, delta));
        }

        private void setPositions() {
            for(int i = 0; i < modes.size(); i++) {
                Clickable clickable = modes.get(i);
                clickable.setY(DropdownBox.this.y+DropdownBox.this.height*(i+1));
            }
        }
    }
}
