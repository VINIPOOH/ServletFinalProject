package dto.maper;

import controller.filters.LocaleFilter;
import dto.DeliveryInfoRequestDto;

import javax.servlet.http.HttpServletRequest;

public class DeliveryInfoRequestToDtoMapper implements RequestDtoMapper<DeliveryInfoRequestDto> {
    @Override
    public DeliveryInfoRequestDto mapToDto(HttpServletRequest request) throws NumberFormatException {

            return DeliveryInfoRequestDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter("deliveryWeight")))
                .localityGetID(Long.parseLong(request.getParameter("localityGetID")))
                .localitySandID(Long.parseLong(request.getParameter("localitySandID")))
                .build();

    }
}
