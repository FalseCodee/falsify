package falsify.falsify.module.modules;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventRender;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.ModuleManager;
import falsify.falsify.utils.Alignment;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;

public class GuiArrayList extends Module {
    public GuiArrayList() {
        super("Arraylist", Category.MISC, GLFW.GLFW_KEY_F10, true);
    }

    @Override
    public void onEvent(Event e) {
        if(e instanceof EventRender){
            int count = 0;
            ModuleManager.enabledModules.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(((Module)m).name)).reversed());
            for(Module module : ModuleManager.enabledModules){
                if(module instanceof GuiArrayList){
                    continue;
                }
                RenderUtils.AlignFill(mc.textRenderer.getWidth(module.name)+10,(mc.textRenderer.fontHeight+6)*count, 0,(mc.textRenderer.fontHeight+6)*(count+1), 0x70000000, Alignment.RIGHT);
                RenderUtils.AlignText(module.name,5, (mc.textRenderer.fontHeight+6)*count+3, -1, Alignment.RIGHT);
                count++;
            }
            RenderUtils.AlignCenteredText(Math.round(mc.player.getX())+", "+Math.round(mc.player.getY())+", "+Math.round(mc.player.getZ()),0,mc.textRenderer.fontHeight+10, 0xff6054, Alignment.XCENTER);
            RenderUtils.AlignCenteredText(mc.player.getHorizontalFacing().getName(),0,2*mc.textRenderer.fontHeight+10, 0xff6054, Alignment.XCENTER);

        }
    }
}
