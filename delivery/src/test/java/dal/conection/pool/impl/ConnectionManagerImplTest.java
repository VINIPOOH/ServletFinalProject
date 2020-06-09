package dal.conection.pool.impl;

import dal.conection.ConnectionAdapeter;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionManagerImplTest {

    @InjectMocks
    ConnectionManagerImpl connectionManager;

    @Mock
    BasicDataSource basicDataSource;

    @Spy
    ConnectionAdapeter connectionAdapeter;

    @Spy
    Connection connection;

    @Mock
    ThreadLocal<ConnectionAdapeter> connectionThreadLocal;

    @Test
    public void init() throws NoSuchFieldException, IllegalAccessException {
        setFieldValueByItName("dbUrl", "1");
        setFieldValueByItName("dbUser", "1");
        setFieldValueByItName("dbPassword", "1");
        setFieldValueByItName("dbDriver", "1");
        setFieldValueByItName("dbMinIdle", "1");
        setFieldValueByItName("dbMaxIdle", "1");
        setFieldValueByItName("dbInitialSize", "1");
        setFieldValueByItName("dbMaxOpenStatement", "1");

        connectionManager.init();

        verify(basicDataSource, times(1)).setUrl(anyString());
        verify(basicDataSource, times(1)).setUsername(anyString());
        verify(basicDataSource, times(1)).setPassword(anyString());
        verify(basicDataSource, times(1)).setDriverClassName(anyString());
        verify(basicDataSource, times(1)).setMinIdle(anyInt());
        verify(basicDataSource, times(1)).setMaxIdle(anyInt());
        verify(basicDataSource, times(1)).setInitialSize(anyInt());
        verify(basicDataSource, times(1)).setMaxOpenPreparedStatements(anyInt());
    }


    @Test
    public void getConnectionLocalThreadAlreadyHaveConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);

        ConnectionAdapeter result = connectionManager.getConnection();

        verify(connectionThreadLocal, times(1)).get();
        verify(basicDataSource, times(0)).getConnection();
        assertEquals(connectionAdapeter, result);
    }

    @Test
    public void getConnectionLocalThreadHaveNotConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(basicDataSource.getConnection()).thenReturn(connection);

        ConnectionAdapeter result = connectionManager.getConnection();

        verify(connectionThreadLocal, times(1)).get();
        verify(basicDataSource, times(1)).getConnection();
        assertEquals(connection, result.getSubject());
    }

    @Test(expected = SQLException.class)
    public void getConnectionLocalDataSourceCantGetConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(basicDataSource.getConnection()).thenThrow(SQLException.class);

        connectionManager.getConnection();

        fail();
    }

    @Test(expected = SQLException.class)
    public void startTransactionThreadAlreadyHaveConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);

        connectionManager.startTransaction();

        fail();
    }

    @Test
    public void startTransactionThreadHaveNotConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(basicDataSource.getConnection()).thenReturn(connection);

        connectionManager.startTransaction();

        verify(basicDataSource, times(1)).getConnection();
        verify(connectionThreadLocal, times(1)).set(any(ConnectionAdapeter.class));
        verify(connection, times(1)).setAutoCommit(false);
//        verify(connectionAdapeter, times(1)).setIsTransaction(true);

    }

    @Test
    public void commit() {
    }

    @Test
    public void rollBack() {
    }

    @Test
    public void close() {
    }

    @Test
    public void testFinalize() {
    }

    private void setFieldValueByItName(String fieldName, String fieldValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = connectionManager.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(connectionManager, fieldValue);
    }

}