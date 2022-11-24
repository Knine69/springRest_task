package com.jhuguet.sb_taskv1.app.web.utils;

import java.util.Map;

public class ControllerJwtUtils {
    public String retrieveJwt(Map<String, String> headers) {
        return headers.get("authorization").split(" ")[1];
    }

}
