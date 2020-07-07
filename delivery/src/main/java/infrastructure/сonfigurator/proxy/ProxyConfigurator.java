package infrastructure.сonfigurator.proxy;

import infrastructure.ApplicationContext;

/**
 * Declare interface for proxying class.
 * If you need add own proxy configurator to chain tuning object. You must implement this interface
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ProxyConfigurator {

    Object replaceWithProxyIfNeeded(Object t, Class implClass, ApplicationContext context);
}
