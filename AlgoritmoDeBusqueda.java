import java.util.*;

public class AlgoritmoDeBusqueda {


    //Metodo BFS
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

    //Metodo DIJKSTRA (Todos tienen el mismo peso)
    public List<Set<Integer>> Dijkstra(Grafo grafo){
        
        int n = grafo.getCantidadDeNodos();
        Queue<Integer> cola = new LinkedList<>();
        int[] distancia = new int[n];
        int[] anterior = new int[n];

        Arrays.fill(distancia, Integer.MAX_VALUE);
        Arrays.fill(anterior, -1);

        distancia[0] = 0;
        cola.add(0);

        while(!cola.isEmpty()){
            
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
                
                int distanciaActual = distancia[nodoActual] + 1;
                
                if(distanciaActual < distancia[adyacente]){
                    distancia[adyacente] = distanciaActual;
                    anterior[adyacente] = nodoActual;
                    cola.add(adyacente);
                }
            }    

        }
        return null;

    }

    //Metodo A*
    public List<Set<Integer>> AEstrella(Grafo grafo){

        int n = grafo.getCantidadDeNodos();
        int[] g = new int[n];
        int[] f = new int[n];
        Set<Integer> visitado = new HashSet<>();
        int[] anterior = new int[n];

        Arrays.fill(g, Integer.MAX_VALUE);
        Arrays.fill(f, Integer.MAX_VALUE);
        Arrays.fill(anterior, -1);

        g[0] = 0;
        f[0] = n - 1;

        List<Integer> cola = new ArrayList<>();
        cola.add(0);

        while(!cola.isEmpty()){
            int mejorNodo = cola.get(0);
            for(int nodo : cola){
                if(f[nodo] < f[mejorNodo]){
                    mejorNodo = nodo;
                }
            }

            cola.remove((Integer) mejorNodo);

            if(mejorNodo == n - 1){
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

            visitado.add(mejorNodo);

            Object[] aNodo = grafo.listarNodosAdyacentes(mejorNodo).toArray();

            for(int i = 0; i < aNodo.length; i++){
                int adyacente = (int) aNodo[i];

                if(!visitado.contains(adyacente)){
                    int mejorG = g[mejorNodo] + 1;

                    if(mejorG < g[adyacente]) {
                        anterior[adyacente] = mejorNodo;
                        g[adyacente] = mejorG;
                        f[adyacente] = g[adyacente] + (n - 1 - adyacente);

                        if (!cola.contains(adyacente)) {
                            cola.add(adyacente);
                        }
                    }

                }

            }
            
        }

        return null;

    }

}
