package falsify.falsify.module.modules.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.ColorUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class PluginFinder extends Module {
    public PluginFinder() {
        super("Plugin Finder", "Finds all plugins on a server.", false, Category.MISC, -1);
    }

    @Override
    public void onEnable() {
        if(mc.player == null || mc.world == null || mc.isConnectedToLocalServer()) {
            this.toggle();
            return;
        }
        CommandDispatcher<CommandSource> commandDispatcher = mc.player.networkHandler.getCommandDispatcher();

        ParseResults<CommandSource> parse = commandDispatcher.parse(new StringReader(""), mc.player.networkHandler.getCommandSource());

        CompletableFuture<Suggestions> pendingSuggestions = commandDispatcher.getCompletionSuggestions(parse, 0);
        pendingSuggestions.thenRun(() -> {
            if (!pendingSuggestions.isDone()) {
                return;
            }

            StringBuilder builder = new StringBuilder();
            int sum = 0;
            for(Suggestion s : pendingSuggestions.getNow(null).getList()) {
                String text = s.getText();
                int i;
                if((i = text.indexOf(":")) != -1 && !builder.toString().contains(text.substring(0, i) + "§f,")) {
                    builder.append(ColorUtils.CC_PREFIX).append("a")
                            .append(text, 0, i)
                            .append(ColorUtils.CC_PREFIX).append("f")
                            .append(", ");
                    sum++;
                }
            }

            mc.player.sendMessage(Text.of("Found (" + ColorUtils.CC_PREFIX + "a" + sum + ColorUtils.CC_PREFIX + "f" + ") Plugins: " + builder), false);
            this.toggle();
        });
    }
}
