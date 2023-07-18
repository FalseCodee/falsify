package falsify.falsify.module.modules.chat;

import com.google.gson.JsonObject;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.JsonHelper;

public class Quote extends Module {
    public Quote() {
        super("Quote", "Finds a random quote and types it in chat.", false, Category.MISC, -1);
    }

    @Override
    public void onEnable() {
        new FalseRunnable() {
            @Override
            public void run() {
                JsonObject json = JsonHelper.fromUrl("https://api.quotable.io/random");
                if(json == null || !json.has("author") || !json.has("content")) return;
                ChatModuleUtils.sendMessage("\"" + json.getAsJsonPrimitive("content").getAsString() + "\" -" + json.getAsJsonPrimitive("author").getAsString(), false);
            }
        }.runTaskAsync();
        this.toggle();
    }
}
