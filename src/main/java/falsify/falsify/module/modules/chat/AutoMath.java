package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventPacketRecieve;
import falsify.falsify.listeners.events.EventPacketSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.ChatModule;
import falsify.falsify.module.settings.BooleanSetting;
import falsify.falsify.module.settings.RangeSetting;
import falsify.falsify.utils.ChatModuleUtils;
import falsify.falsify.utils.FalseRunnable;
import falsify.falsify.utils.MessageExecutor;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

public class AutoMath extends ChatModule {

    public AutoMath() {
        super("Auto Math", "Automatically solve math.", Category.MISC, -1);
        settings.add(chatBot);
        settings.add(autoSolve);
        settings.add(showEquation);
        settings.add(delay);
        vars.put("pi", Math.PI);
        vars.put("e", Math.E);
        vars.put("g", (1 + Math.sqrt(5)) / 2);
        funcs.put("rad", "x / 0.017453292519943295");
    }
    private final BooleanSetting chatBot = new BooleanSetting("ChatBot", false);
    private final BooleanSetting autoSolve = new BooleanSetting("Auto Solve", false);
    private final BooleanSetting showEquation = new BooleanSetting("Show Equation", false);
    private final RangeSetting delay = new RangeSetting("Delay", 1000, 1, 5000, 50);
    private final HashMap<String, Double> vars = new HashMap<>();
    private final HashMap<String, String> funcs = new HashMap<>();
    final DecimalFormat format = new DecimalFormat("#.################");

    @Override
    public void onEvent(Event<?> event) {
        super.onEvent(event);
        if(event instanceof EventPacketSend packetSend && packetSend.getPacket() instanceof ChatMessageC2SPacket packet) {
            String message = packet.chatMessage();
            if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " solve ")) {
                message = message.toLowerCase();
                mc.player.sendMessage(Text.of(message));
                final String solveThis = mc.player.getGameProfile().getName().toLowerCase() + " solve ";
                final int beginIndex = solveThis.length() + message.indexOf(solveThis);
                message = message.substring(beginIndex).toLowerCase();
                String finalMessage = message;
                new FalseRunnable() {
                    @Override
                    public void run() {
                        try {
                            if (showEquation.getValue()) ChatModuleUtils.sendMessage(finalMessage + " = " + format.format(eval(finalMessage, vars, funcs)), true);
                            else ChatModuleUtils.sendMessage(format.format(eval(finalMessage, vars, funcs)), true);
                        } catch (RuntimeException e) {
                            ChatModuleUtils.sendMessage(e.getMessage(), false);
                        }
                    }
                }.runTaskLater(500);
            } else if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " push ")) {
                String[] args = message.split(" ");
                int startIndex = 0;
                while (!args[startIndex].toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase())) startIndex++;
                if(args.length < startIndex + 1 + 4) {
                    new MessageExecutor("Invalid Syntax", 500).runTaskLater();
                    return;
                }
                try {
                    String var = args[startIndex + 2].toLowerCase();
                    if(var.equalsIgnoreCase("x")) throw new RuntimeException("The variable 'x' is reserved for function declarations.");
                    if(funcs.containsKey(var)) throw new RuntimeException("'" + var + "' is already defined as a function.");
                    double val = eval(ChatModuleUtils.joinString(args, startIndex + 4), vars, funcs);
                    vars.put(var,val);
                    new MessageExecutor("loaded '" + var + "' as " + format.format(val), 500).runTaskLater();
                } catch (Exception e) {
                    ChatModuleUtils.sendMessage(e.getMessage(), false);
                }
            }
            else if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " pushfunc ")) {
                String[] args = message.split(" ");
                int startIndex = 0;
                while (!args[startIndex].toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase())) startIndex++;
                if(args.length < startIndex + 1 + 4) {
                    new MessageExecutor("Invalid Syntax", 500).runTaskLater();
                    return;
                }
                try {
                    String var = args[startIndex + 2].toLowerCase();
                    if(vars.containsKey(var)) throw new RuntimeException("'" + var + "' is already defined as a variable.");
                    String val = ChatModuleUtils.joinString(args, startIndex + 4);
                    eval(val.replace("x", "0"), vars, funcs);
                    funcs.put(var,val);
                    new MessageExecutor("loaded function '" + var + "(x)' as " + val, 500).runTaskLater();
                } catch (Exception e) {
                    ChatModuleUtils.sendMessage(e.getMessage(), false);
                }
            }
            else if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " graph ")) {
                String[] args = message.split(" ");
                int startIndex = 0;
                while (!args[startIndex].toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase())) startIndex++;
                if(args.length < startIndex + 1 + 10) {
                    new MessageExecutor("Invalid Syntax", 500).runTaskLater();
                    return;
                }
                try {
                    String var = args[startIndex + 2].toLowerCase();
                    double from = Double.parseDouble(args[startIndex + 4].toLowerCase());
                    double to = Double.parseDouble(args[startIndex + 6].toLowerCase());
                    double fromY = Double.parseDouble(args[startIndex + 8].toLowerCase());
                    double toY = Double.parseDouble(args[startIndex + 10].toLowerCase());
                    String[] graph = graph(var, from, to, 17, fromY, toY, vars, funcs);
                    for(int i = 0; i < graph.length; i++) {
                        new MessageExecutor(graph[i], 500 + delay.getValue().longValue() * (graph.length-i)).runTaskLater();
                    }
                } catch (Exception e) {
                    ChatModuleUtils.sendMessage(e.getMessage(), false);
                }
            }
        }
    }

    @Override
    public void onChat(EventPacketRecieve eventPacketRecieve, String message) {
        if(message == null || message.split(" ").length == 0 || mc.player == null) return;
        if(autoSolve.getValue() && message.startsWith("[MathC] New Question: ")) {
            message = message.substring("[MathC] New Question: ".length(), message.length()-4).replace("x", "*");
            int val = (int) eval(message, vars, funcs);
            new FalseRunnable() {
                @Override
                public void run() {
                    mc.player.sendMessage(Text.of(val + ""));
                }
            }.runTaskLater((long) ((val > 200) ? (Math.random()*6000L) : (Math.random()*2000L))+500);
        } else if(chatBot.getValue()) {
            if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " solve ")) {
                message = message.toLowerCase();
                mc.player.sendMessage(Text.of(message));
                final String solveThis = mc.player.getGameProfile().getName().toLowerCase() + " solve ";
                final int beginIndex = solveThis.length() + message.indexOf(solveThis);
                message = message.substring(beginIndex).toLowerCase();
                String finalMessage = message;
                new FalseRunnable() {
                    @Override
                    public void run() {
                        try {
                            if (showEquation.getValue()) ChatModuleUtils.sendMessage(finalMessage + " = " + format.format(eval(finalMessage, vars, funcs)), true);
                            else ChatModuleUtils.sendMessage(format.format(eval(finalMessage, vars, funcs)), true);
                        } catch (RuntimeException e) {
                            ChatModuleUtils.sendMessage(e.getMessage(), false);
                        }
                    }
                }.runTaskLater(500);
            } else if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " push ")) {
                String[] args = message.split(" ");
                int startIndex = 0;
                while (!args[startIndex].toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase())) startIndex++;
                if(args.length < startIndex + 1 + 4) {
                    new MessageExecutor("Invalid Syntax", 500).runTaskLater();
                    return;
                }
                try {
                    String var = args[startIndex + 2].toLowerCase();
                    if(var.equalsIgnoreCase("x")) throw new RuntimeException("The variable 'x' is reserved for function declarations.");
                    if(funcs.containsKey(var)) throw new RuntimeException("'" + var + "' is already defined as a function.");
                    double val = eval(ChatModuleUtils.joinString(args, startIndex + 4), vars, funcs);
                    vars.put(var,val);
                    new MessageExecutor("loaded '" + var + "' as " + format.format(val), 500).runTaskLater();
                } catch (Exception e) {
                    ChatModuleUtils.sendMessage(e.getMessage(), false);
                }
            }
            else if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " pushfunc ")) {
                String[] args = message.split(" ");
                int startIndex = 0;
                while (!args[startIndex].toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase())) startIndex++;
                if(args.length < startIndex + 1 + 4) {
                    new MessageExecutor("Invalid Syntax", 500).runTaskLater();
                    return;
                }
                try {
                    String var = args[startIndex + 2].toLowerCase();
                    if(vars.containsKey(var)) throw new RuntimeException("'" + var + "' is already defined as a variable.");
                    String val = ChatModuleUtils.joinString(args, startIndex + 4);
                    eval(val.replace("x", "0"), vars, funcs);
                    funcs.put(var,val);
                    new MessageExecutor("loaded function '" + var + "(x)' as " + val, 500).runTaskLater();
                } catch (Exception e) {
                    ChatModuleUtils.sendMessage(e.getMessage(), false);
                }
            }
            else if (message.toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase() + " graph ")) {
                String[] args = message.split(" ");
                int startIndex = 0;
                while (!args[startIndex].toLowerCase().contains(mc.player.getGameProfile().getName().toLowerCase())) startIndex++;
                if(args.length < startIndex + 1 + 10) {
                    new MessageExecutor("Invalid Syntax", 500).runTaskLater();
                    return;
                }
                try {
                    String var = args[startIndex + 2].toLowerCase();
                    double from = Double.parseDouble(args[startIndex + 4].toLowerCase());
                    double to = Double.parseDouble(args[startIndex + 6].toLowerCase());
                    double fromY = Double.parseDouble(args[startIndex + 8].toLowerCase());
                    double toY = Double.parseDouble(args[startIndex + 10].toLowerCase());
                    String[] graph = graph(var, from, to, 17, fromY, toY, vars, funcs);
                    for(int i = 0; i < graph.length; i++) {
                        new MessageExecutor(graph[i], 500 + delay.getValue().longValue() * (graph.length-i)).runTaskLater();
                    }
                } catch (Exception e) {
                    ChatModuleUtils.sendMessage(e.getMessage(), false);
                }
            }
        }
    }

    public static String[] graph(final String function, double from, double to, int stepCount, double fromY, double toY, HashMap<String, Double> vars, HashMap<String, String> funcs) {
        if(!funcs.containsKey(function)) return new String[0];
        if(from > to) {
            double x = from;
            from = to;
            to = x;
        }
        String[] arr = new String[stepCount];
        double step = (toY - fromY) / (stepCount-1.0);
        double stepX = (to - from) / (stepCount -1.0);
        double avgStep = (step + stepX) / 2;
        Arrays.fill(arr, "");
        for(int i = 0; i < stepCount; i++) {
            double x = from + stepX*i;
            double y = eval(function + "(" + x + ")", vars, funcs);
            for(int j = 0; j < stepCount; j++) {
//                if(Math.abs((from+step*j)-y) < step) {
                DecimalFormat dec = new DecimalFormat("0.00");
                double y1 = fromY + step * j;
                y1 = Double.parseDouble(dec.format(y1));
                if(i == 0) {
                    arr[j] += (y1 >= 0) ? "+" + dec.format(y1) : dec.format(y1);
                }
                if(Math.abs(y1-y) < step+step/4){
//                String val = "■";
                    String gradient = "▒▓█";
                    gradient = new StringBuilder(gradient).reverse().toString();
                String val = String.valueOf(gradient.charAt((int) Math.min(Math.abs(y1-y)/step*3 , 2)));
                    arr[j] += val;
                } else {
                    if (y1 == 0) arr[j] += "._";
                    else if(x == 0) arr[j] += "._";
                    else arr[j] += "._";
                }
            }
        }
        return arr;
    }

    public static double eval(final String str, HashMap<String, Double> vars, HashMap<String, String> funcs) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if(vars.containsKey(func)) x = vars.get(func);
                    else if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("log")) x = Math.log10(x);
                    else if (func.equals("ln")) x = Math.log(x);
                    else if(vars.containsKey(func)) x = vars.get(func);
                    else if(funcs.containsKey(func)) x = AutoMath.eval(funcs.get(func).replace("x", x+""), vars, funcs);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
