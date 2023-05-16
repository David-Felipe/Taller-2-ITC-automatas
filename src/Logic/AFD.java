package Logic;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AFD {

    // Atributos clase AFD
    private Alfabeto sigma;
    private TreeSet<String> Q;
    private String q0;
    private TreeSet<String> F;
    private TreeMap<String, TreeMap<Character, String>> delta;
    private TreeSet<String> estadosLimbo;
    private TreeSet<String> estadosInasequibles;

    // Constructores de la clase AFD
    // Constructor de la clase AFD para conjuntos
    public AFD(Alfabeto sigma, TreeSet<String> Q, String q0, TreeSet<String> F,
            TreeMap<String, TreeMap<Character, String>> delta) {

        this.sigma = sigma;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;
        this.estadosLimbo = new TreeSet<>();
        this.estadosInasequibles = new TreeSet<>();

        // TODO Verificar que el AFD sea válido y esté completo
        if (!this.verificarCorregirCompletitudAFD()) {

            System.out.println("El AFD no es válido");

        }

    }

    // Constructor de la clase AFD para archivo .afd inicial
    public AFD(String rutaArchivo) throws Exception {

        // Leer archivo .afd y crear AFD
        // Depurar archivo en lineas
        ArrayList<String> lineasArchivo = leerArchivo(rutaArchivo);

        // Por cada componente crear una lista de lineas con el contenido
        HashMap<String, ArrayList<String>> secciones = depurarSecciones(lineasArchivo);

        // Depurar cada componente desde su lista de lineas
        // Depurar alfabeto
        Alfabeto sigma = depurarAlfabeto(secciones.get("#alphabet"));

        // Depurar estados
        TreeSet<String> Q = new TreeSet<>(secciones.get("#states"));

        // Depurar estado inicial
        String q0 = secciones.get("#initial").get(0);

        // Depurar estados de aceptación
        TreeSet<String> F = new TreeSet<>(secciones.get("#accepting"));

        // Depurar transiciones
        TreeMap<String, TreeMap<Character, String>> delta = depurarTransiciones(secciones.get("#transitions"));

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

        // Verificar que sí corresponda a un AFD
        if (linea.equals("#!dfa")) {

            linea = reader.readLine();

        } else {

            // Cerrar el buffer
            reader.close();
            throw new IOException("El archivo no corresponde a un AFD");

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

    // Leer componentes del AFD y devolverlos con sus contenidos en un HashMap
    private HashMap<String, ArrayList<String>> depurarSecciones(ArrayList<String> lineasArchivo) {

        // Leer componentes del AFD
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

        // Crear la lista de caracteres que ira el alfabeto
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

    // Depurar transiciones desde su lista de lineas
    // TreeMap<Character, String>>
    private TreeMap<String, TreeMap<Character, String>> depurarTransiciones(ArrayList<String> listaLineas) {

        // Crear el mapa de transiciones
        TreeMap<String, TreeMap<Character, String>> transiciones = new TreeMap<>();

        // Por cada linea de transicion
        for (String linea : listaLineas) {

            // Separar las partes de la transicion
            String[] partesTransicion = linea.split(":");
            String estadoOrigen = partesTransicion[0].trim();
            String[] partesDestino = partesTransicion[1].split(">");
            String caracterTransicion = partesDestino[0].trim();
            String estadoDestino = partesDestino[1].trim();

            // Si el estado de origen ya existe
            if (transiciones.containsKey(estadoOrigen)) {

                // Obtener el mapa de transiciones del estado de origen
                TreeMap<Character, String> transicionesEstado = transiciones.get(estadoOrigen);

                // Si el caracter de transicion ya existe
                if (transicionesEstado.containsKey(caracterTransicion.charAt(0))) {

                    // Si el estado de destino es diferente al que ya existe
                    if (!transicionesEstado.get(caracterTransicion.charAt(0)).equals(estadoDestino)) {
                        // Mandar error
                        System.out.println("Transicion ambigua");
                    }

                } else {
                    // Si el caracter de transicion no existe
                    // Agregar el caracter de transicion con su estado de destino correspondiente
                    transicionesEstado.put(caracterTransicion.charAt(0), estadoDestino);
                }

            } else {
                // Si el estado de origen no existe
                // Crear el mapa de transiciones del estado de origen
                TreeMap<Character, String> transicionesEstado = new TreeMap<>();
                // Agregar el caracter de transicion con su estado de destino correspondiente
                transicionesEstado.put(caracterTransicion.charAt(0), estadoDestino);
                // Agregar el estado de origen con su mapa de transiciones
                transiciones.put(estadoOrigen, transicionesEstado);
            }

        }

        return transiciones;

    }

    // Verificar que el AFD sea válido y esté completo y corregirlo si es necesario
    private boolean verificarCorregirCompletitudAFD() {

        // TODO Verificar que el AFD sea válido y esté completo

        // TODO Corregir el AFD si es necesario

        return true;

    }

}