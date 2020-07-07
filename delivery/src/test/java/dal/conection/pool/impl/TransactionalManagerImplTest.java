package dal.conection.pool.impl;

import dal.conection.ConnectionAdapter;
import dal.conection.pool.TransactionalConnectionPool;
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
    TransactionalConnectionPool transactionalConnectionPool;

    @Spy
    ConnectionAdapter connectionAdapter;

    @Mock
    ThreadLocal<ConnectionAdapter> connectionThreadLocal;




    @Test
    public void getConnectionLocalThreadAlreadyHaveConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapter);

        ConnectionAdapter result = connectionManager.getConnection();

        verify(connectionThreadLocal, times(1)).get();
        verify(transactionalConnectionPool, times(0)).getConnectionAdapter();
        assertEquals(connectionAdapter, result);
    }

    @Test
    public void getConnectionLocalThreadHaveNotConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(transactionalConnectionPool.getConnectionAdapter()).thenReturn(connectionAdapter);

        ConnectionAdapter result = connectionManager.getConnection();

        verify(connectionThreadLocal, times(1)).get();
        verify(transactionalConnectionPool, times(1)).getConnectionAdapter();
        assertEquals(connectionAdapter, result);
    }

    @Test(expected = SQLException.class)
    public void getConnectionLocalDataSourceCantGetConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(transactionalConnectionPool.getConnectionAdapter()).thenThrow(SQLException.class);

        connectionManager.getConnection();

        fail();
    }

    @Test(expected = SQLException.class)
    public void startTransactionThreadAlreadyHaveConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapter);

        connectionManager.startTransaction();

        fail();
    }

    @Test
    public void startTransactionThreadHaveNotConnection() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);
        when(transactionalConnectionPool.getConnectionAdapterPreparedForTransaction()).thenReturn(connectionAdapter);

        connectionManager.startTransaction();

        verify(transactionalConnectionPool, times(1)).getConnectionAdapterPreparedForTransaction();
        verify(connectionThreadLocal, times(1)).set(any(ConnectionAdapter.class));
    }

    @Test(expected = SQLException.class)
    public void commitTransactionNotStarted() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);

        connectionManager.commit();

        fail();
    }

    @Test
    public void commitAllGood() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapter);

        connectionManager.commit();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionAdapter, times(1)).commit();
    }

    @Test(expected = SQLException.class)
    public void rollBackTransactionNotStarted() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);

        connectionManager.rollBack();

        fail();
    }

    @Test
    public void rollBackAllGood() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapter);

        connectionManager.rollBack();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionAdapter, times(1)).rollBack();
    }

    @Test
    public void closeTransactionalConnectionExist() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapter);

        connectionManager.close();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionThreadLocal, times(1)).remove();
        verify(connectionAdapter, times(1)).setIsTransaction(false);
        verify(connectionAdapter, times(1)).close();
    }

    @Test
    public void closeTransactionalConnectionNotExist() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(null);

        connectionManager.close();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionThreadLocal, times(0)).remove();
        verify(connectionAdapter, times(0)).setIsTransaction(false);
        verify(connectionAdapter, times(0)).close();
    }

    @Test
    public void closeProblemsWithDb() throws SQLException {
        when(connectionThreadLocal.get()).thenReturn(connectionAdapter);
        doThrow(SQLException.class).when(connectionAdapter).close();

        connectionManager.close();

        verify(connectionThreadLocal, times(1)).get();
        verify(connectionThreadLocal, times(1)).remove();
        verify(connectionAdapter, times(1)).setIsTransaction(false);
        verify(connectionAdapter, times(1)).close();
    }


}