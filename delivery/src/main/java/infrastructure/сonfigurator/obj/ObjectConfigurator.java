package infrastructure.сonfigurator.obj;

import infrastructure.ApplicationContext;

/**
 * Declare interface for configuration class.
 * If you need add own configurator to chain tuning object. You must implement this interface
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ObjectConfigurator {

    void configure(Object t, Class clazz, ApplicationContext context);
}
