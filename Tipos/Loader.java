package Tipos;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Loader {

    public static List<Programa> carregarProgramas(String pasta) throws IOException {
        List<Programa> programas = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pasta), "*.txt")) {
            for (Path arquivo : stream) {
                Programa p = carregarPrograma(arquivo.toFile());
                programas.add(p);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return programas;
    }

    private static Programa carregarPrograma(File arquivo) throws IOException {
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
                labels.put(label, codigo.size()); // aponta para a próxima instrução
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

        return new Programa(variaveis, codigo, labels, 0, 0); // pid e admissão serão definidos depois
    }
}
