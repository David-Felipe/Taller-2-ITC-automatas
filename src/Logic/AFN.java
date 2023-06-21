package Logic;

import java.time.*;
import java.util.*;
import java.io.*;

public class AFN {

    // Atributos clase AFN
    private Alfabeto sigma;
    private HashSet<String> Q;
    private String q0;
    private HashSet<String> F;
    private HashSet<String> estadosInaccesibles;
    private HashMap<String, HashMap<Character, HashSet<String>>> delta;

    // Constructores de la clase AFN
    // Constructor de la clase AFN para conjuntos
    public AFN(Alfabeto sigma, HashSet<String> Q, String q0, HashSet<String> F,
            HashMap<String, HashMap<Character, HashSet<String>>> delta) {
        this.sigma = sigma;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;
        this.estadosInaccesibles = new HashSet<>();

        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInaccesibles();
    }

    // Constructor de la clase AFN para archivo .nfa inicial
    public AFN(String rutaArchivo) throws Exception {

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
                if (!this.Q.contains(linea)) {
                    throw new IllegalArgumentException("\"" + linea + "\" no pertenece al conjunto de estados.");
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

        if (!seccionesEsperadas.isEmpty()) {
            throw new IllegalArgumentException("Faltan secciones en el archivo.");
        }
        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInaccesibles();
    }

    // Getterers
    public String getq0() {

        return this.q0;

    }

    public NavigableSet<Character> getSigma() {

        return this.sigma.getAlfabeto();

    }

    public Alfabeto getAlfabeto() {

        return this.sigma;

    }

    // Determinar si un estado es de aceptacion
    public Boolean esDeAceptacion(String estado) {
        return this.F.contains(estado);
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
            if (linea.startsWith("#")) {
                break; // Salir del bucle al encontrar la siguiente sección
            }
            if (!linea.isEmpty()) {
                if (!this.Q.contains(linea)) {
                    throw new IllegalArgumentException("\"" + linea + "\" no pertenece al conjunto de estados.");
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
            for (String estadoSiguiente : estadosSiguientes) {
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

    // Hallar los estados inasequibles del automata y guardarlos en
    // "this.estadosInasequibles"
    private void hallarEstadosInaccesibles() {
        HashSet<String> estados = new HashSet<String>();
        estados.add(this.q0);
        int oldSizeEstados = 0;

        NavigableSet<Character> alfabeto = this.sigma.getAlfabeto();
        while (oldSizeEstados != estados.size()) {
            oldSizeEstados = estados.size();
            Iterator<String> iterator = estados.iterator();
            HashSet<String> copyEstados = new HashSet<String>();
            copyEstados.addAll(estados);
            while (iterator.hasNext()) {
                String estado = iterator.next();
                for (Character simbolo : alfabeto) {
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
        if (this.Q.contains(estado) && this.sigma.contains(simbolo) && this.delta.get(estado) != null) {
            return this.delta.get(estado).get(simbolo);
        } else {
            return null;
        }
    }

    public HashSet<String> delta(HashSet<String> estados, Character simbolo) {
        HashSet<String> imagen = new HashSet<String>();
        for (String estado : estados) {
            if (this.Q.contains(estado) && this.sigma.contains(simbolo) && this.delta.get(estado) != null
                    && this.delta.get(estado).get(simbolo) != null) {
                imagen.addAll(this.delta.get(estado).get(simbolo));
            }
        }
        if (imagen.isEmpty()) {
            return null;
        }
        return imagen;
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
        ProcesamientoCadenaAFN pAFN = new ProcesamientoCadenaAFN(cadena, this);
        String aceptacion = "No Aceptacion";
        if (pAFN.esAceptada()) {
            aceptacion = "Aceptacion";
        }
        System.out.println(pAFN.procesamientoMasCorto() + " " + aceptacion);
        return pAFN.esAceptada();
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
            if (!mostrarInaccesibles && this.estadosInaccesibles.contains(estado)) {
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
            if (!mostrarInaccesibles && this.estadosInaccesibles.contains(estado)) {
                continue;
            }
            sb.append(estado + " \n");
        }

        // Insertar estados inaccesibles si mostrarInaccesibles
        if (addSeccionEstadosInaccesibles) {
            sb.append("#inaccessible\n");
            for (String estado : this.estadosInaccesibles) {
                sb.append(estado + " \n");
            }
        }

        // Insertar transiciones
        sb.append("#transitions\n");
        for (String preEstado : this.delta.keySet()) {
            if (!mostrarInaccesibles && this.estadosInaccesibles.contains(preEstado)) {
                continue;
            }
            for (Character simbolo : this.delta.get(preEstado).keySet()) {
                Boolean masDeUnPostEstado = false;
                sb.append(preEstado + ":" + simbolo + ">");
                for (String postEstado : this.delta.get(preEstado).get(simbolo)) {
                    if (masDeUnPostEstado) {
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

    public void imprimirAFNSimplificado() {
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
            escritor.write(this.preToString(false, false));
            escritor.close();

            System.out.println("Archivo del AFN creado exitosamente");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo");

        }
    }

    public static AFD AFNtoAFD(AFN afn) {
        int pad = afn.q0.length();
        ArrayDeque<String> estadosAlcanzables = new ArrayDeque<String>();

        HashSet<String> q0 = new HashSet<String>();
        q0.add(afn.q0);
        HashSet<String> Q = new HashSet<String>();
        HashSet<String> F = new HashSet<String>();
        Boolean limboEsNecesario = false;
        ArrayDeque<HashSet<String>> q = new ArrayDeque<HashSet<String>>();
        q.add(q0);
        HashMap<String, HashMap<Character, String>> delta = new HashMap<String, HashMap<Character, String>>();
        while (!q.isEmpty()) {

            HashSet<String> estadoNuevo = q.remove();
            String strEstadoNuevo = afn.statesSetToString(estadoNuevo);

            estadosAlcanzables.add(strEstadoNuevo);

            pad = Math.max(pad, strEstadoNuevo.length());
            for (String estado : estadoNuevo) {
                if (afn.F.contains(estado)) {
                    F.add(strEstadoNuevo);
                    break;
                }
            }
            Q.add(strEstadoNuevo);

            for (Character simbolo : afn.sigma.getAlfabeto()) {
                HashSet<String> imagen = afn.delta(estadoNuevo, simbolo);
                if (imagen == null) {
                    if (delta.get(strEstadoNuevo) != null) {
                        delta.get(strEstadoNuevo).put(simbolo, "limbo");
                    } else {
                        HashMap<Character, String> estadosImagen = new HashMap<Character, String>();
                        estadosImagen.put(simbolo, "limbo");
                        delta.put(strEstadoNuevo, estadosImagen);
                    }

                    limboEsNecesario = true;
                } else {
                    String strImagen = afn.statesSetToString(imagen);
                    if (delta.get(strEstadoNuevo) != null) {
                        delta.get(strEstadoNuevo).put(simbolo, strImagen);
                    } else {
                        HashMap<Character, String> estadosImagen = new HashMap<Character, String>();
                        estadosImagen.put(simbolo, strImagen);
                        delta.put(strEstadoNuevo, estadosImagen);
                    }

                    if (!Q.contains(strImagen)) {
                        q.add(imagen);
                    }
                }
            }
        }
        if (limboEsNecesario) {
            Q.add("limbo");
            pad = Math.max(pad, 5);
            for (Character simbolo : afn.sigma.getAlfabeto()) {
                if (delta.get("limbo") != null) {
                    delta.get("limbo").put(simbolo, "limbo");
                } else {
                    HashMap<Character, String> estadosImagen = new HashMap<Character, String>();
                    estadosImagen.put(simbolo, "limbo");
                    delta.put("limbo", estadosImagen);
                }
            }
        }

        pad += 3;
        int sizeAlfabeto = afn.sigma.getAlfabeto().size();
        StringBuilder sb = new StringBuilder();
        sb.append("|" + String.format("%" + pad + "s", "delta |"));
        for (Character simbolo : afn.sigma.getAlfabeto()) {
            sb.append(String.format("%" + pad + "s", simbolo + " |"));
        }
        sb.append("\n");
        sb.append(String.join("", Collections.nCopies(1 + pad * (sizeAlfabeto + 1), "-")) + "\n");
        while (!estadosAlcanzables.isEmpty()) {
            String estado = estadosAlcanzables.remove();
            sb.append("|" + String.format("%" + pad + "s", estado + " |"));
            for (Character simbolo : afn.sigma.getAlfabeto()) {
                sb.append(String.format("%" + pad + "s", delta.get(estado).get(simbolo) + " |"));
            }
            sb.append("\n");
        }
        sb.append("|");
        if (limboEsNecesario) {
            for (int i = 0; i <= sizeAlfabeto; i++) {
                sb.append(String.format("%" + pad + "s", "limbo |"));
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        return new AFD(afn.sigma, Q, afn.statesSetToString(q0), F, delta);
    }

    private String statesSetToString(HashSet<String> estados) {
        StringBuilder sb = new StringBuilder();
        List<String> listaEstados = new ArrayList<String>(estados);
        java.util.Collections.sort(listaEstados);
        sb.append("{");
        Boolean masDeUnEstado = false;
        for (String estado : listaEstados) {
            if (masDeUnEstado) {
                sb.append("," + estado);
            } else {
                sb.append(estado);
                masDeUnEstado = true;
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public HashSet<String[]> darPasoComputacional(String[] configuracion) {
        if (configuracion[1].length() > 0) {
            HashSet<String> estadosImagen = this.delta(configuracion[0], configuracion[1].charAt(0));
            if (estadosImagen != null) {
                HashSet<String[]> postConfiguraciones = new HashSet<String[]>();
                String subcadena = configuracion[1].substring(1);
                for (String estado : estadosImagen) {
                    String[] postConfiguracion = { estado, subcadena };
                    postConfiguraciones.add(postConfiguracion);
                }
                return postConfiguraciones;
            }
        }
        return null;
    }

    public int computarTodosLosProcesamientos(String cadena, String nombreArchivo) {

        ProcesamientoCadenaAFN pAFN = new ProcesamientoCadenaAFN(cadena, this);

        // Imprimir cada uno de los posibles procesamientos
        System.out.println(pAFN.listaProcesamientos());

        // Crear Los archivos
        String nombreArchivoAceptadas = nombreArchivo + "Aceptadas.txt";
        String nombreArchivoRechazadas = nombreArchivo + "Rechazadas.txt";
        String nombreArchivoAbortadas = nombreArchivo + "Abortadas.txt";

        // Crear los archivos y llenarlo con las listas de estados correspondientes
        try {

            // Crear el archivo de cadenas aceptadas
            File archivoAceptadas = new File(nombreArchivoAceptadas);
            archivoAceptadas.createNewFile();

            // Escribir la lista en el archivo
            FileWriter escritor = new FileWriter(archivoAceptadas);
            escritor.write(pAFN.listaProcesamientosAceptacion());
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
            escritor.write(pAFN.listaProcesamientosRechazados());
            escritor.close();

            System.out.println("Archivo de cadenas rechazadas creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo de cadenas rechazadas.");

        }
        try {

            // Crear el archivo de procesamientos abortados
            File archivoAbortadas = new File(nombreArchivoAbortadas);
            archivoAbortadas.createNewFile();

            // Escribir la lista en el archivo
            FileWriter escritor = new FileWriter(archivoAbortadas);
            escritor.write(pAFN.listaProcesamientosAbortados());
            escritor.close();

            System.out.println("Archivo de procesamientos abortados creado exitosamente.");

        } catch (IOException e) {

            System.out.println("Error al crear el archivo de procesamientos abortados.");

        }

        // Retornar el numero total de procesamientos posibles
        return pAFN.numProcesamientos();
    }

    public void procesarListaCadenas(Iterable<String> listaCadenas, String nombreArchivo, Boolean imprimirPantalla) {

        StringBuilder sb = new StringBuilder();

        for (String cadena : listaCadenas) {

            if (cadena.equals("$")){
                cadena = "";
            }
            ProcesamientoCadenaAFN pAFN = new ProcesamientoCadenaAFN(cadena, this);

            String fueAceptada = "no";
            if (pAFN.esAceptada()) {
                fueAceptada = "si";
            }
            sb.append(cadena + "\t"
                    + pAFN.procesamientoMasCorto() + "\t"
                    + pAFN.numProcesamientos() + "\t"
                    + pAFN.numProcesamientosAceptacion() + "\t"
                    + pAFN.numProcesamientosAbortados() + "\t"
                    + pAFN.numProcesamientosRechazados() + "\t"
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
                nombreArchivo = "procesamiento-cadenas_"
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
            nombreArchivo = "procesamiento-cadenas_"
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

    public Boolean procesarCadenaConversion(String cadena) {
        AFD afd = AFNtoAFD(this);
        return afd.procesarCadena(cadena);
    }

    public Boolean procesarCadenaConDetallesConversion(String cadena) {
        AFD afd = AFNtoAFD(this);
        return afd.procesarCadena(cadena, true);
    }

    public void procesarListaCadenasConversion(Iterable<String> listaCadenas, String nombreArchivo,
            Boolean imprimirPantalla) {
        // Comprobar que el nombre del archivo es valido
        for (int i = 0; i < nombreArchivo.length(); i++) {
            Character simbolo = nombreArchivo.charAt(i);
            if (!Character.isDigit(simbolo) && !Character.isLetter(simbolo) && !(simbolo >= ',' && simbolo <= '.')
                    && !(simbolo == '_')) {
                nombreArchivo = "procesamiento-cadenas_"
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
            nombreArchivo = "procesamiento-cadenas_"
                        + LocalDateTime.now().getYear() + "-"
                        + LocalDateTime.now().getMonthValue() + "-"
                        + LocalDateTime.now().getDayOfMonth() + "-"
                        + LocalDateTime.now().getHour() + "-"
                        + LocalDateTime.now().getMinute() + "-"
                        + LocalDateTime.now().getSecond() + "_"
                        + Integer.toString(this.hashCode());
        }

        AFD afd = AFNtoAFD(this);
        afd.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
    }

    public void procesarListaCadenasConversion(Iterable<String> listaCadenas, String nombreArchivo,
            Boolean imprimirPantalla, AFD afd) {
        // Comprobar que el nombre del archivo es valido
        for (int i = 0; i < nombreArchivo.length(); i++) {
            Character simbolo = nombreArchivo.charAt(i);
            if (!Character.isDigit(simbolo) && !Character.isLetter(simbolo) && !(simbolo >= ',' && simbolo <= '.')
                    && !(simbolo == '_')) {
                nombreArchivo = "procesamiento-cadenas_"
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
            nombreArchivo = "procesamiento-cadenas_"
                        + LocalDateTime.now().getYear() + "-"
                        + LocalDateTime.now().getMonthValue() + "-"
                        + LocalDateTime.now().getDayOfMonth() + "-"
                        + LocalDateTime.now().getHour() + "-"
                        + LocalDateTime.now().getMinute() + "-"
                        + LocalDateTime.now().getSecond() + "_"
                        + Integer.toString(this.hashCode());
        }

        afd.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
    }

    public void procesarListaCadenasConversion(String[] listaCadenas, String nombreArchivo, Boolean imprimirPantalla) {
        this.procesarListaCadenasConversion(Arrays.asList(listaCadenas), nombreArchivo, imprimirPantalla);
    }
}