import Tipos.Escalonador;
import Tipos.Loader;
import Tipos.CPU;

public class Main {
    public static void main(String[] args) throws Exception {

        CPU cpu = new CPU();
        Escalonador escalonador = new Escalonador(cpu);
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