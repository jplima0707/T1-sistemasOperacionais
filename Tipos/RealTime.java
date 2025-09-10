package Tipos;

import java.util.List;
import java.util.Map;

public class RealTime extends Programa {

    private int deadline; // prazo em segundos
    private boolean altaPrioridade; // true = alta, false = baixa
    private int quantum; // fatia de tempo em segundos

    public RealTime(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels, int pid, int admissao, int deadline, int quantum) {
        super(variaveis, codigo, labels, pid, admissao);
        this.deadline = deadline;
        this.altaPrioridade = false;
        this.quantum = quantum;
    }

    public RealTime(Programa p, int deadline, int quantum) {
        super(p.getVariaveis(), p.getCodigo(), p.getLabels(), p.getPid(), p.getAdmissao());
        this.deadline = deadline;
        this.quantum = quantum;
    }

    public int getDeadline() {
        return deadline;
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

    public void setDeadline(int deadline) {
        this.deadline = deadline;
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
