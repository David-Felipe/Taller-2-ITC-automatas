package Logic;

import java.util.*;
import java.io.*;
import org.jgrapht.alg.util.*;

public class AFPN implements Comparable<AFPN> {

    // Nombre AFPN
    private String nombreAFPN;

    // Atributos clase AFPN
    private TreeSet<String> Q;
    private String q0;
    private TreeSet<String> F;
    private Alfabeto sigma;
    private Alfabeto gamma;
    private TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> delta;

    // Atributos funcionales AFPN
    private LinkedList<Character> pila;
    private StringBuilder logUltimoProcesamiento;

    // * Constructores de la clase AFPN
    // Constructor de la clase AFPN para conjuntos
    public AFPN(TreeSet<String> Q, String q0, TreeSet<String> F, Alfabeto sigma, Alfabeto gamma,
            TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> delta) throws Exception {

        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.sigma = sigma;
        this.gamma = gamma;
        this.delta = delta;

        // Verificar que el AFPN sea valido
        this.eliminarEstadosInalcanzables();

    }

    // Constructor de la clase AFPN para archivos
    public AFPN(String rutaArchivo) throws Exception {

        // Leer archivo .afpN y crear AFPN
        // Depurar archivo en lineas
        ArrayList<String> lineasArchivo = this.leerArchivo(rutaArchivo);

        // Por cada componente crear una lista de lineas con el contenido del mismo
        HashMap<String, ArrayList<String>> secciones = this.depurarSecciones(lineasArchivo);

        // Depurar estados
        this.Q = new TreeSet<>(secciones.get("#states"));

        // Depurar estado inicial
        this.q0 = secciones.get("#initial").get(0).trim();

        // Depurar estados de aceptacion
        this.F = new TreeSet<>(secciones.get("#accepting"));

        // Depurar alfabeto de cinta
        this.sigma = depurarAlfabeto(secciones.get("#tapeAlphabet"));

        // Depurar alfabeto de pila
        this.gamma = depurarAlfabeto(secciones.get("#stackAlphabet"));

        // Depurar transiciones
        this.delta = depurarTransiciones(secciones.get("#transitions"));

        // Verificar que el AFPN sea valido
        this.eliminarEstadosInalcanzables();

    }

    // * Getters y Setters de la clase AFPN
    // Getters y Setters atributos AFPN
    public String getNombreAFPN() {
        return nombreAFPN;
    }

    public void setNombreAFPN(String nombreAFPN) {
        this.nombreAFPN = nombreAFPN;
    }

    // ** Subclases de la clase AFPN
    // Subclase Destino para el par estadoDestino y topeDestino
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

    // Sublclase tupla de estados para hacer productos cartesianos
    private class TuplaEstados implements Comparable<TuplaEstados> {

        private String estadoAFPN;
        private String estadoAFN;

        public TuplaEstados(String estadoAFPN, String estadoAFN) {

            this.estadoAFPN = estadoAFPN;
            this.estadoAFN = estadoAFN;

        }

        @Override
        public int compareTo(TuplaEstados tuplaComparada) {

            if (this.estadoAFPN.equals(tuplaComparada.getEstado1())) {

                return this.estadoAFN.compareTo(tuplaComparada.getEstado2());

            } else {

                return this.estadoAFPN.compareTo(tuplaComparada.getEstado1());

            }

        }

        // Getters y Setters atributos TuplaEstados
        public String getEstado1() {
            return estadoAFPN;
        }

        public void setEstado1(String estadoAFPN) {
            this.estadoAFPN = estadoAFPN;
        }

        public String getEstado2() {
            return estadoAFN;
        }

        public void setEstado2(String estadoAFN) {
            this.estadoAFN = estadoAFN;
        }

    }

    // ** Para que AFPN sea comparable
    @Override
    public int compareTo(AFPN afpN) {

        return this.nombreAFPN.compareTo(afpN.nombreAFPN);

    }

    // ** Leer AFPN desde un archivo
    // Leer lineas del archivo y devolverlas en una lista
    private ArrayList<String> leerArchivo(String rutaArchivo) throws Exception {

        // Crear lista de lineas del archivo
        ArrayList<String> lineasArchivo = new ArrayList<String>();

        // Crear el lector del archivo
        BufferedReader lectorArchivo = new BufferedReader(new FileReader(rutaArchivo));

        // Leer la primera linea del archivo
        String lineaActual = lectorArchivo.readLine();

        // Verificar que sí comience por #!pda
        if (!lineaActual.trim().equals("#!pda")) {

            // Cerrar el buffer
            lectorArchivo.close();
            throw new Exception("El archivo no comienza por #!pda");

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

    // Leer componentes del AFPN y devolverlos con sus contenidos en un HashMap
    private HashMap<String, ArrayList<String>> depurarSecciones(ArrayList<String> lineasArchivo) {

        // Establecer componentes del AFPN
        List<String> componentesAFPN = Arrays.asList("#states", "#initial", "#accepting", "#tapeAlphabet",
                "#stackAlphabet", "#transitions");

        // Crear HashMap para almacenar los componentes del AFPN
        HashMap<String, ArrayList<String>> secciones = new HashMap<String, ArrayList<String>>();

        // Por cada componente del AFPN
        for (String componente : componentesAFPN) {

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

            transiciones.putIfAbsent(estadoOrigen, new TreeMap<>());
            TreeMap<Character, TreeMap<Character, Destino>> carTransiciones = transiciones.get(estadoOrigen);
            carTransiciones.putIfAbsent(caracterEntrada, new TreeMap<>());
            TreeMap<Character, Destino> topTransiciones = carTransiciones.get(caracterEntrada);
            topTransiciones.put(topeEntrada, new Destino(estadoDestino, topeSalida));

        }

        return transiciones;

    }

    // ** Verificaciones AFPN
    // Eliminar estados inalcanzables en el AFPN
    private void eliminarEstadosInalcanzables() {

        TreeSet<String> estadosAlcanzables = this.hallarEstadosAlcanzables();
        TreeSet<String> estadosInalcanzables = new TreeSet<String>();

        // Por cada estado del AFPN
        for (String estado : this.Q) {

            // Si el estado NO esta en la lista de estados alcanzables
            if (!estadosAlcanzables.contains(estado)) {

                // Agregarlo a la lista de estados inalcanzables
                estadosInalcanzables.add(estado);

            }

        }

        // Eliminar los estados inalcanzables de F y delta
        for (String estado : estadosInalcanzables) {

            this.Q.remove(estado);
            if (this.F.contains(estado))
                this.F.remove(estado);
            this.delta.remove(estado);

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

    // Procesar una cadena y devolver si es aceptada o no
    public Boolean procesarCadena(String cadena, Boolean detalles) {

        // Trimear la cadena
        cadena = cadena.trim();

        // Crear nueva pila
        this.pila = new LinkedList<Character>();

        // Crear una lista enlazada a partir de la cadena
        LinkedList<Character> listaCadena = new LinkedList<Character>();
        for (Character caracter : cadena.toCharArray()) {

            if (caracter != '$' && caracter != ' ' && caracter != '\t' && caracter != '\n') {

                listaCadena.addLast(caracter);

            }

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
        NavigableSet<Character> alfabetoPila = this.gamma.getAlfabeto();
        // Declarar la pila pospaso y la cinta pospaso
        StringBuilder pilaPosPaso;
        StringBuilder cintaPosPaso;

        // Por cada caracter de la cadena hasta que esta y la pila esten vacias o pase
        // algo
        while (!listaCadena.isEmpty() || !this.pila.isEmpty()) {

            // Declarar la cadena y la pila restante
            StringBuilder pilaRestante = new StringBuilder();
            StringBuilder cadenaRestante = new StringBuilder();

            // Obtener la cadena y la pila restante
            for (Character caracter : listaCadena) {
                cadenaRestante.append(caracter);
            }
            for (Character caracter : this.pila) {
                pilaRestante.append(caracter);
            }
            // Si la pila o la cadena estan vacias, meter lambda a la pilaRestante o cadena
            // segun corresponda
            if (this.pila.isEmpty()) {
                pilaRestante.append('$');
            }
            if (listaCadena.isEmpty()) {
                cadenaRestante.append('$');
            }

            // Obtener el caracter actual de cinta y de pila
            Character caracterActual;
            if (listaCadena.isEmpty()) {
                caracterActual = '$';
            } else {
                caracterActual = listaCadena.pop();
            }
            Character topeActual;
            if (this.pila.isEmpty()) {
                topeActual = '$';
            } else {
                topeActual = this.pila.getFirst();
            }

            // Declarar la pila pospaso y la cinta pospaso
            pilaPosPaso = new StringBuilder();
            cintaPosPaso = new StringBuilder();

            // ** Transicion por caracter de cinta
            // Obtener las transiciones del estado actual por caracter
            TreeMap<Character, TreeMap<Character, Destino>> transicionesEstAct = this.delta.get(estadoActual);

            // Si el caracter no pertenece al alfabetoCinta y NO es lambda, rechazar
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
                log.append("El caracter " + caracterActual + " no pertenece al alfabeto de cinta ni es lambda\n");

                // Guardar procesamiento en logUltimoProcesamiento
                this.logUltimoProcesamiento = log;

                return false;

            }

            // Verificar si puedo hacer transiciones lambda desde caracter y de ser posible,
            // hacerlas
            if (transicionesEstAct.containsKey('$')) {

                // Camino a la transicion lambda
                // Obtener las transiciones lambda del estado actual
                TreeMap<Character, Destino> transicionesTop = transicionesEstAct.get('$');

                // Verificar si hay transiciones lambda entre las transicionesTop
                if (transicionesTop.containsKey('$')) {

                    // Obtener destinos
                    Destino destinoTranscn = transicionesTop.get('$');
                    String estadoDestino = destinoTranscn.getEstadoDes();
                    Character topeDestino = destinoTranscn.getTopeDestino();

                    // Hacer la transicion/paso computacional
                    // Dejar la cadena sin cambios
                    if (caracterActual != '$') {
                        listaCadena.addFirst(caracterActual);
                    }

                    // Dejar la pila sin cambios
                    if (topeActual != '$') {
                        topeActual = '$';
                    }

                    // Paso computacional
                    estadoActual = estadoDestino;
                    this.modificarPila(topeActual, topeDestino);

                    // informar el paso computacional
                    for (Character caracter : this.pila) {
                        pilaPosPaso.append(caracter);
                    }

                    // si la pila o la cinta estan vacias, agregar lambda
                    if (this.pila.isEmpty()) {
                        pilaPosPaso.append('$');
                    }

                    // informar detalles en consola
                    if (detalles) {

                        System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante
                                + ") -> (" + estadoDestino + ", " + cadenaRestante + ", " + pilaPosPaso + ")");

                    }

                    // Meter lo mismo al log
                    log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> ["
                            + estadoDestino + ", " + cadenaRestante + ", " + pilaPosPaso + "]\n");

                    // Seguir en el bucle
                    continue;

                }

                if (transicionesTop.containsKey(topeActual)) {

                    // Obtener destinos
                    Destino destinoTranscn = transicionesTop.get(topeActual);
                    String estadoDestino = destinoTranscn.getEstadoDes();
                    Character topeDestino = destinoTranscn.getTopeDestino();

                    // Hacer la transicion/paso computacional
                    // Dejar la cadena sin cambios
                    if (caracterActual != '$') {
                        listaCadena.addFirst(caracterActual);
                    }

                    // Paso computacional
                    estadoActual = estadoDestino;
                    this.modificarPila(topeActual, topeDestino);

                    // informar el paso computacional
                    for (Character caracter : this.pila) {
                        pilaPosPaso.append(caracter);
                    }
                    // si la pila o la cinta estan vacias, agregar lambda
                    if (this.pila.isEmpty()) {
                        pilaPosPaso.append('$');
                    }

                    // Dar los detalles en consola
                    if (detalles) {

                        System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante
                                + ") -> (" + estadoDestino + ", " + cadenaRestante + ", " + pilaPosPaso + ")");

                    }

                    // Meter lo mismo al log
                    log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> ["
                            + estadoDestino + ", " + cadenaRestante + ", " + pilaPosPaso + "]\n");

                    // Seguir en el bucle
                    continue;

                }

            }

            // Verificar si NO puedo hacer transicion por caracter, Abortar
            if (!transicionesEstAct.containsKey(caracterActual)) {

                // Informar el aborto del proceso
                if (detalles) {

                    System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + ") -> ?");
                    System.out.println("La cadena no es aceptada");
                    System.out.println("En el estado " + estadoActual + " no existe una transicion para el caracter "
                            + caracterActual);

                }

                // Meter lo mismo al log
                log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> ?\n");
                log.append("No\n");
                log.append("La cadena no es aceptada\n");
                log.append("En el estado " + estadoActual + " no existe una transicion para el caracter "
                        + caracterActual + "\n");

                // Guardar procesamiento en logUltimoProcesamiento
                this.logUltimoProcesamiento = log;

                return false;

            }

            // ** Transicion por tope
            // Obtener las transiciones por tope del estado actual con el caracter actual
            TreeMap<Character, Destino> transicionesTop = transicionesEstAct.get(caracterActual);

            // Verificar si con el tope puedo hacer transiciones lambda y de ser posible,
            // hacerlas
            if (transicionesTop.containsKey('$')) {

                // Obtener destinos
                Destino destinoTranscn = transicionesTop.get('$');
                String estadoDestino = destinoTranscn.getEstadoDes();
                Character topeDestino = destinoTranscn.getTopeDestino();

                // Dejar la pila sin cambios
                if (topeActual != '$') {
                    topeActual = '$';
                }

                // Paso computacional
                estadoActual = estadoDestino;
                this.modificarPila(topeActual, topeDestino);

                // informar el paso computacional
                for (Character caracter : this.pila) {
                    pilaPosPaso.append(caracter);
                }
                for (Character caracter : listaCadena) {
                    cintaPosPaso.append(caracter);
                }

                // si la pila o la cinta estan vacias, agregar lambda
                if (this.pila.isEmpty()) {
                    pilaPosPaso.append('$');
                }
                if (listaCadena.isEmpty()) {
                    cintaPosPaso.append('$');
                }

                // dar los detalles en consola
                if (detalles) {

                    System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + ") -> ("
                            + estadoDestino + ", " + cintaPosPaso + ", " + pilaPosPaso + ")");

                }

                // Meter lo mismo al log
                log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> [" + estadoDestino
                        + ", " + cintaPosPaso + ", " + pilaPosPaso + "]\n");

                // Seguir en el bucle
                continue;

            }

            // Si el topeActual no pertenece al alfabeto de pila y no es lambda, abortar
            if (!alfabetoPila.contains(topeActual) && topeActual != '$') {

                // Informar el aborto del proceso
                if (detalles) {

                    System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + ") -> ?");
                    System.out.println("La cadena no es aceptada");
                    System.out.println("El tope de la pila " + topeActual + " no pertenece al alfabeto de pila");

                }

                // Meter lo mismo al log
                log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> ?\n");
                log.append("No\n");
                log.append("La cadena no es aceptada\n");
                log.append("El tope de la pila " + topeActual + " no pertenece al alfabeto de pila\n");

                // Guardar procesamiento en logUltimoProcesamiento
                this.logUltimoProcesamiento = log;

                return false;

            }

            // Verificar si NO puedo hacer transicion por tope, de ser asi, Abortar
            if (!transicionesTop.containsKey(topeActual)) {

                // Informar el aborto del proceso
                if (detalles) {

                    System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + ") -> ?");
                    System.out.println("La cadena no es aceptada");
                    System.out.println("En el estado " + estadoActual + " no existe una transicion para el caracter "
                            + caracterActual + " y el tope de la pila " + topeActual);

                }

                // Meter lo mismo al log
                log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> ?\n");
                log.append("No\n");
                log.append("La cadena no es aceptada\n");
                log.append("En el estado " + estadoActual + " no existe una transicion para el caracter "
                        + caracterActual + " y el tope de la pila " + topeActual + "\n");

                // Guardar procesamiento en logUltimoProcesamiento
                this.logUltimoProcesamiento = log;

                return false;

            }

            // ** Paso computacional común
            // Hacer el paso computacional con el top verificado
            Destino destinoTranscn = transicionesTop.get(topeActual);
            String estadoDestino = destinoTranscn.getEstadoDes();
            Character topeDestino = destinoTranscn.getTopeDestino();

            // Paso computacional
            estadoActual = estadoDestino;
            this.modificarPila(topeActual, topeDestino);

            // informar el paso computacional
            for (Character caracter : this.pila) {
                pilaPosPaso.append(caracter);
            }
            for (Character caracter : listaCadena) {
                cintaPosPaso.append(caracter);
            }
            // si la pila o la cinta estan vacias, agregar lambda
            if (this.pila.isEmpty()) {
                pilaPosPaso.append('$');
            }
            if (listaCadena.isEmpty()) {
                cintaPosPaso.append('$');
            }

            // dar los detalles en consola
            if (detalles) {

                System.out.println("(" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + ") -> ("
                        + estadoDestino + ", " + cintaPosPaso + ", " + pilaPosPaso + ")");

            }

            // Meter lo mismo al log
            log.append("[" + estadoActual + ", " + cadenaRestante + ", " + pilaRestante + "] -> [" + estadoDestino
                    + ", " + cintaPosPaso + ", " + pilaPosPaso + "]\n");

        }

        // Finalizar el proceso verificando aceptacion
        // Guardar procesamiento en logUltimoProcesamiento
        this.logUltimoProcesamiento = log;

        // Si el estado actual es de aceptacion, aceptar
        if (this.F.contains(estadoActual)) {

            // Informar el exito del proceso
            if (detalles) {

                System.out.println("La cadena es aceptada");
                System.out.println();

            }

            // Meter lo mismo al log
            log.append("Si\n");
            log.append("La cadena es aceptada\n");

            return true; // Caso en que el estado actual es de aceptacion

        } else {

            // La cadena NO es aceptada
            // Informar que se rechazo la cadena
            if (detalles) {

                System.out.println("La cadena no es aceptada");
                System.out.println();

            }

            // Meter lo mismo al log
            log.append("No\n");
            log.append("La cadena no es aceptada\n");

            return false; // Caso en que el estado actual no es de aceptacion

        }

    }

    public Boolean procesarCadena(String cadena) {

        return this.procesarCadena(cadena, false);

    }

    public Boolean procesarCadenaConDetalles(String cadena) {

        return this.procesarCadena(cadena, true);

    }

    public void procesarListaCadenas(Iterable<String> listaCadenas, String nombreArchivo, Boolean imprimirPantalla) {

        // Meter todo el contenido de los procesamientos en un StringBuilder
        StringBuilder resultado = new StringBuilder();

        for (String cadena : listaCadenas) {

            this.procesarCadena(cadena, imprimirPantalla);

            resultado.append(this.logUltimoProcesamiento);

        }

        // Escribir en un archivo nombreArchivo el resultado
        String[] nombreArchivoSplit = nombreArchivo.split("\\.");
        String extension = nombreArchivoSplit[nombreArchivoSplit.length - 1];
        String nombreNuevoArchivo;

        if (extension.equals("txt")) {

            nombreNuevoArchivo = nombreArchivo;

        } else {

            nombreNuevoArchivo = nombreArchivo + ".txt";

        }

        // Crear el archivo y llenarlo con resultado
        try {

            // Crear el archivo
            File archivo = new File(nombreNuevoArchivo);
            archivo.createNewFile();

            // Escribir el AFN en el archivo
            FileWriter escritor = new FileWriter(archivo);
            escritor.write(resultado.toString());
            escritor.close();

            System.out.println("Archivo creado exitosamente");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo");

        }

    }

    // ** Operaciones AFPN
    // Producto Cartesiano con un AFN
    // public AFPN hallarProductoCartesianoConAFN(AFN afnInput, String operacion)
    // throws Exception {

    // // Obterner los atributos basicos de afdInput
    // TreeSet<String> QInput = afnInput.getQ();
    // String q0Input = afnInput.getQ0();
    // TreeSet<String> FInput = afnInput.getF();
    // TreeMap<String, TreeMap<Character, String>> deltaInput = afnInput.getDelta();

    // // Inicializar los que van a ser los nuevos atributos del AFPN nuevo
    // TreeSet<String> QNuevo = new TreeSet<String>();
    // String q0Nuevo = "(" + this.q0 + ", " + q0Input + ")";
    // TreeSet<String> FNuevo = new TreeSet<String>();
    // TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>> deltaNuevo =
    // new TreeMap<String, TreeMap<Character, TreeMap<Character, Destino>>>();

    // // Inicializar el TreeSet para las tuplas de estados
    // TreeSet<TuplaEstados> tuplasEstados = new TreeSet<TuplaEstados>();

    // // Obtener el QNuevo
    // for (String estadoAFPN : this.Q) {

    // for (String estadoAFN : QInput) {

    // String tuplaEstados = "(" + estadoAFPN + ", " + estadoAFN + ")";
    // QNuevo.add(tuplaEstados);
    // tuplasEstados.add(new TuplaEstados(estadoAFPN, estadoAFN));

    // }

    // }

    // // Obtener el FNuevo
    // for (String estadoAFPN : this.Q) {

    // for (String estadoAFN : QInput) {

    // switch (operacion) {

    // case "union":

    // if (this.F.contains(estadoAFPN) || FInput.contains(estadoAFN)) {

    // FNuevo.add("(" + estadoAFPN + ", " + estadoAFN + ")");

    // }

    // break;

    // case "interseccion":

    // if (this.F.contains(estadoAFPN) && FInput.contains(estadoAFN)) {

    // FNuevo.add("(" + estadoAFPN + ", " + estadoAFN + ")");

    // }

    // break;

    // case "diferencia":

    // if (this.F.contains(estadoAFPN) && !FInput.contains(estadoAFN)) {

    // FNuevo.add("(" + estadoAFPN + ", " + estadoAFN + ")");

    // }

    // break;

    // case "diferencia simetrica":

    // if ((this.F.contains(estadoAFPN) && !FInput.contains(estadoAFN))
    // || (!this.F.contains(estadoAFPN) && FInput.contains(estadoAFN))) {

    // FNuevo.add("(" + estadoAFPN + ", " + estadoAFN + ")");

    // }

    // break;

    // default:

    // System.out.println("Operacion no valida");

    // break;
    // }

    // }

    // }

    // // ** Obtener el deltaNuevo
    // // ! Solo se agrega algo al prod si el AFPN lo contenía, si no, se pierde el
    // // determinismo
    // // Obtener los alfabetos
    // NavigableSet<Character> alfabetoCinta = this.sigma.getAlfabeto();

    // // Obtener el deltaNuevo
    // for (TuplaEstados pareja : tuplasEstados) {

    // // Obtener los estados de la pareja de origen
    // String estadoAFPN = pareja.getEstado1();
    // String estadoAFN = pareja.getEstado2();
    // String estadoOrigen = "(" + estadoAFPN + ", " + estadoAFN + ")";

    // // Inicializar transiciones de esta tupla y agregarlas a deltaNuevo
    // TreeMap<Character, TreeMap<Character, Destino>> transicionesCarProd = new
    // TreeMap<Character, TreeMap<Character, Destino>>();
    // deltaNuevo.put(estadoOrigen, transicionesCarProd);

    // // Obtener transiciones por caracter para este origen
    // TreeMap<Character, String> transicionesCarAFN = deltaInput.get(estadoAFN);
    // TreeMap<Character, TreeMap<Character, Destino>> transicionesCarAFPN =
    // this.delta.get(estadoAFPN);

    // // Si el estadoAFPN tiene transiciones lambda, ya que no está en el alfabeto
    // de
    // // cinta
    // if (transicionesCarAFPN.containsKey('$')) {

    // // Obtener la transicion con lambda como caracter para el AFPN
    // TreeMap<Character, Destino> transicionesLambda =
    // transicionesCarAFPN.get('$');

    // // Inicializar transicionesLambda en tope para este prod y agregarlas a
    // // transicionesCarProd
    // TreeMap<Character, Destino> transicionesLambdaTopeProd = new
    // TreeMap<Character, Destino>();
    // transicionesCarProd.put('$', transicionesLambdaTopeProd);

    // // Por cada transicion posible desde este estado con lambda
    // for (Map.Entry<Character, Destino> posTranLamb :
    // transicionesLambda.entrySet()) {

    // // Crear el nuevo destino a la transicion con lambda
    // Character topeTransLambda = posTranLamb.getKey();
    // Destino destinoLambda = posTranLamb.getValue();
    // String estadoDestTransLambda = destinoLambda.getEstadoDes();
    // Character topeDestTransLambda = destinoLambda.getTopeDestino();
    // String estadoDestinoProd = "(" + estadoDestTransLambda + ", " + estadoAFN +
    // ")";

    // // Agregar la transicion con lambda al AFPN nuevo
    // transicionesLambdaTopeProd.put(topeTransLambda,
    // new Destino(estadoDestinoProd, topeDestTransLambda));

    // }

    // }

    // // Por cada caracter en sigma
    // for (Character caracterCinta : alfabetoCinta) {

    // // ! Solo se agrega algo al prod si el AFPN lo contenía, si no, se pierde el
    // // determinismo
    // // Si el AFPN tiene transiciones para este caracter
    // if (transicionesCarAFPN.containsKey(caracterCinta)) {

    // // Inicializar transiciones de este caracter para el prod y agregarlas a
    // // transicionesCarProd
    // TreeMap<Character, Destino> transicionesPilaProd = new TreeMap<Character,
    // Destino>();
    // transicionesCarProd.put(caracterCinta, transicionesPilaProd);

    // // Obtener transiciones por caracter para este origen
    // // Obtener el destino del AFN
    // String destinoAFN = transicionesCarAFN.get(caracterCinta);

    // // Obtenemos las transiciones de la pila del AFPN para este caracter
    // TreeMap<Character, Destino> transicionesPilaAFPN =
    // transicionesCarAFPN.get(caracterCinta);

    // // Por cada transicion de la pila del AFPN para este caracter
    // for (Map.Entry<Character, Destino> posTranPila :
    // transicionesPilaAFPN.entrySet()) {

    // // Obtenemos el tope y el destino de la transicion por pila actual
    // Character topePilaActAFPN = posTranPila.getKey();
    // Destino destinoPilaActAFPN = posTranPila.getValue();
    // String estadoDestinoPilaActual = destinoPilaActAFPN.getEstadoDes();
    // Character topePilaDestino = destinoPilaActAFPN.getTopeDestino();

    // // Creamos el destino del prod
    // String estadoDestinoProd = "(" + estadoDestinoPilaActual + ", " + destinoAFN
    // + ")";
    // Destino destinoPilaProd = new Destino(estadoDestinoProd, topePilaDestino);

    // // Agregamos la transicion al las transicionesPilaProd
    // transicionesPilaProd.put(topePilaActAFPN, destinoPilaProd);

    // }

    // }

    // }

    // }

    // // Retornar el AFPN resultante
    // return new AFPN(QNuevo, q0Nuevo, FNuevo, this.sigma, this.gamma, deltaNuevo);

    // }

    // public AFPN hallarProductoCartesianoConAFN(AFN afnInput) throws Exception {

    // return this.hallarProductoCartesianoConAFN(afnInput, "interseccion");

    // }

    // ** Imprimir y exportar AFPN
    // Devolver todos los parametros del AFPN en un String
    @Override
    public String toString() {

        // Instanciar el StringBuilder
        StringBuilder automata = new StringBuilder();

        // Formato .pda
        automata.append("#!pda\n");

        // Insertar Q
        automata.append("#states\n");
        for (String estado : this.Q) {
            automata.append(estado + "\n");
        }

        // Insertar q0
        automata.append("#initial\n");
        automata.append(this.q0 + "\n");

        // Insertar F
        automata.append("#accepting\n");
        for (String estado : this.F) {
            automata.append(estado + "\n");
        }

        // Insertar Sigma
        automata.append("#tapeAlphabet\n");
        NavigableSet<Character> alfabetoCinta = this.sigma.getAlfabeto();
        for (Character caracter : alfabetoCinta) {
            automata.append(caracter + "\n");
        }

        // Insertar Gamma
        automata.append("#stackAlphabet\n");
        NavigableSet<Character> alfabetoPila = this.gamma.getAlfabeto();
        for (Character caracter : alfabetoPila) {
            automata.append(caracter + "\n");
        }

        // Insertar Delta
        automata.append("#transitions\n");
        // Por cada estado
        for (String estado : this.Q) {

            // Adquiero las transiciones de ese estado con Caracter
            TreeMap<Character, TreeMap<Character, Destino>> transicionesCar = this.delta.get(estado);

            // Por cada transicion con caracter en este estado
            for (Map.Entry<Character, TreeMap<Character, Destino>> entradaCar : transicionesCar.entrySet()) {

                Character caracterEntrada = entradaCar.getKey();
                TreeMap<Character, Destino> transicionesTop = entradaCar.getValue();

                // Por cada transicion con tope en este estado con este caracter
                for (Map.Entry<Character, Destino> entradaTope : transicionesTop.entrySet()) {

                    Character topeEntrada = entradaTope.getKey();
                    Destino destino = entradaTope.getValue();

                    String estadoDestino = destino.getEstadoDes();
                    Character topeDestino = destino.getTopeDestino();

                    automata.append(estado + " : " + caracterEntrada + " : " + topeEntrada + " > " + estadoDestino
                            + " : " + topeDestino
                            + "\n");

                }

            }

        }

        // Devolver el resultado
        return automata.toString();

    }

    // Exportar AFPN a un archivo .pda
    public void exportar(String nombreArchivo) {

        String[] nombreArchivoSplit = nombreArchivo.split("\\.");
        String extension = nombreArchivoSplit[nombreArchivoSplit.length - 1].trim();
        String nombreNuevoArchivo;

        if (extension.equals("pda")) {

            nombreNuevoArchivo = nombreArchivo;

        } else {

            nombreNuevoArchivo = nombreArchivoSplit[0] + ".pda";

        }

        // Crear el archivo y llenarlo con toString
        try {

            // Crear el archivo
            File archivo = new File(nombreNuevoArchivo);
            archivo.createNewFile();

            // Escribir el AFN en el archivo
            FileWriter escritor = new FileWriter(archivo);
            escritor.write(this.toString());
            escritor.close();

            System.out.println("Archivo del automata creado exitosamente");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo");

        }

    }

}
