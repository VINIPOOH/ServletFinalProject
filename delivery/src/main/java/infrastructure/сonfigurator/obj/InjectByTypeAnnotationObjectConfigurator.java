package infrastructure.сonfigurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;

import java.lang.reflect.Field;

public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    public void configure(Object t, Class clazz, ApplicationContext context) {

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                field.setAccessible(true);
                Object object = context.getObject(field.getType());
                try {
                    field.set(t, object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("impossible we made accessible true");
                }
            }
        }

    }
}
