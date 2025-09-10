import Tipos.BestEffort;
import Tipos.Escalonador;
import Tipos.Loader;
import Tipos.Programa;
import Tipos.RealTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Programa> programas = Loader.carregarProgramas("Codigos");
        Escalonador escalonador = new Escalonador();
        Scanner in = new Scanner(System.in);

        int i = 1;
        for (Programa p : programas) {
            System.out.println("\n=== Admitindo programa " + i + " ===");
            p.setPid(i);

            System.out.println("Qual o tempo de admissão (em segundos)?");
            int tempoAdmissao = in.nextInt();
            p.setAdmissao(tempoAdmissao);
            System.out.println("Tempo de admissão: " + tempoAdmissao);

            System.out.println("O programa é BestEffort ou RealTime? (0 BestEffort/ 1 RealTime)");
            int tipo = in.nextInt();
            System.out.println("Tipo: " + tipo);

            if (tipo == 0) {
                BestEffort be = new BestEffort(p);
                escalonador.admitirPrograma(be);
            } else if (tipo == 1) {
                
                System.out.println("Qual o deadline (em segundos)?");
                int deadline = in.nextInt();
                System.out.println("Deadline: " + deadline);

                System.out.println("Qual o quantum (em segundos)?");
                int quantum = in.nextInt();
                System.out.println("Quantum: " + quantum);

                RealTime rt = new RealTime(p, deadline, quantum);

                System.out.println("O programa é de alta prioridade? (0 Não / 1 Sim)");
                int prioridade = in.nextInt();
                System.out.println("Prioridade: " + prioridade);
                rt.setAltaPrioridade(prioridade == 1);

                escalonador.admitirPrograma(rt);
            } else {
                System.out.println("Tipo inválido. Pulando programa.");
                continue;
            }
            i++;
        }
    }
}