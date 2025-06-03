import java.util.*;

public class AlgoritmoDeBusqueda {


    public List<Set<Integer>> BFS(Grafo grafo){
        int n = grafo.getCantidadDeNodos();
        Queue<Integer> cola = new LinkedList<>();
        Set<Integer> visitados = new HashSet<>();
        int[] anterior = new int[n];
        Arrays.fill(anterior,-1);
        
        cola.add(0);
        visitados.add(0);
        while(!cola.isEmpty()) {

            int nodoActual = cola.poll();
            Object[] aNodo = grafo.listarNodosAdyacentes(nodoActual).toArray();
            
            if(nodoActual == n-1){
                List<Set<Integer>> rutaInversa = new ArrayList<>();
                int nodo = n-1;
                while(nodo != -1){
                    Set<Integer> paso = new HashSet<>();
                    paso.add(nodo);
                    rutaInversa.add(paso);
                    nodo = anterior[nodo];
                }

                List<Set<Integer>> ruta = new ArrayList<>();
                for(int i = rutaInversa.size() - 1; i >= 0; i--){
                    ruta.add(rutaInversa.get(i));
                }
                return ruta;
            }
            
            for(int i = 0; i < aNodo.length; i++){
                int adyacente = (int) aNodo[i];
                if(!visitados.contains(adyacente)){
                    anterior[adyacente] = nodoActual;

                    cola.add(adyacente);
                    visitados.add(adyacente);
                }
            }
        }
        return null;

    }

}
