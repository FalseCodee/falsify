package falsify.falsify.module.modules.chat;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventChatSend;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeTranslate extends Module {

    private final Pattern unicodePattern;
    public UnicodeTranslate() {
        super("Unicode Translate", "Translates uXXXXXX into unicode char.", false, Category.MISC, -1);
        unicodePattern = Pattern.compile("[uU][0-9]+");
    }

    @Override
    public void onEvent(Event<?> event) {
        if(event instanceof EventChatSend e) {
            if(mc.player != null) {
                String message = e.getMessage();
                Matcher matcher = unicodePattern.matcher(message);


                List<MatchResult> matches = matcher.results().toList();
                if(matches.size() > 0) {
                    for (int i = matches.size() - 1; i >= 0; i--) {
                        MatchResult matchResult = matches.get(i);

                        String unicode = matchResult.group();
                        int number = Integer.parseInt(unicode.substring(1));
                        message = message.substring(0, matchResult.start()) + (char) number + message.substring(matchResult.end());
                    }
                    e.setMessage(message);
                }
            }
        }
    }
}
