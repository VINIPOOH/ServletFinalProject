package infrastructure.Configurators;

import infrastructure.ApplicationContext;
import infrastructure.anotation.HasParentWhichNeedConfig;
import infrastructure.anotation.InjectProperty;
import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static Logger log = LogManager.getLogger(InjectPropertyAnnotationObjectConfigurator.class);

    private ResourceBundle resourceBundle;

    @SneakyThrows
    public InjectPropertyAnnotationObjectConfigurator() {
        log.debug("");

        resourceBundle = ResourceBundle.getBundle("application");
    }

    @Override
    @SneakyThrows
    public void configure(Object t, ApplicationContext context) {
        log.debug("");
        ArrayList<Class> classes = new ArrayList<>();
        Class quentClass = t.getClass();
        classes.add(quentClass);
        while (quentClass.isAnnotationPresent(HasParentWhichNeedConfig.class)) {
            quentClass = quentClass.getSuperclass();
            classes.add(quentClass);
        }
        for (Class clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                InjectProperty annotation = field.getAnnotation(InjectProperty.class);
                if (annotation != null) {
                    String value = annotation.value().isEmpty() ? resourceBundle.getString(field.getName()) : resourceBundle.getString(annotation.value());
                    field.setAccessible(true);
                    field.set(t, value);
                }
            }
        }
    }
}
