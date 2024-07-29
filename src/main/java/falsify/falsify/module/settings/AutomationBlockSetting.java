package falsify.falsify.module.settings;

import falsify.falsify.automation.block.AutomationBlock;

import java.util.ArrayList;

public class AutomationBlockSetting extends Setting<ArrayList<AutomationBlock>> {
    public AutomationBlockSetting(String name) {
        super(name);
        value = new ArrayList<>();
    }

    public void addBlock(AutomationBlock block) {
        value.add(block);
    }

    public void removeBlock(AutomationBlock block) {
        value.remove(block);
    }

    public AutomationBlock get(int index) {
        return value.get(index);
    }

    public int size() {
        return value.size();
    }
}
