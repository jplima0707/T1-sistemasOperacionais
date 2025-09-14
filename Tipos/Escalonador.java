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

    public Escalonador() {
        this.filaBestEffort = new LinkedList<>();
        this.filaRealTimeBaixa = new LinkedList<>();
        this.filaRealTimeAlta = new LinkedList<>();
        this.filaBloqueados = new LinkedList<>();
        this.todosProgramas = new ArrayList<>();
        this.tempoAtual = 0;
    }

    public void executar() {

        admitirProgramas();
        System.out.println("");
        if (!filaRealTimeAlta.isEmpty()) {
            resolverRealTimeAlta();
        } else if (!filaRealTimeBaixa.isEmpty()) {
            resolverRealTimeBaixa();
        } else if (!filaBestEffort.isEmpty()) {
            resolverBestEffort();
        } else {
            System.out.println("Nenhum programa para executar neste tick.");
        }
        
        // Atualiza os processos bloqueados
        resolverProcessosBloqueados();

        tempoAtual++;
    }

    public void adicionarPrograma(Programa p) {
        todosProgramas.add(p);
    }

    private void resolverRealTimeAlta() {
        RealTime p = filaRealTimeAlta.peek();
        p.setStatus(Status.EXECUTANDO);
        System.out.println("Executando programa: " + p.getPid());
        System.out.println(p.toString());
        p.executarTick();
        if (p.getStatus() == Status.BLOQUEADO) {
            filaRealTimeAlta.poll();
            filaBloqueados.add(p);
            p.setQuantum(p.getQuantum()); // reseta quantum ao ser bloqueado
        } else if (p.getStatus() == Status.FINALIZADO) {
            filaRealTimeAlta.poll();
        } else if (p.getQuantumRestante() == 0) {
            filaRealTimeAlta.poll();
            filaRealTimeAlta.add(p); // vai para o final da fila
        }
    }

    private void resolverRealTimeBaixa() {
        RealTime p = filaRealTimeBaixa.peek();
        p.setStatus(Status.EXECUTANDO);
        System.out.println("Executando: " + p.getPid());
        System.out.println(p.toString());
        p.executarTick();
        if (p.getStatus() == Status.BLOQUEADO) {
            filaRealTimeBaixa.poll();
            filaBloqueados.add(p);
            p.setQuantum(p.getQuantum()); // reseta quantum ao ser bloqueado
        } else if (p.getStatus() == Status.FINALIZADO) {
            filaRealTimeBaixa.poll();
        } else if (p.getQuantumRestante() == 0) {
            filaRealTimeBaixa.poll();
            filaRealTimeBaixa.add(p); // vai para o final da fila
        }
    }

    private void resolverBestEffort(){
        BestEffort p = filaBestEffort.peek();
        p.setStatus(Status.EXECUTANDO);
        System.out.println("Executando: " + p.getPid());
        System.out.println(p.toString());
        p.executarTick();
        if (p.getStatus() == Status.BLOQUEADO) {
            filaBestEffort.poll();
            filaBloqueados.add(p);
        } else if (p.getStatus() == Status.FINALIZADO) {
            filaBestEffort.poll();
        }
    }

    private void admitirProgramas() {
        for (Programa existente : todosProgramas) {
            if (existente.getAdmissao() <= tempoAtual && 
                existente.getStatus() == Status.NOVO) {
                if (existente instanceof RealTime prt) {
                    if (prt.isAltaPrioridade()) filaRealTimeAlta.add(prt);
                    else filaRealTimeBaixa.add(prt);
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
            System.out.println("Atualizando bloqueado: " + p.getPid() + " ,ticks restantes: " + (p.getTicksBloqueados()));
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
