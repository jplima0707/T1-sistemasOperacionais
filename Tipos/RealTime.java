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

    public RealTime(ProgramaBase p, int quantum) {
        super(p.getVariaveis(), p.getCodigo(), p.getLabels(), p.getPid(), p.getAdmissao());
        this.quantum = quantum;
        this.quantumRestante = quantum;
        this.altaPrioridade = false;
    }

    @Override
    public void executarTick() {
        if (pc < codigo.size() && quantumRestante > 0) {
            Instrucao instrucao = codigo.get(pc);

            interpretar(instrucao);

            pc++;
            quantumRestante--;
        } else if (quantumRestante <= 0 && status == Status.EXECUTANDO) {
            status = Status.PRONTO;
            quantumRestante = quantum; // reseta para próxima vez que for escalonado
        } else {
            status = Status.FINALIZADO;
        }
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
        return "Real Time { \n" +
                " pid=" + pid +",\n"
                + " status=" + status +",\n"
                + " pc=" + pc +",\n"
                + " admissao=" + admissao + "\n" +
                " altaPrioridade=" + altaPrioridade +",\n"
                + " quantum=" + quantum +",\n"
                + " quantumRestante=" + quantumRestante + "\n" +
                '}';
    }

}
