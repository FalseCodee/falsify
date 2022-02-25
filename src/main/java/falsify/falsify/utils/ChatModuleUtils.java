package falsify.falsify.utils;

import falsify.falsify.Falsify;

import java.awt.event.KeyEvent;
import java.util.UUID;

public class ChatModuleUtils {
    public static boolean isPlayer(String player){
        return Falsify.mc.getNetworkHandler().getPlayerListEntry(player) != null;
    }

    public static boolean isPlayer(UUID player){
        return Falsify.mc.getNetworkHandler().getPlayerListEntry(player) != null;
    }

    public static String keyCodeToString(int keyCode) {
        String text = KeyEvent.getKeyText(keyCode);

        if(text.contains("Unknown keyCode: ")) return "NONE";
        return text;
    }
}
