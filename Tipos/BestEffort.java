package Tipos;

import java.util.List;
import java.util.Map;

public class BestEffort extends Programa {

    public BestEffort(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels) {
        super(variaveis, codigo, labels);
    }

    public BestEffort(ProgramaBase p) {
        super(p);
    }

    @Override
    public void executarTick() {
        if (pc < codigo.size()) {
            Instrucao instrucao = codigo.get(pc);

            interpretar(instrucao);

            pc++;
        } else {
            status = Status.FINALIZADO;
        }
    }

    @Override
    public String toString() {
        return "BestEffort{ \n" +
                " pid=" + pid + ",\n"
                + " status=" + status + ",\n"
                + " pc=" + pc + ",\n"
                + " admissao=" + admissao + "\n" +
                '}';
    }
}
