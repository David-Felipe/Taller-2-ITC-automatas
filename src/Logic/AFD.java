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
    private AFD AFDMinimizado;
    private ArrayList<ArrayList<String>> tablaEstadosEquivalentes;
    private TreeMap<tuplaEstados, TreeMap<Character, tuplaEstados>> tablaChequeoEquivalencia;

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

        // Verificar que el AFD esté completo y agregar estado limbo donde no
        this.verificarCorregirCompletitudAFD();

        // Hallar estados limbo y guardarlos
        this.hallarEstadosLimbo();

        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInasequibles();

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

    // Clase tupla de estados
    private class tuplaEstados implements Comparable<tuplaEstados> {

        private String estado1;
        private String estado2;

        public tuplaEstados(String estado1, String estado2) {

            this.estado1 = estado1;
            this.estado2 = estado2;

        }

        @Override
        public int compareTo(tuplaEstados tuplaComparada) {

            if (this.estado1.equals(tuplaComparada.getEstado1())) {

                return this.estado2.compareTo(tuplaComparada.getEstado2());

            } else {

                return this.estado1.compareTo(tuplaComparada.getEstado1());

            }

        }

        public String getEstado1() {
            return estado1;
        }

        public String getEstado2() {
            return estado2;
        }

        public void setEstado1(String estado1) {
            this.estado1 = estado1;
        }

        public void setEstado2(String estado2) {
            this.estado2 = estado2;
        }

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

    // Verificar que el AFD este completo y si no corregirlo para que lo este
    private boolean verificarCorregirCompletitudAFD() {

        // Verificar que todos los estados tengan sus salidas completas
        // Por cada estado del AFD
        for (String estado : this.Q) {

            // Si no tiene todas sus salidas
            if (this.delta.get(estado).size() != this.sigma.getAlfabeto().size()) {

                // Agregar las salidas faltantes
                for (Character caracter : this.sigma.getAlfabeto()) {

                    // Si no tiene la salida
                    if (!this.delta.get(estado).containsKey(caracter)) {

                        // Agregar la salida al estado limbo
                        this.delta.get(estado).put(caracter, "limbo");

                    }

                }

            }

        }

        // Verificar que el estado limbo tenga todas sus salidas
        // Si no tiene todas sus salidas
        if (this.delta.get("limbo").size() != this.sigma.getAlfabeto().size()) {

            // Agregar las salidas faltantes
            for (Character caracter : this.sigma.getAlfabeto()) {

                // Si no tiene la salida
                if (!this.delta.get("limbo").containsKey(caracter)) {

                    // Agregar la salida al estado limbo
                    this.delta.get("limbo").put(caracter, "limbo");

                }

            }

        }

        return true;

    }

    // Hallar los estados limbo del automata y guardarlos en "this.estadosLimbo"
    private void hallarEstadosLimbo() {

        // Crear la lista de estados limbo
        TreeSet<String> estadosLimbo = new TreeSet<String>();

        // Por cada estado del AFD
        for (String estado : this.Q) {

            // Si el estado no tiene transiciones hacia otros estados
            if (this.delta.get(estado).values().stream().allMatch(e -> e.equals(estado))) {

                // Agregar el estado a la lista de estados limbo
                estadosLimbo.add(estado);

            }
            // Si el estado solo tiene transiciones hacia el estado limbo
            if (this.delta.get(estado).values().stream().allMatch(e -> e.equals("limbo"))) {

                // Agregar el estado a la lista de estados limbo
                estadosLimbo.add(estado);

            }

        }

        // Guardar los estados limbo en "this.estadosLimbo"
        this.estadosLimbo = estadosLimbo;

    }

    // Hallar los estados inasequibles del automata y guardarlos en
    // "this.estadosInasequibles"
    private void hallarEstadosInasequibles() {

        // Crear la lista de estados inasequibles
        TreeSet<String> estadosInasequibles = new TreeSet<String>();
        TreeSet<String> estadosAlcanzables = this.hallarEstadosAlcanzables();

        // Por cada estado del AFD
        for (String estado : this.Q) {

            // Si el estado no es alcanzable desde el estado inicial
            if (!estadosAlcanzables.contains(estado)) {

                // Agregar el estado a la lista de estados inasequibles
                estadosInasequibles.add(estado);

            }

        }

        // Guardar los estados inasequibles en "this.estadosInasequibles"
        this.estadosInasequibles = estadosInasequibles;

    }

    // Hallar los estados alcanzables del automata
    private TreeSet<String> hallarEstadosAlcanzables() {

        // Crear la lista de estados alcanzables
        TreeSet<String> estadosAlcanzables = new TreeSet<String>();

        // Crear la cola de estados por visitar
        LinkedList<String> estadosPorVisitar = new LinkedList<String>();

        // Agregar el estado inicial a la cola de estados por visitar
        estadosPorVisitar.add(this.q0);

        // Mientras la cola de estados por visitar no este vacia
        while (!estadosPorVisitar.isEmpty()) {

            // Obtener el estado actual
            String estadoActual = estadosPorVisitar.removeFirst();

            // Agregar el estado actual a la lista de estados alcanzables
            estadosAlcanzables.add(estadoActual);

            // Por cada estado al que se puede llegar desde el estado actual
            Collection<String> estadosDestino = this.delta.get(estadoActual).values();
            for (String estadoDestino : estadosDestino) {

                // Si el estado destino no ha sido visitado
                if (!estadosAlcanzables.contains(estadoDestino)) {

                    // Agregar el estado destino a la cola de estados por visitar
                    estadosPorVisitar.add(estadoDestino);

                }

            }

        }

        return estadosAlcanzables;

    }

    // Entregar una String con la informacion del AFD
    // Entrega el alfabeto sobre el que está definido (sigma), estados (Q), el
    // estado inicial (q0), estados de aceptacion (F), estados limbo (estadosLimbo),
    // estadosInasequibles (estadosInasequibles) y tabla de transiciones (delta)
    @Override
    public String toString() {

        StringBuilder automata = new StringBuilder();

        // Formato dfa
        automata.append("\n#!dfa\n\n");

        // Insertar sigma
        automata.append("#alphabet\n");
        for (Character caracter : this.sigma.getAlfabeto()) {
            automata.append(caracter + " \n");
        }

        // Insertar Q
        automata.append("\n#states\n");
        for (String estado : this.Q) {
            automata.append(estado + " \n");
        }

        // Insertar q0
        automata.append("\n#initial\n");
        automata.append(this.q0 + "\n");

        // Insertar F
        automata.append("\n#accepting\n");
        for (String estado : this.F) {
            automata.append(estado + " \n");
        }

        // Insertar transiciones
        automata.append("\n#transitions\n");
        NavigableSet<Character> caracteres = this.sigma.getAlfabeto();
        for (String estado : this.Q) {

            TreeMap<Character, String> transicionesAct = this.delta.get(estado);

            for (Character caracter : caracteres) {
                automata.append(estado + " : " + caracter + " > " + transicionesAct.get(caracter) + "\n");
            }

        }

        // Insertar estados limbo
        automata.append("\n#limbo\n");
        for (String estado : this.estadosLimbo) {
            automata.append(estado + " \n");
        }

        // Insertar estados inasequibles
        automata.append("\n#unreachable\n");
        for (String estado : this.estadosInasequibles) {
            automata.append(estado + " \n");
        }

        return automata.toString();

    }

    // Simplificar el automata a su minima expresion
    public void simplificarAFD() {

        hallarEstadosEquivalentes();
        // TODO terminar la simplificacion, con lo hallado crear el AFD simplificado y
        // guardarlo, quitar estados inasequibles y de ser necesario dejar un único
        // estado limbo

    }

    private void hallarEstadosEquivalentes() {

        // Crear tablas usadas para el procedimiento
        this.tablaEstadosEquivalentes = new ArrayList<ArrayList<String>>();
        this.tablaChequeoEquivalencia = new TreeMap<tuplaEstados, TreeMap<Character, tuplaEstados>>();

        // * Poblar la tabla de equivalencias con una fila y una columna por cada estado
        // a
        // * la vez que comparamos estado de aceptacion en la celda
        Integer fila = 0;

        // Por cada estado (fila)
        for (String estado : this.Q) {

            // Hay una linea de columnas
            ArrayList<String> filaActual = new ArrayList<String>();
            Iterator<String> iteradorQ = this.Q.iterator();

            // Con (fila + 1) columnas, pues es una matriz triangular con base abajo
            for (int columna = 0; columna <= fila; columna++) {

                // Comparar estados celda actual
                filaActual.add(this.sonEstadosEquivalentes(estado, iteradorQ.next()));

            }

            // Agregar la fila a la tabla de estados equivalentes
            this.tablaEstadosEquivalentes.add(filaActual);
            // Suma uno a la fila
            fila++;

        }

        // TODO Para cada "estado equivalente" en la tabla hallada anterior, verificar
        // con la tabla si sí lleva a estados equivalentes y hacer las correcciones
        // pertinentes

    }

    private String sonEstadosEquivalentes(String estado1, String estado2) {

        // iteracion por triangulo, verificar si tienen el mismo estado de aceptacion
        if (this.F.contains(estado1) != this.F.contains(estado2)) {

            return "1";

        }

        // iteracion por tabla de transiciones
        tuplaEstados origen = new tuplaEstados(estado1, estado2);
        NavigableSet<Character> alfabeto = this.sigma.getAlfabeto();
        TreeMap<Character, tuplaEstados> transiciones = new TreeMap<Character, tuplaEstados>();

        // Por cada caracter del alfabeto para esta tupla de origen
        for (Character caracter : alfabeto) {

            // Hay una tupla de destino
            String estadoDestino1 = this.delta.get(estado1).get(caracter);
            String estadoDestino2 = this.delta.get(estado2).get(caracter);
            tuplaEstados destino = new tuplaEstados(estadoDestino1, estadoDestino2);

            // Agregarla al mapa de caracter a tupla destino para este origen
            transiciones.put(caracter, destino);

            // Caso en el que llevan a estados no equivalentes pero tenian mismo estado de
            // aceptacion
            if (this.F.contains(estadoDestino1) != this.F.contains(estadoDestino2)) {

                this.tablaChequeoEquivalencia.put(origen, transiciones);
                return "2";

            }

        }

        // Agregar la tupla de origen y el mapa de transiciones a la tabla de
        // chequeo de equivalencia
        this.tablaChequeoEquivalencia.put(origen, transiciones);
        return "E";

    }

}