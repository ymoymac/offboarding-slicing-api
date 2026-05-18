package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum EmployeeStatus {
    ACTIVE(1, "ACTIVE"),
    TERMINATED(2, "TERMINATED"),

    ;

    private final int id;
    private final String name;


    EmployeeStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
