package infrastructure.сonfigurator.obj;

import infrastructure.ApplicationContext;

public interface ObjectConfigurator {

    void configure(Object t, Class clazz, ApplicationContext context);
}
