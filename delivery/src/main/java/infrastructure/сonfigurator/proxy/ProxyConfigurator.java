package infrastructure.сonfigurator.proxy;

import infrastructure.ApplicationContext;

public interface ProxyConfigurator {

    Object replaceWithProxyIfNeeded(Object t, Class implClass, ApplicationContext context);
}
