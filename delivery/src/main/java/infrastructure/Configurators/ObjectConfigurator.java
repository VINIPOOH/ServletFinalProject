package infrastructure.Configurators;

import infrastructure.ApplicationContext;
import lombok.SneakyThrows;

public interface ObjectConfigurator {

    @SneakyThrows
    void configure(Object t, Class clazz, ApplicationContext context);
}
