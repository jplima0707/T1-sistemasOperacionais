package Tipos;

import java.util.*;

public abstract class Programa {
    protected Map<String, Integer> variaveis; // nome -> valor
    protected Map<String, Integer> labels; // label -> posição na lista de código
    protected List<Instrucao> codigo; // lista de instruções
    protected int acc; // acumulador
    protected int pc; // program counter
    protected int pid; // identificador do processo
    protected int admissao; // tempo de admissão do processo
    protected Status status; // status do processo
    protected int ticksBloqueados; // ticks restantes em estado BLOQUEADO

    public Programa(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels) {
        this.variaveis = variaveis;
        this.codigo = codigo;
        this.labels = labels;
        this.acc = 0;
        this.pc = 0;
        this.pid = 0;
        this.admissao = 0;
        this.status = Status.NOVO;
        this.ticksBloqueados = 0;
    }

    public Programa(ProgramaBase p) {
        this(p.getVariaveis(), p.getCodigo(), p.getLabels());
    }

    @Override
    public String toString() {
        return "{" +
                "variaveis=" + variaveis +
                ", labels=" + labels +
                '}';
    }

    public Map<String, Integer> getVariaveis() {
        return variaveis;
    }

    public Map<String, Integer> getLabels() {
        return labels;
    }

    public List<Instrucao> getCodigo() {
        return codigo;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAdmissao() {
        return admissao;
    }

    public void setAdmissao(int admissao) {
        this.admissao = admissao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public int getTicksBloqueados() {
        return ticksBloqueados;
    }

    public void setTicksBloqueados(int ticksBloqueados) {
        this.ticksBloqueados = ticksBloqueados;
    }
}
