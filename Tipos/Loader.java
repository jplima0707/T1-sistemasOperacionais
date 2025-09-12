package Tipos;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Loader {

    public static List<ProgramaBase> carregarProgramas(String pasta) throws IOException {
        List<ProgramaBase> programas = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pasta), "*.txt")) {
            for (Path arquivo : stream) {
                ProgramaBase p = carregarPrograma(arquivo.toFile());
                programas.add(p);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return programas;
    }

    private static ProgramaBase carregarPrograma(File arquivo) throws IOException {
        Map<String, Integer> variaveis = new HashMap<>();
        Map<String, Integer> labels = new HashMap<>();
        List<Instrucao> codigo = new ArrayList<>();

        boolean lendoCode = false; // indica se está na seção de código
        boolean lendoData = false; // indica se está na seção de dados

        List<String> linhas = Files.readAllLines(arquivo.toPath());
        for (String linha : linhas) {
            linha = linha.trim().toUpperCase();
            if (linha.isEmpty() || linha.startsWith("//")) continue;

            // Controle de seções
            if (linha.equalsIgnoreCase(".code")) { lendoCode = true; continue; }
            if (linha.equalsIgnoreCase(".endcode")) { lendoCode = false; continue; }
            if (linha.equalsIgnoreCase(".data")) { lendoData = true; continue; }
            if (linha.equalsIgnoreCase(".enddata")) { lendoData = false; continue; }

            if (lendoCode) {
                // Label
                if (linha.endsWith(":")) {
                String label = linha.substring(0, linha.length() - 1).trim();
                labels.put(label, codigo.size()); // próxima instrução
                continue;
            }

                // Instrução
                String[] parts = linha.split(" ", 2);
                String comando = parts[0].trim();
                String operando = (parts.length > 1) ? parts[1].trim() : null;
                codigo.add(new Instrucao(comando, operando));
            }

            if (lendoData) {
                String[] parts = linha.split(" ");
                String nome = parts[0].trim();
                int valor = Integer.parseInt(parts[1].trim());
                variaveis.put(nome, valor);
            }
        }

        return new ProgramaBase(variaveis, codigo, labels, 0, 0); // pid e admissão serão definidos depois
    }

    public void classificaProgramas(Escalonador escalonador) throws IOException{
        List<ProgramaBase> programas = carregarProgramas("Codigos");
        Scanner in = new Scanner(System.in);

        int i = 1;
        for (ProgramaBase info : programas) {
            System.out.println("\n=== Admitindo programa " + i + " ===");

            System.out.println("Qual o tempo de admissão (em segundos)?");
            int tempoAdmissao = in.nextInt();

            System.out.println("O programa é BestEffort ou RealTime? (0 BestEffort / 1 RealTime)");
            int tipo = in.nextInt();

            if (tipo == 0) {
                BestEffort be = new BestEffort(info);
                be.setAdmissao(tempoAdmissao);
                be.setPid(i);
                escalonador.adicionarPrograma(be);

            } else if (tipo == 1) {
                System.out.println("Qual o quantum?");
                int quantum = in.nextInt();

                System.out.println("O programa é de alta prioridade? (0 Não / 1 Sim)");
                int prioridade = in.nextInt();

                RealTime rt = new RealTime(info, quantum);
                rt.setAdmissao(tempoAdmissao);
                rt.setPid(i);
                rt.setAltaPrioridade(prioridade == 1);
                escalonador.adicionarPrograma(rt);

            } else {
                System.out.println("Tipo inválido. Pulando programa.");
            }

            i++;
        }
        in.close();
    }
}
