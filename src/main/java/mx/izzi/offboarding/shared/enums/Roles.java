package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN(1, "ROLE_ADMIN", "ADMIN"),
    IMMEDIATE_BOSS(2, "ROLE_IMMEDIATE_BOSS", "IMMEDIATE_BOSS"),
    RRHH(3, "ROLE_RRHH", "RRHH"),

    ;

    private final int id;
    private final String name;
    private final String alias;


    Roles(int id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }
}
