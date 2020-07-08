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
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ObjectFactory {

    <T> T createObject(Class<T> implClassKey) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}




