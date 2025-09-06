package Tipos;

public class Instrucao {
    private String comando;
    private String operando;

    public Instrucao(String comando, String operando) {
        this.comando = comando;
        this.operando = operando;
    }

    public String getComando() {
        return comando;
    }

    public String getOperando() {
        return operando;
    }
}
