import Tipos.Loader;
import Tipos.Programa;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Programa> programas = Loader.carregarProgramas("Codigos");

        int i = 1;
        for (Programa p : programas) {
            System.out.println("=== Executando programa " + i + " ===");
            System.out.println("Programa " + i + ": " + p.toString());
            p.executar();
            System.out.println();
            i++;
        }
    }
}