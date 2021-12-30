package falsify.falsify.utils;

import falsify.falsify.Falsify;

public class ChatModuleUtils {
    public static boolean isPlayer(String player){
        return Falsify.mc.getNetworkHandler().getPlayerListEntry(player) != null;
    }
}
