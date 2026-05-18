package mx.izzi.offboarding.shared.utils;

import java.util.UUID;

public class DynFolio {

    public static String folio() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 21);
    }
}
