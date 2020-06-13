package infrastructure;

import infrastructure.Config.Config;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import infrastructure.currency.CurrencyInfo;
import infrastructure.currency.CurrencyInfoLoader;
import infrastructure.exceptions.ReflectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;
import web.comand.impl.EmptyCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public class ApplicationContext {
    private static Logger log = LogManager.getLogger(ApplicationContext.class);
    private final Map<Class, Object> objectsCash;
    private final Map<String, MultipleMethodCommand> commands;
    private final Map<String, CurrencyInfo> currencies;
    private final Class defaultEndpoint = EmptyCommand.class;
    private ObjectFactory factory;
    private Config config;

    public ApplicationContext(Config config, Map<Class, Object> preparedCash, Map<String, MultipleMethodCommand> commandsPrepared, CurrencyInfoLoader currencyInfoLoader) {
        log.debug("");

        this.commands = commandsPrepared;
        this.config = config;
        this.objectsCash = preparedCash;
        currencies = currencyInfoLoader.getCurrencyInfo();
    }

    public CurrencyInfo getCurrencyInfo(String string) {
        return currencies.get(string);
    }

    public void init() {
        log.debug("");
        for (Class<?> clazz : config.getScanner().getTypesAnnotatedWith(Singleton.class)) {
            Singleton annotation = clazz.getAnnotation(Singleton.class);
            if (!annotation.isLazy()) {
                log.debug("created" + clazz.getName());
                getObject(clazz);
            }
        }
    }

    public <T> T getObject(Class<T> type) {
        log.debug("");

        if (objectsCash.containsKey(type)) {
            return (T) objectsCash.get(type);
        }
        synchronized (objectsCash) {
            if (objectsCash.containsKey(type)) {
                return (T) objectsCash.get(type);
            }
            Class<? extends T> implClass = type;

            if (type.isInterface()) {
                implClass = config.getImplClass(type);
            }
            T t = null;
            try {
                t = factory.createObject(implClass);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new ReflectionException();
            }
            if (implClass.isAnnotationPresent(Singleton.class)) {
                objectsCash.put(type, t);
            }
            return t;
        }
    }

    public MultipleMethodCommand getCommand(String link) {
        log.debug("");

        if (commands.containsKey(link)) {
            return commands.get(link);
        }
        synchronized (commands) {
            if (commands.containsKey(link)) {
                return commands.get(link);
            }
            for (Class<?> clazz : config.getScanner().getTypesAnnotatedWith(Endpoint.class)) {
                Endpoint annotation = clazz.getAnnotation(Endpoint.class);
                if (annotation.value().equals(link)) {
                    MultipleMethodCommand toReturn = (MultipleMethodCommand) getObject(clazz);
                    if (clazz.isAnnotationPresent(Singleton.class)) {
                        commands.put(link, toReturn);
                    }
                    return toReturn;
                }
            }
        }
        return (MultipleMethodCommand) getObject(defaultEndpoint);

    }

    Config getConfig() {
        return this.config;
    }

    public void setFactory(ObjectFactory factory) {
        this.factory = factory;
    }
}
