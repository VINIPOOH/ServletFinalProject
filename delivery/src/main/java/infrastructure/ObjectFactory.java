package infrastructure;

import infrastructure.anotation.NeedConfig;
import infrastructure.exceptions.ReflectionException;
import infrastructure.сonfigurator.obj.ObjectConfigurator;
import infrastructure.сonfigurator.proxy.ProxyConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates beans
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class ObjectFactory {
    private static final Logger log = LogManager.getLogger(ObjectFactory.class);

    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators = new ArrayList<>();
    private final List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    public ObjectFactory(ApplicationContext context) {
        log.debug("");

        this.context = context;

        try {

            for (Class<? extends ObjectConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(ObjectConfigurator.class)) {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            }
            for (Class<? extends ProxyConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(ProxyConfigurator.class)) {
                proxyConfigurators.add(aClass.getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
    }


    <T> T createObject(Class<T> implClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.debug("");

        T t = create(implClass);
        configure(t);
        invokeInit(implClass, t);
        t = wrapWithProxyIfNeeded(implClass, t);
        return t;
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }

    private <T> void configure(T t) {
        Class currentClass = t.getClass();
        while (currentClass.isAnnotationPresent(NeedConfig.class)) {
            Class finalCurrentClass = currentClass;
            configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, finalCurrentClass, context));
            currentClass = currentClass.getSuperclass();
        }
    }

    private <T> void invokeInit(Class<T> implClass, T t) {
        Arrays.stream(implClass.getMethods()).filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    try {
                        method.invoke(t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ReflectionException();
                    }
                });
    }

    private <T> T wrapWithProxyIfNeeded(Class<T> implClass, T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = (T) proxyConfigurator.replaceWithProxyIfNeeded(t, implClass, context);
        }
        return t;
    }
}




