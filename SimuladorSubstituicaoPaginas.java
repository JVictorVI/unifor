import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SimuladorSubstituicaoPaginas {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulador de Substituição de Páginas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel paginasLabel = new JLabel("Sequência de páginas (separadas por vírgula):");
        JTextField paginasField = new JTextField("1,2,3,4,2,1,5,6,2,1,2,3,7");
        JLabel quadrosLabel = new JLabel("Número de quadros:");
        JTextField quadrosField = new JTextField("3");
        JButton calcularButton = new JButton("Calcular");

        inputPanel.add(paginasLabel);
        inputPanel.add(paginasField);
        inputPanel.add(quadrosLabel);
        inputPanel.add(quadrosField);
        inputPanel.add(new JLabel()); 
        inputPanel.add(calcularButton);

        JTextArea resultadosArea = new JTextArea();
        resultadosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultadosArea);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        calcularButton.addActionListener(e -> {
            try {
                String[] partes = paginasField.getText().split(",");
                int[] sequenciaPaginas = new int[partes.length];
                for (int i = 0; i < partes.length; i++) {
                    sequenciaPaginas[i] = Integer.parseInt(partes[i].trim());
                }
                int quadros = Integer.parseInt(quadrosField.getText());

                String resultados = "Resultados:\n";
                resultados += "FIFO: " + algoritmoFIFO(sequenciaPaginas, quadros) + " faltas\n";
                resultados += "LRU: " + algoritmoLRU(sequenciaPaginas, quadros) + " faltas\n";
                resultados += "LFU: " + algoritmoLFU(sequenciaPaginas, quadros) + " faltas\n";
                resultados += "Clock: " + algoritmoClock(sequenciaPaginas, quadros) + " faltas\n";
                resultados += "Envelhecimento: " + algoritmoEnvelhecimento(sequenciaPaginas, quadros) + " faltas";

                resultadosArea.setText(resultados);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Entrada inválida! Use números separados por vírgula.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static int algoritmoFIFO(int[] paginas, int capacidade) {
        int faltas = 0;
        Queue<Integer> memoria = new LinkedList<>();

        for (int pagina : paginas) {
            if (!memoria.contains(pagina)) {
                if (memoria.size() == capacidade) {
                    memoria.poll();
                }
                memoria.add(pagina);
                faltas++;
            }
        }
        return faltas;
    }

    public static int algoritmoLRU(int[] paginas, int capacidade) {
        int faltas = 0;
        LinkedHashSet<Integer> memoria = new LinkedHashSet<>();

        for (int pagina : paginas) {
            if (memoria.contains(pagina)) {
                memoria.remove(pagina);
            } else {
                faltas++;
                if (memoria.size() == capacidade) {
                    Iterator<Integer> it = memoria.iterator();
                    it.next();
                    it.remove();
                }
            }
            memoria.add(pagina);
        }
        return faltas;
    }

    public static int algoritmoLFU(int[] paginas, int capacidade) {
        int faltas = 0;
        Map<Integer, Integer> frequencia = new HashMap<>();
        Map<Integer, Integer> tempo = new HashMap<>();
        Set<Integer> memoria = new HashSet<>();
        int relogio = 0;

        for (int pagina : paginas) {
            relogio++;
            frequencia.put(pagina, frequencia.getOrDefault(pagina, 0) + 1);
            tempo.put(pagina, relogio);

            if (!memoria.contains(pagina)) {
                faltas++;
                if (memoria.size() >= capacidade) {
                    int vitima = memoria.stream()
                        .min((a, b) -> {
                            int freqA = frequencia.get(a);
                            int freqB = frequencia.get(b);
                            if (freqA != freqB) return freqA - freqB;
                            return tempo.get(a) - tempo.get(b);
                        })
                        .get();
                    memoria.remove(vitima);
                }
                memoria.add(pagina);
            }
        }
        return faltas;
    }

    public static int algoritmoClock(int[] paginas, int capacidade) {
        int faltas = 0;
        int[] memoria = new int[capacidade];
        boolean[] usado = new boolean[capacidade];
        Arrays.fill(memoria, -1);
        int ponteiro = 0;

        for (int pagina : paginas) {
            boolean encontrada = false;
            
            for (int i = 0; i < capacidade; i++) {
                if (memoria[i] == pagina) {
                    usado[i] = true;
                    encontrada = true;
                    break;
                }
            }

            if (!encontrada) {
                faltas++;
                while (true) {
                    if (!usado[ponteiro]) {
                        memoria[ponteiro] = pagina;
                        usado[ponteiro] = true;
                        ponteiro = (ponteiro + 1) % capacidade;
                        break;
                    }
                    usado[ponteiro] = false;
                    ponteiro = (ponteiro + 1) % capacidade;
                }
            }
        }
        return faltas;
    }

    public static int algoritmoEnvelhecimento(int[] paginas, int capacidade) {
        int faltas = 0;
        Map<Integer, Integer> registros = new HashMap<>();
        Set<Integer> memoria = new HashSet<>();

        for (int pagina : paginas) {
            for (int p : memoria) {
                registros.put(p, registros.get(p) >>> 1);
            }

            registros.put(pagina, registros.getOrDefault(pagina, 0) | (1 << 7));

            if (!memoria.contains(pagina)) {
                faltas++;
                if (memoria.size() >= capacidade) {
                    int vitima = memoria.stream()
                        .min(Comparator.comparingInt(p -> registros.get(p)))
                        .get();
                    memoria.remove(vitima);
                }
                memoria.add(pagina);
            }
        }
        return faltas;
    }
}