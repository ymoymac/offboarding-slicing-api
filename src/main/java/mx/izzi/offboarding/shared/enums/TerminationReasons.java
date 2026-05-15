package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum TerminationReasons {
    VOLUNTARY_RESIGNATION(1, "VOLUNTARY_RESIGNATION", "Renuncia voluntaria"),
    INVOLUNTARY_TERMINATION(2, "INVOLUNTARY_TERMINATION", "Despido"),
    CONTRACT_END(3, "CONTRACT_END", "Fin de contrato"),
    RETIREMENT(4, "RETIREMENT", "Jubilación"),
    DECEASED(5, "DECEASED", "FEllecimiento"),
    POSITION_ELIMINATION(6, "POSITION_ELIMINATION", "Elimincación del puesto"),
    DISABILITY(7, "DISABILITY", "Baja por incapacidad"),
    END_OF_PROJECT(8, "END_OF_PROJECT", "Fin del proyecto"),
    JOB_ABANDONMENT(9, "JOB_ABANDONMENT", "Abandono de trabajo"),
    FAULTS(10, "FAULTS", "Faltas, artículo 47"),
    INTERNAL_TRANSFER(11, "INTERNAL_TRANSFER", "Transferencia interna"),

    ;

    private final int id;
    private final String name;
    private final String alias;


    TerminationReasons(int id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }
}
