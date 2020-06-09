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
import java.util.Map;


public class ApplicationContext {
    private static Logger log = LogManager.getLogger(ApplicationContext.class);
    private final Map<Class, Object> objectsCash;
    private final Map<String, ActionCommand> commands;
    private final Class defaultEndpoint = EmptyCommand.class;
    @Setter
    private ObjectFactory factory;
    @Getter
    private Config config;

    public ApplicationContext(Config config, Map<Class, Object> preparedCash, Map<String, ActionCommand> commandsPrepared) {
        log.debug("");

        this.commands = commandsPrepared;
        this.config = config;
        this.objectsCash = preparedCash;
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

    public ActionCommand getCommand(String link) {
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
                    ActionCommand toReturn = (ActionCommand) getObject(clazz);
                    if (clazz.isAnnotationPresent(Singleton.class)) {
                        commands.put(link, toReturn);
                    }
                    return toReturn;
                }
            }
        }
        return (ActionCommand) getObject(defaultEndpoint);

    }

}
