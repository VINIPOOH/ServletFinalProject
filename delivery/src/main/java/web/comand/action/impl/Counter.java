package web.comand.action.impl;

import bl.service.DeliveryProcessService;
import bl.service.LocalityService;
import dto.DeliveryInfoRequestDto;
import dto.PriceAndTimeOnDeliveryDto;
import dto.mapper.RequestDtoMapper;
import dto.validation.Validator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.action.MultipleMethodCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

import static web.constants.AttributeConstants.SESSION_LANG;
import static web.constants.PageConstance.COUNTER_FILE_NAME;
import static web.constants.PageConstance.MAIN_WEB_FOLDER;

public class Counter extends MultipleMethodCommand {
    private static final String LOCALITY_LIST = "localityList";
    private static final String COST_AND_TIME_DTO = "CostAndTimeDto";
    private static final String IS_NOT_EXIST_SUCH_WAY_OR_WEIGHT_FOR_THIS_WAY = "IsNotExistSuchWayOrWeightForThisWay";
    private static final String DELIVERY_WEIGHT = "deliveryWeight";
    private static final String LOCALITY_GET_ID = "localityGetID";
    private static final String LOCALITY_SAND_ID = "localitySandID";
    private static Logger log = LogManager.getLogger(Counter.class);
    private final LocalityService localityService;
    private final DeliveryProcessService deliveryProcessService;
    private String INPUT_HAS_ERRORS = "inputHasErrors";

    public Counter(LocalityService localityService, DeliveryProcessService deliveryProcessService) {
        this.localityService = localityService;
        this.deliveryProcessService = deliveryProcessService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        log.debug("");

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        log.debug("isValidRequest = " + getDeliveryInfoRequestDtoValidator().isValid(request));

        request.setAttribute(LOCALITY_LIST, localityService.getLocaliseLocalities((Locale) request.getSession().getAttribute(SESSION_LANG)));
        if (!getDeliveryInfoRequestDtoValidator().isValid(request)) {
            request.setAttribute(INPUT_HAS_ERRORS, true);
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }
        Optional<PriceAndTimeOnDeliveryDto> deliveryCostAndTimeDto = deliveryProcessService.getDeliveryCostAndTimeDto
                (getDeliveryInfoRequestDtoRequestDtoMapper(request).mapToDto(request));
        if (deliveryCostAndTimeDto.isPresent()) {
            request.setAttribute(COST_AND_TIME_DTO, deliveryCostAndTimeDto.get());
            return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
        }
        request.setAttribute(IS_NOT_EXIST_SUCH_WAY_OR_WEIGHT_FOR_THIS_WAY, true);
        return MAIN_WEB_FOLDER + COUNTER_FILE_NAME;
    }

    private RequestDtoMapper<DeliveryInfoRequestDto> getDeliveryInfoRequestDtoRequestDtoMapper(HttpServletRequest request) {
        return request1 -> DeliveryInfoRequestDto.builder()
                .deliveryWeight(Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)))
                .localityGetID(Long.parseLong(request.getParameter(LOCALITY_GET_ID)))
                .localitySandID(Long.parseLong(request.getParameter(LOCALITY_SAND_ID)))
                .build();
    }

    private Validator getDeliveryInfoRequestDtoValidator() {
        return request -> {
            try {
                return ((Integer.parseInt(request.getParameter(DELIVERY_WEIGHT)) > 0) &&
                        (Long.parseLong(request.getParameter(LOCALITY_GET_ID)) > 0) &&
                        (Long.parseLong(request.getParameter(LOCALITY_SAND_ID)) > 0));
            } catch (NumberFormatException ex) {
                return false;
            }
        };
    }
}
