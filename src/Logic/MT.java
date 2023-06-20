package Logic;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.*;
import java.util.*;
import java.io.*;

public class MT {

    // Atributos clase MT
    private Alfabeto sigma;
    private Alfabeto gama;
    private HashSet<String> Q;
    private String q0;
    private HashSet<String> F;
    private HashSet<String> estadosInaccesibles;
    private HashMap<String, HashMap<Character, String[]>> delta;
    
    // Cinta:
    private Stack<Character> prefijo = new Stack<Character>();
    private Stack<Character> sufijo = new Stack<Character>();


    // Registro procesamientos
    private String procesamiento = null;
    private Boolean esAceptado = false;
    private String ultimoEstado = this.q0;


    // Constructores de la clase MT
    // Constructor de la clase MT para conjuntos
    public MT(Alfabeto sigma, Alfabeto gama, HashSet<String> Q, String q0, HashSet<String> F,
            HashMap<String, HashMap<Character, String[]>> delta) {
        this.sigma = sigma;
        this.gama = gama;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;

        // Hallar estados inaccesibles y guardarlos
        this.hallarEstadosInaccesibles();
    }

    // Constructor de la clase MT para archivo .tm inicial
    public MT(String rutaArchivo) throws Exception {

        // Leer archivo .msm y crear MT
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
            if (!linea0.startsWith("#!tm")) {

                // Cerrar el buffer
                br.close();
                throw new IllegalArgumentException("El archivo no corresponde a una MT");

            }
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea);
                }
            }
        }

        List<String> seccionesEsperadas = new ArrayList<>(
                Arrays.asList("#states", "#initial", "#accepting", "#inputAlphabet", "#tapeAlphabet", "#transitions"));

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
            if (linea.startsWith("#inputAlphabet")) {
                Set<Character> sigmaSet = leerAlfabetoSigma(iteratorSigma);
                this.sigma = new Alfabeto(sigmaSet);
                seccionesEsperadas.remove("#inputAlphabet");
            }
        }

        while (iteratorGama.hasNext()) {
            linea = iteratorGama.next();
            if (linea.startsWith("#tapeAlphabet")) {
                Set<Character> gamaSet = leerAlfabetoGama(iteratorGama);
                this.gama = new Alfabeto(gamaSet);
                seccionesEsperadas.remove("#tapeAlphabet");
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

    public String getProcesamiento() {

        return this.procesamiento;
    }

    public NavigableSet<Character> getSigma() {

        return this.sigma.getAlfabeto();
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
            if (linea.contains(":")) {
                throw new IllegalArgumentException("Los estados no deben contener el caracter ':'.");
            }
            if (linea.contains("?")) {
                throw new IllegalArgumentException("Los estados no deben contener el caracter '?'.");
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

    private Set<Character> leerAlfabetoSigma(Iterator<String> iterator) {
        Set<Character> alfSet = new HashSet<>();
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();

            // Salir del bucle al encontrar la siguiente sección
            if (linea.isEmpty() || linea.startsWith("#")) {
                break;
            }

            if (!linea.contains("$") && !linea.contains("#") && !linea.contains("!") && !linea.contains(":") && !linea.contains("?") && !linea.isBlank()) {
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

    private Set<Character> leerAlfabetoGama(Iterator<String> iterator) {
        Set<Character> alfSet = new HashSet<>();
        while (iterator.hasNext()) {
            String linea = iterator.next();
            linea = linea.trim();

            // Salir del bucle al encontrar la siguiente sección
            if (linea.isEmpty() || linea.startsWith("#")) {
                break;
            }

            if (!linea.contains("$") && !linea.contains(":") && !linea.contains("?") && !linea.isBlank()) {
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
            
            // Salir del bucle al encontrar la siguiente sección
            if (linea.isEmpty() || linea.startsWith("#")) {
                break; 
            }

            String[] in_out = linea.split("\\?");
            String[] ins = in_out[0].split(":");

            String fromState = ins[0].trim();
            Character symbol = ins[1].trim().charAt(0);
            
            String[] out = in_out[1].split(":");
            
            String toState = out[0].trim();
            Character replacement = out[1].trim().charAt(0);
            Character movement = out[2].trim().charAt(0);
            this.addTransition(fromState, 
                            symbol,
                            toState,
                            replacement,
                            movement);
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
                        
                        String[] imagen = this.delta.get(estado).get(symbol);

                        // Agregar estado de llegada (elemento[0]) 
                        // al set de estados accesibles
                        copyEstados.add(imagen[0]);
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
    public String[] delta(String estado, Character simbolo) {
        if (this.Q.contains(estado) && 
        (this.gama.contains(simbolo) || simbolo == '!') &&
        this.delta.get(estado) != null) {
            return this.delta.get(estado).get(simbolo);
        } else {
            return null;
        }
    }

    public void addTransition(String fromState, Character symbol, String toState,  Character replacementSymbol, Character movement) {
        if (this.Q.contains(fromState) && 
        this.Q.contains(toState) && 
        (this.sigma.contains(symbol) || symbol == '!') &&
        (this.gama.contains(replacementSymbol) || replacementSymbol == '!') &&
        (movement.equals('-') || movement.equals('<') || movement.equals('>'))) {

            String[] toConfig = {toState, String.valueOf(replacementSymbol), String.valueOf(movement)};
            HashMap<Character, String[]> stateTransition = this.delta.computeIfAbsent(fromState, k -> new HashMap<>());
            stateTransition.computeIfAbsent(symbol, k -> toConfig);
        }
    }

    public void modificarCinta(Character replacement, Character movement) {
        this.sufijo.pop();
        this.sufijo.push(replacement);
        switch (movement){
            case '-':
                break;
            case '>':
                this.prefijo.push(this.sufijo.pop());
                break;
            case '<':
                if (this.prefijo.isEmpty()) {
                    this.sufijo.push('!');
                } else {
                    this.sufijo.push(this.prefijo.pop());
                }
                break;
            default:
                System.out.println("La operacion para pilas \"" + movement + "\" no esta definida.");
        }
    }

    public void preprocesarCadena(String cadena) {
        
        this.esAceptado = true;
        this.ultimoEstado = this.q0;
        String currentState = this.q0;
        StringBuilder sbProcesamiento = new StringBuilder("(" + this.q0 + ")" + cadena);
        this.prefijo = new Stack<Character>();
        this.sufijo = new Stack<Character>();

        for (int i = cadena.length()-1; i >= 0; i--) {
            this.sufijo.push(cadena.charAt(i));
        }

        Character readOnTape;

        while (!this.F.contains(currentState)) {

            if (this.sufijo.empty()) {
                this.sufijo.push('!');
            }
            readOnTape = this.sufijo.peek();
            readOnTape = (readOnTape.equals('$')) ? ('!'):(readOnTape);

            String[] image = this.delta(currentState, readOnTape);
            if (image != null){
                this.modificarCinta(image[1].charAt(0), image[2].charAt(0));
                currentState = image[0];
                this.ultimoEstado = currentState;

                String prefijoStr = prefijo.toString().replaceAll(", |\\[|\\]","").replaceFirst("^!+", "");
                String sufijoStr = sufijo.toString().replaceAll(", |\\[|\\]","").replaceFirst("^!+", "");
                sufijoStr = new StringBuilder(sufijoStr).reverse().toString();

                sbProcesamiento.append("->" + prefijoStr +"(" + currentState + ")" + sufijoStr);
            } else {
                this.esAceptado = false;
                sbProcesamiento.append("\t>>Rejected");
                break;
            }
        }
        if (this.esAceptado) {
            sbProcesamiento.append("\t>>Accepted");
        }

        this.procesamiento = sbProcesamiento.toString();
    }

    public Boolean procesarCadena(String cadena) {
        this.preprocesarCadena(cadena);
        return this.esAceptado;
    }

    public Boolean procesarCadenaConDetalles(String cadena) {
        this.preprocesarCadena(cadena);
        System.out.println("Cadena: " + cadena);
        System.out.println(this.procesamiento);
        return this.esAceptado;
    }

    public String procesarFuncion(String cadena) {
        this.preprocesarCadena(cadena);
        String sufijoStr = sufijo.toString().replaceAll(", |\\[|\\]","").replaceFirst("^!+", "");
        sufijoStr = new StringBuilder(sufijoStr).reverse().toString();
        return "("+this.ultimoEstado+")"+sufijoStr;
    }

    public void procesarListaCadenas(Iterable<String> listaCadenas, String nombreArchivo, Boolean imprimirPantalla) {

        StringBuilder sb = new StringBuilder();

        for (String cadena : listaCadenas) {

            this.preprocesarCadena(cadena);

            String fueAceptada = "no";
            if (this.esAceptado) {
                fueAceptada = "yes";
            }
            String sufijoStr = sufijo.toString().replaceAll(", |\\[|\\]","").replaceFirst("^!+", "");
            sufijoStr = new StringBuilder(sufijoStr).reverse().toString();

            sb.append(cadena + "\t"
                    + "("+this.ultimoEstado+")"+sufijoStr + "\t"
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
                nombreArchivo = "procesamiento-cadenas-MT_"
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
            nombreArchivo = "procesamiento-cadenas-MT_"
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

        // formato tm
        sb.append("#!tm\n");

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
        sb.append("#inputAlphabet\n");
        for (Character simbolo : this.sigma.getAlfabeto()) {
            sb.append(simbolo + " \n");
        }

        // Insertar gama
        sb.append("#tapeAlphabet\n");
        for (Character simbolo : this.gama.getAlfabeto()) {
            sb.append(simbolo + " \n");
        }

        // Insertar transiciones
        sb.append("#transitions\n");
        for (String fromState : this.delta.keySet()) {

            if (!this.estadosInaccesibles.contains(fromState)){

                for (Character symbol : this.delta.get(fromState).keySet()) {
                    
                    String[] imagen = this.delta.get(fromState).get(symbol);
                    sb.append(fromState + ":" + symbol + "?" + imagen[0] + ":" + imagen[1] + ":" + imagen[2]);
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    public void exportar(String archivo) throws IOException {
        String[] nombreArchivoSplit = archivo.split("\\.");
        String extension = nombreArchivoSplit[nombreArchivoSplit.length - 1].trim();
        String nombreNuevoArchivo;

        if (extension.equals("tm")) {

            nombreNuevoArchivo = archivo;

        } else {

            nombreNuevoArchivo = nombreArchivoSplit[0] + ".tm";

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

            System.out.println("Archivo de la MT creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo.");

        }
    }

}