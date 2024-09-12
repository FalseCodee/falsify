package falsify.falsify.module.settings;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Setting<T> {

    protected T value;
    protected final String name;
    protected Predicate<Void> isActive = null;
    protected Consumer<T> changedConsumer = null;

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
        if(changedConsumer != null) changedConsumer.accept(value);
        this.value = value;
    }

    public void setActivePredicate(Predicate<Void> predicate) {
        this.isActive = predicate;
    }

    public boolean checkActive() {
        if(isActive == null) return true;
        return isActive.test(null);
    }

    public void setChangedConsumer(Consumer<T> changedConsumer) {
        this.changedConsumer = changedConsumer;
    }
}
