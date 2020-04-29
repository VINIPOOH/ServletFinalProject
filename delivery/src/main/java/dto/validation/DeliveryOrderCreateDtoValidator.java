package dto.validation;

import dto.DeliveryInfoRequestDto;
import dto.DeliveryOrderCreateDto;

public class DeliveryOrderCreateDtoValidator implements Validator<DeliveryOrderCreateDto> {
    private static final String EMAIL_REGEX = "([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})";//"^([A-Za-z \\\\d-_.]+)(@[A-Za-z]+)(\\\\.[A-Za-z]{2,4})$";


    @Override
    public boolean isValid(DeliveryOrderCreateDto dto) {
        return (dto.getDeliveryWeight()>0)&&(dto.getLocalityGetID()>0)&&(dto.getLocalitySandID()>0 && isStringValid(dto.getAddresseeEmail(), EMAIL_REGEX));

    }
}
