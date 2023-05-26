package Logic;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class AFN_draft {

    // Atributos clase AFN
    private Alfabeto sigma;
    private TreeSet<String> Q;
    private String q0;
    private TreeSet<String> F;
    private TreeSet<String> estadosInaccesibles;
    private Map<String, Map<Character, Set<String>>> delta;

    // Constructores de la clase AFN
    // Constructor de la clase AFN para conjuntos
    public AFN_draft(Alfabeto sigma, TreeSet<String> Q, String q0, TreeSet<String> F,
            Map<String, Map<Character, Set<String>>> delta) {
        this.sigma = sigma;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;

        // Hallar estados inasequibles y guardarlos
        //this.hallarEstadosInasequibles();
    }
    // Constructor de la clase AFN para archivo .nfa inicial
    public AFN_draft(String rutaArchivo) throws Exception {

        // Leer archivo .nfa y crear AFN
        // Depurar archivo en lineas
        ArrayList<String> lineasArchivo = leerArchivo(rutaArchivo);

        // Por cada componente crear una lista de lineas con el contenido
        HashMap<String, ArrayList<String>> secciones = depurarSecciones(lineasArchivo);

        // Depurar cada componente desde su lista de lineas
        // Depurar alfabeto
        this.sigma = depurarAlfabeto(secciones.get("#alphabet"));

        // Depurar estados
        this.Q = new TreeSet<>(depurarEstados(secciones.get("#states")));

        // Depurar estado inicial
        this.q0 = secciones.get("#initial").get(0);
        // verificar que el estado inicial se encuentra en el conjunto de estados
        if (!this.enQ(this.q0)){
          throw new IllegalArgumentException("'"+this.q0+"' no pertenece al conjunto de estados.");
        }

        // Depurar estados de aceptación
        this.F = new TreeSet<>(secciones.get("#accepting"));

        // Depurar transiciones
        //this.delta = depurarTransiciones(secciones.get("#transitions"));

    }
    
    // Leer lineas del archivo y devolverlas como un ArrayList<String>
    private ArrayList<String> leerArchivo(String rutaArchivo) throws Exception {

        // Hacer el buffer para leer el archivo
        ArrayList<String> lineasArchivo = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));

        // Leer el archivo línea por línea
        // Leer primera linea
        String linea = reader.readLine();
        linea.trim();

        // Verificar que sí corresponda a un AFN
        if (linea.equals("#!nfa")) {

            linea = reader.readLine();

        } else {

            // Cerrar el buffer
            reader.close();
            throw new IOException("El archivo no corresponde a un AFN");

        }

        // Leer todo el resto de lineas
        while (linea != null) {
            linea.trim();
            lineasArchivo.add(linea);
            linea = reader.readLine();
        }

        // Cerrar el buffer
        reader.close();

        return lineasArchivo;

    }

    // Leer componentes del AFN y devolverlos con sus contenidos en un HashMap
    private HashMap<String, ArrayList<String>> depurarSecciones(ArrayList<String> lineasArchivo) {

        // Leer componentes del AFN
        List<String> seccionesEsperadas = new ArrayList<>(
                Arrays.asList("#alphabet", "#states", "#initial", "#accepting", "#transitions"));

        // Por cada componente crear una lista de lineas con el contenido
        HashMap<String, ArrayList<String>> secciones = new HashMap<>();
        // Por cada seccion
        for (String seccion : seccionesEsperadas) {

            // Hay un contenido
            ArrayList<String> contenido = new ArrayList<>();
            secciones.put(seccion, contenido);

            // Buscar la sección en el archivo
            Integer indice = 0;
            for (String lineaArchivo : lineasArchivo) {

                lineaArchivo.trim();
                // Si se encuentra la sección, guardar el contenido
                if (lineaArchivo.equals(seccion)) {

                    Integer inicioSeccion = indice; // Aqui inicia la seccion

                    indice++;
                    String lineaActual = " ";
                    // Guardar el contenido de la sección hasta encontrar la siguiente sección o el
                    // fin del documento
                    while (!seccionesEsperadas.contains(lineaActual) && indice < lineasArchivo.size()) {
                        lineaActual = lineasArchivo.get(indice);
                        lineaActual.trim();

                        if (lineaActual != null || lineaActual != " " || lineaActual != "")
                            contenido.add(lineaActual);

                        contenido.add(lineaActual);
                        indice++;
                    }

                    // Eliminar el contenido de la seccion encontrada en las lineas del archivo
                    lineasArchivo.subList(inicioSeccion, indice).clear();
                    break;

                }

                indice++;

            }

        }

        return secciones;

    }

    // Depurar alfabeto desde su lista de lineas a un conjunto de caracteres
    private Alfabeto depurarAlfabeto(ArrayList<String> listaLineas) {

        // Crear la lista de caracteres que ira en el alfabeto
        LinkedList<Character> alfabeto = new LinkedList<Character>();

        // Por cada linea del alfabeto
        for (String linea : listaLineas) {

            // Si la linea contiene un solo caracter
            if (linea.length() == 1) {

                alfabeto.addLast(linea.charAt(0));

            } else if (linea.contains("-")) {

                // Si la linea contiene un rango de caracteres
                String[] rango = linea.split("-");
                Character inicio = rango[0].charAt(0);
                Character fin = rango[1].charAt(0);
                Character caracter = inicio;

                // Por cada caracter en el rango
                while (caracter <= fin) {
                    alfabeto.addLast(caracter);
                    caracter++;
                }

            }

        }

        // Pasar el iterador de la lista enlazada a un array de caracteres
        Character[] alfabetoArray = new Character[alfabeto.size()];

        // Crear el alfabeto
        return new Alfabeto(alfabetoArray);

    }

    // Método para verificar que el caracter ';' no esté en los nombres de los estados
    private ArrayList<String> depurarEstados(ArrayList<String> listaLineas) {

        // Por cada linea bajo #states
        for (String linea : listaLineas) {

          // Si la linea contiene ';'
          if (linea.contains(";")){
            throw new IllegalArgumentException("Los estados no deben contener el caracter ';'.");
          } 
        }
        
        return listaLineas;

    }
  
    public void exportar(String archivo) throws IOException {
        ;
    }

    // Hallar los estados inasequibles del automata y guardarlos en
    // "this.estadosInasequibles"
    private boolean enQ(String q) {
        return this.Q.contains(q);
    }
}


class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
  }
}
