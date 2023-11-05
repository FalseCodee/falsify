package falsify.falsify.module.modules.render;

import falsify.falsify.Falsify;
import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.Alignment;
import falsify.falsify.utils.RenderUtils;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class GuiArrayList extends Module {

    public GuiArrayList() {
        super("Arraylist", "Shows Active Mods", false, Category.RENDER, -1);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventRender e){
            ModuleManager.enabledModules.sort(Comparator.comparingDouble(m -> Falsify.fontRenderer.getStringWidth(((Module)m).name)).reversed());
            List<Module> filteredModules = ModuleManager.enabledModules.stream().filter(module -> !(module instanceof GuiArrayList)).toList();
            for(int i = 0; i < filteredModules.size(); i++){
                Module module = filteredModules.get(i);
                int width = (int) Falsify.fontRenderer.getStringWidth(module.name);
                int height = (int) Falsify.fontRenderer.getStringHeight(module.name);
                RenderUtils.AlignFill(e.getDrawContext(), width+10,(height+6)*i, 0,(height+6)*(i+1), 0x70000000, Alignment.RIGHT);
                RenderUtils.AlignFill(e.getDrawContext(), width+10-1,(height+6)*i-1, width+10+1,(height+6)*(i+1)+1, module.category.getColor().getRGB(), Alignment.RIGHT);
                RenderUtils.AlignFillGradient(e.getDrawContext(), width+10+1,(height+6)*(i+1)-1, (i == filteredModules.size()-1) ? 0 : (int)Falsify.fontRenderer.getStringWidth(filteredModules.get(i+1).name)+10-1,(height+6)*(i+1)+1, module.category.getColor().getRGB(), (i == filteredModules.size()-1) ? module.category.getColor().getRGB() : filteredModules.get(i+1).category.getColor().getRGB(),  Alignment.RIGHT);

                RenderUtils.AlignText(e.getDrawContext(), module.name,5, (height+6)*i+3, Color.WHITE, Alignment.RIGHT);
            }
            RenderUtils.AlignCenteredText(e.getDrawContext(), Math.round(mc.player.getX())+", "+Math.round(mc.player.getY())+", "+Math.round(mc.player.getZ()),0,mc.textRenderer.fontHeight+10, new Color(255, 96, 84), Alignment.XCENTER);
            RenderUtils.AlignCenteredText(e.getDrawContext(), mc.player.getHorizontalFacing().getName(),0,2*mc.textRenderer.fontHeight+10, new Color(255, 96, 84), Alignment.XCENTER);
        }
    }
}
