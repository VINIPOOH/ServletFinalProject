package dto.maper;

import dto.DeliveryInfoRequestDto;
import dto.DeliveryOrderCreateDto;

import javax.servlet.http.HttpServletRequest;

public class DeliveryOrderCreateDtoMapper implements RequestDtoMapper<DeliveryOrderCreateDto> {
    @Override
    public DeliveryOrderCreateDto mapToDto(HttpServletRequest request) {
        return DeliveryOrderCreateDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter("deliveryWeight")))
                .localityGetID(Long.parseLong(request.getParameter("localityGetID")))
                .localitySandID(Long.parseLong(request.getParameter("localitySandID")))
                .addresseeEmail(request.getParameter("addresseeEmail"))
                .build();
    }
}
