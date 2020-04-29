package dto.validation;

import dto.DeliveryInfoRequestDto;
import dto.DeliveryOrderCreateDto;

public class DeliveryInfoRequestDtoValidator implements Validator<DeliveryInfoRequestDto> {

    @Override
    public boolean isValid(DeliveryInfoRequestDto dto) {
        return (dto.getDeliveryWeight() > 0) && (dto.getLocalityGetID() > 0) && (dto.getLocalitySandID() > 0);

    }
}
