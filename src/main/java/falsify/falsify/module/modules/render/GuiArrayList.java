package falsify.falsify.module.modules.render;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.Alignment;
import falsify.falsify.utils.RenderUtils;

import java.util.Comparator;
import java.util.List;

public class GuiArrayList extends Module {

    public GuiArrayList() {
        super("Arraylist", "Shows Active Mods", false, Category.RENDER, -1);
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventRender e){
            ModuleManager.enabledModules.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(((Module)m).name)).reversed());
            List<Module> filteredModules = ModuleManager.enabledModules.stream().filter(module -> !(module instanceof GuiArrayList)).toList();
            for(int i = 0; i < filteredModules.size(); i++){
                Module module = filteredModules.get(i);

                RenderUtils.AlignFill(e.getDrawContext(), mc.textRenderer.getWidth(module.name)+10,(mc.textRenderer.fontHeight+6)*i, 0,(mc.textRenderer.fontHeight+6)*(i+1), 0x70000000, Alignment.RIGHT);
                RenderUtils.AlignText(e.getDrawContext(), module.name,5, (mc.textRenderer.fontHeight+6)*i+3, -1, Alignment.RIGHT);

                RenderUtils.AlignFill(e.getDrawContext(), mc.textRenderer.getWidth(module.name)+10-1,(mc.textRenderer.fontHeight+6)*i-1, mc.textRenderer.getWidth(module.name)+10+1,(mc.textRenderer.fontHeight+6)*(i+1)+1, module.category.getColor().getRGB(), Alignment.RIGHT);
                RenderUtils.AlignFillGradient(e.getDrawContext(), mc.textRenderer.getWidth(module.name)+10+1,(mc.textRenderer.fontHeight+6)*(i+1)-1, (i == filteredModules.size()-1) ? 0 : mc.textRenderer.getWidth(filteredModules.get(i+1).name)+10-1,(mc.textRenderer.fontHeight+6)*(i+1)+1, module.category.getColor().getRGB(), (i == filteredModules.size()-1) ? module.category.getColor().getRGB() : filteredModules.get(i+1).category.getColor().getRGB(),  Alignment.RIGHT);
            }
            RenderUtils.AlignCenteredText(e.getDrawContext(), Math.round(mc.player.getX())+", "+Math.round(mc.player.getY())+", "+Math.round(mc.player.getZ()),0,mc.textRenderer.fontHeight+10, 0xff6054, Alignment.XCENTER);
            RenderUtils.AlignCenteredText(e.getDrawContext(), mc.player.getHorizontalFacing().getName(),0,2*mc.textRenderer.fontHeight+10, 0xff6054, Alignment.XCENTER);
//            List<Entity> entityList = Lists.newArrayList(mc.world.getEntities()).stream().filter(LivingEntity.class::isInstance).sorted(Comparator.comparingDouble(entity -> 5 * entity.squaredDistanceTo(mc.player) + 1 * MathUtils.squaredCursorDistanceTo(entity)/5)).collect(Collectors.toList());
//            if(entityList.size()==0) return;
//            Entity entity = entityList.get(0);
//            LivingEntity en = (LivingEntity) entity;
//                float scale = MathUtils.clamp((float) (1/mc.player.squaredDistanceTo(en))*100, 0.25f, 1f);
//                if(scale <= 0.5f) return;
//                float[] pos = MathUtils.toScreenXY(MathUtils.getInterpolatedPos(entity, e.getTickDelta()));
//                float lowerCorrection = MathUtils.clamp(mc.getWindow().getScaledHeight()/4f+pos[1]-mc.getWindow().getScaledHeight(), 0, 100);
//
//                MatrixStack matrices = e.getDrawContext().getMatrices();
//                matrices.push();
//                matrices.scale(scale, scale, 1);
//                matrices.translate(pos[0]/scale, (pos[1]-lowerCorrection)/scale, 0);
//                if((pos[1]-lowerCorrection) > mc.getWindow().getScaledHeight()-50) {
//                    matrices.translate((pos[0] > mc.getWindow().getScaledWidth()/2f ? -1 : 1)*lowerCorrection*2/scale, -lowerCorrection*2/scale, 0);
//                }
//                e.getDrawContext().fill( -75,  -25,  75, 25, new Color(56, 56, 56, 194).getRGB());
//                e.getDrawContext().drawCenteredTextWithShadow(mc.textRenderer, en.getDisplayName(), 0, -23, Color.WHITE.getRGB());
//                e.getDrawContext().drawCenteredTextWithShadow(mc.textRenderer, "Can Take Damage: " + (en.canTakeDamage() ? "Yes" : "No"), 0, -13, Color.WHITE.getRGB());
//
//                e.getDrawContext().drawCenteredTextWithShadow(mc.textRenderer, en.getHealth() + " / " + en.getMaxHealth(), 0, 11, Color.WHITE.getRGB());
//                float percentage = en.getHealth()/ en.getMaxHealth();
//                e.getDrawContext().fill( -75,  20, (int) (150*percentage-75), 21, new Color((1-percentage), percentage, 56/255f, 219/255f).getRGB());
//                matrices.pop();

        }
    }
}
