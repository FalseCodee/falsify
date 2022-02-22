package falsify.falsify.module.settings;

public class BooleanSetting extends Setting<Boolean>{

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }
}
