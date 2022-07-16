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

    public static String joinString(String[] arr, int start) {
        StringBuilder sb = new StringBuilder();
        for(int i = start; i < arr.length; i++) {
            sb.append(arr[i]);
            if(i != arr.length-1) sb.append(" ");
        }
        return sb.toString();
    }
}
