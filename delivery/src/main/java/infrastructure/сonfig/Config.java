package infrastructure.сonfig;

import org.reflections.Reflections;

/**
 * Declare interface for represent application configuration info
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> ifc);

    Reflections getScanner();
}
