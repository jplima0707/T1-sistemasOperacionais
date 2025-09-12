package Tipos;

import java.util.*;

public abstract class Programa {
    private Map<String, Integer> variaveis;   // nome -> valor
    private Map<String, Integer> labels;      // label -> posição na lista de código
    private List<Instrucao> codigo;           // lista de instruções
    private int acc;                          // acumulador 
    private int pc;                           // program counter
    private int pid;                          // identificador do processo
    private int admissao;                     // tempo de admissão do processo
    private Status status;                  // status do processo

    public Programa(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels, int pid, int admissao) {
        this.variaveis = variaveis;
        this.codigo = codigo;
        this.labels = labels;
        this.acc = 0;
        this.pc = 0;
        this.pid = pid;
        this.admissao = admissao;
        this.status = Status.NOVO;
    }

    public abstract void executarTick();

    private int getValorOperando(String op) {
        if (op == null) return 0;
        if (op.startsWith("#")) { // imediato (valor após o #)
            return Integer.parseInt(op.substring(1));
        }
        return variaveis.get(op); // direto (valor da variável)
    }

    private void setValorVariavel(String nome, int valor) {
        variaveis.put(nome, valor);
    }


    public void executar() {
        while (pc < codigo.size()) {
            Instrucao instrucao = codigo.get(pc);
            interpretar(instrucao);
        }
    }

    private void interpretar(Instrucao instrucao) {
        try {
            Thread.sleep(500); // pausa de 0.5 segundo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String cmd = instrucao.getComando();
        String op = instrucao.getOperando();

        switch (cmd) {
            // Aritmético
            case "ADD":
                acc += getValorOperando(op);
                System.out.println("ADD " + op + " na linha " + pc + " /acc=" + acc);
                pc++;
                break;
            case "SUB":
                acc -= getValorOperando(op);
                System.out.println("SUB " + op + " na linha " + pc + " /acc=" + acc);
                pc++;
                break;
            case "MULT":
                acc *= getValorOperando(op);
                System.out.println("MULT " + op + " na linha " + pc + " /acc=" + acc);
                pc++;
                break;
            case "DIV":
                acc /= getValorOperando(op);
                System.out.println("DIV " + op + " na linha " + pc + " /acc=" + acc);
                pc++;
                break;

            // Memória
            case "LOAD":
                acc = getValorOperando(op);
                System.out.println("LOAD " + op + " na linha " + pc + " /acc=" + acc);
                pc++;
                break;
            case "STORE":
                setValorVariavel(op, acc);
                System.out.println("STORE " + op + " na linha " + pc + " /acc=" + acc);
                pc++;
                break;

            // Saltos
            case "BRANY":
                pc = labels.get(op);
                System.out.println("Pulou para " + op + " (BRANY)" + " /acc=" + acc);
                break;
            case "BRPOS":
                if (acc > 0){
                    pc = labels.get(op);
                    System.out.println("Pulou para " + op + " (BRPOS)" + " /acc=" + acc);
                }
                else pc++;
                break;
            case "BRZERO":
                if (acc == 0){
                 pc = labels.get(op);
                System.out.println("Pulou para " + op + " (BRZERO)" + " /acc=" + acc);
                }
                else pc++;
                break;
            case "BRNEG":
                if (acc < 0){
                    pc = labels.get(op);
                    System.out.println("Pulou para " + op + " (BRNEG)" + " /acc=" + acc);
                }
                else pc++;
                break;

            // Sistema
            case "SYSCALL":
                try {
                    Random rand = new Random();
                    Thread.sleep(rand.nextInt(2000) + 3000); // pausa aleatória entre 3 e 5 segundos
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

    private void syscall(int index) {
        switch (index) {
            case 0:
                System.out.println("Fim do programa.");
                pc = codigo.size(); // força saída
                break;
            case 1:
                System.out.println("ACC = " + acc);
                break;
            case 2:
                Scanner scanner = new Scanner(System.in);
                System.out.println("Digite um valor inteiro:");
                int valor = scanner.nextInt();
                acc = valor;
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
}
