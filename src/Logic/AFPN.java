package Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;

public class AFPN {
    private Set<String> estados;
    private String estadoInicial;
    private Set<String> estadosAceptacion;
    private Set<String> alfabetoCinta;
    private Set<String> alfabetoPila;
    private Set<Transicion> delta;

    // Constructor
    public AFPN(Set<String> estados, String estadoInicial, Set<String> estadosAceptacion, Set<String> alfabetoCinta,
            Set<String> alfabetoPila, Set<Transicion> delta) {
        this.estados = estados;
        this.estadoInicial = estadoInicial;
        this.estadosAceptacion = estadosAceptacion;
        this.alfabetoCinta = alfabetoCinta;
        this.alfabetoPila = alfabetoPila;
        this.delta = delta;
    }

    // Constructor de archivos
    public AFPN(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        try {
            estados = new HashSet<>();
            estadosAceptacion = new HashSet<>();
            alfabetoCinta = new HashSet<>();
            alfabetoPila = new HashSet<>();
            delta = new HashSet<>();
            List<String> lineas = new ArrayList<>();

            // Leer todas las líneas del archivo y almacenarlas en una lista
            Scanner scanner = new Scanner(archivo);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();
                lineas.add(linea);
            }
            scanner.close();

            // Procesar las líneas del archivo
            while (!lineas.isEmpty()) {
                String linea = lineas.get(0);
                lineas.remove(0);

                if (linea.isEmpty()) {
                    continue; // Ignorar líneas vacías
                }

                if (linea.startsWith("#states")) {
                    lineas = leerEstados(lineas);
                } else if (linea.startsWith("#initial")) {
                    lineas = leerEstadoInicial(lineas);
                } else if (linea.startsWith("#accepting")) {
                    lineas = leerEstadosAceptacion(lineas);
                } else if (linea.startsWith("#tapeAlphabet")) {
                    lineas = leerAlfabetoCinta(lineas);
                } else if (linea.startsWith("#stackAlphabet")) {
                    lineas = leerAlfabetoPila(lineas);
                } else if (linea.startsWith("#transitions")) {
                    lineas = leerTransiciones(lineas);
                }
            }

            // Verificar si todos los atributos del AFPN están llenos
            if (estados.isEmpty() || estadosAceptacion.isEmpty() || alfabetoCinta.isEmpty()
                    || alfabetoPila.isEmpty() || delta.isEmpty() || estadoInicial == null) {
                System.out.println("El automata no es valido. Faltan atributos.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> leerEstados(List<String> lineas) {
        lineas.remove(0); // Remover la línea del encabezado
        while (!lineas.isEmpty()) {
            String linea = lineas.get(0);
            if (linea.startsWith("#")) {
                break; // Se encontró otro encabezado, salir del bucle
            }
            estados.add(linea);
            lineas.remove(0);
        }
        return lineas;
    }

    private List<String> leerEstadoInicial(List<String> lineas) {
        lineas.remove(0); // Remover la línea del encabezado
        if (!lineas.isEmpty()) {
            estadoInicial = lineas.get(0);
            lineas.remove(0);
        }
        return lineas;
    }

    private List<String> leerEstadosAceptacion(List<String> lineas) {
        lineas.remove(0); // Remover la línea del encabezado
        while (!lineas.isEmpty()) {
            String linea = lineas.get(0);
            if (linea.startsWith("#")) {
                break; // Se encontró otro encabezado, salir del bucle
            }
            estadosAceptacion.add(linea);
            lineas.remove(0);
        }
        return lineas;
    }

    private List<String> leerAlfabetoCinta(List<String> lineas) {
        lineas.remove(0); // Remover la línea del encabezado
        while (!lineas.isEmpty()) {
            String linea = lineas.get(0);
            if (linea.startsWith("#")) {
                break; // Se encontró otro encabezado, salir del bucle
            }
            alfabetoCinta.add(linea);
            lineas.remove(0);
        }
        return lineas;
    }

    private List<String> leerAlfabetoPila(List<String> lineas) {
        lineas.remove(0); // Remover la línea del encabezado
        while (!lineas.isEmpty()) {
            String linea = lineas.get(0);
            if (linea.startsWith("#")) {
                break; // Se encontró otro encabezado, salir del bucle
            }
            alfabetoPila.add(linea);
            lineas.remove(0);
        }
        return lineas;
    }

    private List<String> leerTransiciones(List<String> lineas) {
        lineas.remove(0); // Remover la línea del encabezado
        while (!lineas.isEmpty()) {
            String linea = lineas.get(0);
            if (linea.startsWith("#")) {
                break; // Se encontró otro encabezado, salir del bucle
            }
            String[] partes = linea.split(":");
            String estadoOrigen = partes[0];
            String[] transiciones = partes[1].split(";");
            for (String transicion : transiciones) {
                String[] destinoYPila = transicion.split(">");
                String estadoDestino = destinoYPila[0];
                String[] simbolosPila = destinoYPila[1].split(",");
                for (String simboloPila : simbolosPila) {
                    delta.add(new Transicion(estadoOrigen, "", simboloPila, estadoDestino, ""));
                }
            }
            lineas.remove(0);
        }
        return lineas;
    }

    private void leerTransiciones(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            if (linea.startsWith("#")) {
                break;
            }
            // Separar la línea en sus componentes correspondientes
            String[] partes = linea.split(":");
            String estadoOrigen = partes[0];
            String simboloEntrada = partes[1];
            String simboloPila = partes[2];
            String[] destinos = partes[3].split(";");
            for (String destino : destinos) {
                String[] transicion = destino.split(">");
                String estadoDestino = transicion[0];
                String simboloPilaNuevo = transicion[1];
                addTransicion(
                        new Transicion(estadoOrigen, simboloEntrada, simboloPila, estadoDestino, simboloPilaNuevo));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estados: ").append(estados).append("\n");
        sb.append("Estado inicial: ").append(estadoInicial).append("\n");
        sb.append("Estados de aceptación: ").append(estadosAceptacion).append("\n");
        sb.append("Alfabeto de cinta: ").append(alfabetoCinta).append("\n");
        sb.append("Alfabeto de pila: ").append(alfabetoPila).append("\n");
        sb.append("Transiciones: ").append(delta).append("\n");
        return sb.toString();
    }

    public void addEstado(String estado) {
        estados.add(estado);
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public void addEstadoAceptacion(String estadoAceptacion) {
        estadosAceptacion.add(estadoAceptacion);
    }

    public void addSimboloCinta(String simboloCinta) {
        alfabetoCinta.add(simboloCinta);
    }

    public void addSimboloPila(String simboloPila) {
        alfabetoPila.add(simboloPila);
    }

    public void addTransicion(Transicion transicion) {
        delta.add(transicion);
    }

    // Getters y setters para acceder a los atributos y conjuntos
    public Set<String> getEstados() {
        return estados;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public Set<String> getEstadosAceptacion() {
        return estadosAceptacion;
    }

    public Set<String> getAlfabetoCinta() {
        return alfabetoCinta;
    }

    public Set<String> getAlfabetoPila() {
        return alfabetoPila;
    }

    public Set<Transicion> getDelta() {
        return delta;
    }

    public void setDelta(Set<Transicion> delta) {
        this.delta = delta;
    }

    public static class Transicion {
        private String estadoOrigen;
        private String simboloEntrada;
        private String simboloPila;
        private String estadoDestino;
        private String simboloPilaNuevo;
        private String operacionPila;
        private String parametroPilaNuevo;

        public Transicion(String estadoOrigen, String simboloEntrada, String simboloPila, String estadoDestino,
                String simboloPilaNuevo) {
            this.estadoOrigen = estadoOrigen;
            this.simboloEntrada = simboloEntrada;
            this.simboloPila = simboloPila;
            this.estadoDestino = estadoDestino;
            this.simboloPilaNuevo = simboloPilaNuevo;
        }

        // Getters para acceder a los atributos de la transición
        public String getEstadoOrigen() {
            return estadoOrigen;
        }

        public String getSimboloEntrada() {
            return simboloEntrada;
        }

        public String getSimboloPila() {
            return simboloPila;
        }

        public String getEstadoDestino() {
            return estadoDestino;
        }

        public String getSimboloPilaNuevo() {
            return simboloPilaNuevo;
        }

        public String getOperacionPila() {
            return operacionPila;
        }

        public String getParametroPilaNuevo() {
            return parametroPilaNuevo;
        }
    }

    public Stack<String> modificarPila(Stack<String> pila, String operacion, String parametro) {
        Stack<String> nuevaPila = new Stack<>();
        if (operacion.equals("PUSH")) {
            nuevaPila.addAll(pila); // Copiar elementos actuales de la pila
            nuevaPila.push(parametro); // Agregar el nuevo elemento al tope de la pila
        } else if (operacion.equals("POP")) {
            nuevaPila.addAll(pila); // Copiar elementos actuales de la pila
            nuevaPila.pop(); // Eliminar el elemento del tope de la pila
        } else if (operacion.equals("REPLACE")) {
            if (!pila.isEmpty()) {
                nuevaPila.addAll(pila.subList(0, pila.size() - 1)); // Copiar todos los elementos excepto el tope
                nuevaPila.push(parametro); // Agregar el nuevo elemento al tope de la pila
            }
        }
        return nuevaPila;
    }

    public boolean procesarCadena(String cadena) {
        System.out.println("Cadena: " + cadena);
        List<String> procesamientos = new ArrayList<>();
        String estadoOrigen = getEstadoInicial(); // Obtener el estado inicial
        // Realizar el procesamiento de la cadena con el estado inicial
        boolean aceptada = procesarCadenaRecursiva(estadoOrigen, cadena, "$", procesamientos);
        if (aceptada) {
            System.out.println("La cadena es aceptada por el AFPN.");
        } else {
            System.out.println("La cadena es rechazada por el AFPN.");
        }
        return aceptada;
    }

    public boolean procesarCadenaConDetalles(String cadena) {
        System.out.println("Cadena: " + cadena);
        List<String> procesamientos = new ArrayList<>();
        String estadoOrigen = getEstadoInicial(); // Obtener el estado inicial
        // Realizar el procesamiento de la cadena con el estado inicial
        boolean aceptada = procesarCadenaRecursiva(estadoOrigen, cadena, "$", procesamientos);
        if (aceptada) {
            for (int i = 0; i < procesamientos.size(); i++) {
                System.out.println("Procesamiento " + (i + 1) + ": " + procesamientos.get(i) + ">>accepted");
            }
        } else {
            for (int i = 0; i < procesamientos.size(); i++) {
                System.out.println("Procesamiento " + (i + 1) + ": " + procesamientos.get(i) + ">>rejected");
            }
        }
        return aceptada;
    }

    private boolean procesarCadenaRecursiva(String estado, String cadena, String pila, List<String> procesamientos) {
        // Verificar si se alcanzó el final de la cadena y la pila está vacía
        if (cadena.isEmpty() && pila.equals("$")) {
            return estadosAceptacion.contains(estado); // Verificar si el estado actual es de aceptación
        }

        boolean aceptada = false;

        // Obtener todas las transiciones que coinciden con el estado actual, el símbolo
        // de entrada y el símbolo de la pila actual
        List<Transicion> transiciones = obtenerTransiciones(estado, cadena.charAt(0), pila.charAt(0));

        // Procesar cada transición
        for (Transicion transicion : transiciones) {
            String nuevoEstado = transicion.getEstadoDestino();
            String nuevoPila = transicion.getSimboloPilaNuevo() + pila.substring(1); // Actualizar la pila

            // Realizar el procesamiento recursivo con el siguiente símbolo de entrada y
            // pila actualizada
            aceptada = procesarCadenaRecursiva(nuevoEstado, cadena.substring(1), nuevoPila, procesamientos);

            // Si se encontró un procesamiento aceptado, agregarlo a la lista de
            // procesamientos y terminar el bucle
            if (aceptada) {
                procesamientos.add(transicion.toString());
                break;
            }
        }

        return aceptada;
    }

    private List<Transicion> obtenerTransiciones(String estado, char simboloEntrada, char simboloPila) {
        List<Transicion> transiciones = new ArrayList<>();
        // Buscar todas las transiciones que coincidan con el estado actual, el símbolo
        // de entrada y el símbolo de la pila actual
        for (Transicion transicion : delta) {
            if (transicion.getEstadoOrigen().equals(estado) &&
                    transicion.getSimboloEntrada().charAt(0) == simboloEntrada &&
                    transicion.getSimboloPila().charAt(0) == simboloPila) {
                transiciones.add(transicion);
            }
        }
        return transiciones;
    }

    private Set<Transicion> obtenerTransiciones(String estado, String simboloEntrada, String simboloPila) {
        Set<Transicion> transicionesValidas = new HashSet<>();
        for (Transicion transicion : delta) {
            if (transicion.getEstadoOrigen().equals(estado)
                    && transicion.getSimboloEntrada().equals(simboloEntrada)
                    && transicion.getSimboloPila().equals(simboloPila)) {
                transicionesValidas.add(transicion);
            }
        }
        return transicionesValidas;
    }

    private String modificarPila(String pila, String operacion, String parametro) {
        if (operacion.equals("push")) {
            return pila + parametro;
        } else if (operacion.equals("pop")) {
            if (pila.isEmpty()) {
                return "";
            } else {
                return pila.substring(0, pila.length() - 1);
            }
        } else if (operacion.equals("replace")) {
            if (pila.isEmpty()) {
                return parametro;
            } else {
                return pila.substring(0, pila.length() - 1) + parametro;
            }
        }
        return pila;
    }

    public int computarTodosLosProcesamientos(String cadena, String nombreArchivo) {
        List<String> procesamientosAceptados = new ArrayList<>();
        List<String> procesamientosRechazados = new ArrayList<>();
        realizarProcesamientoRecursivo(estadoInicial, cadena, "$", "", procesamientosAceptados,
                procesamientosRechazados);
        guardarProcesamientosEnArchivo(procesamientosAceptados, nombreArchivo + "AceptadasAFPN.txt");
        guardarProcesamientosEnArchivo(procesamientosRechazados, nombreArchivo + "RechazadasAFPN.txt");
        System.out.println("Procesamientos aceptados:");
        for (String procesamiento : procesamientosAceptados) {
            System.out.println(procesamiento);
        }
        System.out.println("Procesamientos rechazados:");
        for (String procesamiento : procesamientosRechazados) {
            System.out.println(procesamiento);
        }
        return procesamientosAceptados.size() + procesamientosRechazados.size();
    }

    private void realizarProcesamientoRecursivo(String estadoActual, String cadena, String pila,
            String procesamientoActual, List<String> procesamientosAceptados, List<String> procesamientosRechazados) {
        // Verificar si se alcanzó el estado de aceptación
        if (estadosAceptacion.contains(estadoActual) && cadena.isEmpty() && pila.equals("$")) {
            procesamientosAceptados.add(procesamientoActual + ">>accepted");
            return;
        }
        // Verificar si se alcanzó el final de la cadena y se llegó a un estado de
        // rechazo
        if (cadena.isEmpty() && !estadosAceptacion.contains(estadoActual)) {
            procesamientosRechazados.add(procesamientoActual + ">>rejected");
            return;
        }
        // Recorrer todas las transiciones para el estado actual
        for (Transicion transicion : delta) {
            // Verificar si la transición coincide con el estado actual, el símbolo de
            // entrada y el símbolo de pila
            if (transicion.getEstadoOrigen().equals(estadoActual) &&
                    (transicion.getSimboloEntrada().equals(cadena.charAt(0) + "")
                            || transicion.getSimboloEntrada().equals("$"))
                    &&
                    (transicion.getSimboloPila().equals(pila.charAt(0) + "")
                            || transicion.getSimboloPila().equals("$"))) {
                // Realizar la transición y actualizar el estado, la cadena y la pila
                String nuevoEstado = transicion.getEstadoDestino();
                String nuevaCadena = cadena.length() > 0 ? cadena.substring(1) : "";
                String nuevaPila = modificarPila(pila, transicion.getOperacionPila(),
                        transicion.getParametroPilaNuevo());
                // Generar el nuevo procesamiento actual
                String nuevoProcesamiento = procesamientoActual.isEmpty()
                        ? "(" + estadoActual + "," + cadena + "," + pila + ")"
                        : procesamientoActual + "->" + "(" + estadoActual + "," + cadena + "," + pila + ")";
                nuevoProcesamiento += "->" + "(" + nuevoEstado + "," + nuevaCadena + "," + nuevaPila + ")";
                // Realizar el siguiente procesamiento recursivamente
                realizarProcesamientoRecursivo(nuevoEstado, nuevaCadena, nuevaPila, nuevoProcesamiento,
                        procesamientosAceptados, procesamientosRechazados);
            }
        }
        // Agregar el procesamiento actual a las listas después de realizar las llamadas
        // recursivas
        // Esto asegura que el resultado se registre correctamente
        // ...
    }

    private void guardarProcesamientosEnArchivo(List<String> procesamientos, String nombreArchivo) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));
            for (String procesamiento : procesamientos) {
                writer.write(procesamiento);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {
        // Verificar si el nombre del archivo es válido, de lo contrario asignar un
        // nombre por defecto
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            nombreArchivo = "ResultadosAFPN.txt";
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));

            // Iterar sobre cada cadena de la lista
            for (String cadena : listaCadenas) {
                List<String> procesamientosAceptados = new ArrayList<>();
                List<String> procesamientosRechazados = new ArrayList<>();

                realizarProcesamientoRecursivo(estadoInicial, cadena, "$", "",
                        procesamientosAceptados, procesamientosRechazados);

                // Obtener el primer procesamiento de aceptación si hay alguno, de lo contrario
                // obtener el primer procesamiento de rechazo
                String procesamientoResultado = procesamientosAceptados.isEmpty()
                        ? (procesamientosRechazados.isEmpty() ? "" : procesamientosRechazados.get(0))
                        : procesamientosAceptados.get(0);

                int numProcesamientos = procesamientosAceptados.size() + procesamientosRechazados.size();
                int numProcesamientosAceptados = procesamientosAceptados.size();
                int numProcesamientosRechazados = procesamientosRechazados.size();
                boolean cadenaAceptada = !procesamientosAceptados.isEmpty();

                // Construir la línea de resultados
                StringBuilder resultado = new StringBuilder();
                resultado.append(cadena).append("\t");
                resultado.append(procesamientoResultado).append("\t");
                resultado.append(numProcesamientos).append("\t");
                resultado.append(numProcesamientosAceptados).append("\t");
                resultado.append(numProcesamientosRechazados).append("\t");
                resultado.append(cadenaAceptada ? "yes" : "no");

                // Guardar el resultado en el archivo
                writer.write(resultado.toString());
                writer.newLine();

                // Imprimir en pantalla si se solicita
                if (imprimirPantalla) {
                    System.out.println(resultado.toString());
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AFPN hallarProductoCartesianoConAFD(AFD afd) {
        // Crear conjuntos de estados, alfabetos y transiciones del AFPN resultante
        Set<String> estadosProducto = new TreeSet<>();
        Set<String> alfabetoCintaProducto = new TreeSet<>();
        Set<String> alfabetoPilaProducto = new TreeSet<>();
        Set<AFPN.Transicion> transicionesProducto = new TreeSet<>();

        // Obtener los atributos del AFD
        TreeSet<String> estadosAFD = afd.getQ();
        String estadoInicialAFD = afd.getQ0();
        TreeSet<String> estadosAceptacionAFD = afd.getF();
        TreeMap<String, TreeMap<Character, String>> deltaAFD = afd.getDelta();

        // Obtener los atributos del AFPN original
        Set<String> estadosOriginal = getEstados();
        Set<String> alfabetoCintaOriginal = getAlfabetoCinta();
        Set<String> alfabetoPilaOriginal = getAlfabetoPila();
        Set<AFPN.Transicion> transicionesOriginal = getDelta();

        // Calcular el producto cartesiano de los estados
        for (String estadoAFD : estadosAFD) {
            for (String estadoOriginal : estadosOriginal) {
                String estadoProducto = estadoAFD + estadoOriginal;
                estadosProducto.add(estadoProducto);

                // Verificar si el estado producto es un estado de aceptación
                boolean esEstadoAceptacion = estadosAceptacionAFD.contains(estadoAFD)
                        && estadosAceptacion.contains(estadoOriginal);
                if (esEstadoAceptacion) {
                    estadosAceptacion.add(estadoProducto);
                }
            }
        }

        // Calcular el producto cartesiano del alfabeto de cinta
        NavigableSet<Character> alfabetoCintaAFD = afd.getSigma().getAlfabeto();
        for (Character simbolo : alfabetoCintaAFD) {
            alfabetoCintaProducto.add(String.valueOf(simbolo));
        }
        alfabetoCintaAFD.addAll(afd.getSigma().getAlfabeto());

        // Calcular el producto cartesiano del alfabeto de pila
        alfabetoPilaProducto.addAll(alfabetoPilaOriginal);

        // Calcular el producto cartesiano de las transiciones
        for (AFPN.Transicion transicionAFD : transicionesOriginal) {
            String estadoOrigenAFD = transicionAFD.getEstadoOrigen();
            String simboloEntradaAFD = transicionAFD.getSimboloEntrada();
            String simboloPilaAFD = transicionAFD.getSimboloPila();
            String estadoDestinoAFD = transicionAFD.getEstadoDestino();
            String simboloPilaNuevoAFD = transicionAFD.getSimboloPilaNuevo();

            for (String estadoOrigen : estadosOriginal) {
                for (String simboloEntrada : alfabetoCintaOriginal) {
                    for (String simboloPila : alfabetoPilaOriginal) {
                        String estadoProducto = estadoOrigenAFD + estadoOrigen;
                        AFPN.Transicion transicionProducto = new AFPN.Transicion(estadoProducto, simboloEntrada,
                                simboloPila, estadoDestinoAFD, simboloPilaNuevoAFD);
                        transicionesProducto.add(transicionProducto);
                    }
                }
            }
        }

        // Crear y retornar el AFPN resultante
        AFPN afpnProducto = new AFPN(estadosProducto, estadoInicial, estadosAceptacion, alfabetoCintaProducto,
                alfabetoPilaProducto, transicionesProducto);
        afpnProducto.setDelta(transicionesProducto);
        return afpnProducto;
    }

}
