package Tipos;

import java.util.*;

public abstract class Programa {
    protected Map<String, Integer> variaveis;   // nome -> valor
    protected Map<String, Integer> labels;      // label -> posição na lista de código
    protected List<Instrucao> codigo;           // lista de instruções
    protected int acc;                          // acumulador 
    protected int pc;                           // program counter
    protected int pid;                          // identificador do processo
    protected int admissao;                     // tempo de admissão do processo
    protected Status status;                  // status do processo
    protected int ticksBloqueados;            // ticks restantes em estado BLOQUEADO

    public Programa(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels, int pid, int admissao) {
        this.variaveis = variaveis;
        this.codigo = codigo;
        this.labels = labels;
        this.acc = 0;
        this.pc = 0;
        this.pid = pid;
        this.admissao = admissao;
        this.status = Status.NOVO;
        this.ticksBloqueados = 0;
    }

    public abstract void executarTick();

    protected int getValor(String op) {
        if (op == null) return 0;
        if (op.startsWith("#")) { // imediato (valor após o #)
            return Integer.parseInt(op.substring(1));
        }
        return variaveis.get(op); // direto (valor da variável)
    }

    protected void setValorVariavel(String nome, int valor) {
        variaveis.put(nome, valor);
    }

    protected void interpretar(Instrucao instrucao) {
        String cmd = instrucao.getComando();
        String op = instrucao.getOperando();
        status = Status.EXECUTANDO;

        switch (cmd) {
            // Aritmético
            case "ADD":
                acc += getValor(op);
                System.out.printf("[PID %d | PC %d] ADD %s -> ACC = %d%n", pid, pc, op, acc);
                break;
            case "SUB":
                acc -= getValor(op);
                System.out.printf("[PID %d | PC %d] SUB %s -> ACC = %d%n", pid, pc, op, acc);
                break;
            case "MULT":
                acc *= getValor(op);
                System.out.printf("[PID %d | PC %d] MULT %s -> ACC = %d%n", pid, pc, op, acc);
                break;
            case "DIV":
                acc /= getValor(op);
                System.out.printf("[PID %d | PC %d] DIV %s -> ACC = %d%n", pid, pc, op, acc);
                break;

            // Memória
            case "LOAD":
                acc = getValor(op);
                System.out.printf("[PID %d | PC %d] LOAD %s -> ACC = %d%n", pid, pc, op, acc);
                break;
            case "STORE":
                setValorVariavel(op, acc);
                System.out.printf("[PID %d | PC %d] STORE %s -> ACC = %d%n", pid, pc, op, acc);
                break;

            // Saltos
            case "BRANY":
                pc = labels.get(op) -1; // -1 porque o pc será incrementado depois
                System.out.printf("[PID %d | PC %d] BRANY -> Linha %s | ACC = %d%n", pid, pc, op, acc);
                break;
            case "BRPOS":
                if (acc > 0){
                    pc = labels.get(op)-1;
                    System.out.printf("[PID %d | PC %d] BRPOS -> Linha %s | ACC = %d%n", pid, pc, op, acc);
                }
                break;
            case "BRZERO":
                if (acc == 0){
                 pc = labels.get(op)-1;
                System.out.printf("[PID %d | PC %d] BRZERO -> Linha %s | ACC = %d%n", pid, pc, op, acc);
                }
                break;
            case "BRNEG":
                if (acc < 0){
                    pc = labels.get(op)-1;
                    System.out.printf("[PID %d | PC %d] BRNEG -> Linha %s | ACC = %d%n", pid, pc, op, acc);
                }
                break;

            // Sistema
            case "SYSCALL":
                try {
                    ticksBloqueados = new Random().nextInt(3) + 3; // 3 a 5 ticks
                    syscall(Integer.parseInt(op));
                    break;
                } catch (Exception e) {
                    throw new RuntimeException("Erro na syscall com operando: " + op);
                }

            default:
                throw new RuntimeException("Instrução desconhecida: " + cmd);
        }
    }

    protected void syscall(int index) {
        switch (index) {
            case 0:
                System.out.printf("[PID %d] Fim do programa.%n", pid);
                pc = codigo.size(); // força saída
                status = Status.FINALIZADO;
                break;
            case 1:
                status = Status.BLOQUEADO;
                System.out.printf("[PID %d] Syscall 1 -> ACC = %d | BLOQUEADO%n", pid, acc);
                break;
            case 2:
                status = Status.BLOQUEADO;
                System.out.printf("[PID %d] Syscall 2 -> Digite um valor inteiro:%n", pid);
                Scanner scanner = new Scanner(System.in);
                int valor = scanner.nextInt();
                acc = valor;
                System.out.printf("[PID %d] Acc atualizado para: %d%n", pid, acc);
                break;
            default:
                System.out.printf("[PID %d] Syscall não implementada: %d%n", pid, index);
        }
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
