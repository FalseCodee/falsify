package falsify.falsify.automation.block;

import falsify.falsify.automation.Automation;
import falsify.falsify.listeners.Event;

import java.util.ArrayList;
import java.util.function.Predicate;

public class IfBlock extends AutomationBlock {
    private final ArrayList<AutomationBlock> blocks = new ArrayList<>();
    private int index = 0;
    private Predicate<Object> filter;

    public IfBlock(Predicate<Object> filter) {
        setFilter(filter);
    }

    @Override
    protected boolean run(Event<?> event) {
        if(!testFilter((Automation.getValue("message")))) return true;

        AutomationBlock currentBlock = blocks.get(index);
        currentBlock.onEvent(event);

        if(currentBlock.isComplete()) return moveToNextBlock();
        return false;
    }

    @Override
    public String getName() {
        return "If";
    }

    @Override
    public void begin() {
        index = 0;
        blocks.get(index).begin();
    }

    public boolean moveToNextBlock() {
        AutomationBlock currentBlock = blocks.get(index);
        currentBlock.end();

        if(index != blocks.size() - 1) {
            index++;
            currentBlock = blocks.get(index);
            currentBlock.begin();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        super.reset();

        for(AutomationBlock block : blocks) {
            block.reset();
        }
    }

    public boolean testFilter(Object test) {
        return filter.test(test);
    }

    public void addBlock(AutomationBlock block) {
        blocks.add(block);
    }


    public void setFilter(Predicate<Object> filter) {
        this.filter = filter;
    }

    public ArrayList<AutomationBlock> getBlocks() {
        return blocks;
    }
}
