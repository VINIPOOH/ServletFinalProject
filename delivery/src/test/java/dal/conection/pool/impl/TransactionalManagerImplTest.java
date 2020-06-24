package dal.conection.pool.impl;

import dal.conection.ConnectionAdapeter;
import dal.conection.pool.WrappedTransactionalConnectionPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionalManagerImplTest {

    @InjectMocks
    TransactionalManagerImpl connectionManager;

    @Mock
    WrappedTransactionalConnectionPool wrappedTransactionalConnectionPool;

    @Spy
    ConnectionAdapeter connectionAdapeter;

    @Mock
    ThreadLocal<ConnectionAdapeter> connectionThreadLocal;




    @Test
    public void getConnectionLocalThreadAlreadyHaveConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);

        ConnectionAdapeter result = connectionManager.getConnection();

        verify(connectionThreadLocal, times(1)).get();
        verify(wrappedTransactionalConnectionPool, times(0)).getConnectionAdapter();
        assertEquals(connectionAdapeter, result);
    }

    @Test
    public void getConnectionLocalThreadHaveNotConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(wrappedTransactionalConnectionPool.getConnectionAdapter()).thenReturn(connectionAdapeter);

        ConnectionAdapeter result = connectionManager.getConnection();

        verify(connectionThreadLocal, times(1)).get();
        verify(wrappedTransactionalConnectionPool, times(1)).getConnectionAdapter();
        assertEquals(connectionAdapeter, result);
    }

    @Test(expected = SQLException.class)
    public void getConnectionLocalDataSourceCantGetConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(wrappedTransactionalConnectionPool.getConnectionAdapter()).thenThrow(SQLException.class);

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
        when(wrappedTransactionalConnectionPool.getConnectionAdapterPreparedForTransaction()).thenReturn(connectionAdapeter);

        connectionManager.startTransaction();

        verify(wrappedTransactionalConnectionPool, times(1)).getConnectionAdapterPreparedForTransaction();
        verify(connectionThreadLocal, times(1)).set(any(ConnectionAdapeter.class));
    }

    @Test(expected = SQLException.class)
    public void commitTransactionNotStarted() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);

        connectionManager.commit();

        fail();
    }

    @Test
    public void commitAllGood() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);

        connectionManager.commit();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionAdapeter, times(1)).commit();
    }

    @Test(expected = SQLException.class)
    public void rollBackTransactionNotStarted() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);

        connectionManager.rollBack();

        fail();
    }

    @Test
    public void rollBackAllGood() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);

        connectionManager.rollBack();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionAdapeter, times(1)).rollBack();
    }

    @Test
    public void closeTransactionalConnectionExist() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);

        connectionManager.close();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionThreadLocal, times(1)).remove();
        verify(connectionAdapeter, times(1)).setIsTransaction(false);
        verify(connectionAdapeter, times(1)).close();
    }

    @Test
    public void closeTransactionalConnectionNotExist() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);

        connectionManager.close();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionThreadLocal, times(0)).remove();
        verify(connectionAdapeter, times(0)).setIsTransaction(false);
        verify(connectionAdapeter, times(0)).close();
    }

    @Test
    public void closeProblemsWithDb() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapeter);
        doThrow(SQLException.class).when(connectionAdapeter).close();

        connectionManager.close();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionThreadLocal, times(1)).remove();
        verify(connectionAdapeter, times(1)).setIsTransaction(false);
        verify(connectionAdapeter, times(1)).close();
    }


}