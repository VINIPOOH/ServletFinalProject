package dal.conection.pool.impl;

import logiclayer.service.impl.BillServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionManagerImplTest {

    @InjectMocks
    ConnectionManagerImpl connectionManager;

    @Spy
    BasicDataSource basicDataSource;

    @Test
    public void init() {
        ReflectionFactory.GetReflectionFactoryAction
        SetFildVlue("dbMinIdle1");

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
    public void getConnection() {
    }

    @Test
    public void startTransaction() {
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
}