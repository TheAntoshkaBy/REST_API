package com.epam.esm.controller.support;

public class ControllerSupporter {

    public static final int DEFAULT_PAGE = 1;
    public static final String DEFAULT_PAGE_STRING = "1";
    public static final int DEFAULT_SIZE = 5;
    public static final String DEFAULT_SIZE_STRING = "5";
    public static final String PAGE_PARAM_NAME = "page";
    public static final String SIZE_PARAM_NAME = "size";

    public static int getValidPaginationParam(String page, String paramName) {
        int defaultReturn = paramName.equals(PAGE_PARAM_NAME) ? DEFAULT_SIZE : DEFAULT_PAGE;
        try {
            int paramInteger = Integer.parseInt(page);
            return paramInteger > 0 ? paramInteger : defaultReturn;
        } catch (NumberFormatException e) {
            return defaultReturn;
        }
    }
}
