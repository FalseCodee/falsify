package falsify.falsify.module.settings;

public class TextSetting extends Setting<String>{
    public TextSetting(String name, String value) {
        super(name);
        setValue(value);
    }
}
