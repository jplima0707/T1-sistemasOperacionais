package Tipos;

import java.util.LinkedList;
import java.util.Queue;

public class Escalonador {
    private Queue<BestEffort> filaBestEffort;
    private Queue<RealTime> filaRealTimeBaixa;
    private Queue<RealTime> filaRealTimeAlta;
    private int tempoAtual;

    public Escalonador() {
        this.filaBestEffort = new LinkedList<>();
        this.filaRealTimeBaixa = new LinkedList<>();
        this.filaRealTimeAlta = new LinkedList<>();
        this.tempoAtual = 0;
    }

    public void admitirPrograma(Programa p) {
        if (p instanceof RealTime prt) {
            if (prt.isAltaPrioridade()) filaRealTimeAlta.add(prt);
            else filaRealTimeBaixa.add(prt);
        } else if (p instanceof BestEffort pbe) {
            filaBestEffort.add(pbe);
        }
        p.setStatus(Status.PRONTO);
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
}
