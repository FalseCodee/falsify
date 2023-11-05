package falsify.falsify.utils;

import falsify.falsify.Falsify;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

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
        if(keyCode == -1) return "NONE";
        if (keyCode >= 33 && keyCode <= 96) {
            return String.valueOf((char)keyCode);
        }
        return switch (keyCode) {
            case 32 -> "SPACE";
            case 259 -> "DELETE";
            case 290 -> "F1";
            case 291 -> "F2";
            case 292 -> "F3";
            case 293 -> "F4";
            case 294 -> "F5";
            case 295 -> "F6";
            case 296 -> "F7";
            case 297 -> "F8";
            case 298 -> "F9";
            case 299 -> "F10";
            case 300 -> "F11";
            case 301 -> "F12";
            case 340 -> "SHIFT";
            case 256 -> "ESCAPE";
            case 265 -> "UP ARROW";
            case 263 -> "LEFT ARROW";
            case 264 -> "DOWN ARROW";
            case 262 -> "RIGHT ARROW";
            case 320 -> "NUMPAD 0";
            case 321 -> "NUMPAD 1";
            case 322 -> "NUMPAD 2";
            case 323 -> "NUMPAD 3";
            case 324 -> "NUMPAD 4";
            case 325 -> "NUMPAD 5";
            case 326 -> "NUMPAD 6";
            case 327 -> "NUMPAD 7";
            case 328 -> "NUMPAD 8";
            case 329 -> "NUMPAD 9";
            case 330 -> "NUMPAD .";
            case 282 -> "NUM LOCK";
            case 331 -> "NUMPAD /";
            case 332 -> "NUMPAD *";
            case 333 -> "NUMPAD -";
            case 334 -> "NUMPAD +";
            case 335 -> "NUMPAD ENTER";
            case 341 -> "LEFT CONTROL";
            case 342 -> "LEFT ALT";
            case 343 -> "LEFT HOME";
            case 346 -> "RIGHT ALT";
            case 347 -> "RIGHT HOME";
            case 345 -> "RIGHT CONTROL";
            default -> "UNKNOWN";
        };
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
        if (!chatText.isEmpty()) {
            if (addToHistory) {
                Falsify.mc.inGameHud.getChatHud().addToMessageHistory(chatText);
            }

            if (chatText.startsWith("/")) {
                Falsify.mc.player.networkHandler.sendChatCommand(chatText.substring(1));
            } else {
                Falsify.mc.player.networkHandler.sendChatMessage(chatText);
            }

        }
        return true;
    }

    public static Text join(Text text1, Text text2) {
        MutableText t = MutableText.of(text1.getContent());
        t.setStyle(text1.getStyle());
        return t.append(text2);
    }

     public static String normalize(String chatText) {
        return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
    }


    public static String capitalizeFirst(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }
}
