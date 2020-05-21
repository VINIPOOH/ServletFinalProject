package bl.service.impl;

import dal.dao.DeliveryDao;
import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import dal.entity.Delivery;
import dto.DeliveryInfoRequestDto;
import dto.DeliveryInfoToGetDto;
import dto.PriceAndTimeOnDeliveryDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static bl.service.impl.ServisesTestConstant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeliveryServiceImplTest {

    @InjectMocks
    DeliveryServiceImpl deliveryService;

    @Mock
    WayDao billDao;

    @Mock
    DeliveryDao deliveryDao;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getDeliveryCostAndTimeDto() {
        DeliveryCostAndTimeDto deliveryCostAndTimeDto = DeliveryCostAndTimeDto.builder()
                .costInCents(1)
                .timeOnWayInHours(1)
                .build();
        PriceAndTimeOnDeliveryDto expected = getPriceAndTimeOnDeliveryDto();
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto();
        when(billDao.findByLocalitySandIdAndLocalityGetId(anyLong(), anyInt(), anyInt())).thenReturn(Optional.of(deliveryCostAndTimeDto));

        Optional<PriceAndTimeOnDeliveryDto> result = deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);

        verify(billDao, times(1)).findByLocalitySandIdAndLocalityGetId(anyLong(), anyInt(), anyInt());
        assertEquals(expected, result.get());
    }

    @Test
    public void getDeliveryInfoToGetRu() {
        Delivery delivery = getDelivery();
        delivery.setBill(getBill());
        DeliveryInfoToGetDto deliveryInfoToGetDto = getDeliveryInfoToGetDto();
        deliveryInfoToGetDto.setLocalityGetName(delivery.getWay().getLocalityGet().getNameRu());
        deliveryInfoToGetDto.setLocalitySandName(delivery.getWay().getLocalitySand().getNameRu());
        when(deliveryDao.getDeliveryInfoToGet(anyLong(), any(Locale.class))).thenReturn(Collections.singletonList(delivery));

        List<DeliveryInfoToGetDto> result = deliveryService.getInfoToGetDeliverisByUserID(1, getLocaleRu());

        verify(deliveryDao, times(1)).getDeliveryInfoToGet(anyLong(), any(Locale.class));
        assertEquals(deliveryInfoToGetDto, result.get(0));
        assertEquals(getDeliveres().size(), result.size());
    }

    @Test
    public void getDeliveryInfoToGetEn() {
        Delivery delivery = getDelivery();
        delivery.setBill(getBill());
        DeliveryInfoToGetDto deliveryInfoToGetDto = getDeliveryInfoToGetDto();
        deliveryInfoToGetDto.setLocalityGetName(delivery.getWay().getLocalityGet().getNameEn());
        deliveryInfoToGetDto.setLocalitySandName(delivery.getWay().getLocalitySand().getNameEn());
        when(deliveryDao.getDeliveryInfoToGet(anyLong(), any(Locale.class))).thenReturn(Collections.singletonList(delivery));

        List<DeliveryInfoToGetDto> result = deliveryService.getInfoToGetDeliverisByUserID(1, getLocaleEn());

        verify(deliveryDao, times(1)).getDeliveryInfoToGet(anyLong(), any(Locale.class));
        assertEquals(deliveryInfoToGetDto, result.get(0));
        assertEquals(getDeliveres().size(), result.size());
    }

    @Test
    public void confirmGettingDelivery() {
        when(deliveryDao.confirmGettingDelivery(anyLong(), anyLong())).thenReturn(true);

        boolean result = deliveryService.confirmGettingDelivery(1, 1);

        assertTrue(result);
    }

    private PriceAndTimeOnDeliveryDto getPriceAndTimeOnDeliveryDto() {
        return PriceAndTimeOnDeliveryDto.builder()
                .costInCents(1)
                .timeOnWayInHours(1)
                .build();
    }

    private DeliveryInfoRequestDto getDeliveryInfoRequestDto() {
        return DeliveryInfoRequestDto.builder()
                .localitySandID(1)
                .localityGetID(1)
                .deliveryWeight(1)
                .build();
    }

    private DeliveryInfoRequestDto getDeliveryInfoRequestDto(int weightRangeReal) {
        return DeliveryInfoRequestDto.builder()
                .deliveryWeight(weightRangeReal)
                .localityGetID(1)
                .localitySandID(1)
                .build();
    }
}