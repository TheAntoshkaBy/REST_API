package com.epam.esm.controller.support;

public class ControllerSupporter {

    public static int getValidPaginationParam(String page, String paramName) {
        int defaultPage = 1;
        int defaultSize = 5;
        String paramNamePage = "page";

        int defaultReturn = paramName.equals(paramNamePage) ? defaultSize : defaultPage;
        try {
            int paramInteger = Integer.parseInt(page);
            return paramInteger > 0 ? paramInteger : defaultReturn;
        } catch (NumberFormatException e) {
            return defaultReturn;
        }
    }
}
