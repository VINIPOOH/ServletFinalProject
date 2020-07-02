package infrastructure;

import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import infrastructure.currency.CurrencyInfo;
import infrastructure.currency.CurrencyInfoLoader;
import infrastructure.exceptions.ReflectionException;
import infrastructure.сonfig.Config;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;
import web.comand.impl.PhantomCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public class ApplicationContext {
    private static final Logger log = LogManager.getLogger(ApplicationContext.class);
    private final Map<Class, Object> objectsCash;
    private final Map<String, MultipleMethodCommand> commands;
    private final Map<String, CurrencyInfo> currencies;
    private final Class defaultEndpoint = PhantomCommand.class;
    private final Config config;
    private ObjectFactory factory;

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
            T toReturn;
            try {
                toReturn = factory.createObject(implClass);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new ReflectionException();
            }
            putToObjectsCashIfSingleton(type, implClass, toReturn);
            return toReturn;
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
                for (String i: annotation.value()){
                    if (i.equals(link)) {
                        MultipleMethodCommand toReturn = (MultipleMethodCommand) getObject(clazz);
                        putToCommandMapIfSingleton(link, clazz, toReturn);
                        return toReturn;
                    }
                }
            }
        }
        return (MultipleMethodCommand) getObject(defaultEndpoint);
    }

    public void setFactory(ObjectFactory factory) {
        this.factory = factory;
    }

    Config getConfig() {
        return this.config;
    }

    private <T> void putToObjectsCashIfSingleton(Class<T> type, Class<? extends T> implClass, T t) {
        if (implClass.isAnnotationPresent(Singleton.class)) {
            objectsCash.put(type, t);
        }
    }

    private void putToCommandMapIfSingleton(String link, Class<?> clazz, MultipleMethodCommand toReturn) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            commands.put(link, toReturn);
        }
    }
}
