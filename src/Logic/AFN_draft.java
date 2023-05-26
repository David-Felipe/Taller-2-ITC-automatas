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
    private HashSet<String> Q;
    private String q0;
    private HashSet<String> F;
    private HashSet<String> estadosInaccesibles;
    private Map<String, Map<Character, Set<String>>> delta;

    // Constructores de la clase AFN
    // Constructor de la clase AFN para conjuntos
    public AFN_draft(Alfabeto sigma, HashSet<String> Q, String q0, HashSet<String> F,
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
        this.Q = new HashSet<>();
        this.F = new HashSet<>();
        this.estadosInaccesibles = new HashSet<>();
        this.delta = new HashMap<>();

        // Leer todas las líneas del archivo y almacenarlas en un ArrayList
        List<String> lineas = new ArrayList<>();
        String linea;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea0 = br.readLine();
            linea0.trim();

            // Verificar que sí corresponda a un AFN
            if (!linea0.startsWith("#!nfa")) {

                // Cerrar el buffer
                br.close();
                throw new IllegalArgumentException("El archivo no corresponde a un AFN");

            }
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea);
                }
            }
        }

        List<String> seccionesEsperadas = new ArrayList<>(
                Arrays.asList("#alphabet", "#states", "#initial", "#accepting", "#transitions"));


        Iterator<String> iteratorSigma = lineas.iterator();
        Iterator<String> iteratorQ = lineas.iterator();
        Iterator<String> iteratorq0 = lineas.iterator();
        Iterator<String> iteratorF = lineas.iterator();
        Iterator<String> iteratorDelta = lineas.iterator();

        while (iteratorSigma.hasNext()) {
            linea = iteratorSigma.next();
            if (linea.startsWith("#alphabet")) {
                Set<Character> sigmaSet = leerAlfabeto(iteratorSigma);
                this.sigma = new Alfabeto(sigmaSet);
                seccionesEsperadas.remove("#alphabet");
            }
        }

        while (iteratorQ.hasNext()) {
            linea = iteratorQ.next();
            if (linea.startsWith("#states")) {
                leerEstados(iteratorQ);
                seccionesEsperadas.remove("#states");
            }
        }

        while (iteratorq0.hasNext()) {
            linea = iteratorq0.next();
            if (linea.startsWith("#initial")) {
                linea = iteratorq0.next().trim();
                if (!this.Q.contains(linea)){
                    throw new IllegalArgumentException("\""+linea+"\" no pertenece al conjunto de estados.");
                }
                this.q0 = linea;
                seccionesEsperadas.remove("#initial");
            }
        }

        while (iteratorF.hasNext()) {
            linea = iteratorF.next();
            if (linea.startsWith("#accepting")) {
                leerEstadosAceptacion(iteratorF);
                seccionesEsperadas.remove("#accepting");
            }
        }

        while (iteratorDelta.hasNext()) {
            linea = iteratorDelta.next();
            if (linea.startsWith("#transitions")) {
                leerTransiciones(iteratorDelta);
                seccionesEsperadas.remove("#transitions");
            }
        }

        if(!seccionesEsperadas.isEmpty()){
            throw new IllegalArgumentException("Faltan secciones en el archivo.");
        }
        System.out.println(this.q0);
        System.out.println(this.Q);
        System.out.println(this.delta);
    }

    private Set<Character> leerAlfabeto(Iterator<String> iterator) {
        Set<Character> sigmaSet = new HashSet<>();
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
                    sigmaSet.add(c);
                }
            } else {
                char c = linea.charAt(0);
                sigmaSet.add(c);
            }
        }
        return sigmaSet;
    }
    
    private void leerEstados(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();
            if (linea.startsWith("#") || linea.isEmpty()) {
                break;
            }
            if (linea.contains(";")){
                throw new IllegalArgumentException("Los estados no deben contener el caracter ';'.");
            }
            if (!linea.isEmpty()) {
                this.Q.add(linea);
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
                if (!this.Q.contains(linea)){
                    throw new IllegalArgumentException("\""+linea+"\" no pertenece al conjunto de estados.");
                }
                this.F.add(linea);
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
            String estadoActual = partes[0].trim();
            String[] subpartes = partes[1].split(">");
            char simbolo = subpartes[0].trim().charAt(0);
            String[] estadosSiguientes = subpartes[1].split(";");
            for (String estadoSiguiente : estadosSiguientes){
                estadoSiguiente = estadoSiguiente.trim();
                addTransition(estadoActual, simbolo, estadoSiguiente);
            }
        }
    }

    public void addTransition(String fromState, Character symbol, String toState) {
        if (this.Q.contains(fromState) && this.Q.contains(toState) && this.sigma.contains(symbol)) {
            Map<Character, Set<String>> stateTransitions = this.delta.computeIfAbsent(fromState,
                    k -> new HashMap<>());
            Set<String> toStates = stateTransitions.computeIfAbsent(symbol, k -> new HashSet<>());
            toStates.add(toState);
        }
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
