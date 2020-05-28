package infrastructure.Configurators;

import infrastructure.ApplicationContext;

public interface ObjectConfigurator {
    void configure(Object t, ApplicationContext context);
}
