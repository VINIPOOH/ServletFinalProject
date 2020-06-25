package infrastructure.сonfigurator.obj;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectProperty;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ResourceBundle;


public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static final Logger log = LogManager.getLogger(InjectPropertyAnnotationObjectConfigurator.class);

    private final ResourceBundle resourceBundle;


    public InjectPropertyAnnotationObjectConfigurator() {
        log.debug("");

        resourceBundle = ResourceBundle.getBundle("application");
    }

    @Override
    public void configure(Object t, Class clazz, ApplicationContext context) {
        for (Field field : clazz.getDeclaredFields()) {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);
            if (annotation != null) {
                String value = annotation.value().isEmpty() ? resourceBundle.getString(field.getName()) : resourceBundle.getString(annotation.value());
                field.setAccessible(true);
                try {
                    field.set(t, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("impossible we made accessible true");
                }
            }
        }
    }
}
