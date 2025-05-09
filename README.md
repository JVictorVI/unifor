# Simulador de Substituição de Páginas

Este projeto é uma aplicação gráfica em Java que simula cinco algoritmos clássicos de substituição de páginas utilizados em sistemas operacionais: FIFO, LRU, LFU, Clock e Envelhecimento. A aplicação permite que o usuário insira uma sequência de páginas e a quantidade de quadros de memória, e então exibe o número de faltas de página para cada algoritmo.

## 🛠 Bibliotecas Utilizadas

- **Java SE**
- **Swing (GUI)**
- **AWT (Eventos e Layouts)**

## 💻 Funcionalidades

- Interface gráfica para entrada de dados.
- Implementação dos seguintes algoritmos de substituição de páginas:
  - **FIFO (First-In First-Out)**
  - **LRU (Least Recently Used)**
  - **LFU (Least Frequently Used)**
  - **Clock**
  - **Envelhecimento**
- Exibição do número de **faltas de página** para cada algoritmo.

## ▶️ Como Executar

1. Versão do Java (JDK) mínima necessária: versão 8 ou superior.
2. Compile e Execute o programa.

## 🧪 Exemplo de Uso

- Sequência de páginas: `1,2,3,4,2,1,5,6,2,1,2,3,7`
- Número de quadros: `3`

Ao clicar no botão **"Calcular"**, o programa exibirá o número de faltas de página para cada algoritmo.
