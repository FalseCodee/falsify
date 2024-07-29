package falsify.falsify.automation.block;

import falsify.falsify.listeners.Event;
import falsify.falsify.listeners.events.EventUpdate;
import falsify.falsify.utils.ChatModuleUtils;

public class SendChatMessageBlock extends AutomationBlock {
    private String message;
    public SendChatMessageBlock(String message) {
        this.message = message;
    }
    @Override
    protected boolean run(Event<?> event) {
        if(event instanceof EventUpdate) {
            ChatModuleUtils.sendMessage(message, false);
            return true;
        }

        return false;
    }

    @Override
    public String getName() {
        return "Send Message";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
