package Tipos;

import java.util.List;
import java.util.Map;

public class ProgramaBase {
    private Map<String, Integer> variaveis;
    private List<Instrucao> codigo;
    private Map<String, Integer> labels;

    public ProgramaBase(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels) {
        this.variaveis = variaveis;
        this.codigo = codigo;
        this.labels = labels;
    }

    public Map<String, Integer> getVariaveis() {
        return variaveis;
    }

    public List<Instrucao> getCodigo() {
        return codigo;
    }

    public Map<String, Integer> getLabels() {
        return labels;
    }
}
