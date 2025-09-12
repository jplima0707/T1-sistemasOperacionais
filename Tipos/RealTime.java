package Tipos;

import java.util.List;
import java.util.Map;

public class RealTime extends Programa {

    private boolean altaPrioridade; // true = alta, false = baixa
    private int quantum; // fatia de tempo em segundos
    private int quantumRestante; // fatia de tempo restante em segundos

    public RealTime(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels, int pid, int admissao, int deadline, int quantum) {
        super(variaveis, codigo, labels, pid, admissao);
        this.altaPrioridade = false;
        this.quantum = quantum;
        this.quantumRestante = quantum;
    }

    public RealTime(Programa p, int deadline, int quantum) {
        super(p.getVariaveis(), p.getCodigo(), p.getLabels(), p.getPid(), p.getAdmissao());
        this.quantum = quantum;
    }

    @Override
    public void executarTick() {
        quantumRestante--;
    }

    public boolean isAltaPrioridade() {
        return altaPrioridade;
    }

    public void setAltaPrioridade(boolean altaPrioridade) {
        this.altaPrioridade = altaPrioridade;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getQuantumRestante() {
        return quantumRestante;
    }

    public void setQuantumRestante(int quantumRestante) {
        this.quantumRestante = quantumRestante;
    }

    @Override
    public String toString() {
        return "RealTime { \n" +
                "deadline=" + deadline + ",\n" +
                "altaPrioridade=" + altaPrioridade + ",\n" +
                "quantum=" + quantum + "\n" +
                "} " + super.toString();
    }
}
