package Tipos;

import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class CPU {
    
    public CPU() {
    }

    public void executar(Programa p, Queue<BestEffort> filaBestEffort, Queue<RealTime> filaRealTimeBaixa, Queue<RealTime> filaRealTimeAlta, Queue<Programa> filaBloqueados) {
        if (p instanceof RealTime) {
            if (((RealTime) p).isAltaPrioridade()) {
                resolverRealTimeAlta((RealTime) p, filaRealTimeAlta, filaBloqueados);
            } else {
                resolverRealTimeBaixa((RealTime) p, filaRealTimeBaixa, filaBloqueados);
            }
        } else if (p instanceof BestEffort) {
            resolverBestEffort((BestEffort) p, filaBestEffort, filaBloqueados);
        } else {
            throw new RuntimeException("Tipo de programa desconhecido.");
        }
    }

    private void resolverRealTimeAlta(RealTime p, Queue<RealTime> filaRealTimeAlta, Queue<Programa> filaBloqueados) {
        p.setStatus(Status.EXECUTANDO);
        System.out.println("Executando programa: " + p.getPid());
        System.out.println(p.toString());
        executarTickRealtime(p);
        if (p.getStatus() == Status.BLOQUEADO) {
            filaRealTimeAlta.poll();
            filaBloqueados.add(p);
            p.setQuantumRestante(p.getQuantum()); // reseta quantum ao ser bloqueado
        } else if (p.getStatus() == Status.FINALIZADO) {
            filaRealTimeAlta.poll();
        } else if (p.getQuantumRestante() == 0) {
            filaRealTimeAlta.poll();
            filaRealTimeAlta.add(p); // vai para o final da fila
            p.setQuantumRestante(p.getQuantum());
            p.setStatus(Status.PRONTO);
        }
    }

    private void resolverRealTimeBaixa(RealTime p, Queue<RealTime> filaRealTimeBaixa, Queue<Programa> filaBloqueados) {
        p.setStatus(Status.EXECUTANDO);
        System.out.println("Executando programa: " + p.getPid());
        System.out.println(p.toString());
        executarTickRealtime(p);
        if (p.getStatus() == Status.BLOQUEADO) {
            filaRealTimeBaixa.poll();
            filaBloqueados.add(p);
            p.setQuantumRestante(p.getQuantum()); // reseta quantum ao ser bloqueado
        } else if (p.getStatus() == Status.FINALIZADO) {
            filaRealTimeBaixa.poll();
        } else if (p.getQuantumRestante() == 0) {
            filaRealTimeBaixa.poll();
            filaRealTimeBaixa.add(p); // vai para o final da fila
            p.setQuantumRestante(p.getQuantum());
            p.setStatus(Status.PRONTO);
        }
    }

    private void resolverBestEffort(BestEffort p, Queue<BestEffort> filaBestEffort, Queue<Programa> filaBloqueados) {
        p.setStatus(Status.EXECUTANDO);
        System.out.println("Executando programa: " + p.getPid());
        System.out.println(p.toString());
        executarTickBestEffort(p);
        if (p.getStatus() == Status.BLOQUEADO) {
            filaBestEffort.poll();
            filaBloqueados.add(p);
        } else if (p.getStatus() == Status.FINALIZADO) {
            filaBestEffort.poll();
        }
    }

    private void executarTickBestEffort(BestEffort p){
        if (p.getPc() < p.getCodigo().size()) {
            Instrucao instrucao = p.getCodigo().get(p.getPc());

            interpretar(instrucao, p);

            p.setPc(p.getPc() + 1);
        } else {
            p.setStatus(Status.FINALIZADO);
        }
    }

    private void executarTickRealtime(RealTime p){
        if (p.getPc() < p.getCodigo().size() && p.getQuantumRestante() > 0) {
            Instrucao instrucao = p.getCodigo().get(p.getPc());

            interpretar(instrucao, p);

            p.setPc(p.getPc() + 1);
            p.setQuantumRestante(p.getQuantumRestante() - 1);
        } else if (p.getQuantumRestante() <= 0 && p.getStatus() == Status.EXECUTANDO) {
            p.setStatus(Status.PRONTO);
            p.setQuantumRestante(p.getQuantum()); // reseta para próxima vez que for escalonado
        } else {
            p.setStatus(Status.FINALIZADO);
        }
    }

    protected int getValor(String op, Programa p) {
        if (op == null)
            return 0;
        if (op.startsWith("#")) { // imediato (valor após o #)
            return Integer.parseInt(op.substring(1));
        }
        return p.getVariaveis().get(op); // direto (valor da variável)
    }

    protected void setValorVariavel(String nome, int valor, Programa p) {
        p.getVariaveis().put(nome, valor);
    }

    protected void interpretar(Instrucao instrucao, Programa p) {
        String cmd = instrucao.getComando();
        String op = instrucao.getOperando();
        p.setStatus(Status.EXECUTANDO);

        switch (cmd) {
            // Aritmético
            case "ADD":
                p.setAcc(p.getAcc() + getValor(op, p));
                System.out.printf("[PID %d | PC %d] ADD %s -> ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;
            case "SUB":
                p.setAcc(p.getAcc() - getValor(op, p));
                System.out.printf("[PID %d | PC %d] SUB %s -> ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;
            case "MULT":
                p.setAcc(p.getAcc() * getValor(op, p));
                System.out.printf("[PID %d | PC %d] MULT %s -> ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;
            case "DIV":
                p.setAcc(p.getAcc() / getValor(op, p));
                System.out.printf("[PID %d | PC %d] DIV %s -> ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;

            // Memória
            case "LOAD":
                p.setAcc(getValor(op, p));
                System.out.printf("[PID %d | PC %d] LOAD %s -> ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;
            case "STORE":
                setValorVariavel(op, p.getAcc(), p);
                System.out.printf("[PID %d | PC %d] STORE %s -> ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;

            // Saltos
            case "BRANY":
                p.setPc(p.getLabels().get(op) - 1); // -1 porque o pc será incrementado depois
                System.out.printf("[PID %d | PC %d] BRANY -> Linha %s | ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                break;
            case "BRPOS":
                if (p.getAcc() > 0) {
                    p.setPc(p.getLabels().get(op) - 1);
                    System.out.printf("[PID %d | PC %d] BRPOS -> Linha %s | ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                }
                break;
            case "BRZERO":
                if (p.getAcc() == 0) {
                    p.setPc(p.getLabels().get(op) - 1);
                    System.out.printf("[PID %d | PC %d] BRZERO -> Linha %s | ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                }
                break;
            case "BRNEG":
                if (p.getAcc() < 0) {
                    p.setPc(p.getLabels().get(op) - 1);
                    System.out.printf("[PID %d | PC %d] BRNEG -> Linha %s | ACC = %d%n", p.getPid(), p.getPc(), op, p.getAcc());
                }
                break;

            // Sistema
            case "SYSCALL":
                try {
                    p.setTicksBloqueados(new Random().nextInt(3) + 2); // 3 a 5 ticks
                    syscall(Integer.parseInt(op), p);
                    break;
                } catch (Exception e) {
                    throw new RuntimeException("Erro na syscall com operando: " + op);
                }

            default:
                p.setStatus(Status.FINALIZADO);
                throw new RuntimeException("Instrução desconhecida: " + cmd);
        }
    }

    protected void syscall(int index, Programa p) {
        switch (index) {
            case 0:
                System.out.printf("[PID %d] Fim do programa.%n", p.getPid());
                p.setPc(p.getCodigo().size()); // força saída
                p.setStatus(Status.FINALIZADO);
                break;
            case 1:
                p.setStatus(Status.BLOQUEADO);
                System.out.printf("[PID %d] Syscall 1 -> ACC = %d | BLOQUEADO%n", p.getPid(), p.getAcc());
                break;
            case 2:
                p.setStatus(Status.BLOQUEADO);
                System.out.printf("[PID %d] Syscall 2 -> Digite um valor inteiro:%n", p.getPid());
                Scanner scanner = new Scanner(System.in);
                int valor = scanner.nextInt();
                p.setAcc(valor);
                System.out.printf("[PID %d] Acc atualizado para: %d%n", p.getPid(), p.getAcc());
                break;
            default:
                System.out.printf("[PID %d] Syscall não implementada: %d%n", p.getPid(), index);
        }
    }
}
