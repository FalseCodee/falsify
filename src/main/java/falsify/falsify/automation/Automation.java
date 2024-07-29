package falsify.falsify.automation;

import falsify.falsify.automation.block.AutomationBlock;
import falsify.falsify.listeners.Event;
import falsify.falsify.module.Category;
import falsify.falsify.module.Module;
import falsify.falsify.module.settings.AutomationBlockSetting;

import java.util.WeakHashMap;
import java.util.function.Predicate;

public abstract class Automation extends Module {
    private boolean enabled;
    private boolean singleUse = true;

    private Predicate<Event<?>> shouldEnable = event -> true;
    private static final WeakHashMap<String, Object> specialValues = new WeakHashMap<>();
    private int index = 0;
    private final AutomationBlockSetting blocks = new AutomationBlockSetting("Blocks");

    public Automation(String name, String description, int keyCode) {
        super(name, description, false, Category.AUTOMATION, keyCode);
        settings.add(blocks);
    }

    @Override
    public void onEvent(Event<?> event) {
        if(!enabled && shouldEnable.test(event))
            enable();
        if(enabled)
            run(event);
    }

    public void run(Event<?> event) {
        if(blocks.size() == 0) {
            disable();
            return;
        }

        AutomationBlock currentBlock = blocks.get(index);
        currentBlock.onEvent(event);

        if(currentBlock.isComplete()) moveToNextBlock();
    }

    public void moveToNextBlock() {
        AutomationBlock currentBlock = blocks.get(index);
        currentBlock.end();

        if(index != blocks.size() - 1) {
            index++;
            currentBlock = blocks.get(index);
            currentBlock.begin();
        } else {
            disable();
        }
    }

    public void enable() {
        enabled = true;
        index = 0;
        resetBlocks();
        blocks.get(index).begin();
    }

    public void disable() {
        enabled = false;
        if(singleUse) toggle();
    }

    public void resetBlocks() {
        for(AutomationBlock block : blocks.getValue()) {
            block.reset();
        }
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }

    public void addBlock(AutomationBlock block) {
        blocks.addBlock(block);
    }

    public void setShouldEnable(Predicate<Event<?>> predicate) {
        shouldEnable = predicate;
    }

    public void setValue(String key, Object value) {
        specialValues.put(key, value);
    }

    public static Object getValue(String key) {
        return specialValues.get(key);
    }
}
