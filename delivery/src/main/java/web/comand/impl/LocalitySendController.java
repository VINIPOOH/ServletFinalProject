package web.comand.impl;

import com.google.gson.Gson;
import dto.validation.IDValidator;
import infrastructure.anotation.Endpoint;
import infrastructure.anotation.InjectByType;
import infrastructure.anotation.NeedConfig;
import infrastructure.anotation.Singleton;
import logiclayer.service.LocalityService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.comand.MultipleMethodCommand;
import web.exception.OnClientSideProblemException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static web.Servlet.JSON_RESPONSE;
import static web.constant.AttributeConstants.SESSION_LANG;

@Singleton
@NeedConfig
@Endpoint("get/localitiesGet/by/localitySend/id")
public class LocalitySendController implements MultipleMethodCommand {
    private static final Logger log = LogManager.getLogger(LocalitySendController.class);

    @InjectByType
    LocalityService localityService;
    @InjectByType
    private IDValidator idValidator;
    private static final String ID = "id";

    @Override
    public String doGet(HttpServletRequest request) {
        log.debug("");
        long id = Long.parseLong(request.getParameter(ID));
        if (!idValidator.isValid(request, ID)) {
            log.error("id is not valid client is broken");
            throw new OnClientSideProblemException();
        }
        return JSON_RESPONSE +
                new Gson().toJson(localityService.getLocaliseLocalitiesGetByLocalitySendId(
                        (Locale) request.getSession().getAttribute(SESSION_LANG),
                        Long.parseLong(request.getParameter(ID))));
    }

    @Override
    public String doPost(HttpServletRequest request) {
        return null;
    }
}
