package Logic;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class AFN_draft {

    // Atributos clase AFN
    private Alfabeto sigma;
    private HashSet<String> Q;
    private String q0;
    private HashSet<String> F;
    private HashSet<String> estadosInaccesibles;
    private HashMap<String, HashMap<Character, HashSet<String>>> delta;

    // Constructores de la clase AFN
    // Constructor de la clase AFN para conjuntos
    public AFN_draft(Alfabeto sigma, HashSet<String> Q, String q0, HashSet<String> F,
            HashMap<String, HashMap<Character, HashSet<String>>> delta) {
        this.sigma = sigma;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;

        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInaccesibles();
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
        this.hallarEstadosInaccesibles();
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
            HashMap<Character, HashSet<String>> stateTransitions = this.delta.computeIfAbsent(fromState,
                    k -> new HashMap<>());
            HashSet<String> toStates = stateTransitions.computeIfAbsent(symbol, k -> new HashSet<>());
            toStates.add(toState);
        }
    }
    
    private void hallarEstadosInaccesibles() {
        HashSet<String> estados = new HashSet<String>();
        estados.add(this.q0);
        int oldSizeEstados = 0;

        NavigableSet<Character> alfabeto = this.sigma.getAlfabeto();
        while (oldSizeEstados != estados.size()){
            oldSizeEstados = estados.size();
            Iterator<String> iterator = estados.iterator();
            HashSet<String> copyEstados = new HashSet<String>();
            copyEstados.addAll(estados);
            while (iterator.hasNext()) {
                String estado = iterator.next();
                for (Character simbolo : alfabeto){
                    HashSet<String> imagen = this.delta(estado, simbolo);
                    if (imagen != null) {
                        copyEstados.addAll(imagen);
                    }
                }
            }
            estados = copyEstados;
        }

        Iterator<String> iterator = this.Q.iterator();
        while (iterator.hasNext()) {
            String estado = iterator.next();
            if (!estados.contains(estado)) {
                this.estadosInaccesibles.add(estado);
            }
        }
    }

    // Implementacion de la funcion de trancision
    public HashSet<String> delta(String estado, Character simbolo) {
        if (this.Q.contains(estado) && this.sigma.contains(simbolo) && this.delta.get(estado)!=null) {
            return this.delta.get(estado).get(simbolo);
        } else {
            return null;
        }
    }

    public Boolean procesarCadena(String cadena) {
        HashSet<String> estados = new HashSet<String>();
        estados.add(this.q0);
        for (int i = 0; i < cadena.length(); i++) {
            HashSet<String> postEstados = new HashSet<String>();

            Iterator<String> iterator = estados.iterator();
            while (iterator.hasNext()) {
                HashSet<String> imagen = this.delta(iterator.next(), cadena.charAt(i));
                if (imagen != null) {
                    postEstados.addAll(imagen);
                }
            }
            if (postEstados.isEmpty()) {
                return false;
            }
            estados = postEstados;
        }
        estados.retainAll(this.F);
        if (estados.isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean procesarCadenaConDetalles(String cadena) {
        ArrayList<HashSet<String>> sets = new ArrayList<HashSet<String>>();

        HashSet<String> estados = new HashSet<String>();
        estados.add(this.q0);
        sets.add(estados);

        for (int i = 0; i < cadena.length(); i++) {
            HashSet<String> postEstados = new HashSet<String>();

            Iterator<String> iterator = estados.iterator();
            while (iterator.hasNext()) {
                HashSet<String> imagen = this.delta(iterator.next(), cadena.charAt(i));
                if (imagen != null) {
                    postEstados.addAll(imagen);
                }
            }
            if (postEstados.isEmpty()) {
                return false;
            }
            sets.add(postEstados);
            estados = postEstados;
        }
        estados.retainAll(this.F);
        if (estados.isEmpty()) {
            return false;
        }


        ArrayList<String> estadosProcesamiento = new ArrayList<String>();
        String estadoFinal = " ";
        for (String estado : sets.get(cadena.length())) {
            estadoFinal = estado;
            break;
        }
        estadosProcesamiento.add(estadoFinal);
        for (int i=cadena.length()-1 ; i>=0 ; i--) {
            for (String estado : sets.get(i)) {
                if (this.delta(estado, cadena.charAt(i))!= null && this.delta(estado, cadena.charAt(i)).contains(estadoFinal)){
                    estadosProcesamiento.add(estado);
                    estadoFinal = estado;
                    break;
                }
            }
        }
        for (int i=cadena.length() ; i>0 ; i--) {
            System.out.printf("["+estadosProcesamiento.get(i)+","+cadena.substring(cadena.length()-i)+"]->");
        }
        System.out.println("Aceptacion");
        return true;
    }

    private String preToString(Boolean mostrarInaccesibles, Boolean addSeccionEstadosInaccesibles) {
        StringBuilder sb = new StringBuilder();

        // formato nfa
        sb.append("#!nfa\n");

        // Insertar sigma
        sb.append("#alphabet\n");
        for (Character simbolo : this.sigma.getAlfabeto()) {
            sb.append(simbolo + " \n");
        }

        // Insertar Q
        sb.append("#states\n");
        for (String estado : this.Q) {
            if (!mostrarInaccesibles && this.estadosInaccesibles.contains(estado)){
                continue;
            }
            sb.append(estado + " \n");
        }

        // Insertar q0
        sb.append("#initial\n");
        sb.append(this.q0 + "\n");

        // Insertar F
        sb.append("#accepting\n");
        for (String estado : this.F) {
            if (!mostrarInaccesibles && this.estadosInaccesibles.contains(estado)){
                continue;
            }
            sb.append(estado + " \n");
        }

        // Insertar estados inaccesibles si mostrarInaccesibles
        if (addSeccionEstadosInaccesibles){
            sb.append("#inaccessible\n");
            for (String estado : this.estadosInaccesibles) {
                sb.append(estado + " \n");
            }
        }

        // Insertar transiciones
        sb.append("#transitions\n");
        for (String preEstado : this.delta.keySet()) {
            for (Character simbolo : this.delta.get(preEstado).keySet()){
                Boolean masDeUnPostEstado = false;
                sb.append(preEstado + ":" + simbolo + ">");
                for (String postEstado: this.delta.get(preEstado).get(simbolo)){
                    if (masDeUnPostEstado){
                        sb.append(";" + postEstado);
                    } else {
                        sb.append(postEstado);
                        masDeUnPostEstado = true;
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.preToString(true, true);
    }

    public void imprimirAFNLSimplificado() {
        System.out.println(this.preToString(false, false));
    }

    public void exportar(String archivo) throws IOException {
        String[] nombreArchivoSplit = archivo.split("\\.");
        String extension = nombreArchivoSplit[nombreArchivoSplit.length - 1].trim();
        String nombreNuevoArchivo;

        if (extension.equals("nfa")) {

            nombreNuevoArchivo = archivo;

        } else {

            nombreNuevoArchivo = nombreArchivoSplit[0] + ".nfa";

        }

        // Crear el archivo y llenarlo con toString
        try {

            // Crear el archivo
            File archivoAFN = new File(nombreNuevoArchivo);
            archivoAFN.createNewFile();

            // Escribir el AFD en el archivo
            FileWriter escritor = new FileWriter(archivoAFN);
            escritor.write(this.preToString(true, false));
            escritor.close();

            System.out.println("Archivo del AFN creado exitosamente");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo");

        }
    }

    // Hallar los estados inasequibles del automata y guardarlos en
    // "this.estadosInasequibles"
    private boolean enQ(String q) {
        return this.Q.contains(q);
    }
}
