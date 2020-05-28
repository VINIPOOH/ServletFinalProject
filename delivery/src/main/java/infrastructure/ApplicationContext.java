package infrastructure;

import infrastructure.Config.Config;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.Singleton;
import infrastructure.exceptions.ReflectionException;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.ActionCommand;
import web.comand.impl.EmptyCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class ApplicationContext {
    private static Logger log = LogManager.getLogger(ApplicationContext.class);

    @Setter
    private ObjectFactory factory;
    private Map<Class, Object> cache;
    @Getter
    private Config config;
    private static final Map<String, ActionCommand> COMMANDS = new HashMap<>();
    private static final Class defaultEndpoint = EmptyCommand.class;

    public ApplicationContext(Config config, Map<Class, Object> preparedCash) {
        log.debug("");

        this.config = config;
        this.cache = preparedCash;
    }

    public <T> T getObject(Class<T> type) {
        log.debug("");

        if (cache.containsKey(type)) {
            return (T) cache.get(type);
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
            cache.put(type, t);
        }
        return t;
    }

    public ActionCommand getCommand(String link) {
        log.debug("");

        if (COMMANDS.containsKey(link)) {
            return COMMANDS.get(link);
        }
        for (Class<?> clazz : config.getScanner().getTypesAnnotatedWith(Endpoint.class)) {
            Endpoint annotation = clazz.getAnnotation(Endpoint.class);
            if (annotation.value().equals(link)) {
                ActionCommand toReturn = (ActionCommand) getObject(clazz);
                if (clazz.isAnnotationPresent(Singleton.class)) {
                    COMMANDS.put(link, toReturn);
                }
                return toReturn;
            }
        }
        return (ActionCommand) getObject(defaultEndpoint);
    }

}
