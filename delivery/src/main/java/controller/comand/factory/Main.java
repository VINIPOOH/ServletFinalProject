package controller.comand.factory;

import bll.dto.RegistrationInfoDto;
import bll.dto.validation.RegistrationDtoValidator;

public class Main {
    public static void main(String[] dd) {
        System.out.println(new RegistrationDtoValidator().isValid(
                new RegistrationInfoDto("aaaaaa", "1", "1")));
        System.out.println("aaaaaa".matches("([A-Za-z \\d-_.]+)(@[A-Za-z]+)(\\.[A-Za-z]{2,4})"));
    }
}
