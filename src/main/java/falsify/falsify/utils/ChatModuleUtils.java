package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
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

    public static String concatArray(String[] array, String concat) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if(i < array.length - 1) {
                sb.append(concat);
            }
        }

        return sb.toString();
    }

    public static String concatArray(List<Text> array, String concat) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.size(); i++) {
            sb.append(array.get(i).getString());
            if(i < array.size() - 1) {
                sb.append(concat);
            }
        }

        return sb.toString();
    }

    public static String capitalize(String string) {
        if(string.contains(" ")) {
            String[] args = string.split(" ");

            for (String str : args) {
                capitalizeFirst(str);
            }

            return concatArray(args, " ");
        } else {
            return capitalizeFirst(string);
        }
    }

    public static boolean sendMessage(String chatText, boolean addToHistory) {
        chatText = normalize(chatText);
        if (chatText.isEmpty()) {
            return true;
        } else {
            if (addToHistory) {
                Falsify.mc.inGameHud.getChatHud().addToMessageHistory(chatText);
            }

            if (chatText.startsWith("/")) {
                Falsify.mc.player.networkHandler.sendChatCommand(chatText.substring(1));
            } else {
                Falsify.mc.player.networkHandler.sendChatMessage(chatText);
            }

            return true;
        }
    }

     public static String normalize(String chatText) {
        return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
    }


    public static String capitalizeFirst(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }
}
