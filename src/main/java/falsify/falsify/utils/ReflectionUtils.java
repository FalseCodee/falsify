package falsify.falsify.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    public static <T> Field findField(Class<? super T> cls, String name){
        try{
            Field field = cls.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static void setFinalStatic( Class<?> cls, Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static void setField(Field field, Object target, Object value){
        try {
            field.set(target, value);
        } catch (ReflectiveOperationException throwable) {
            throw new RuntimeException("Failed Reflective set", throwable);
        }
    }
}
