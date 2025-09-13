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

    public void adicionarPrograma(Programa p) {
        todosProgramas.add(p);
    }

    private void admitirProgramas() {
        for (Programa existente : todosProgramas) {
            if (existente.getAdmissao() <= tempoAtual && 
                (existente.getStatus() == Status.PRONTO || 
                existente.getStatus() == Status.NOVO)) {
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

    public boolean executar() {

        admitirProgramas();

        if (!filaRealTimeAlta.isEmpty()) {
            RealTime p = filaRealTimeAlta.peek();
            p.setStatus(Status.EXECUTANDO);
            System.out.println("Executando: " + p.getPid());
            System.out.println(p.toString());
            p.executarTick();
            if (p.getStatus() == Status.BLOQUEADO) {
                filaBloqueados.add(p);
            } else if (p.getStatus() == Status.FINALIZADO) {
                filaRealTimeAlta.poll();
            } else if (p.getQuantumRestante() == 0) {
                filaRealTimeAlta.poll();
                filaRealTimeAlta.add(p); // vai para o final da fila
            }
        } else if (!filaRealTimeBaixa.isEmpty()) {
            RealTime p = filaRealTimeBaixa.peek();
            p.setStatus(Status.EXECUTANDO);
            System.out.println("Executando: " + p.getPid());
            System.out.println(p.toString());
            p.executarTick();
            if (p.getStatus() == Status.BLOQUEADO) {
                filaBloqueados.add(p);
            } else if (p.getStatus() == Status.FINALIZADO) {
                filaRealTimeBaixa.poll();
            } else if (p.getQuantumRestante() == 0) {
                filaRealTimeBaixa.poll();
                filaRealTimeBaixa.add(p); // vai para o final da fila
            }
        } else if (!filaBestEffort.isEmpty()) {
            Programa p = filaBestEffort.peek();
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


        // Atualiza os processos bloqueados
        Queue<Programa> aindaBloqueados = new LinkedList<>();
        while (!filaBloqueados.isEmpty()) {
            Programa p = filaBloqueados.poll();
            int ticksRestantes = p.getTicksBloqueados();
            if (ticksRestantes > 1) {
                p.setTicksBloqueados(ticksRestantes - 1);
                aindaBloqueados.add(p);
            } else {
                p.setTicksBloqueados(0);
                p.setStatus(Status.PRONTO);
            }
        }
        filaBloqueados = aindaBloqueados;

        tempoAtual++;

        return !(filaBestEffort.isEmpty() && filaRealTimeBaixa.isEmpty() && filaRealTimeAlta.isEmpty() && filaBloqueados.isEmpty() && todosProgramas.stream().allMatch(pr -> pr.getStatus() == Status.FINALIZADO));
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
