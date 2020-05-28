package infrastructure.Configurators;

import infrastructure.ApplicationContext;
import infrastructure.anotation.InjectProperty;
import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
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

        Class<?> implClass = t.getClass();
        for (Field field : implClass.getDeclaredFields()) {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);
            if (annotation != null) {
                String value = annotation.value().isEmpty() ? resourceBundle.getString(field.getName()) : resourceBundle.getString(annotation.value());
                field.setAccessible(true);
                field.set(t, value);
            }
        }
    }
}
