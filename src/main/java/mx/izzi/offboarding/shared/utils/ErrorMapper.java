package mx.izzi.offboarding.shared.utils;

import mx.izzi.offboarding.shared.enums.OBErrorCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMapper {

    public static List<Map<String, Object>> errors(OBErrorCodes errorCode) {
        List<Map<String, Object>> errors = new ArrayList<>();
        Map<String, Object> err = new HashMap<>();
        err.put("code", errorCode.getCode());
        err.put("display", errorCode.getDisplay());
        errors.add(err);
        return errors;
    }
}
