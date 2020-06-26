package infrastructure.сonfigurator.proxy;

import dal.conection.pool.TransactionalManager;
import infrastructure.ApplicationContext;
import infrastructure.anotation.Transaction;
import net.sf.cglib.proxy.Enhancer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;


public class TransactionProxyConfigurator implements ProxyConfigurator {
    private static final Logger log = LogManager.getLogger(TransactionProxyConfigurator.class);

    @Override
    public Object replaceWithProxyIfNeeded(Object t, Class implClass, ApplicationContext context) {

        for (Method method : implClass.getMethods()) {
            if (method.isAnnotationPresent(Transaction.class)) {
                if (implClass.getInterfaces().length == 0) {
                    return Enhancer.create(implClass, new net.sf.cglib.proxy.InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            return getInvocationHandlerLogic(method, args, t, context);
                        }
                    });
                }
                return Proxy.newProxyInstance(implClass.getClassLoader(), implClass.getInterfaces(),
                        (proxy, met, args) -> getInvocationHandlerLogic(met, args, t, context));
            }
        }
        return t;
    }

    private Object getInvocationHandlerLogic(Method method, Object[] args, Object t, ApplicationContext context) throws Throwable {
        log.debug("getInvocationHandlerLogic");
        try {
            if (t.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Transaction.class)) {
                return doTransactionMethodCall(method, args, t, context.getObject(TransactionalManager.class));
            }
        } catch (NoSuchMethodException | SQLException | InstantiationException ex) {
            log.debug(ex);
        }
        return method.invoke(t, args);
    }

    private Object doTransactionMethodCall(Method method, Object[] args, Object t, TransactionalManager transactionalManager) throws Throwable {
        try {
            transactionalManager.startTransaction();
            Object result = method.invoke(t, args);
            transactionalManager.commit();
            transactionalManager.close();
            return result;
        } catch (InvocationTargetException e) {
            transactionalManager.rollBack();
            transactionalManager.close();
            throw e.getCause().getClass().getConstructor().newInstance();
        }
    }
}
