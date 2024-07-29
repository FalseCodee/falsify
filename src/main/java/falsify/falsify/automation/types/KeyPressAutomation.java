package falsify.falsify.automation.types;

import falsify.falsify.automation.Automation;

public class KeyPressAutomation extends Automation {
    public KeyPressAutomation(String name, int keyCode) {
        super(name, "Ran on key press.", keyCode);
    }
}
