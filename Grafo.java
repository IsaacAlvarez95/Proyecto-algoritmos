import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grafo {

    private List<Set<Integer>> listaDeAdyacencia;

    public Grafo(List<Set<Integer>> listaDeAdyacencia){
        this.listaDeAdyacencia=listaDeAdyacencia;
    }

    public Grafo(){

    }


    public void inicializarListaDeAdyacencia(int numeroDeNodos){
        listaDeAdyacencia = new ArrayList<>();

        for(int i=0; i<numeroDeNodos; i++){
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
