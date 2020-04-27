package controller.comand.action.impl;

import controller.comand.action.MultipleMethodCommand;
import service.LocalityService;

import javax.servlet.http.HttpServletRequest;

import static controller.constants.PageConstance.HOME_PATH;

public class Home extends MultipleMethodCommand {

    private final LocalityService localityService;

    public Home(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Override
    protected String performGet(HttpServletRequest request) {
        request.setAttribute("localityList",localityService.getLocaliseLocalities());
        return HOME_PATH;
    }

    @Override
    protected String performPost(HttpServletRequest request) {
        return null;
    }
}
