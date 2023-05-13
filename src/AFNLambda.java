import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.print.event.PrintEvent;

public class AFNLambda {
    private Set<Character> alphabet;
    private Set<String> states;
    private String initialState;
    private Set<String> acceptingStates;
    private Set<String> inaccessibleStates;
    private Map<String, Map<Character, Set<String>>> transitionFunction;

    // Constructor(alfabeto, estados, estadoInicial, estadosAceptacion,Delta) de la
    // clase para inicializar los atributos.
    public AFNLambda(Set<Character> alphabet, Set<String> states, String initialState, Set<String> acceptingStates) {
        this.alphabet = alphabet;
        this.states = states;
        this.initialState = initialState;
        this.acceptingStates = acceptingStates;
        this.inaccessibleStates = new HashSet<>();
        this.transitionFunction = new HashMap<>();
    }
    // Constructor(alfabeto, estados, estadoInicial, estadosAceptacion,Delta) de la
    // clase para inicializar los atributos.

    // Constructor(nombreArchivo) de la clase para inicializar los atributos a
    // partir de un archivo cuyo formato es el especificado (ver archivo adjunto).
    public AFNLambda(String nombreArchivo) throws IOException {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.acceptingStates = new HashSet<>();
        this.inaccessibleStates = new HashSet<>();
        this.transitionFunction = new HashMap<>();

        // Leer todas las líneas del archivo y almacenarlas en un ArrayList
        List<String> lineas = new ArrayList<>();
        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea);
                }
            }
        }
        List<String> seccionesEsperadas = new ArrayList<>(Arrays.asList("#alphabet", "#states", "#initial", "#accepting", "#transitions"));
        Iterator<String> iteratoralphabet = lineas.iterator();
        Iterator<String> iteratorstates = lineas.iterator();
        Iterator<String> iteratorinitial = lineas.iterator();
        Iterator<String> iteratoraccepting = lineas.iterator();
        Iterator<String> iteratortransitions = lineas.iterator();
        System.out.println(lineas);

        while (iteratoralphabet.hasNext()) {
            linea = iteratoralphabet.next();
            if (linea.startsWith("#!nfe")) {
                continue;
            } else if (linea.startsWith("#alphabet")) {
                leerAlfabeto(iteratoralphabet);
                seccionesEsperadas.remove("#alphabet");
            }
        }

        while (iteratorstates.hasNext()) {
            linea = iteratorstates.next();
            if (linea.startsWith("#!nfe")) {
                continue;
            } else if (linea.startsWith("#states")) {
                leerEstados(iteratorstates);
                seccionesEsperadas.remove("#states");
            }
        }

        while (iteratorinitial.hasNext()) {
            linea = iteratorinitial.next();
            if (linea.startsWith("#!nfe")) {
                continue;
            } else if (linea.startsWith("#initial")) {
                this.initialState = iteratorinitial.next().trim();
                seccionesEsperadas.remove("#initial");
            }
        }

        while (iteratoraccepting.hasNext()) {
            linea = iteratoraccepting.next();
            if (linea.startsWith("#!nfe")) {
                continue;
            } else if (linea.startsWith("#accepting")) {
                leerEstadosAceptacion(iteratoraccepting);
                seccionesEsperadas.remove("#accepting");
            }
        }

        while (iteratortransitions.hasNext()) {
            linea = iteratortransitions.next();
            if (linea.startsWith("#!nfe")) {
                continue;
            } else if (linea.startsWith("#transitions")) {
                leerTransiciones(iteratortransitions);
                seccionesEsperadas.remove("#transitions");
            }
        }
    }

    private void leerAlfabeto(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) {
                break; // Salir del bucle al encontrar la siguiente sección
            }

            if (linea.contains("-")) {
                String[] rangos = linea.split("-");
                char inicio = rangos[0].charAt(0);
                char fin = rangos[1].charAt(0);

                for (char c = inicio; c <= fin; c++) {
                    this.alphabet.add(c);
                }
            } else {
                char c = linea.charAt(0);
                this.alphabet.add(c);
            }
        }
    }

    private void leerEstados(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();
            if (linea.startsWith("#") || linea.isEmpty()) {
                break;
            }

            String[] estados = linea.split(",");
            for (String estado : estados) {
                estado = estado.trim();
                if (!estado.isEmpty()) {
                    this.states.add(estado);
                }
            }
        }
    }

    private void leerEstadosAceptacion(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();
            if (linea.startsWith("#")) {
                break; // Salir del bucle al encontrar la siguiente sección
            }
            if (!linea.isEmpty()) {
                this.acceptingStates.add(linea);
            }
        }
    }

    private void leerTransiciones(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) {
                break; // Salir del bucle al encontrar la siguiente sección
            }
    
            String[] partes = linea.split(":");
            if (partes.length >= 2) {
                String estadoActual = partes[0].trim();
                String[] transiciones = partes[1].split(",");
    
                for (String transicion : transiciones) {
                    String[] subpartes = transicion.split(">");
                    if (subpartes.length >= 2) {
                        Character simbolo = subpartes[0].trim().charAt(0);
                        String[] estadosSiguientes = subpartes[1].split("\\|");
    
                        for (String estadoSiguiente : estadosSiguientes) {
                            estadoSiguiente = estadoSiguiente.trim();
                            addTransition(estadoActual, simbolo, estadoSiguiente);
                        }
                    }
                }
            }
        }
    }
    
    public void addTransition(String fromState, Character symbol, String toState) {
        if (states.contains(fromState) && states.contains(toState) && alphabet.contains(symbol)) {
            Map<Character, Set<String>> stateTransitions = transitionFunction.computeIfAbsent(fromState,
                    k -> new HashMap<>());
            Set<String> toStates = stateTransitions.computeIfAbsent(symbol, k -> new HashSet<>());
            toStates.add(toState);
        }
    }
    // Constructor(nombreArchivo) de la clase para inicializar los atributos a
    // partir de un archivo cuyo formato es el especificado (ver archivo adjunto).

    // lista calcularLambdaClausura(estado): calcula y retorna la λ-Clausura de un
    // estado
    public Set<String> calcularLambdaClausura(String estado) {
        Set<String> lambdaClausura = new HashSet<>();
        Stack<String> stack = new Stack<>();
        lambdaClausura.add(estado);
        stack.push(estado);

        while (!stack.isEmpty()) {
            String currentState = stack.pop();
            Map<Character, Set<String>> transitions = transitionFunction.get(currentState);

            if (transitions != null) {
                Set<String> lambdaTransitions = transitions.get(null);
                if (lambdaTransitions != null) {
                    for (String nextState : lambdaTransitions) {
                        if (!lambdaClausura.contains(nextState)) {
                            lambdaClausura.add(nextState);
                            stack.push(nextState);
                        }
                    }
                }
            }
        }

        return lambdaClausura;
    }
    // lista calcularLambdaClausura(estado): calcula y retorna la λ-Clausura de un
    // estado

    // lista calcularLambdaClausura(conjuntoEstados): calcula y retorna la λClausura
    // de un conjunto de estados.
    public Set<String> calcularLambdaClausura(Set<String> conjuntoEstados) {
        Set<String> lambdaClausura = new HashSet<>();

        for (String estado : conjuntoEstados) {
            Set<String> estadoLambdaClausura = calcularLambdaClausura(estado);
            lambdaClausura.addAll(estadoLambdaClausura);
        }

        return lambdaClausura;
    }
    // lista calcularLambdaClausura(conjuntoEstados): calcula y retorna la λClausura
    // de un conjunto de estados

    // hallarEstadosInaccesibles() para determinar los estados inacessibles del
    // autómata y guardarlos en el atributo correspondiente.
    public void hallarEstadosInaccesibles() {
        Set<String> estadosVisitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        estadosVisitados.add(initialState);
        cola.offer(initialState);

        while (!cola.isEmpty()) {
            String currentState = cola.poll();
            Map<Character, Set<String>> transitions = transitionFunction.get(currentState);

            if (transitions != null) {
                for (Set<String> toStates : transitions.values()) {
                    for (String nextState : toStates) {
                        if (!estadosVisitados.contains(nextState)) {
                            estadosVisitados.add(nextState);
                            cola.offer(nextState);
                        }
                    }
                }
            }
        }

        inaccessibleStates.clear();
        for (String state : states) {
            if (!estadosVisitados.contains(state)) {
                inaccessibleStates.add(state);
            }
        }
    }
    // hallarEstadosInaccesibles() para determinar los estados inacessibles del
    // autómata y guardarlos en el atributo correspondiente.

    /*
     * toString() o método para imprimir donde se vean los estados, estado inicial,
     * estados de aceptación, estados inaccesibles, y tabla de transiciones. El
     * formato debe ser el adjunto acá (Formato de Entrada.pdf). Se darán puntos
     * adicionales si se muestra el grafo.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Estados
        sb.append("Estados:\n");
        for (String state : states) {
            sb.append(state);
            if (state.equals(initialState)) {
                sb.append(" (inicial)");
            }
            if (acceptingStates.contains(state)) {
                sb.append(" (aceptación)");
            }
            if (inaccessibleStates.contains(state)) {
                sb.append(" (inaccesible)");
            }
            sb.append("\n");
        }

        sb.append("\n");

        // Tabla de transiciones
        sb.append("Tabla de transiciones:\n");
        for (String state : states) {
            Map<Character, Set<String>> transitions = transitionFunction.getOrDefault(state, Collections.emptyMap());
            for (Character symbol : alphabet) {
                Set<String> toStates = transitions.getOrDefault(symbol, Collections.emptySet());
                sb.append(state).append(":").append(symbol).append(">");
                if (!toStates.isEmpty()) {
                    sb.append(String.join(",", toStates));
                } else {
                    sb.append("-");
                }
                sb.append("\n");
            }
            Set<String> lambdaTransitions = transitions.getOrDefault(null, Collections.emptySet());
            if (!lambdaTransitions.isEmpty()) {
                sb.append(state).append(":$>").append(String.join(",", lambdaTransitions)).append("\n");
            }
        }

        return sb.toString();
    }
    /*
     * toString() o método para imprimir donde se vean los estados, estado inicial,
     * estados de aceptación, estados inaccesibles, y tabla de transiciones. El
     * formato debe ser el adjunto acá (Formato de Entrada.pdf). Se darán puntos
     * adicionales si se muestra el grafo.
     */

    // imprimirAFNLSimplificado(): método para imprimir donde se vean los estados,
    // estado inicial, estados de aceptación y tabla de transiciones. No se deben
    // mostrar los estados inaccesibles.
    public void imprimirAFNLSimplificado() {
        System.out.println("Estados: " + states);
        System.out.println("Estado inicial: " + initialState);
        System.out.println("Estados de aceptacion: " + acceptingStates);

        System.out.println("Tabla de transiciones:");
        System.out.println("----------------------");
        System.out.println("| Estado |  Simbolo  |  Siguientes Estados  |");
        System.out.println("----------------------");

        for (String fromState : transitionFunction.keySet()) {
            Map<Character, Set<String>> transitions = transitionFunction.get(fromState);
            for (Character symbol : transitions.keySet()) {
                Set<String> toStates = transitions.get(symbol);
                String nextStateStr = String.join(",", toStates);
                System.out.printf("|  %s  |    %c     |         %s        |\n", fromState, symbol, nextStateStr);
            }
        }

        System.out.println("----------------------");
    }
    // imprimirAFNLSimplificado(): método para imprimir donde se vean los estados,
    // estado inicial, estados de aceptación y tabla de transiciones. No se deben
    // mostrar los estados inaccesibles.

    // exportar(archivo): Guardar el autómata en un archivo con el formato
    // especificado (Formato de Entrada.pdf).
    public void exportar(String archivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

        writer.write("#!nfe");
        writer.newLine();
        writer.write("#alphabet");
        writer.newLine();

        for (char symbol : alphabet) {
            writer.write(Character.toString(symbol));
            writer.newLine();
        }

        writer.write("#states");
        writer.newLine();

        for (String state : states) {
            writer.write(state);
            writer.newLine();
        }

        writer.write("#initial");
        writer.newLine();
        writer.write(initialState);
        writer.newLine();

        writer.write("#accepting");
        writer.newLine();

        for (String acceptingState : acceptingStates) {
            writer.write(acceptingState);
            writer.newLine();
        }

        writer.write("#transitions");
        writer.newLine();

        for (String fromState : transitionFunction.keySet()) {
            Map<Character, Set<String>> transitions = transitionFunction.get(fromState);

            for (Character symbol : transitions.keySet()) {
                Set<String> toStates = transitions.get(symbol);

                String toStatesStr = String.join(",", toStates);
                String transitionStr = String.format("%s:%c>%s", fromState, symbol, toStatesStr);

                writer.write(transitionStr);
                writer.newLine();
            }
        }

        writer.close();
    }
    // exportar(archivo): Guardar el autómata en un archivo con el formato
    // especificado (Formato de Entrada.pdf).

    // AFN AFN_LambdaToAFN(AFN_Lambda afnl): recibe un AFN-λ y retorna el AFN
    // equivalente. Debe imprimir la λ-clausura de cada estado y la definición de
    // cada transición incluyendo todos los pasos del procesamiento

    // AFN AFN_LambdaToAFN(AFN_Lambda afnl): recibe un AFN-λ y retorna el AFN
    // equivalente. Debe imprimir la λ-clausura de cada estado y la definición de
    // cada transición incluyendo todos los pasos del procesamiento

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                // Crear instancia de AFNLambda a partir de un archivo
                AFNLambda automata = new AFNLambda(args[0]);

                // Realizar operaciones con el autómata
                // Set<String> lambdaClausura = automata.calcularLambdaClausura("estado");

                // Imprimir el resultado o realizar otras operaciones
                // System.out.println("λ-Clausura: " + lambdaClausura);

                automata.imprimirAFNLSimplificado();
                automata.toString();

            } catch (IOException e) {
                System.out.println("Error al leer el archivo");
                e.printStackTrace();
            }
        } else {
            // Argumentos no proporcionados, utilizar los valores por defecto o solicitar al
            // usuario
            Set<Character> alphabet = new HashSet<>();
            Set<String> states = new HashSet<>();
            String initialState = "estado_inicial";
            Set<String> acceptingStates = new HashSet<>();

            AFNLambda automata = new AFNLambda(alphabet, states, initialState, acceptingStates);

            // Realizar operaciones con el autómata
            Set<String> lambdaClausura = automata.calcularLambdaClausura("estado");

            // Imprimir el resultado o realizar otras operaciones
            System.out.println("λ-Clausura: " + lambdaClausura);
        }
    }
}
