package infrastructure.Configurators;

import infrastructure.ApplicationContext;
import infrastructure.anotation.HasParentWhichNeedConfig;
import infrastructure.anotation.InjectByType;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object t, ApplicationContext context) {
        ArrayList<Class> classes = new ArrayList<>();
        Class quentClass = t.getClass();
        classes.add(quentClass);
        while (quentClass.isAnnotationPresent(HasParentWhichNeedConfig.class)) {
            quentClass = quentClass.getSuperclass();
            classes.add(quentClass);
        }
        for (Class clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectByType.class)) {
                    field.setAccessible(true);
                    Object object = context.getObject(field.getType());
                    field.set(t, object);
                }
            }
        }
    }
}
