import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.util.*;
import java.util.List;


public class LaberintoGUI extends JFrame{

    private JPanel panelPrincipal, panelLaberitoOriginal, panelBotones, panelLaberintoResuelto;
    private JButton botonLaberintoAleatorio, botonLaberintoManual, botonLaberintoCSV, botonResolver;
    private ChartPanel graficaDeTiempos;
    JFrame ventanaGrafico;
    private Grafo laberinto;
    private List<Set<Integer>> caminoResuelto;
    private JLabel etiquetaAlgoritmo;



    public LaberintoGUI(){

        setTitle("Resolucion de laberintos GUI");
        setSize(1000,750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        inicializarComponentes();


    }

    private void inicializarComponentes() {

        panelPrincipal= new JPanel(new GridLayout(1,2));
        panelLaberitoOriginal= new JPanel();
        panelLaberitoOriginal.setBackground(Color.WHITE);

        ventanaGrafico = new JFrame("Comparación de Tiempos");
        ventanaGrafico.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaGrafico.setSize(500, 400);
        ventanaGrafico.setLocationRelativeTo(null);
        panelLaberintoResuelto= new JPanel();
        panelLaberintoResuelto.setBackground(Color.WHITE);

        botonLaberintoAleatorio= new JButton("Generar laberinto aleatorio");
        botonLaberintoAleatorio.addActionListener(e -> {
            laberinto = new Grafo().generarLaberintoAleatorio();
            dibujarLaberinto();

        });


        botonLaberintoCSV= new JButton("Generar laberinto a partir de CSV");
        botonLaberintoCSV.addActionListener(e -> {
            ArchivoCSV archivoCSV= new ArchivoCSV("src/csvDePrueba");
            archivoCSV.leerArchivo();
            laberinto = archivoCSV.obtenerGrafo();
            dibujarLaberinto();

        });


        botonLaberintoManual= new JButton("Generar laberinto manualmente");
        botonLaberintoManual.addActionListener(e -> {
            try {
                String dimensiones = JOptionPane.showInputDialog(
                        this, "Ingrese las dimensiones en el siguiente formato: lineas,columnas", "Dimensiones", JOptionPane.QUESTION_MESSAGE
                );

                if (dimensiones == null || !dimensiones.matches("\\d+,\\d+")){
                    return;
                }

                String[] partes = dimensiones.split(",");
                int filas = Integer.parseInt(partes[0].trim());
                int columnas = Integer.parseInt(partes[1].trim());
                int totalNodos = filas * columnas;

                Grafo grafoManual = new Grafo();
                grafoManual.inicializarListaDeAdyacencia(filas, columnas);

                while (true) {
                    String linea = JOptionPane.showInputDialog(
                            this, "Ingrese una linea de adyacencia en el siguiente formato: nodoOrigen:nododestino1,nododestino2,nododestinon...", "Adyacencias", JOptionPane.QUESTION_MESSAGE
                    );

                    if (linea == null || linea.trim().isEmpty()){
                        break;
                    }

                    if (!linea.contains(":")){
                        continue;
                    }

                    String[] partesLinea = linea.split(":");
                    int origen = Integer.parseInt(partesLinea[0].trim());

                    if (origen < 0 || origen >= totalNodos){
                        continue;
                    }

                    String[] vecinos = partesLinea[1].split(",");
                    for (String vecinoTexto : vecinos) {
                        int destino = Integer.parseInt(vecinoTexto.trim());
                        if (destino >= 0 && destino < totalNodos) {
                            grafoManual.agregarArista(origen, destino);
                        }
                    }
                }

                laberinto = grafoManual;
                dibujarLaberinto();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Entrada inválida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        botonResolver= new JButton("Resolver Laberinto");
        botonResolver.addActionListener(e -> {
            if (laberinto == null){
                return;
            }

            JCheckBox opcionbfs = new JCheckBox("BFS");
            JCheckBox opciondijkstra = new JCheckBox("Dijkstra");
            JCheckBox opcionaestrella = new JCheckBox("A*");

            JPanel panelOpciones = new JPanel(new GridLayout(0, 1));
            panelOpciones.add(new JLabel("Selecciona los algoritmos para resolver el laberinto:"));
            panelOpciones.add(opcionbfs);
            panelOpciones.add(opciondijkstra);
            panelOpciones.add(opcionaestrella);

            int resultado = JOptionPane.showConfirmDialog(
                    this, panelOpciones, "Algoritmos de búsqueda",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION){
                return;
            }

            AlgoritmoDeBusqueda buscador = new AlgoritmoDeBusqueda();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            Map<String, Long> tiempos = new HashMap<>();

            if (opcionbfs.isSelected()) {
                long inicio = System.nanoTime();
                caminoResuelto = buscador.BFS(laberinto);
                long fin = System.nanoTime();
                long tiempo = fin - inicio;
                tiempos.put("BFS", tiempo);
                dataset.addValue((double) tiempo, "Tiempo (ns)", "BFS");
            }

            if (opciondijkstra.isSelected()) {
                long inicio = System.nanoTime();
                caminoResuelto = buscador.Dijkstra(laberinto);
                long fin = System.nanoTime();
                long tiempo = fin - inicio;
                tiempos.put("Dijkstra", tiempo);
                dataset.addValue((double) tiempo, "Tiempo (ns)", "Dijkstra");
            }

            if (opcionaestrella.isSelected()) {
                long inicio = System.nanoTime();
                caminoResuelto = buscador.AEstrella(laberinto);
                long fin = System.nanoTime();
                long tiempo = fin - inicio;
                tiempos.put("A*", tiempo);
                dataset.addValue((double) tiempo, "Tiempo (ns)", "A*");
            }

            if (caminoResuelto == null) {
                JOptionPane.showMessageDialog(this,
                        "El laberinto no tiene solución",
                        "Laberinto sin solución", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String mejorAlgoritmo = null;
            long mejorTiempo = Long.MAX_VALUE;

            for (Map.Entry<String, Long> entrada : tiempos.entrySet()) {
                if (entrada.getValue() < mejorTiempo) {
                    mejorTiempo = entrada.getValue();
                    mejorAlgoritmo = entrada.getKey();
                }
            }


            etiquetaAlgoritmo.setText("Algoritmo más eficiente: " + mejorAlgoritmo + " (" + mejorTiempo + " ns)");

            panelLaberintoResuelto.removeAll();
            panelLaberintoResuelto.setLayout(new BorderLayout());

            JPanel dibujoSolucion = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (laberinto == null || caminoResuelto == null){
                        return;
                    }

                    int filas = laberinto.getFilas();
                    int columnas = laberinto.getColumnas();
                    int maxFilas = 20, maxColumnas = 20;

                    int anchoCelda = getWidth() / maxColumnas;
                    int altoCelda = getHeight() / maxFilas;
                    int offsetX = (maxColumnas - columnas) * anchoCelda / 2;
                    int offsetY = (maxFilas - filas) * altoCelda / 2;

                    Set<Integer> conjuntoCamino = new HashSet<>();
                    for (Set<Integer> paso : caminoResuelto) conjuntoCamino.addAll(paso);

                    for (int i = 0; i < laberinto.getCantidadDeNodos(); i++) {
                        int fila = i / columnas;
                        int col = i % columnas;
                        int x = offsetX + col * anchoCelda;
                        int y = offsetY + fila * altoCelda;

                        Set<Integer> vecinos = laberinto.listarNodosAdyacentes(i);
                        boolean arriba = !vecinos.contains(i - columnas);
                        boolean abajo = !vecinos.contains(i + columnas);
                        boolean izquierda = !vecinos.contains(i - 1) || col == 0;
                        boolean derecha = !vecinos.contains(i + 1) || col == columnas - 1;

                        if (i == 0){
                            izquierda = false;
                        }
                        if (i == laberinto.getCantidadDeNodos() - 1){
                            derecha = false;
                        }

                        if (conjuntoCamino.contains(i)) {
                            g.setColor(Color.yellow);
                            g.fillRect(x + 1, y + 1, anchoCelda - 2, altoCelda - 2);
                        }

                        g.setColor(Color.BLACK);
                        if (arriba){
                            g.drawLine(x, y, x + anchoCelda, y);
                        }
                        if (abajo){
                            g.drawLine(x, y + altoCelda, x + anchoCelda, y + altoCelda);
                        }
                        if (izquierda){
                            g.drawLine(x, y, x, y + altoCelda);
                        }
                        if (derecha){
                            g.drawLine(x + anchoCelda, y, x + anchoCelda, y + altoCelda);
                        }
                    }
                }
            };
            dibujoSolucion.setBackground(Color.WHITE);
            panelLaberintoResuelto.add(dibujoSolucion, BorderLayout.CENTER);
            panelLaberintoResuelto.revalidate();
            panelLaberintoResuelto.repaint();

            JFreeChart chart = ChartFactory.createBarChart("Comparación de Tiempos", "Algoritmo", "Tiempo (ns)",
                    dataset, PlotOrientation.VERTICAL, false, true, false);
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setBarPainter(new StandardBarPainter());
            Color azulMarino = new Color(0, 0, 128);
            renderer.setSeriesPaint(0, azulMarino);

            graficaDeTiempos = new ChartPanel(chart);
            ventanaGrafico.getContentPane().removeAll();
            ventanaGrafico.add(graficaDeTiempos);
            ventanaGrafico.setVisible(true);
        });



        panelBotones= new JPanel(new FlowLayout());

        panelBotones.add(botonLaberintoAleatorio);
        panelBotones.add(botonLaberintoCSV);
        panelBotones.add(botonLaberintoManual);
        panelBotones.add(botonResolver);

        etiquetaAlgoritmo = new JLabel("Algoritmo más eficiente: ");
        panelBotones.add(etiquetaAlgoritmo);

        add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.add(panelLaberitoOriginal);
        panelPrincipal.add(panelLaberintoResuelto);
        add(panelPrincipal);


    }

    public void dibujarLaberinto(){
        panelLaberitoOriginal.removeAll();
        panelLaberitoOriginal.setLayout(new BorderLayout());

        JPanel dibujoLaberinto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (laberinto == null){
                    return;
                }

                int filas = laberinto.getFilas();
                int columnas = laberinto.getColumnas();


                int maximoDeFilas = 20;
                int maximoDeColumnas = 20;

                int anchoCelda = getWidth() / maximoDeColumnas;
                int altoCelda  = getHeight() / maximoDeFilas;

                int offsetX = (maximoDeColumnas - columnas) * anchoCelda / 2;
                int offsetY = (maximoDeFilas - filas) * altoCelda / 2;


                for (int i = 0; i < laberinto.getCantidadDeNodos(); i++) {

                    int fila = i / columnas;
                    int col  = i % columnas;
                    int x = offsetX + col * anchoCelda;
                    int y = offsetY + fila * altoCelda;

                    Set<Integer> vecinos = laberinto.listarNodosAdyacentes(i);


                    boolean arriba = !vecinos.contains(i - columnas);
                    boolean abajo = !vecinos.contains(i + columnas);
                    boolean izquierda = !vecinos.contains(i - 1) || col == 0;
                    boolean derecha = !vecinos.contains(i + 1) || col == columnas - 1;
                    if (i == 0){
                        izquierda = false;
                    }
                    if (i == laberinto.getCantidadDeNodos() - 1){
                        derecha = false;
                    }


                    g.setColor(Color.BLACK);
                    if (arriba){
                        g.drawLine(x, y, x + anchoCelda, y);
                    }
                    if (abajo){
                        g.drawLine(x, y + altoCelda, x + anchoCelda, y + altoCelda);
                    }
                    if (izquierda){
                        g.drawLine(x, y, x, y + altoCelda);
                    }
                    if (derecha){
                        g.drawLine(x + anchoCelda, y, x + anchoCelda, y + altoCelda);
                    }
                }
            }
        };
        dibujoLaberinto.setBackground(Color.WHITE);



        panelLaberitoOriginal.add(dibujoLaberinto, BorderLayout.CENTER);
        panelLaberitoOriginal.revalidate();
        panelLaberitoOriginal.repaint();
    }




    public static void main(String[] args){

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(()-> new LaberintoGUI());
    }


}

