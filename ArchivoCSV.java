import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;

public class ArchivoCSV {

    private BufferedReader lector;
    private String fila;
    private String[] divisiones;
    private String rutaCSV;
    private Grafo grafoGenerado;

    public ArchivoCSV(String rutaCSV){
        this.rutaCSV=rutaCSV;
    }

    public void leerArchivo(){


        String primeraFila= "";
        String[] splitPrimeraLinea;
        int filas;
        int columnas;

        try{
            lector= new BufferedReader(new FileReader(rutaCSV));
             primeraFila = lector.readLine();

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }

        splitPrimeraLinea=primeraFila.split(",");

        filas= Integer.parseInt(splitPrimeraLinea[0]);
        columnas= Integer.parseInt(splitPrimeraLinea[1]);

        grafoGenerado= new Grafo();
        grafoGenerado.inicializarListaDeAdyacencia(filas, columnas);

        try{


            while ((fila= lector.readLine())!=null){
                String[] splitAdyacencias= fila.split(":");
                int nodoOrigen= Integer.parseInt(splitAdyacencias[0]);

                if(splitAdyacencias[1].contains(",")){
                    String[] splitNodosAdyacentes= splitAdyacencias[1].split(",");
                    for(int i=0; i<splitNodosAdyacentes.length; i++){
                        int nodoDestino= Integer.parseInt(splitNodosAdyacentes[i]);
                        grafoGenerado.agregarArista(nodoOrigen,nodoDestino);
                    }
                }
                else{
                    int nodoDestino= Integer.parseInt(splitAdyacencias[1]);
                    grafoGenerado.agregarArista(nodoOrigen, nodoDestino);
                }


            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
        }finally {
            try{
                lector.close();

            }catch (Exception e){
                JOptionPane.showMessageDialog(null,e);
            }

        }
    }


    public Grafo obtenerGrafo(){

        return grafoGenerado;
    }

    public static void main(String[] args){
        ArchivoCSV nuevoArchivo= new ArchivoCSV("C://Users//irvin//IdeaProjects//ProyectoFinalAlgoritmos//src//csvDePrueba");
        nuevoArchivo.leerArchivo();
        Grafo ejemplo= nuevoArchivo.obtenerGrafo();

        ejemplo.imprimirListaDeAdyacencia();
    }
}
