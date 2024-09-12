package falsify.falsify.module.settings;

import java.util.function.Predicate;

public class Setting<T> {

    protected T value;
    protected final String name;
    protected Predicate<Void> isActive = null;

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

    public void setActivePredicate(Predicate<Void> predicate) {
        this.isActive = predicate;
    }

    public boolean checkActive() {
        if(isActive == null) return true;
        return isActive.test(null);
    }
}
