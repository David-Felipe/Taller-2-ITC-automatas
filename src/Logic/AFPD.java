package Logic;

import java.util.*;
import java.io.*;
import org.jgrapht.alg.util.*;

public class AFPD implements Comparable<AFPD> {

    // Nombre AFPD
    private String nombreAFPD;

    // Atributos clase AFPD
    private TreeSet<String> Q;
    private String q0;
    private TreeSet<String> F;
    private Alfabeto sigma;
    private Alfabeto gamma;
    private TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> delta;

    // Atributos funcionales AFPD
    private LinkedList<Character> pila = new LinkedList<Character>();
    private StringBuilder logUltimoProcesamiento;

    // * Constructores de la clase AFPD
    // Constructor de la clase AFPD para conjuntos
    public AFPD(TreeSet<String> Q, String q0, TreeSet<String> F, Alfabeto sigma, Alfabeto gamma,
            TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> delta) throws Exception {

        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.sigma = sigma;
        this.gamma = gamma;
        this.delta = delta;

        // Verificar que el AFPD sea valido
        this.verificarDeterminismo();
        this.eliminarEstadosInalcanzables();

    }

    // Constructor de la clase AFPD para archivos
    public AFPD(String rutaArchivo) throws Exception {

        // Leer archivo .afpd y crear AFPD
        // Depurar archivo en lineas
        ArrayList<String> lineasArchivo = this.leerArchivo(rutaArchivo);

        // Por cada componente crear una lista de lineas con el contenido del mismo
        HashMap<String, ArrayList<String>> secciones = this.depurarSecciones(lineasArchivo);

        // Depurar estados
        this.Q = new TreeSet<>(secciones.get("#states"));

        // Depurar estado inicial
        this.q0 = secciones.get("#initial").get(0);

        // Depurar estados de aceptacion
        this.F = new TreeSet<>(secciones.get("#accepting"));

        // Depurar alfabeto de cinta
        this.sigma = depurarAlfabeto(secciones.get("#tapeAlphabet"));

        // Depurar alfabeto de pila
        this.gamma = depurarAlfabeto(secciones.get("#stackAlphabet"));

        // Depurar transiciones
        this.delta = depurarTransiciones(secciones.get("#transitions"));

        // Verificar que el AFPD sea valido
        this.verificarDeterminismo();
        this.eliminarEstadosInalcanzables();

    }

    // ** Subclases de la clase AFPD
    private class Destino {

        private String estadoDes;
        private Character topeDestino;

        public Destino(String estadoDes, Character topeDestino) {

            this.estadoDes = estadoDes;
            this.topeDestino = topeDestino;

        }

        // Getters y Setters atributos Transicion
        public String getEstadoDes() {
            return estadoDes;
        }

        public void setEstadoDes(String estadoDes) {
            this.estadoDes = estadoDes;
        }

        public Character getTopeDestino() {
            return topeDestino;
        }

        public void setTopeDestino(Character topeDestino) {
            this.topeDestino = topeDestino;
        }

    }

    // ** Para que AFPD sea comparable
    @Override
    public int compareTo(AFPD afpd) {

        return this.nombreAFPD.compareTo(afpd.nombreAFPD);

    }

    // ** Leer AFPD desde un archivo
    // Leer lineas del archivo y devolverlas en una lista
    private ArrayList<String> leerArchivo(String rutaArchivo) throws Exception {

        // Crear lista de lineas del archivo
        ArrayList<String> lineasArchivo = new ArrayList<String>();

        // Crear el lector del archivo
        BufferedReader lectorArchivo = new BufferedReader(new FileReader(rutaArchivo));

        // Leer la primera linea del archivo
        String lineaActual = lectorArchivo.readLine();

        // Verificar que sí comience por #!dpda
        if (!lineaActual.trim().equals("#!dpda")) {

            // Cerrar el buffer
            lectorArchivo.close();
            throw new Exception("El archivo no comienza por #!dpda");

        }

        // Leer el archivo linea por linea
        while (lineaActual != null) {

            // Agregar la linea actual a la lista de lineas del archivo
            lineasArchivo.add(lineaActual.trim());

            // Leer la siguiente linea del archivo
            lineaActual = lectorArchivo.readLine();

        }

        // Cerrar el lector del archivo
        lectorArchivo.close();

        // Retornar la lista de lineas del archivo
        return lineasArchivo;

    }

    // Leer componentes del AFPD y devolverlos con sus contenidos en un HashMap
    private HashMap<String, ArrayList<String>> depurarSecciones(ArrayList<String> lineasArchivo) {

        // Establecer componentes del AFPD
        List<String> componentesAFPD = Arrays.asList("#states", "#initial", "#accepting", "#tapeAlphabet",
                "#stackAlphabet", "#transitions");

        // Crear HashMap para almacenar los componentes del AFPD
        HashMap<String, ArrayList<String>> secciones = new HashMap<String, ArrayList<String>>();

        // Por cada componente del AFPD
        for (String componente : componentesAFPD) {

            // Hay un contenido para el componente
            ArrayList<String> contenido = new ArrayList<String>();
            secciones.put(componente, contenido);

            // Buscar la seccion en el archivo
            Integer indice = 0;
            for (String lineaArchivo : lineasArchivo) {

                // Trimear la linea del archivo
                lineaArchivo = lineaArchivo.trim();

                // Si se encuentra la seccion, guardar el contenido
                if (lineaArchivo.equals(componente)) {

                    // Marcar el inicio de seccion
                    Integer inicioSeccion = indice;

                    // Pasar a la primera linea del contenido
                    indice++;
                    String lineaActual = lineasArchivo.get(indice).trim();

                    // Guardar el contenido de la seccion hasta encontrar la siguiente seccion o el
                    // final del archivo
                    while (!lineaActual.contains("#") && indice < lineasArchivo.size()) {

                        // Agregar la linea actual al contenido de la seccion
                        if (lineaActual != null || lineaActual != " " || lineaActual != "")
                            contenido.add(lineaActual);

                        // Pasar a la siguiente linea
                        indice++;
                        lineaActual = lineasArchivo.get(indice).trim();

                    }

                    // Eliminar la seccion del archivo
                    lineasArchivo.subList(inicioSeccion, indice).clear();
                    break;

                }

                // Actualizar el indice
                indice++;

            }

        }

        return secciones;

    }

    // Depurar alfabeto desde su lista de lineas a un conjunto de caracteres
    private Alfabeto depurarAlfabeto(ArrayList<String> listaLineas) {

        // Crear la lista de caracteres que ira el alfabeto
        TreeSet<Character> alfabeto = new TreeSet<Character>();

        // Por cada linea del alfabeto
        for (String linea : listaLineas) {

            // Si la linea contiene un solo caracter
            if (linea.trim().length() == 1) {

                alfabeto.add(linea.charAt(0));

            } else if (linea.contains("-")) {

                // Si la linea contiene un rango de caracteres
                String[] rango = linea.split("-");
                Character inicio = rango[0].trim().charAt(0);
                Character fin = rango[1].trim().charAt(0);
                Character caracter = inicio;

                // Por cada caracter en el rango
                while (caracter <= fin) {
                    alfabeto.add(caracter);
                    caracter++;
                }

            }

        }

        // Crear el alfabeto
        return new Alfabeto(alfabeto);

    }

    // Depurar transiciones desde su lista de lineas
    private TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> depurarTransiciones(
            ArrayList<String> listaLineas) throws Exception {

        // Crear el mapa de transiciones
        TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> transiciones = new TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>>();

        // Por cada linea de transicion
        for (String linea : listaLineas) {

            // Separar las partes de la transicion en arrays
            String[] partesTransicion = linea.split(">");
            String[] partesOrigen = partesTransicion[0].split(":");
            String[] partesDestino = partesTransicion[1].split(":");

            // Separar las partes del origen
            String estadoOrigen = partesOrigen[0].trim();
            Character caracterEntrada = partesOrigen[1].trim().charAt(0);
            Character topeEntrada = partesOrigen[2].trim().charAt(0);

            // Separar las partes del destino
            String estadoDestino = partesDestino[0].trim();
            Character topeSalida = partesDestino[1].trim().charAt(0);

            // Si el estado destino no está contenido en Q
            if (!this.Q.contains(estadoDestino)) {

                // Agregarlo a el y a su mapa de transiciones, alv
                this.Q.add(estadoDestino);
                transiciones.put(estadoDestino, new TreeMap<Character, TreeMap<Character, Destino>>());

            }

            // Si el estado de origen ya existe en el mapa de transiciones
            if (transiciones.containsKey(estadoOrigen)) {

                // Obtener el mapa de transiciones por caracter del estado de origen
                TreeMap<Character, TreeMap<Character, Destino>> carTransiciones = transiciones.get(estadoOrigen);

                // Si el caracter de entrada ya existe
                if (carTransiciones.containsKey(caracterEntrada)) {

                    // Obtener el mapa de transiciones por tope del caracter de entrada
                    TreeMap<Character, Destino> topTransiciones = carTransiciones.get(caracterEntrada);

                    // Si el tope de entrada ya existe
                    if (topTransiciones.containsKey(topeEntrada)) {

                        // Esta es una transicion ambigua
                        System.out.println("El AFPD recien ingresado no es determinista");
                        System.out.println("El estado" + estadoOrigen
                                + "tiene una transición (" + caracterEntrada + ", " + topeEntrada + ") repetida");
                        System.out.println();
                        throw new Exception("El AFPD no es determinista");

                    } else {

                        // Si no, agregamos topeEntrada junto con la transicion que genera
                        topTransiciones.put(topeEntrada, new Destino(estadoDestino, topeSalida));

                    }

                } else {

                    // Si no, agregamos carTransiciones junto con la transicion que genera
                    carTransiciones.put(caracterEntrada, new TreeMap<Character, Destino>());
                    carTransiciones.get(caracterEntrada).put(topeEntrada, new Destino(estadoDestino, topeSalida));

                }

            } else {

                // Si no, agregamos el estado de origen junto con la transicion que genera
                transiciones.put(estadoOrigen, new TreeMap<Character, TreeMap<Character, Destino>>());
                transiciones.get(estadoOrigen).put(caracterEntrada, new TreeMap<Character, Destino>());
                transiciones.get(estadoOrigen).get(caracterEntrada).put(topeEntrada,
                        new Destino(estadoDestino, topeSalida));

            }

        }

        return transiciones;

    }

    // ** Verificaciones AFPD
    // Eliminar estados inalcanzables en el AFPD
    private void eliminarEstadosInalcanzables() {

        TreeSet<String> estadosAlcanzables = this.hallarEstadosAlcanzables();

        // Por cada estado del AFPD
        for (String estado : this.Q) {

            // Si el estado NO esta en la lista de estados alcanzables
            if (!estadosAlcanzables.contains(estado)) {

                // Eliminar el estado del AFPD, de Q, de F(si esta) y de delta
                this.Q.remove(estado);
                if (this.F.contains(estado))
                    this.F.remove(estado);
                this.delta.remove(estado);

            }

        }

    }

    // Hallar los estados alcanzables del automata para luego eliminar los
    // inalcanzables a partir de un depth first search
    private TreeSet<String> hallarEstadosAlcanzables() {

        // Crear la lista de estados alcanzables
        TreeSet<String> estadosAlcanzables = new TreeSet<String>();

        // Crear la cola de estados por visitar
        LinkedList<String> estadosPorVisitar = new LinkedList<String>();

        // Agregar el estado inicial a la cola de estados por visitar
        estadosPorVisitar.add(this.q0);

        // Mientras la cola de estados por visitar NO este vacia
        while (!estadosPorVisitar.isEmpty()) {

            // Obtener el estado actual
            String estadoActual = estadosPorVisitar.removeFirst();

            // Agregar el estado actual a la lista de estados alcanzables
            estadosAlcanzables.add(estadoActual);

            // Obtener las transiciones del estado actual
            TreeMap<Character, TreeMap<Character, Destino>> transicionesAct = this.delta.get(estadoActual);

            // Por cada caracter de transición del estado actual
            for (TreeMap<Character, Destino> carTrans : transicionesAct.values()) {

                // Por cada tope de transición del caracter actual
                for (Destino estadoDestino : carTrans.values()) {

                    String estadoDestinoStr = estadoDestino.getEstadoDes();
                    // Si el estado destino NO esta en la lista de estados alcanzables
                    if (!estadosAlcanzables.contains(estadoDestinoStr)) {

                        // Agregar el estado destino a la cola de estados por visitar
                        estadosPorVisitar.add(estadoDestinoStr);

                    }

                }

            }

        }

        return estadosAlcanzables;

    }

    // Verificar que el AFPD sea Determinista
    private void verificarDeterminismo() throws Exception {

        // Por cada estado del AFPD
        for (String estado : this.Q) {

            // Obtener las posibles salidas para este estado
            TreeMap<Character, TreeMap<Character, Destino>> transicionesAct = this.delta.get(estado);

            // * Garantizar determinismo
            // Si contiene una transición lambda y hay otras transiciones además de esta
            if (transicionesAct.containsKey('$') && transicionesAct.size() > 1) {

                // Obtener posibles transiciones a partir de lambda
                TreeMap<Character, Destino> transicionesLambda = transicionesAct.get('$');

                // Hay una transición con lambda en el tope
                if (transicionesLambda.containsKey('$')) {

                    // El AFPD no es determinista pues tiene una transición con lambda en el tope y
                    // ademas tiene otras transiciones
                    System.out.println("El AFPD recien ingresado no es determinista");
                    System.out.println("El estado" + estado
                            + "tiene una transición ($, $) y al mismo tiempo tiene otras transiciones");
                    System.out.println();
                    throw new Exception("El AFPD no es determinista");

                }

                // Verificar que no haya transiciones con un mismo tope de lambda para este
                // estado
                // Por cada caracter de transición del estado
                for (Map.Entry<Character, TreeMap<Character, Destino>> carTrans : transicionesAct.entrySet()) {

                    // Si el caracter es lambda, saltatelo
                    if (carTrans.getKey() == '$') {

                        continue;

                    }

                    // Si el caracter NO es lambda, verificar que no pidan un mismo tope
                    // Por cada tope de lambda verificar que NO este contenido en el caracter actual
                    for (Character tope : transicionesLambda.keySet()) {

                        // Si el caracter actual contiene un mismo tope que lambda
                        if (carTrans.getValue().containsKey(tope)) {

                            // El AFPD no es determinista pues tiene una transición con lambda donde el tope
                            // se repite en otra transición
                            System.out.println("El AFPD recien ingresado no es determinista");
                            System.out.println("El estado" + estado
                                    + "tiene una transición ($, " + tope + ") y al mismo tiempo tiene otra transición ("
                                    + carTrans.getKey() + ", " + tope + ")");
                            System.out.println();
                            throw new Exception("El AFPD no es determinista");

                        }

                    }

                }

            }

            // Verificar que cada caracter NO tenga un tope lambda y al mismo tiempo otros
            // topes
            // Para cada caracter de transición del estado
            for (Map.Entry<Character, TreeMap<Character, Destino>> carTrans : transicionesAct.entrySet()) {

                // Si este caracter contiene un tope lambda y no es el único tope que contiene
                if (carTrans.getValue().containsKey('$') && carTrans.getValue().size() > 1) {

                    // El AFPD NO es determinista pues para un mismo caracter de transición tiene un
                    // tope lambda y otros topes
                    System.out.println("El AFPD recien ingresado no es determinista");
                    System.out.println("El estado" + estado + "tiene una transición (" + carTrans.getKey()
                            + ", $) y al mismo tiempo tiene otras transiciones para este mismo caracter");
                    System.out.println();
                    throw new Exception("El AFPD no es determinista");

                }

            }

        }

    }

    // ** Procesamiento de cadenas
    // Modificar la pila a partir de un tope de entrada y un tope de salida
    private void modificarPila(Character topeEntrada, Character topeSalida) {

        // Verificar topeEntrada
        if (topeEntrada == '$') {

            // Tope entrada es lambda

            // Verificar topeSalida
            if (topeSalida == '$') {

                // Tope salida es lambda

                // No hacer nada

            } else {

                // Tope salida NO es lambda

                // Agregar topeSalida a la pila
                this.pila.addFirst(topeSalida);

            }

        } else {

            // Tope entrada NO es lambda

            // Verificar topeSalida
            if (topeSalida == '$') {

                // Tope salida es lambda

                // Sacar tope de la pila
                this.pila.removeFirst();

            } else {

                // Tope salida NO es lambda
                // Reemplazaar topeEntrada con topeSalida

                // Sacar tope de la pila
                this.pila.removeFirst();

                // Agregar topeSalida a la pila
                this.pila.addFirst(topeSalida);

            }

        }

    }

    // TODO Procesar una cadena y devolver si es aceptada o no
    public Boolean procesarCadena(String cadena, Boolean detalles) {

        // Crear una lista enlazada a partir de la cadena
        LinkedList<Character> listaCadena = new LinkedList<Character>();
        for (Character caracter : cadena.toCharArray()) {

            listaCadena.addLast(caracter);

        }

        // Crear el log
        StringBuilder log = new StringBuilder();

        // Obtener el estado inicial
        String estadoActual = this.q0;

        // Imprimir la cadena a procesar en caso de que se pidan detalles
        if (detalles) {

            // Espacio para no saturar
            System.out.println();
            System.out.println(cadena);
            System.out.println();

        }

        // Meter lo mismo al log
        log.append("\n\n");
        log.append(cadena);
        log.append("\n");

        // Obtener el alfabeto de cinta y declarar cinta y pila restante
        NavigableSet<Character> alfabetoCinta = this.sigma.getAlfabeto();
        StringBuilder pilaRestante = new StringBuilder();
        StringBuilder cadenaRestante = new StringBuilder();

        // Por cada caracter de la cadena
        while (!listaCadena.isEmpty()) {

            // Si la pila o la cadena estan vacias, agregarles lambda
            if (this.pila.isEmpty()) {

                this.pila.addFirst('$');

            }
            if (listaCadena.isEmpty()) {

                listaCadena.addFirst('$');

            }

            // Obtener la cadena y la pila restante
            for (Character caracter : listaCadena) {
                cadenaRestante.append(caracter);
            }
            for (Character caracter : this.pila) {
                pilaRestante.append(caracter);
            }

            // Obtener el caracter actual
            Character caracterActual = listaCadena.pop();

            // Si el caracter no pertenece al alfabetoCinta y NO es lambda
            if (!alfabetoCinta.contains(caracterActual) && caracterActual != '$') {

                if (detalles) {

                    System.out.println(
                            "El caracter " + caracterActual + " no pertenece al alfabeto de cinta ni es lambda");
                    System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + ") -> ?");
                    System.out.println("La cadena no es aceptada");

                }

                // Meter lo mismo al log
                log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> ?\n");
                log.append("No\n");
                log.append("La cadena no es aceptada\n");
                log.append("El caracter " + caracterActual + " no pertenece al alfabeto ni es lambda\n");

                // Guardar procesamiento en logUltimoProcesamiento
                this.logUltimoProcesamiento = log;

                return false;

            }

            // TODO Verificar si la transicion NO existe para el caracter actual

            // TODO Verificar si la transicion NO existe para el tope actual

            // TODO Obtener el destino y pasar al destino modificando la pila

        }

        // TODO Verificar si el estado actual es final
        return true; // Caso en que descarte todas las negaciones y me dió aceptada

    }

}