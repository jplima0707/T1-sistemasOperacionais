package Tipos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Escalonador {
    private Queue<BestEffort> filaBestEffort;
    private Queue<RealTime> filaRealTimeBaixa;
    private Queue<RealTime> filaRealTimeAlta;
    private Queue<Programa> filaBloqueados;
    private List<Programa> todosProgramas;
    private int tempoAtual;
    private CPU cpu;

    public Escalonador(CPU cpu) {
        this.filaBestEffort = new LinkedList<>();
        this.filaRealTimeBaixa = new LinkedList<>();
        this.filaRealTimeAlta = new LinkedList<>();
        this.filaBloqueados = new LinkedList<>();
        this.todosProgramas = new ArrayList<>();
        this.cpu = cpu;
        this.tempoAtual = 0;
    }

    public void executar() {
        printaStatusDosProcessos(0);

        resolverProcessosBloqueados();
        admitirProgramas();
        System.out.println("");
        if (!filaRealTimeAlta.isEmpty()) {
            cpu.executar(filaRealTimeAlta.peek(), filaBestEffort, filaRealTimeBaixa, filaRealTimeAlta, filaBloqueados);
        } else if (!filaRealTimeBaixa.isEmpty()) {
            cpu.executar(filaRealTimeBaixa.peek(), filaBestEffort, filaRealTimeBaixa, filaRealTimeAlta, filaBloqueados);
        } else if (!filaBestEffort.isEmpty()) {
            cpu.executar(filaBestEffort.peek(), filaBestEffort, filaRealTimeBaixa, filaRealTimeAlta, filaBloqueados);
        } else {
            System.out.println("Nenhum programa para executar neste tick.");
        }

        printaStatusDosProcessos(1);
        tempoAtual++;
    }

    public boolean todosProcessosFinalizados() {
        for (Programa p : todosProgramas) {
            if (p.getStatus() != Status.FINALIZADO) {
                return false;
            }
        }
        return true;
    }

    public void adicionarPrograma(Programa p) {
        todosProgramas.add(p);
    }

    private void admitirProgramas() {
        for (Programa existente : todosProgramas) {
            if (existente.getAdmissao() <= tempoAtual &&
                    existente.getStatus() == Status.NOVO) {
                if (existente instanceof RealTime prt) {
                    if (prt.isAltaPrioridade())
                        filaRealTimeAlta.add(prt);
                    else
                        filaRealTimeBaixa.add(prt);
                } else if (existente instanceof BestEffort pbe) {
                    filaBestEffort.add(pbe);
                }
                existente.setStatus(Status.PRONTO);
            }
        }
    }

    private void resolverProcessosBloqueados() {
        Queue<Programa> aindaBloqueados = new LinkedList<>();
        while (!filaBloqueados.isEmpty()) {
            Programa p = filaBloqueados.poll();
            System.out.println("\n Atualizando bloqueado: " + p.getPid() + " ,ticks restantes: " + (p.getTicksBloqueados()));
            int ticksRestantes = p.getTicksBloqueados();
            if (ticksRestantes >= 1) {
                p.setTicksBloqueados(ticksRestantes - 1);
                aindaBloqueados.add(p);
            } else {
                p.setTicksBloqueados(0);
                p.setStatus(Status.NOVO);
            }
        }
        filaBloqueados = aindaBloqueados;
    }

    public void printaStatusDosProcessos(int i) {
        if (i == 0)
            System.out.println("\n=== Status dos processos antes do tick " + tempoAtual + " ===");
        else
            System.out.println("\nStatus dos processos após o tick " + tempoAtual);
        for (Programa p : todosProgramas) {
            System.out.printf("Processo %d: %s%n", p.getPid(), p.getStatus().toString());
        }
    }

    public Queue<BestEffort> getFilaBestEffort() {
        return filaBestEffort;
    }

    public void setFilaBestEffort(Queue<BestEffort> filaBestEffort) {
        this.filaBestEffort = filaBestEffort;
    }

    public Queue<RealTime> getFilaRealTimeBaixa() {
        return filaRealTimeBaixa;
    }

    public void setFilaRealTimeBaixa(Queue<RealTime> filaRealTimeBaixa) {
        this.filaRealTimeBaixa = filaRealTimeBaixa;
    }

    public Queue<RealTime> getFilaRealTimeAlta() {
        return filaRealTimeAlta;
    }

    public void setFilaRealTimeAlta(Queue<RealTime> filaRealTimeAlta) {
        this.filaRealTimeAlta = filaRealTimeAlta;
    }

    public int getTempoAtual() {
        return tempoAtual;
    }

    public void setTempoAtual(int tempoAtual) {
        this.tempoAtual = tempoAtual;
    }
}
