package infrastructure;

import infrastructure.Configurators.ObjectConfigurator;
import infrastructure.exceptions.ReflectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectFactory {
    private static Logger log = LogManager.getLogger(ObjectFactory.class);

    private final ApplicationContext context;
    private List<ObjectConfigurator> configurators = new ArrayList<>();


    public ObjectFactory(ApplicationContext context) {
        log.debug("");

        this.context = context;
        context.getConfig().getScanner().getSubTypesOf(ObjectConfigurator.class).stream()
                .forEach(aClass -> {
                    try {
                        configurators.add(aClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new ReflectionException();
                    }
                });
    }


    public <T> T createObject(Class<T> implClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.debug("");

        T t = create(implClass);
        configure(t);
        invokeInit(implClass, t);
        return t;
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



    private <T> void configure(T t) {
        configurators.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }
}




