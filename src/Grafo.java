import java.util.ArrayList;
import java.util.List;

public class Grafo {

    private List<List<Integer>> listaDeAdyacencia;

    public Grafo(List<List<Integer>> listaDeAdyacencia){
        this.listaDeAdyacencia=listaDeAdyacencia;
    }

    public Grafo(){

    }


    public void inicializarListaDeAdyacencia(int numeroDeNodos){
        listaDeAdyacencia = new ArrayList<>();

        for(int i=0; i<numeroDeNodos; i++){
            listaDeAdyacencia.add(new ArrayList<>());
        }

    }




}
