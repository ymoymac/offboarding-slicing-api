package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum TerminationTypes {
    NORMAL(1, "NORMAL", "Normal"),
    OUTSIDE_OF_STRUCT(2, "OUTSIDE_OF_STRUCT", "Fuera de estructura"),

    ;

    private final int id;
    private final String name;
    private final String alias;


    TerminationTypes(int id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

}
