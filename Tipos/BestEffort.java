package Tipos;

import java.util.List;
import java.util.Map;

public class BestEffort extends Programa {

    public BestEffort(Map<String, Integer> variaveis, List<Instrucao> codigo, Map<String, Integer> labels, int pid, int admissao) {
        super(variaveis, codigo, labels, pid, admissao);
    }

    public BestEffort(ProgramaBase p) {
        super(p.getVariaveis(), p.getCodigo(), p.getLabels(), p.getPid(), p.getAdmissao());
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
}
