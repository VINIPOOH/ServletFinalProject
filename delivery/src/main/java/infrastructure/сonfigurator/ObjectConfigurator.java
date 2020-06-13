package infrastructure.сonfigurator;

import infrastructure.ApplicationContext;

public interface ObjectConfigurator {

    void configure(Object t, Class clazz, ApplicationContext context);
}
