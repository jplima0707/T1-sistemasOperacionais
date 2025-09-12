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

    protected int getValorOperando(String op) {
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
                acc += getValorOperando(op);
                System.out.println("ADD " + op + " na linha " + pc + " no programa " + pid + " /acc=" + acc);
                pc++;
                break;
            case "SUB":
                acc -= getValorOperando(op);
                System.out.println("SUB " + op + " na linha " + pc + " no programa " + pid + " /acc=" + acc);
                pc++;
                break;
            case "MULT":
                acc *= getValorOperando(op);
                System.out.println("MULT " + op + " na linha " + pc + " no programa " + pid + " /acc=" + acc);
                pc++;
                break;
            case "DIV":
                acc /= getValorOperando(op);
                System.out.println("DIV " + op + " na linha " + pc + " no programa " + pid + " /acc=" + acc);
                pc++;
                break;

            // Memória
            case "LOAD":
                acc = getValorOperando(op);
                System.out.println("LOAD " + op + " na linha " + pc + " no programa " + pid + " /acc=" + acc);
                pc++;
                break;
            case "STORE":
                setValorVariavel(op, acc);
                System.out.println("STORE " + op + " na linha " + pc + " no programa " + pid + " /acc=" + acc);
                pc++;
                break;

            // Saltos
            case "BRANY":
                pc = labels.get(op);
                System.out.println("Pulou para " + op + " (BRANY)" + " no programa " + pid + " /acc=" + acc);
                break;
            case "BRPOS":
                if (acc > 0){
                    pc = labels.get(op);
                    System.out.println("Pulou para " + op + " (BRPOS)" + " no programa " + pid + " /acc=" + acc);
                }
                else pc++;
                break;
            case "BRZERO":
                if (acc == 0){
                 pc = labels.get(op);
                System.out.println("Pulou para " + op + " (BRZERO)" + " no programa " + pid + " /acc=" + acc);
                }
                else pc++;
                break;
            case "BRNEG":
                if (acc < 0){
                    pc = labels.get(op);
                    System.out.println("Pulou para " + op + " (BRNEG)" + " no programa " + pid + " /acc=" + acc);
                }
                else pc++;
                break;

            // Sistema
            case "SYSCALL":
                try {
                    ticksBloqueados = new Random().nextInt(3) + 3; // 3 a 5 ticks
                    syscall(Integer.parseInt(op));
                    pc++;
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
                System.out.println("Fim do programa.");
                pc = codigo.size(); // força saída
                status = Status.FINALIZADO;
                break;
            case 1:
                System.out.println("ACC = " + acc);
                status = Status.BLOQUEADO;
                break;
            case 2:
                Scanner scanner = new Scanner(System.in);
                System.out.println("Digite um valor inteiro:");
                int valor = scanner.nextInt();
                acc = valor;
                status = Status.BLOQUEADO;
                break;
            default:
                System.out.println("Syscall não implementada: " + index);
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
