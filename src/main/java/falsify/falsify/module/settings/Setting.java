package falsify.falsify.module.settings;

public class Setting<T> {

    protected T value;
    protected final String name;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
