package Logic;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.*;
import java.util.*;
import java.io.*;

public class AF2P {

    // Atributos clase AF2P
    private Alfabeto sigma;
    private Alfabeto gama;
    private HashSet<String> Q;
    private String q0;
    private HashSet<String> F;
    private HashSet<String> estadosInaccesibles;
    private HashMap<String, HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>>> delta;
    private Stack<Character> pila1 = new Stack<Character>();
    private Stack<Character> pila2 = new Stack<Character>();
    
    // Registro procesamientos
    private String procesamientoAceptacion = null;
    private String procesamientoRechazo = null;
    private HashSet<String> procesamientos = null;
    private HashSet<String> procesamientosAceptacion = null;
    private HashSet<String> procesamientosRechazo = null;
    private int numPasosProcesamientoAceptacion;
    private int numPasosProcesamientoRechazo;
    private Boolean esAceptado = false;
    private int numProcesamientos = 0;


    // Constructores de la clase AF2P
    // Constructor de la clase AFN para conjuntos
    public AF2P(Alfabeto sigma, Alfabeto gama, HashSet<String> Q, String q0, HashSet<String> F,
            HashMap<String, HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>>> delta) {
        this.sigma = sigma;
        this.gama = gama;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;

        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInaccesibles();
    }

    // Constructor de la clase AF2P para archivo .msm inicial
    public AF2P(String rutaArchivo) throws Exception {

        // Leer archivo .msm y crear AF2P
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
            if (!linea0.startsWith("#!msm")) {

                // Cerrar el buffer
                br.close();
                throw new IllegalArgumentException("El archivo no corresponde a un AF2P");

            }
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea);
                }
            }
        }

        List<String> seccionesEsperadas = new ArrayList<>(
                Arrays.asList("#states", "#initial", "#accepting", "#tapeAlphabet", "#stackAlphabet", "#transitions"));

        Iterator<String> iteratorSigma = lineas.iterator();
        Iterator<String> iteratorGama = lineas.iterator();
        Iterator<String> iteratorQ = lineas.iterator();
        Iterator<String> iteratorq0 = lineas.iterator();
        Iterator<String> iteratorF = lineas.iterator();
        Iterator<String> iteratorDelta = lineas.iterator();

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
                if (!this.Q.contains(linea)) {
                    throw new IllegalArgumentException("El estado inicial \"" + linea + "\" no pertenece al conjunto de estados.");
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

        while (iteratorSigma.hasNext()) {
            linea = iteratorSigma.next();
            if (linea.startsWith("#tapeAlphabet")) {
                Set<Character> sigmaSet = leerAlfabeto(iteratorSigma);
                this.sigma = new Alfabeto(sigmaSet);
                seccionesEsperadas.remove("#tapeAlphabet");
            }
        }

        while (iteratorGama.hasNext()) {
            linea = iteratorGama.next();
            if (linea.startsWith("#stackAlphabet")) {
                Set<Character> gamaSet = leerAlfabeto(iteratorGama);
                this.gama = new Alfabeto(gamaSet);
                seccionesEsperadas.remove("#stackAlphabet");
            }
        }

        while (iteratorDelta.hasNext()) {
            linea = iteratorDelta.next();
            if (linea.startsWith("#transitions")) {
                leerTransiciones(iteratorDelta);
                seccionesEsperadas.remove("#transitions");
            }
        }

        if (!seccionesEsperadas.isEmpty()) {
            throw new IllegalArgumentException("Faltan secciones en el archivo.");
        }
        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInaccesibles();
    }

    // Getters
    public Boolean getEsAceptada() {

        return this.esAceptado;
    }
    
    public String getProcesamientoAceptacion() {

        return this.procesamientoAceptacion;
    }

    public String getProcesamientoRechazo() {

        return this.procesamientoRechazo;
    }

    public String getStringListaProcesamientos() {

        StringBuilder sb = new StringBuilder("");
        for (String procesamiento : this.procesamientos) {
            sb.append(procesamiento + "\n");
        }
        return sb.toString();
    }

    public String getStringProcesamientosAceptacion() {

        StringBuilder sb = new StringBuilder();
        for (String procesamientoAceptado : this.procesamientosAceptacion) {
            sb.append(procesamientoAceptado + "\n");
        }
        return sb.toString();
    }

    public String getStringProcesamientosRechazo() {

        StringBuilder sb = new StringBuilder();
        for (String procesamientoRechazado : this.procesamientosRechazo) {
            sb.append(procesamientoRechazado + "\n");
        }
        return sb.toString();
    }

    public NavigableSet<Character> getSigma() {

        return this.sigma.getAlfabeto();
    }
    
    public Alfabeto getAlfabeto() {

        return this.sigma;
    }

    private void leerEstados(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.replaceAll("\\s","");
            if (linea.startsWith("#") || linea.isEmpty()) {
                break;
            }
            if (linea.contains(";")) {
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

            // Salir del bucle al encontrar la siguiente sección
            if (linea.startsWith("#")) {
                break;
            }
            if (!linea.isEmpty()) {
                if (!this.Q.contains(linea)) {
                    throw new IllegalArgumentException("\"" + linea + "\" no pertenece al conjunto de estados.");
                } else {
                    this.F.add(linea);
                }
            }
        }
    }

    private Set<Character> leerAlfabeto(Iterator<String> iterator) {
        Set<Character> alfSet = new HashSet<>();
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();

            // Salir del bucle al encontrar la siguiente sección
            if (linea.isEmpty() || linea.startsWith("#")) {
                break;
            }

            if (!linea.contains("$") && !linea.isBlank()) {
                if (linea.contains("-")) {
                    String[] rangos = linea.split("-");
                    char inicio = rangos[0].charAt(0);
                    char fin = rangos[1].charAt(0);

                    for (char c = inicio; c <= fin; c++) {
                        alfSet.add(c);
                    }
                } else {
                    char c = linea.charAt(0);
                    alfSet.add(c);
                }
            }
        }
        return alfSet;
    }

    private void leerTransiciones(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) {
                break; // Salir del bucle al encontrar la siguiente sección
            }

            String[] in_out = linea.split(">");
            String[] ins = in_out[0].split(":");

            String fromState = ins[0].trim();
            Character symbol = ins[1].trim().charAt(0);
            Character fromTop1 = ins[2].trim().charAt(0);
            Character fromTop2 = ins[3].trim().charAt(0);

            String[] outs = in_out[1].split(";");
            for (String singleOut : outs) {
                String[] outParts = singleOut.split(":");
                String toState = outParts[0].trim();
                Character toTop1 = outParts[1].trim().charAt(0);
                Character toTop2 = outParts[2].trim().charAt(0);
                this.addTransition(fromState, 
                                symbol,
                                fromTop1,
                                fromTop2,
                                toState,
                                toTop1,
                                toTop2);
            }
        }
    }

    // Hallar los estados inasequibles del automata y guardarlos en
    // "this.estadosInasequibles"
    public void hallarEstadosInaccesibles() {
        this.estadosInaccesibles = new HashSet<>();
        HashSet<String> estados = new HashSet<String>();
        estados.add(this.q0);
        int oldSizeEstados = 0;

        while (oldSizeEstados != estados.size()) {
            oldSizeEstados = estados.size();
            Iterator<String> iterator = estados.iterator();
            HashSet<String> copyEstados = new HashSet<String>();
            copyEstados.addAll(estados);
            while (iterator.hasNext()) {
                String estado = iterator.next();

                if (this.delta.get(estado) != null){

                    for (Character symbol : this.delta.get(estado).keySet()) {
                        
                        for (Character fromTop1 : this.delta.get(estado).get(symbol).keySet()) {

                            for (Character fromTop2 : this.delta.get(estado).get(symbol).get(fromTop1).keySet()) {

                                HashSet<String[]> imagen = this.delta.get(estado).get(symbol).get(fromTop1).get(fromTop2);

                                // Agregar cada estado de llegada (elemento[0] para todo elemento en imagen) 
                                // al set de estados accesibles
                                for (String[] elemento : imagen) {
                                        copyEstados.add(elemento[0]);
                                }
                            }
                        }
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
    public HashSet<String[]> delta(String estado, Character simbolo, Character top1, Character top2) {
        if (this.Q.contains(estado) && 
        (this.sigma.contains(simbolo) || simbolo == '$') && 
        (this.gama.contains(top1) || top1 == '$') &&
        (this.gama.contains(top2) || top2 == '$') &&
        this.delta.get(estado) != null &&
        this.delta.get(estado).get(simbolo) != null &&
        this.delta.get(estado).get(simbolo).get(top1) != null) {
            return this.delta.get(estado).get(simbolo).get(top1).get(top2);
        } else {
            return null;
        }
    }

    public void addTransition(String fromState, Character symbol, Character fromTop1, Character fromTop2, String toState,  Character toTop1, Character toTop2) {
        if (this.Q.contains(fromState) && 
        this.Q.contains(toState) && 
        (this.sigma.contains(symbol) || symbol == '$') &&
        (this.gama.contains(fromTop1) || fromTop1 == '$') &&
        (this.gama.contains(fromTop2) || fromTop2 == '$') &&
        (this.gama.contains(toTop1) || toTop1 == '$') &&
        (this.gama.contains(toTop2) || toTop2 == '$')) {
            String[] toConfig = {toState, String.valueOf(toTop1), String.valueOf(toTop2)};
            HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>> stateTransitions = this.delta.computeIfAbsent(fromState,
                    k -> new HashMap<>());
            HashMap<Character, HashMap<Character, HashSet<String[]>>> toStatesTop1 = stateTransitions.computeIfAbsent(symbol,
                    k -> new HashMap<>());
            HashMap<Character, HashSet<String[]>> toStatesTop2 = toStatesTop1.computeIfAbsent(fromTop1,
                    k -> new HashMap<>());
            HashSet<String[]> toStates = toStatesTop2.computeIfAbsent(fromTop2,
                    k -> new HashSet<>());
            toStates.add(toConfig);
        }
    }

    public void modificarPila(Stack<Character> pila, String operacion, String newTop) {
        CharacterIterator iterator = new StringCharacterIterator(newTop);
        switch (operacion){
            case "insertar":
                while (iterator.current() != CharacterIterator.DONE) {
                    Character nextChar = iterator.current();
                    if (!nextChar.equals('$')) {
                        pila.push(iterator.current());
                    }
                    iterator.next();
                }
                break;
            case "reemplazar":
                if (!pila.empty()) {
                    pila.pop();
                    while (iterator.current() != CharacterIterator.DONE) {
                        pila.push(iterator.current());
                        iterator.next();
                    }
                }
                break;
            case "eliminar":
                if (!pila.empty()) {
                    pila.pop();
                }
                break;
            default:
                System.out.println("La operacion para pilas \"" + operacion + "\" no esta definida.");
        }
    }

    public void modificarPila(Stack<Character> pila, String operacion) {
        if (!pila.empty()) {
            pila.pop();
        }
    }

    public void preprocesarCadena(String estado, String cadena, Stack<Character> pila1, Stack<Character> pila2, String str, int numPasos) {
        if (cadena.isEmpty() || cadena.equals("$")) {
            
            this.numProcesamientos++;

            if (pila1.empty() && pila2.empty() && this.F.contains(estado)){
                this.esAceptado = true;
                String finalStr = str + ">>accepted";
                this.procesamientosAceptacion.add(finalStr);
                this.procesamientos.add(finalStr);
                if (this.numPasosProcesamientoAceptacion < 0) {
                    this.numPasosProcesamientoAceptacion = numPasos;
                    this.procesamientoAceptacion = finalStr;
                } else {
                    if (this.numPasosProcesamientoAceptacion > numPasos) {
                        this.numPasosProcesamientoAceptacion = numPasos;
                        this.procesamientoAceptacion = finalStr;
                    }
                }
            } else {
                String finalStr = str + ">>rejected";
                this.procesamientosRechazo.add(finalStr);
                this.procesamientos.add(finalStr);
                if (this.numPasosProcesamientoRechazo < 0) {
                    this.numPasosProcesamientoRechazo = numPasos;
                    this.procesamientoRechazo = finalStr;
                } else {
                    if (this.numPasosProcesamientoRechazo > numPasos) {
                        this.numPasosProcesamientoRechazo = numPasos;
                        this.procesamientoRechazo = finalStr;
                    }
                }
            }
            
            
        } else {

            List<Character> possibleTops1 = new ArrayList<Character>();
            possibleTops1.add('$');
            List<Character> possibleTops2 = new ArrayList<Character>();
            possibleTops2.add('$');
            
            if (!pila1.empty()) {
                possibleTops1.add(pila1.peek());
            } 
            if (!pila2.empty()) {
                possibleTops2.add(pila2.peek());
            }
            Boolean abortado = true;
            for (Character top1 : possibleTops1) {
                for (Character top2 : possibleTops2) {
                    if (this.delta.get(estado) != null &&
                        this.delta.get(estado).get(cadena.charAt(0)) != null &&
                        this.delta.get(estado).get(cadena.charAt(0)).get(top1) != null) {
                            
                            HashSet<String[]> imagenes = this.delta.get(estado).get(cadena.charAt(0)).get(top1).get(top2);

                            if (imagenes != null){

                                abortado = false;

                                for (String[] imagen : imagenes) {
                                    Stack<Character> copiaPila1 = new Stack<Character>();
                                    Stack<Character> copiaPila2 = new Stack<Character>();
                                    copiaPila1.addAll(pila1);
                                    copiaPila2.addAll(pila2);

                                    // Actualizar pila 1
                                    if (top1.equals('$')) {
                                        if (!imagen[1].equals("$")) {
                                            this.modificarPila(copiaPila1, "insertar", imagen[1]);
                                        }
                                    } else {
                                        if (imagen[1].equals("$")) {
                                            this.modificarPila(copiaPila1, "eliminar");
                                        } else {
                                            this.modificarPila(copiaPila1, "reemplazar", imagen[1]);
                                        }
                                    }
                                    
                                    // Actualizar pila 2
                                    if (top2.equals('$')) {
                                        if (!imagen[2].equals("$")) {
                                            this.modificarPila(copiaPila2, "insertar", imagen[2]);
                                        }
                                    } else {
                                        if (imagen[2].equals("$")) {
                                            this.modificarPila(copiaPila2, "eliminar");
                                        } else {
                                            this.modificarPila(copiaPila2, "reemplazar", imagen[2]);
                                        }
                                    }
                                    
                                    String newCadena = cadena.substring(1);

                                    String strPila1 = copiaPila1.toString().replaceAll(", |\\[|\\]","");
                                    
                                    strPila1 = new StringBuilder(strPila1).reverse().toString();
                                    

                                    String strPila2 = copiaPila2.toString().replaceAll(", |\\[|\\]","");
                                    strPila2 = new StringBuilder(strPila2).reverse().toString();
                                    
                                    String newStr = str + "->(" + imagen[0] + ",";
                                    if (newCadena.isEmpty()) {
                                        newStr += "$,";
                                    } else {
                                        newStr += newCadena + ",";
                                    }
                                    if (strPila1.isEmpty()) { 
                                        newStr += "$,";
                                    } else {
                                        newStr += strPila1 + ",";
                                    }
                                    if (strPila2.isEmpty()) { 
                                        newStr += "$)";
                                    } else {
                                        newStr += strPila2 + ")";
                                    }
                                    

                                    preprocesarCadena(imagen[0], 
                                                    newCadena, 
                                                    copiaPila1, 
                                                    copiaPila2,
                                                    newStr,
                                                    numPasos + 1);
                                }
                            }
                        }
                }
            }
            

            if (abortado){
                this.numProcesamientos++;
                String finalStr = str + ">>aborted";
                this.procesamientosRechazo.add(finalStr);
                this.procesamientos.add(finalStr);
                if (this.numPasosProcesamientoRechazo < 0) {
                    this.numPasosProcesamientoRechazo = numPasos;
                    this.procesamientoRechazo = finalStr;
                } else {
                    if (this.numPasosProcesamientoRechazo > numPasos) {
                        this.numPasosProcesamientoRechazo = numPasos;
                        this.procesamientoRechazo = finalStr;
                    }
                }
            }
        }

        List<Character> possibleTops1 = new ArrayList<Character>();
        possibleTops1.add('$');
        List<Character> possibleTops2 = new ArrayList<Character>();
        possibleTops2.add('$');
        
        if (!pila1.empty()) {
            possibleTops1.add(pila1.peek());
        } 
        if (!pila2.empty()) {
            possibleTops2.add(pila2.peek());
        }
        for (Character top1 : possibleTops1) {
            for (Character top2 : possibleTops2) {
                
                if (this.delta.get(estado) != null &&
                this.delta.get(estado).get('$') != null &&
                this.delta.get(estado).get('$').get(top1) != null) {

                    HashSet<String[]> imagenes = this.delta.get(estado).get('$').get(top1).get(top2);
                    
                    if (imagenes != null){

                        for (String[] imagen : imagenes) {
                            Stack<Character> copiaPila1 = new Stack<Character>();
                            Stack<Character> copiaPila2 = new Stack<Character>();
                            copiaPila1.addAll(pila1);
                            copiaPila2.addAll(pila2);

                            // Actualizar pila 1
                            if (top1.equals('$')) {
                                if (!imagen[1].equals("$")) {
                                    this.modificarPila(copiaPila1, "insertar", imagen[1]);
                                }
                            } else {
                                if (imagen[1].equals("$")) {
                                    this.modificarPila(copiaPila1, "eliminar");
                                } else {
                                    this.modificarPila(copiaPila1, "reemplazar", imagen[1]);
                                }
                            }
                            
                            // Actualizar pila 2
                            if (top2.equals('$')) {
                                if (!imagen[2].equals("$")) {
                                    this.modificarPila(copiaPila2, "insertar", imagen[2]);
                                }
                            } else {
                                if (imagen[2].equals("$")) {
                                    this.modificarPila(copiaPila2, "eliminar");
                                } else {
                                    this.modificarPila(copiaPila2, "reemplazar", imagen[2]);
                                }
                            }

                            String strPila1 = copiaPila1.toString().replaceAll(", |\\[|\\]","");
                            strPila1 = new StringBuilder(strPila1).reverse().toString();
                            String strPila2 = copiaPila2.toString().replaceAll(", |\\[|\\]","");
                            strPila2 = new StringBuilder(strPila2).reverse().toString();
                            String newStr = str + "->(" + imagen[0] + ",";
                            if (cadena.isEmpty()) { 
                                newStr += "$,";
                            } else {
                                newStr += cadena + ",";
                            }
                            if (strPila1.isEmpty()) { 
                                newStr += "$,";
                            } else {
                                newStr += strPila1 + ",";
                            }
                            if (strPila2.isEmpty()) { 
                                newStr += "$)";
                            } else {
                                newStr += strPila2 + ")";
                            }

                            preprocesarCadena(imagen[0], 
                                            cadena, 
                                            copiaPila1, 
                                            copiaPila2,
                                            newStr,
                                            numPasos + 1);
                            
                        }
                    }
                    
                }
            }
        }
    }

    public void preprocesarCadena(String cadena) {
        this.procesamientoAceptacion = "";
        this.procesamientoRechazo = "";
        this.numPasosProcesamientoAceptacion = -1;
        this.numPasosProcesamientoRechazo = -1;
        this.procesamientos = new HashSet<String>();
        this.procesamientosAceptacion = new HashSet<String>();
        this.procesamientosRechazo = new HashSet<String>();
        this.esAceptado = false;
        this.numProcesamientos = 0;
        String str = "(" + this.q0 + ",";
        if (cadena.isEmpty()) {
            str += "$,$,$)";
        } else {
            str += cadena + ",$,$)";
        }
        preprocesarCadena(this.q0, cadena, this.pila1, this.pila2, str, 0);

    }

    public Boolean procesarCadena(String cadena) {
        this.preprocesarCadena(cadena);
        return this.esAceptado;
    }

    public Boolean procesarCadenaConDetalles(String cadena) {
        this.preprocesarCadena(cadena);
        System.out.println("Cadena: " + cadena);
        if (this.esAceptado){
            System.out.println(this.procesamientoAceptacion);
        } else {
            for (String procesamiento : this.procesamientosRechazo) {
                System.out.println(procesamiento);
            }
        }
        return this.esAceptado;
    }

    public int computarTodosLosProcesamientos(String cadena, String nombreArchivo) {
        this.preprocesarCadena(cadena);

        // Imprimir cada uno de los posibles procesamientos
        System.out.println("Procesamientos posibles para la cadena: " + cadena);
        System.out.println(this.getStringListaProcesamientos());

        String aceptacion = this.getStringProcesamientosAceptacion();
        System.out.println("Procesamientos de aceptacion de la cadena: " + cadena);
        if (this.esAceptado){
            System.out.println(aceptacion);
        } else {
            System.out.println("La cadena \"" + cadena + "\" no es aceptada.");
        }

        String rechazo = this.getStringProcesamientosRechazo();
        System.out.println("Procesamientos rechazados de la cadena: " + cadena);
        if (rechazo.isBlank()){
            System.out.println("todos los procesamientos de la cadena \"" + cadena + "\" son de aceptacion.");
        } else {
            System.out.println(rechazo);
        }

        String nombreArchivoAceptadas = nombreArchivo + "AceptadasAF2P.txt";
        String nombreArchivoRechazadas = nombreArchivo + "RechazadasAF2P.txt";

        // Crear los archivos y llenarlo con las listas de estados correspondientes
        try {

            // Crear el archivo de cadenas aceptadas
            File archivoAceptadas = new File(nombreArchivoAceptadas);
            archivoAceptadas.createNewFile();

            // Escribir la lista en el archivo
            FileWriter escritor = new FileWriter(archivoAceptadas);
            escritor.write(aceptacion);
            escritor.close();

            System.out.println("Archivo de cadenas aceptadas creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo de cadenas aceptadas.");

        }
        try {

            // Crear el archivo de cadenas rechazadas
            File archivoRechazadas = new File(nombreArchivoRechazadas);
            archivoRechazadas.createNewFile();

            // Escribir la lista en el archivo
            FileWriter escritor = new FileWriter(archivoRechazadas);
            escritor.write(rechazo);
            escritor.close();

            System.out.println("Archivo de cadenas rechazadas creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo de cadenas rechazadas.");

        }

        // Retornar el numero total de procesamientos posibles
        return this.numProcesamientos;
    }

    public void procesarListaCadenas(Iterable<String> listaCadenas, String nombreArchivo, Boolean imprimirPantalla) {

        StringBuilder sb = new StringBuilder();

        for (String cadena : listaCadenas) {

            this.preprocesarCadena(cadena);

            String fueAceptada = "no";
            String procesamientoMasCorto;
            if (this.esAceptado) {
                fueAceptada = "yes";
                procesamientoMasCorto = this.procesamientoAceptacion;
            } else {
                procesamientoMasCorto = this.procesamientoRechazo;
            }
            sb.append(cadena + "\t"
                    + procesamientoMasCorto + "\t"
                    + this.numProcesamientos + "\t"
                    + this.procesamientosAceptacion.size() + "\t"
                    + this.procesamientosRechazo.size() + "\t"
                    + fueAceptada + "\n");

        }

        String listaProcesada = sb.toString();

        // Imprimir si imprimirPantalla
        if (imprimirPantalla) {
            System.out.println(listaProcesada);
        }

        // Comprobar que el nombre del archivo es valido
        for (int i = 0; i < nombreArchivo.length(); i++) {
            Character simbolo = nombreArchivo.charAt(i);
            if (!Character.isDigit(simbolo) && !Character.isLetter(simbolo) && !(simbolo >= ',' && simbolo <= '.')
                    && !(simbolo == '_')) {
                nombreArchivo = "procesamiento-cadenas-AF2P_"
                        + LocalDateTime.now().getYear() + "-"
                        + LocalDateTime.now().getMonthValue() + "-"
                        + LocalDateTime.now().getDayOfMonth() + "-"
                        + LocalDateTime.now().getHour() + "-"
                        + LocalDateTime.now().getMinute() + "-"
                        + LocalDateTime.now().getSecond() + "_"
                        + Integer.toString(this.hashCode());
                break;
            }
        }
        if (nombreArchivo.isBlank()){
            nombreArchivo = "procesamiento-cadenas-AF2P_"
                        + LocalDateTime.now().getYear() + "-"
                        + LocalDateTime.now().getMonthValue() + "-"
                        + LocalDateTime.now().getDayOfMonth() + "-"
                        + LocalDateTime.now().getHour() + "-"
                        + LocalDateTime.now().getMinute() + "-"
                        + LocalDateTime.now().getSecond() + "_"
                        + Integer.toString(this.hashCode());
        }

        // Crear el archivo y llenarlo con listaProcesada
        nombreArchivo += ".txt";
        try {

            // Crear el archivo de procesamientos de cadenas
            File archivo = new File(nombreArchivo);
            archivo.createNewFile();

            // Escribir la lista en el archivo
            FileWriter escritor = new FileWriter(archivo);
            escritor.write(listaProcesada);
            escritor.close();

            System.out.println("Archivo de procesamientos de las cadenas listadas creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo de procesamientos de las cadenas listadas.");
        }
    }

    public void procesarListaCadenas(String[] listaCadenas, String nombreArchivo, Boolean imprimirPantalla) {
        this.procesarListaCadenas(Arrays.asList(listaCadenas), nombreArchivo, imprimirPantalla);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // formato nfa
        sb.append("#!msm\n");

        // Insertar Q
        sb.append("#states\n");
        for (String estado : this.Q) {
            if (this.estadosInaccesibles.contains(estado)) {
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
            if (this.estadosInaccesibles.contains(estado)) {
                continue;
            }
            sb.append(estado + " \n");
        }

        // Insertar sigma
        sb.append("#tapeAlphabet\n");
        for (Character simbolo : this.sigma.getAlfabeto()) {
            sb.append(simbolo + " \n");
        }

        // Insertar gama
        sb.append("#stackAlphabet\n");
        for (Character simbolo : this.gama.getAlfabeto()) {
            sb.append(simbolo + " \n");
        }

        // Insertar transiciones
        sb.append("#transitions\n");
        for (String fromState : this.delta.keySet()) {

            if (!this.estadosInaccesibles.contains(fromState)){

                for (Character symbol : this.delta.get(fromState).keySet()) {
                    
                    for (Character fromTop1 : this.delta.get(fromState).get(symbol).keySet()) {

                        for (Character fromTop2 : this.delta.get(fromState).get(symbol).get(fromTop1).keySet()) {

                            Boolean masDeUnaImagen = false;
                            sb.append(fromState + ":" + symbol + ":" + fromTop1 + ":" + fromTop2 + ">");

                            for (String[] imagen : this.delta.get(fromState).get(symbol).get(fromTop1).get(fromTop2)) {

                                if (masDeUnaImagen) {
                                    sb.append(";" + imagen[0] + ":" + imagen[1] + ":" + imagen[2]);
                                } else {
                                    sb.append(imagen[0] + ":" + imagen[1] + ":" + imagen[2]);
                                    masDeUnaImagen = true;
                                }
                            }
                            sb.append("\n");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public void exportar(String archivo) throws IOException {
        String[] nombreArchivoSplit = archivo.split("\\.");
        String extension = nombreArchivoSplit[nombreArchivoSplit.length - 1].trim();
        String nombreNuevoArchivo;

        if (extension.equals("msm")) {

            nombreNuevoArchivo = archivo;

        } else {

            nombreNuevoArchivo = nombreArchivoSplit[0] + ".msm";

        }

        // Crear el archivo y llenarlo con toString
        try {

            // Crear el archivo
            File archivoAF2P = new File(nombreNuevoArchivo);
            archivoAF2P.createNewFile();

            // Escribir el AFD en el archivo
            FileWriter escritor = new FileWriter(archivoAF2P);
            escritor.write(this.toString());
            escritor.close();

            System.out.println("Archivo del AF2P creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo.");

        }
    }

}