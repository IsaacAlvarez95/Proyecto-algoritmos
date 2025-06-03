import java.sql.Time;
import java.util.*;

public class Grafo {

    private List<Set<Integer>> listaDeAdyacencia;
    private Random rm;
    private int filas;
    private int columnas;



    public Grafo(List<Set<Integer>> listaDeAdyacencia){
        rm = new Random();
        this.listaDeAdyacencia=listaDeAdyacencia;
    }

    public Grafo(){
        rm = new Random();
    }

    public List<Set<Integer>> getListaDeAdyacencia() {
        return listaDeAdyacencia;
    }

    public void setListaDeAdyacencia(List<Set<Integer>> listaDeAdyacencia) {
        this.listaDeAdyacencia = listaDeAdyacencia;
    }

    public Random getRm() {
        return rm;
    }

    public void setRm(Random rm) {
        this.rm = rm;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public void inicializarListaDeAdyacencia(int filas, int columnas){
        this.filas = filas;
        this.columnas = columnas;
        int totalNodos = filas*columnas;
        listaDeAdyacencia = new ArrayList<>();

        for(int i=0; i<totalNodos; i++){
            listaDeAdyacencia.add(new HashSet<>());
        }

    }

    public void agregarArista(int nodoOrigen, int nodoDestino){

        listaDeAdyacencia.get(nodoOrigen).add(nodoDestino);
        listaDeAdyacencia.get(nodoDestino).add(nodoOrigen);

    }

    public Set<Integer> listarNodosAdyacentes(int nodo){

        return listaDeAdyacencia.get(nodo);
    }

    public int getCantidadDeNodos(){

        return listaDeAdyacencia.size();
    }

    public Grafo generarLaberintoAleatorio() {
        Random rm = new Random();
        int filas = rm.nextInt(5, 20);
        int columnas = rm.nextInt(5, 20);
        int totalNodos = filas * columnas;

        Grafo grafo = new Grafo();
        grafo.inicializarListaDeAdyacencia(filas, columnas);

        boolean[] visitado = new boolean[totalNodos];

        generarDFS(0, visitado, grafo, filas, columnas, rm);

        return grafo;
    }

    private void generarDFS(int nodo, boolean[] visitado, Grafo grafo, int filas, int columnas, Random rm) {
        visitado[nodo] = true;

        List<Integer> vecinos = obtenerNodosAdyacentes(nodo, filas, columnas);

        for (int i = vecinos.size() - 1; i > 0; i--){
            int j = rm.nextInt(i + 1);
            int temp = vecinos.get(i);
            vecinos.set(i, vecinos.get(j));
            vecinos.set(j, temp);
        }

        for (int vecino : vecinos) {
            if (!visitado[vecino]) {
                grafo.agregarArista(nodo, vecino);
                generarDFS(vecino, visitado, grafo, filas, columnas, rm);
            }
        }
    }

    private List<Integer> obtenerNodosAdyacentes(int nodo, int filas, int columnas) {
        List<Integer> vecinos = new ArrayList<>();
        int fila = nodo / columnas;
        int col = nodo % columnas;

        if (fila > 0){
            vecinos.add(nodo - columnas);
        }
        if (fila < filas - 1){
            vecinos.add(nodo + columnas);
        }
        if (col > 0){
            vecinos.add(nodo - 1);
        }
        if (col < columnas - 1){
            vecinos.add(nodo + 1);
        }

        return vecinos;
    }





    public void imprimirListaDeAdyacencia() {

        for (int i=0; i<listaDeAdyacencia.size(); i++){
            System.out.print(i+": ");
            for (int vecino: listaDeAdyacencia.get(i)){
                System.out.print(vecino+", ");
            }
            System.out.println();
        }
    }





}
