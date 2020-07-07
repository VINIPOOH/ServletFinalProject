package infrastructure.сonfigurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectByType;
import infrastructure.exceptions.ReflectionException;

import java.lang.reflect.Field;

/**
 * Inject in configurable object fields properties marked annotation {@link InjectByType}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
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
                    throw new ReflectionException("impossible we made accessible true");
                }
            }
        }

    }
}
