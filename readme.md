# Simulador de CPU e Escalonador

## 📌 Visão geral
Este projeto implementa um simulador de CPU baseada em acumulador com instruções em *assembly* simples e um escalonador de processos.  
Suporta dois tipos de processos:  
- **RealTime** → escalonados com Round Robin (alta ou baixa prioridade, com quantum específicos por processo).  
- **BestEffort** → escalonados com FCFS (First Come, First Served).

---

## ⚙️ Requisitos
- **Java JDK 11+** instalado.  
- Variáveis de ambiente `JAVA_HOME` e `PATH` configuradas corretamente.  

---

## ▶️ Compilar & Executar
No diretório raiz do projeto:

### Compilação e Execução via terminal
javac Main.java -> compila
java Main -> executa

Caso tenha problemas na execução, é possível rodar a Main.java via UI do VScode (canto superior direito)

## 📂Estrutura dos Programas

Os programas a serem executados devem estar dentro da pasta Códigos.

# *Regras:*

### Apenas arquivos com extensão .txt são carregados.

### Não use comentários nos arquivos.

### Utilize a estrutura mínima:

    .code / .endcode → define as instruções.

    .data / .enddata → define as variáveis.

### Labels devem estar sozinhas em uma linha e terminar com :.

### Operandos:

    #N → valor imediato.

    VAR → variável declarada na seção .data.


Exemplos de programas já estão na pasta Códigos.

