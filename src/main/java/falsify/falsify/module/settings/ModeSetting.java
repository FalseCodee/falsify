package falsify.falsify.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<List<String>> {

    private int index;

    public ModeSetting(String name, String def, String... modes) {
        super(name);
        this.value = Arrays.asList(modes);
        this.index = value.indexOf(def);
    }

    public void cycle() {
        this.index = (this.index + 1) % this.value.size();
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMode() {
        return getValue().get(index);
    }

}
