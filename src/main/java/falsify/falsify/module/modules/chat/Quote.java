package falsify.falsify.module.modules.chat;

import com.google.gson.JsonObject;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.ModeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.JsonHelper;

public class Quote extends Module {
    private final ModeSetting type = new ModeSetting("Type", "Normal", "Normal", "Kanye");
    public Quote() {
        super("Quote", "Finds a random quote and types it in chat.", false, Category.MISC, -1);
        settings.add(type);
    }

    @Override
    public void onEnable() {
        switch (type.getMode()) {
            case "Normal" -> sendNormalQuote();
            case "Kanye" -> sendKanyeQuote();
        }
        this.toggle();
    }

    private void sendNormalQuote() {
        new FalseRunnable() {
            @Override
            public void run() {
                JsonObject json = JsonHelper.fromUrl("https://api.quotable.io/random");
                if(json == null || !json.has("author") || !json.has("content")) return;
                ChatModuleUtils.sendMessage("\"" + json.getAsJsonPrimitive("content").getAsString() + "\" -" + json.getAsJsonPrimitive("author").getAsString(), false);
            }
        }.runTaskAsync();
    }

    private void sendKanyeQuote() {
        new FalseRunnable() {
            @Override
            public void run() {
                JsonObject json = JsonHelper.fromUrl("https://api.kanye.rest/");
                if(json == null || !json.has("quote")) return;
                ChatModuleUtils.sendMessage("\"" + json.getAsJsonPrimitive("quote").getAsString() + "\" -Kanye West", false);
            }
        }.runTaskAsync();
    }
}
