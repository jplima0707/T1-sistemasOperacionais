import Tipos.Escalonador;
import Tipos.Loader;

public class Main {
    public static void main(String[] args) throws Exception {
    Escalonador escalonador = new Escalonador();
    Loader loader = new Loader();
    loader.classificaProgramas(escalonador);
    while (true) {
        escalonador.executar();
        Thread.sleep(1000);

        if (escalonador.todosProcessosFinalizados()) {
            System.out.println("Todos os processos foram finalizados.");
            break;
        }
    }
}

}