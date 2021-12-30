package falsify.falsify.module.modules.chat;

import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.utils.ChatModuleUtils;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class rps extends ChatModule {
    public rps() {
        super("RPS", Category.MISC, GLFW.GLFW_KEY_L);
    }
    HashMap<String,String> playerHashMap = new HashMap<>();
    HashMap<String,String> rpsHashMap = new HashMap<>();

    @Override
    public void onChat(String msg) {
        String[] message = msg.split("\\s+");
        if(msg.contains(" -> me] ") && msg.startsWith("[")){
            String player = message[0].replace("[", "");
            if(ChatModuleUtils.isPlayer(player) && (playerHashMap.containsKey(player) || playerHashMap.containsValue(player))){
                if(!rpsHashMap.containsKey(player)){
                    if(message[3].equalsIgnoreCase("rock") || message[3].equalsIgnoreCase("paper") || message[3].equalsIgnoreCase("scissors")){
                        rpsHashMap.put(player, message[3]);
                        if(playerHashMap.containsKey(player)){
                            if(rpsHashMap.containsKey(playerHashMap.get(player))){
                                winState(player, playerHashMap.get(player));
                            }
                        } else{
                            for(String key : playerHashMap.keySet()){
                                if(player.equalsIgnoreCase(playerHashMap.get(key))){
                                    if(rpsHashMap.containsKey(key)){
                                        winState(key, player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(message.length == 5){
            if(message[2].equalsIgnoreCase(">>")){
                if(message[3].equalsIgnoreCase(".rps")){
                    if(ChatModuleUtils.isPlayer(message[4])){
                        playerHashMap.put(message[4], message[1]);
                        mc.player.sendChatMessage(message[4] + " and " + message[1] + ", message me rock, paper, or scissors");
                    }
                }
            }
        }
    }
    private void winState(String p1, String p2){
        String RPSP1 = rpsHashMap.get(p1);
        String RPSP2 = rpsHashMap.get(p2);
        boolean player1 = true;
        if(RPSP1.equalsIgnoreCase(RPSP2)){
            mc.player.sendChatMessage(p1 + " and " + p2 + " tied. (" + RPSP1.toLowerCase() + ")");
            return;
        } else {
            switch(RPSP1.toLowerCase()){
                case "rock":
                    switch (RPSP2.toLowerCase()){
                        case "paper":
                            player1=false;
                            break;
                        case "scissors":
                            player1=true;
                            break;
                    }
                    break;
                case "paper":
                    switch (RPSP2.toLowerCase()){
                        case "rock":
                            player1=true;
                            break;
                        case "scissors":
                            player1=false;
                            break;
                    }
                    break;
                case "scissors":
                    switch (RPSP2.toLowerCase()){
                        case "paper":
                            player1=true;

                            break;
                        case "rock":
                            player1=false;
                            break;
                    }
                    break;
            }
            if(player1){
                mc.player.sendChatMessage(p1 + " won. ("+ RPSP1.toLowerCase() + " against " + RPSP2.toLowerCase() + ")");
            } else {
                mc.player.sendChatMessage(p2 + " won. ("+ RPSP2.toLowerCase() + " against " + RPSP1.toLowerCase() + ")");
            }
            rpsHashMap.remove(p1);
            rpsHashMap.remove(p2);
            playerHashMap.remove(p1);

        }
    }
}
