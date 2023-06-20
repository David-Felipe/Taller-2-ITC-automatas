package Logic;

// Paquetes propios de java
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// Paquetes creados por nosotros
import Logic.AFD;
import Logic.AFPD;
import Logic.AFPN.Transicion;

public class ClasePrueba {

    static Integer segundosEsperaLector = 3;
    static Boolean devMode = true;
    static Integer maxCharLenName = 15;

    // Automatas guardados
    static HashMap<String, AF2P> automatasAF2PActuales = new HashMap<>();
    static HashMap<String, AFN> automatasAFNActuales = new HashMap<>();
    static HashMap<String, MT> MTActuales = new HashMap<>();

    // Funcion de control, menu inicial
    public static void main() {

        // Setup para el main()
        Boolean probando = true;
        Scanner input = new Scanner(System.in, "utf-8");

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");
        // Espacio al inicio para no saturar
        System.out.println();
        System.out.println();
        // Mensaje de bienvenida
        System.out.println("Bienvenido al programa de prueba de automatas");
        // Esperar a que lea el mensaje
        try {

            // Espera que lea el mensaje
            TimeUnit.SECONDS.sleep(segundosEsperaLector);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        // Seleccion en menu inicial que se repite hasta salir
        while (probando) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Menu inicial");
            System.out.println();

            // Mostrar las opciones iniciales
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Probar automatas");
            System.out.println("2. Probar conversiones");
            System.out.println("0. Salir");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opción deseada
            switch (opcion) {

                case 1:
                    probarAutomatas(input);
                    break;

                case 2:
                    probarConversiones(input);
                    break;

                case 0:
                    probando = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

        // Cerrar el input
        input.close();

        // Espacio al final del uso para no saturar
        System.out.println();
        System.out.println();

    }

    // Menu para elegir que automata probar
    private static void probarAutomatas(Scanner input) {

        // Setup menu seleccion de prueba de automata
        Boolean seleccionandoAutomata = true;

        // Seleccion de automata que se repite hasta volver
        while (seleccionandoAutomata) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Selección de automata");
            System.out.println();

            // Mostrar las opciones de selección de automata
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Probar AFD");
            System.out.println("2. Probar AFN");
            System.out.println("3. Probar AFN Lambda");
            System.out.println("4. Probar AFPD");
            System.out.println("5. Probar AFPN");
            System.out.println("6. Probar AF2P");
            System.out.println("7. Probar MT");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    probarAFD(input);
                    break;

                case 2:
                    probarAFN(input);
                    break;

                case 3:
                    try {
                        probarAFNLambda(input);
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error inesperado: " + e.getMessage());
                    }
                    break;
                case 4:
                    probarAFPD(input);
                    break;

                case 5:
                    try {
                        probarAFPN(input);
                    } catch (Exception e) {
                        System.out.println("Error inesperado: " + e.getMessage());
                    }
                    break;
                case 6:
                    probarAF2P(input);
                    break;

                case 7:
                    probarMT(input);
                    break;

                case 0:
                    seleccionandoAutomata = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // Menu para elegir que conversion probar
    private static void probarConversiones(Scanner input) {

        // Setup menu seleccion de prueba de conversiones
        Boolean seleccionandoConversion = true;

        // Seleccion de conversion que se repite hasta volver
        while (seleccionandoConversion) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Selección tipo de conversion");
            System.out.println();

            // Mostrar las opciones de selección de conversion
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. AFN a AFD");
            System.out.println("2. Validar AFN a AFD con lista de automatas");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    probarAFNtoAFD(input);
                    break;
                case 2:
                    validarAFNtoAFD(input);
                    break;
                case 0:
                    seleccionandoConversion = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }
                    break;
            }
        }
    }

    // ** Probar automatas
    // * Probar AFD
    private static void probarAFD(Scanner input) {

        // Setup menu probar AFD
        Boolean probandoAFD = true;
        TreeSet<AFD> automatasActuales = new TreeSet<>();

        // Menu que muestra las opciones para afd
        while (probandoAFD) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de AFD");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFD actuales son:");
                System.out.println();

                // Imprimir los AFD contenidos en automatasActuales
                for (AFD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones para probar AFD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Editar AFD's ");
            System.out.println("2. Procesar cadenas");
            System.out.println("3. Producto cartesiano");
            System.out.println("4. Exportar AFD");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    editarAFD(automatasActuales, input);
                    break;

                case 2:
                    procesarCadenasAFD(automatasActuales, input);
                    break;

                case 3:
                    productoCartesiano(automatasActuales, input);
                    break;

                case 4:
                    exportarAFD(automatasActuales, input);
                    break;

                case 0:
                    probandoAFD = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // * Editar AFD
    private static void editarAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Setup menu editar AFD
        Boolean editandoAFD = true;

        // Menu que muestra las opciones para editar AFD
        while (editandoAFD) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Edición de AFD's");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFD actuales son:");
                System.out.println();

                // Imprimir los AFD contenidos en automatasActuales
                for (AFD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones para editar AFD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear AFD ");
            System.out.println("2. Eliminar AFD");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    crearAFD(automatasActuales, input);
                    break;

                case 2:
                    eliminarAFD(automatasActuales, input);
                    break;

                case 0:
                    editandoAFD = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    private static void crearAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Setup menu crear AFD
        Boolean creandoAFD = true;

        // Menu que muestra las opciones para crear AFD
        while (creandoAFD) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Creación de AFD's");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFD actuales son:");
                System.out.println();

                // Imprimir los AFD contenidos en automatasActuales
                for (AFD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones para crear AFD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear AFD desde archivo ");
            System.out.println("2. Crear AFD por complemento de existente");
            System.out.println("3. Crear AFD por simplificación de existente");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    crearAFDDesdeArchivo(automatasActuales, input);
                    break;

                case 2:
                    crearAFDPorComplemento(automatasActuales, input);
                    break;

                case 3:
                    crearAFDPorSimplificacion(automatasActuales, input);
                    break;

                case 0:
                    creandoAFD = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    private static void crearAFDDesdeArchivo(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de AFD desde archivo");
        System.out.println();

        // Pedir informacion necesaria para crear el AFD
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene el AFD");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del AFD
        Boolean ingresandoNombre = true;
        String nombreAFD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para el AFD");
            nombreAFD = input.next().trim();
            System.out.println();

            // Verificar condición
            if (nombreAFD.length() <= maxCharLenName && nombreAFD.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            }

        }

        // Crear el AFD
        try {

            AFD nuevoAFD = new AFD(rutaArchivo);
            nuevoAFD.setNombreAFD(nombreAFD);
            automatasActuales.add(nuevoAFD);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }

        }

    }

    private static void crearAFDPorComplemento(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de AFD por complemento de existente");
        System.out.println();

        // Mostrar automatas creados
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        Boolean ingresandoAFD = true;
        Integer numAFD = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFD a partir del cual quiere crear");
            System.out.println("el AFD por complemento");
            numAFD = input.nextInt();
            System.out.println();

            if (numAFD <= 0 || numAFD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD--;

            }

        }

        // Pedir nombre del AFD
        Boolean ingresandoNombre = true;
        String nombreAFD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para el AFD");
            nombreAFD = input.next();
            System.out.println();

            // Verificar condición
            if (nombreAFD.length() <= maxCharLenName && nombreAFD.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            }

        }

        // Crear el AFD
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdBase = listaAutomatas.get(numAFD);
        AFD afdBuscado = afdBase.hallarComplemento();
        afdBuscado.setNombreAFD(nombreAFD);
        automatasActuales.add(afdBuscado);

    }

    static void crearAFDPorSimplificacion(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de AFD por simplificacion de existente");
        System.out.println();

        // Mostrar automatas creados
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        Boolean ingresandoAFD = true;
        Integer numAFD = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFD a partir del cual quiere crear");
            System.out.println("el AFD por simplificación");
            numAFD = input.nextInt();
            System.out.println();

            if (numAFD <= 0 || numAFD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD--;

            }

        }

        // Pedir nombre del AFD
        Boolean ingresandoNombre = true;
        String nombreAFD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de" + Integer.toString(maxCharLenName)
                            + "caracteres sin espacios para el AFD");
            nombreAFD = input.next();
            System.out.println();

            // Verificar condición
            if (nombreAFD.length() <= maxCharLenName && nombreAFD.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            }

        }

        // Crear el AFD
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdBase = listaAutomatas.get(numAFD);
        AFD afdBuscado = afdBase.simplificarAFD();
        afdBuscado.setNombreAFD(nombreAFD);
        automatasActuales.add(afdBuscado);

    }

    private static void calcularYMostrarLambdaClausura(String nombreArchivo) throws IOException {
        // Crear una instancia de AFNLambda
        AFNLambda automata = null;

        try {
            // Crear una instancia de AFNLambda
            automata = new AFNLambda(nombreArchivo);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo.");
            // Lanzar una excepción para salir del método
            throw e;
        }

        // Verificar si el automata se creó correctamente
        if (automata != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Ingrese el estado para calcular la λ-Clausura:");
            String estado = reader.readLine();
            List<String> lambdaClausura = new ArrayList<>(automata.calcularLambdaClausura(estado));
            System.out.println("λ-Clausura de " + estado + ": " + lambdaClausura);
        }
    }

    private static void determinarYMostrarEstadosInaccesibles(AFNLambda automata) {
        List<String> estadosInaccesibles = automata.hallarEstadosInaccesibles();
        System.out.println("Estados inaccesibles: " + estadosInaccesibles);
    }

    private static void imprimirAFNLSimplificado(AFNLambda automata) {
        automata.imprimirAFNLSimplificado();
    }

    private static void exportarAutomata(AFNLambda automata) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el nombre del archivo de exportación:");
        String nombreArchivo = reader.readLine();
        automata.exportar(nombreArchivo);
        System.out.println("El autómata se ha exportado correctamente.");
    }

    private static void procesarCadena(AFNLambda automata) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese la cadena a procesar:");
        String cadena = reader.readLine();
        boolean esAceptada = automata.procesarCadena(cadena);
        if (esAceptada) {
            System.out.println("La cadena es aceptada por el autómata.");
        } else {
            System.out.println("La cadena es rechazada por el autómata.");
        }
    }

    private static void procesarCadenaConDetalles(AFNLambda automata) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese la cadena a procesar:");
        String cadena = reader.readLine();
        automata.procesarCadenaConDetalles(cadena);
    }

    private static void computarTodosLosProcesamientos(AFNLambda automata) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese la cadena a procesar:");
        String cadena = reader.readLine();
        System.out.println("Ingrese el nombre del archivo para guardar los resultados:");
        String nombreArchivo = reader.readLine();
        int numProcesamientos = automata.computarTodosLosProcesamientos(cadena, nombreArchivo);
        System.out.println("Se realizaron " + numProcesamientos + " procesamientos.");
    }

    private static void procesarListaCadenasConDetalles(AFNLambda automata) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el nombre del archivo que contiene la lista de cadenas:");
        String nombreArchivo = reader.readLine();
        System.out.println("Ingrese el nombre del archivo para guardar los resultados:");
        String nombreArchivoResultados = reader.readLine();
        System.out.println("Imprimir resultados en pantalla? (s/n):");
        String imprimirPantallaStr = reader.readLine();
        boolean imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("s");
        List<String> listaCadenas = leerListaCadenasDesdeArchivo(nombreArchivo);
        automata.procesarListaCadenas(listaCadenas, nombreArchivoResultados, imprimirPantalla);
    }

    private static List<String> leerListaCadenasDesdeArchivo(String nombreArchivo) throws IOException {
        List<String> listaCadenas = new ArrayList<>();
        FileReader fileReader = new FileReader(nombreArchivo);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String linea;
        while ((linea = bufferedReader.readLine()) != null) {
            listaCadenas.add(linea);
        }
        bufferedReader.close();
        return listaCadenas;
    }

    static void eliminarAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            System.out.println();
            System.out.println("No hay ningún automata creado, por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Eliminación de AFD");
        System.out.println();

        // Mostrar automatas creados
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        Boolean ingresandoAFD = true;
        Integer numAFD = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFD que desea eliminar");
            numAFD = input.nextInt();
            System.out.println();

            if (numAFD <= 0 || numAFD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD--;

            }

        }

        // Eliminar el AFD
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdEliminado = listaAutomatas.get(numAFD);
        automatasActuales.remove(afdEliminado);

    }

    // * Procesar cadenas
    private static void procesarCadenasAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Setup menu seleccion de tipo de procesamiento
        Boolean procesandoCadenas = true;

        // Seleccion de procesamiento que se repite hasta volver
        while (procesandoCadenas) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Selección tipo de procesamiento");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFD actuales son:");
                System.out.println();

                // Imprimir los AFD contenidos en automatasActuales
                for (AFD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones de selección de automata
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar cadena");
            System.out.println("2. Procesar lista de cadenas desde archivo");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    procesarCadenaAFD(automatasActuales, input);
                    break;

                case 2:
                    procesarListaCadenasAFD(automatasActuales, input);
                    break;

                case 0:
                    procesandoCadenas = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    private static void procesarCadenaAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de cadena");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFD a usar
        Boolean ingresandoAFD = true;
        Integer numAFD = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFD a partir del cual quiere procesar");
            System.out.println("la cadena");
            numAFD = input.nextInt();
            System.out.println();

            if (numAFD <= 0 || numAFD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD--;

            }

        }

        // Ingresar la cadena a procesar
        // Pedir info necesaria
        System.out.println("Por favor ingrese la cadena a procesar");
        String cadenaEntrada = input.next();
        System.out.println();

        // Preguntar si desea detalles
        System.out.println("Desea imprimir los detalles del procesamiento?");
        System.out.println("por favor digite 1 para sí y 0 para no:");
        Integer detalles = input.nextInt();
        System.out.println();

        // Procesar la cadena
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdBase = listaAutomatas.get(numAFD);
        Boolean resultado = afdBase.procesarCadena(cadenaEntrada, detalles == 1);
        System.out.println();

        // Mostrar el resultado
        if (resultado) {

            System.out.println("La cadena fue aceptada por el AFD");
            System.out.println();

        } else {

            System.out.println("La cadena no fue aceptada por el AFD");
            System.out.println();

        }

        // Esperar hasta que presione enter
        System.out.println("Presione enter para continuar");
        try {
            System.in.read(); // Waits for user to press Enter
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void procesarListaCadenasAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de lista de cadenas desde archivo");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFD a usar
        Boolean ingresandoAFD = true;
        Integer numAFD = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFD a partir del cual quiere procesar");
            System.out.println("la lista de cadenas");
            numAFD = input.nextInt();
            System.out.println();

            if (numAFD <= 0 || numAFD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD--;

            }

        }

        // Ingresar la ruta al archivo
        // Pedir info necesaria
        System.out.println("Por favor ingrese la ruta al archivo que contiene la lista de cadenas");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Obtener cadenas desde el archivo
        ArrayList<String> listaCadenas = new ArrayList<>();
        try {

            // Abrir el archivo
            File archivo = new File(rutaArchivo);
            Scanner lector = new Scanner(archivo);

            // Leer todas las lineas y agregarlas
            while (lector.hasNextLine()) {

                String lineaActual = lector.nextLine().trim();
                listaCadenas.add(lineaActual);

            }

            // Cerrar el lector
            lector.close();

        } catch (FileNotFoundException e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException err) {
                err.printStackTrace();
            }

            return;

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Preguntar si desea detalles
        System.out.println("Desea imprimir los detalles del procesamiento?");
        System.out.println("por favor digite 1 para sí y 0 para no:");
        Integer detalles = input.nextInt();
        System.out.println();

        // Procesar las cadenas
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdBase = listaAutomatas.get(numAFD);
        afdBase.procesarListaCadenas(listaCadenas, nombreArchivoSalida, detalles == 1);
        System.out.println();

        // Si pidió detalles, esperar a que los lea
        if (detalles == 1) {

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // * Producto cartesiano
    private static void productoCartesiano(TreeSet<AFD> automatasActuales, Scanner input) {

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Setup menu seleccion de operacion
        Boolean seleccionandoOperacion = true;

        // Seleccion de operacion que se repite hasta volver
        while (seleccionandoOperacion) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Selección de operación Producto Cartesiano");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFD actuales son:");
                System.out.println();

                // Imprimir los AFD contenidos en automatasActuales
                for (AFD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones de seleccion de operacion
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Union");
            System.out.println("2. Interseccion");
            System.out.println("3. Diferencia");
            System.out.println("4. Diferencia simetrica");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    hallarProductoCartesianoAFD(automatasActuales, input, "union");
                    break;

                case 2:
                    hallarProductoCartesianoAFD(automatasActuales, input, "interseccion");
                    break;

                case 3:
                    hallarProductoCartesianoAFD(automatasActuales, input, "diferencia");
                    break;

                case 4:
                    hallarProductoCartesianoAFD(automatasActuales, input, "diferencia simetrica");
                    break;

                case 0:
                    seleccionandoOperacion = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    private static void hallarProductoCartesianoAFD(TreeSet<AFD> automatasActuales, Scanner input, String operacion) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Producto cartesiano:" + operacion);
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFD a usar
        Boolean ingresandoAFD = true;
        Integer numAFD1 = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del primer AFD a partir del cual quiere hacer");
            System.out.println("el producto cartesiano");
            numAFD1 = input.nextInt();
            System.out.println();

            if (numAFD1 <= 0 || numAFD1 > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD1--;

            }

        }

        // Ingresar el AFD a usar
        ingresandoAFD = true;
        Integer numAFD2 = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del segundo AFD a partir del cual quiere hacer");
            System.out.println("el producto cartesiano");
            numAFD2 = input.nextInt();
            System.out.println();

            if (numAFD2 <= 0 || numAFD2 > automatasActuales.size() || numAFD2 == numAFD1 + 1) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD2--;

            }

        }

        // Ingresar nombre del AFD resultante
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del AFD resultante");
        String nombreAFD = input.next();
        System.out.println();

        // Crear el AFD
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afd1 = listaAutomatas.get(numAFD1);
        AFD afd2 = listaAutomatas.get(numAFD2);
        AFD resultado = afd1.hallarProductoCartesianoCon(afd2, operacion);
        resultado.setNombreAFD(nombreAFD);
        automatasActuales.add(resultado);

    }

    // * Exportar AFD
    private static void exportarAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Exportar AFD");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFD actuales son:");
            System.out.println();

            // Imprimir los AFD contenidos en automatasActuales
            for (AFD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFD a exportar
        Boolean ingresandoAFD = true;
        Integer numAFD = 0;

        while (ingresandoAFD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFD a exportar");
            numAFD = input.nextInt();
            System.out.println();

            if (numAFD <= 0 || numAFD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFD = false;
                numAFD--;

            }

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Exportar el AFD
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdExportado = listaAutomatas.get(numAFD);
        afdExportado.exportar(nombreArchivoSalida);

    }


    // ** Probar AFN
    private static void probarAFN(Scanner input) {

        // Setup menu probar AFN
        Boolean probandoAFN = true;

        // Menu que muestra las opciones para afn
        while (probandoAFN) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de AFN");
            System.out.println();

            if (automatasAFNActuales.size() != 0) {

                // Listar AFNS
                imprimirListaNombresAFN();

            }

            // Mostrar las opciones para probar AFN
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Editar AFN ");
            System.out.println("2. Procesar cadena");
            System.out.println("3. Procesar lista de cadenas");
            System.out.println("4. Exportar AFN");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    editarAFN(input);
                    break;

                case 2:
                    procesarCadenasAFN(input);
                    break;

                case 3:
                    procesarListaDeCadenasAFN(input);
                    break;

                case 4:
                    exportarAFN(input);
                    break;

                case 0:
                    probandoAFN = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // * Editar AFN
    private static void editarAFN(Scanner input) {

        // Setup menu editar AFN
        Boolean editandoAFN = true;

        // Menu que muestra las opciones para editar AFN
        while (editandoAFN) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Edición de AFNs");
            System.out.println();

            if (automatasAFNActuales.size() != 0) {

                // Listar AFNs
                imprimirListaNombresAFN();

            }

            // Mostrar las opciones para editar AFN
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear AFN ");
            System.out.println("2. Eliminar AFN");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    crearAFN(input);
                    break;

                case 2:
                    eliminarAFN(input);
                    break;

                case 0:
                    editandoAFN = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // Crear AFN
    private static void crearAFN(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de AFN desde archivo");
        System.out.println();

        // Pedir informacion necesaria para crear el AFN
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene el AFN");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del AFN
        Boolean ingresandoNombre = true;
        String nombreAFN = "";
        while (ingresandoNombre) {
            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para el AFD");
            nombreAFN = input.next().trim();
            System.out.println();

            // Verificar condición
            if (nombreAFN.length() <= maxCharLenName && nombreAFN.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido.");
                System.out.println("Por favor intentelo de nuevo.");
                System.out.println();

            }

        }

        // Crear el AFN
        try {

            AFN nuevoAFN = new AFN(rutaArchivo);
            automatasAFNActuales.put(nombreAFN, nuevoAFN);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo.");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    // Eliminar AFN
    static void eliminarAFN(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasAFNActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AFN.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Eliminación de AFN");
        System.out.println();

        // Mostrar automatas creados
        if (automatasAFNActuales.size() != 0) {

            // Listar AFNS
            imprimirListaNombresAFN();

        }

        Boolean ingresandoAFN = true;
        String nombreAFN = "";

        while (ingresandoAFN) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AFN que desea eliminar");
            nombreAFN = input.next();
            System.out.println();

            if (!automatasAFNActuales.keySet().contains(nombreAFN)) {

                System.out.println("No hay ningun AFN con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAFN = false;
                automatasAFNActuales.remove(nombreAFN);

            }

        }

    }

    // * Exportar AFN
    private static void exportarAFN(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasAFNActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AFN.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Exportar AFN");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasAFNActuales.size() != 0) {

            // Listar AFNS
            imprimirListaNombresAFN();

        }

        // Ingresar el AFN a exportar
        Boolean ingresandoAFN = true;
        String nombreAFN = "";

        while (ingresandoAFN) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AFN a exportar");
            nombreAFN = input.next();
            System.out.println();

            if (!automatasAFNActuales.keySet().contains(nombreAFN)) {

                System.out.println("No hay ningun AFN con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAFN = false;

            }

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Exportar el AFN
        try {

            automatasAFNActuales.get(nombreAFN).exportar(nombreArchivoSalida);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // * Procesar cadenas AFN
    private static void procesarCadenasAFN(Scanner input) {

        // Verificar que sí existan
        if (automatasAFNActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AFN.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de una cadena por AFN");
        System.out.println();

        if (automatasAFNActuales.size() != 0) {

            // Listar AFNS
            imprimirListaNombresAFN();

        }

        // Ingresar el AFN con el que se desea trabajar
        Boolean ingresandoAFN = true;
        String nombreAFN = "";

        while (ingresandoAFN) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AFN que desea utilizar");
            nombreAFN = input.next();
            System.out.println();

            if (!automatasAFNActuales.keySet().contains(nombreAFN)) {

                System.out.println("No hay ningun AFN con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAFN = false;

            }
        }
        AFN afn = automatasAFNActuales.get(nombreAFN);

        procesarCadenasAFNConEsteAFN(afn, input);

    }

    private static void procesarCadenasAFNConEsteAFN(AFN afn, Scanner input) {

        Boolean procesandoVariasCadenas = true;
        while (procesandoVariasCadenas){

            // Ingresar la cadena a procesar
            Boolean ingresandoCadena = true;
            String cadena = "";

            while (ingresandoCadena) {

                // Imprimir alfabeto
                StringBuilder alfabeto = new StringBuilder();
                alfabeto.append("{");
                Boolean masDeUnCaracter = false;
                for (Character simbolo : afn.getSigma()) {
                    if (masDeUnCaracter) {
                        alfabeto.append("," + simbolo);
                    } else {
                        alfabeto.append(simbolo);
                        masDeUnCaracter = true;
                    }
                }
                alfabeto.append("}");
                System.out.println("Alfabeto: " + alfabeto.toString());
                System.out.println("lambda: $");
                System.out.println();

                // Pedir info necesaria
                System.out.println("Ingrese la cadena a procesar");
                cadena = input.next().replaceAll("\\s+","");
                System.out.println();
                Boolean cadenaValida = true;
                for (int i = 0; i < cadena.length(); i++) {
                    if (!afn.getSigma().contains(cadena.charAt(i))) {
                        cadenaValida = false;
                        break;
                    }
                }
                if (!cadenaValida && !cadena.equals("$")) {
                    System.out.println("La cadena ingresada no es valida.");
                    System.out.println("Por favor intentelo nuevamente.");
                    System.out.println();

                } else {
                    if (cadena.equals("$")) {
                        cadena = "";
                    }
                    ingresandoCadena = false;

                }
            }

            ProcesamientoCadenaAFN pAFN = new ProcesamientoCadenaAFN(cadena, afn);

            // Setup menu seleccion de tipo de procesamiento
            Boolean procesandoCadenas = true;

            // Seleccion de procesamiento que se repite hasta volver
            while (procesandoCadenas) {

                // Limpiar consola para que se vea mas fancy
                System.out.print("\033c");

                // Espacio al inicio para no saturar e indicar menu
                System.out.println();
                System.out.println("Selección tipo de procesamiento");
                System.out.println();

                // Mostrar las opciones de selección de automata
                System.out.println("Presionando el entero seguido de la tecla enter, por favor");
                System.out.println("seleccione una opción:");
                System.out.println();
                System.out.println("1. Procesar cadena: \"" + cadena + "\"");
                System.out.println("2. Consultar todos los procesamiento posibles de la cadena: \"" + cadena + "\"");
                System.out.println("3. Consultar los procesamientos de aceptacion de la cadena: \"" + cadena + "\"");
                System.out.println("4. Consultar los procesamientos de rechazo de la cadena: \"" + cadena + "\"");
                System.out.println("5. Consultar los procesamientos abortados de la cadena: \"" + cadena + "\"");
                System.out.println("6. Procesar una cadena distinta.");
                System.out.println("0. Volver");
                System.out.println();
                System.out.print("Ingrese su opción: ");
                Integer opcion = input.nextInt();
                System.out.println();

                // Redirigir a la opcion deseada
                switch (opcion) {

                    case 1:
                        if (pAFN.esAceptada()) {
                            System.out.println("La cadena: \"" + cadena + "\" es aceptada.");
                            System.out.println("Un procesamiento aceptado es: ");
                        } else {
                            System.out.println("La cadena: \"" + cadena + "\" es rechazada.");
                            System.out.println("Uno de los procesamientos mas cortos es: ");
                        }
                        System.out.println(pAFN.procesamientoMasCorto());
                        break;

                    case 2:
                        System.out.println(pAFN.listaProcesamientos());
                        break;

                    case 3:
                        System.out.println(pAFN.listaProcesamientosAceptacion());
                        break;

                    case 4:
                        System.out.println(pAFN.listaProcesamientosRechazados());
                        break;

                    case 5:
                        System.out.println(pAFN.listaProcesamientosAbortados());
                        break;

                    case 6:
                        procesandoCadenas = false;
                        break;

                    case 0:
                        procesandoCadenas = false;
                        procesandoVariasCadenas = false;
                        break;

                    default:

                        System.out.println("Opción inválida");

                        try {

                            // Espera que lea el mensaje
                            TimeUnit.SECONDS.sleep(segundosEsperaLector);

                        } catch (InterruptedException e) {

                            e.printStackTrace();

                        }

                        break;

                }
                // Esperar hasta que se presione enter
                if (procesandoCadenas) {
                    System.out.println("Presione enter para continuar");
                    try {
                        System.in.read();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }
            }
        }
    }


    // * Procesar lista de cadenas AFN
    private static void procesarListaDeCadenasAFN(Scanner input) {

        // Verificar que sí existan
        if (automatasAFNActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AFN.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de cadenas por AFN");
        System.out.println();

        if (automatasAFNActuales.size() != 0) {

            // Listar AFNS
            imprimirListaNombresAFN();

        }

        // Ingresar el AFN con el que se desea trabajar
        Boolean ingresandoAFN = true;
        String nombreAFN = "";

        while (ingresandoAFN) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AFN que desea utilizar");
            nombreAFN = input.next();
            System.out.println();

            if (!automatasAFNActuales.keySet().contains(nombreAFN)) {

                System.out.println("No hay ningun AFN con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAFN = false;

            }
        }
        AFN afn = automatasAFNActuales.get(nombreAFN);

        procesarListaDeCadenasAFNConEsteAFN(afn, input, nombreAFN);

    }
    
    private static void procesarListaDeCadenasAFNConEsteAFN(AFN afn, Scanner input, String nombreAFN) {

        // Setup menu probar AFN
        Boolean escogiendoLista = true;

        // Menu que muestra las opciones para afn
        while (escogiendoLista) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Procesamiento de listas de cadenas AFN");
            System.out.println();
            System.out.println("Automata: " + nombreAFN);

            // Mostrar las opciones para probar AFN
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar lista de cadenas aleatorias");
            System.out.println("2. Importar y procesar lista de cadenas");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            Iterable<String> listaCadenas;
            String nombreArchivo;
            String imprimirPantallaStr;
            boolean imprimirPantalla;

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir cantidad de cadenas
                    int numCadenas = 0;
                    while (numCadenas < 1) {
                        System.out.println("Por favor ingrese el numero de cadenas a generar");
                        while (!input.hasNext()) {
                            // Esperar a que haya un input y luego sí avanzas
                        }
                        numCadenas = input.nextInt();
                    }
                    listaCadenas = afn.getAlfabeto().generarCadenas(numCadenas);

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    //procesar lista
                    afn.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
                    // Esperar hasta que presione enter
                    System.out.println("Presione enter para continuar");
                    try {
                        System.in.read();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    break;

                case 2:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir ruta al archivo
                    System.out.println("Por favor ingrese la ruta al archivo que contiene la lista");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    String rutaArchivo = input.next();
                    System.out.println();

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    // Crear lista
                    try {
                        listaCadenas = leerListaCadenasDesdeArchivo(rutaArchivo);

                        //procesar lista
                        afn.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
                        // Esperar hasta que presione enter
                        System.out.println("Presione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    } catch (Exception e) {
                        if (devMode) {
                            e.printStackTrace();
                        }
                        System.out.println("\nOcurrió un error al buscar el archivo.\n");
                        escogiendoLista = false;

                        // Esperar hasta que presione enter
                        System.out.println("Presione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                    break;

                case 0:
                    escogiendoLista = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }
    }

    private static void imprimirListaNombresAFN() {
        Integer numActual = 1;

        System.out.println("Sus AFN actuales son:");
        System.out.println();

        // Imprimir los AFN contenidos en automatasActuales
        for (String strActual : automatasAFNActuales.keySet()) {

            System.out.println(Integer.toString(numActual) + ". " + strActual + "\n");
            numActual++;

        }

        System.out.println();
    }

    // ** Probar AF2P
    private static void probarAF2P(Scanner input) {

        // Setup menu probar AF2P
        Boolean probandoAF2P = true;

        // Menu que muestra las opciones para af2p
        while (probandoAF2P) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de AF2P");
            System.out.println();

            if (automatasAF2PActuales.size() != 0) {

                // Listar AF2PS
                imprimirListaNombresAF2P();

            }

            // Mostrar las opciones para probar AF2P
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Editar AF2P ");
            System.out.println("2. Procesar cadenas");
            System.out.println("3. Procesar lista de cadenas");
            System.out.println("4. Exportar AF2P");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    editarAF2P(input);
                    break;

                case 2:
                    procesarCadenasAF2P(input);
                    break;

                case 3:
                    procesarListaDeCadenasAF2P(input);
                    break;

                case 4:
                    exportarAF2P(input);
                    break;

                case 0:
                    probandoAF2P = false;
                    break;

                default:

                    System.out.println("Opción inválida.");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // * Editar AF2P
    private static void editarAF2P(Scanner input) {

        // Setup menu editar AF2P
        Boolean editandoAF2P = true;

        // Menu que muestra las opciones para editar AF2P
        while (editandoAF2P) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Edición de AF2Ps");
            System.out.println();

            if (automatasAF2PActuales.size() != 0) {

                // Listar AF2PS
                imprimirListaNombresAF2P();

            }

            // Mostrar las opciones para editar AF2P
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear AF2P ");
            System.out.println("2. Eliminar AF2P");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    crearAF2P(input);
                    break;

                case 2:
                    eliminarAF2P(input);
                    break;

                case 0:
                    editandoAF2P = false;
                    break;

                default:

                    System.out.println("Opción inválida.");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // Crear AF2P
    private static void crearAF2P(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de AF2P desde archivo");
        System.out.println();

        // Pedir informacion necesaria para crear el AF2P
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene el AF2P");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del AF2P
        Boolean ingresandoNombre = true;
        String nombreAF2P = "";
        while (ingresandoNombre) {
            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para el AF2P");
            nombreAF2P = input.next().trim();
            System.out.println();

            // Verificar condición
            if (nombreAF2P.length() <= maxCharLenName && nombreAF2P.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido.");
                System.out.println("Por favor intentelo de nuevo.");
                System.out.println();

            }

        }

        // Crear el AF2P
        try {

            AF2P nuevoAF2P = new AF2P(rutaArchivo);
            automatasAF2PActuales.put(nombreAF2P, nuevoAF2P);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo.");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }

        }

    }

    // Eliminar AF2P
    static void eliminarAF2P(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasAF2PActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AF2P.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Eliminación de AF2P");
        System.out.println();

        // Mostrar automatas creados
        if (automatasAF2PActuales.size() != 0) {

            // Listar AF2PS
            imprimirListaNombresAF2P();

        }

        Boolean ingresandoAF2P = true;
        String nombreAF2P = "";

        while (ingresandoAF2P) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AF2P que desea eliminar");
            nombreAF2P = input.next();
            System.out.println();

            if (!automatasAF2PActuales.keySet().contains(nombreAF2P)) {

                System.out.println("No hay ningun AF2P con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAF2P = false;
                automatasAF2PActuales.remove(nombreAF2P);

            }

        }

    }

    // * Exportar AF2P
    private static void exportarAF2P(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasAF2PActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AF2P.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Exportar AF2P");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasAF2PActuales.size() != 0) {

            // Listar AF2PS
            imprimirListaNombresAF2P();

        }

        // Ingresar el AF2P a exportar
        Boolean ingresandoAF2P = true;
        String nombreAF2P = "";

        while (ingresandoAF2P) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AF2P a exportar");
            nombreAF2P = input.next();
            System.out.println();

            if (!automatasAF2PActuales.keySet().contains(nombreAF2P)) {

                System.out.println("No hay ningun AF2P con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAF2P = false;

            }

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Exportar el AF2P
        try {

            automatasAF2PActuales.get(nombreAF2P).exportar(nombreArchivoSalida);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // * Procesar cadenas AF2P
    private static void procesarCadenasAF2P(Scanner input) {

        // Verificar que sí existan automatas
        if (automatasAF2PActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AF2P.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de una cadena por AF2P");
        System.out.println();

        if (automatasAF2PActuales.size() != 0) {

            // Listar AF2PS
            imprimirListaNombresAF2P();

        }

        // Ingresar el AF2P con el que se desea trabajar
        Boolean ingresandoAF2P = true;
        String nombreAF2P = "";

        while (ingresandoAF2P) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AF2P que desea utilizar");
            nombreAF2P = input.next();
            System.out.println();

            if (!automatasAF2PActuales.keySet().contains(nombreAF2P)) {

                System.out.println("No hay ningun AF2P con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAF2P = false;

            }
        }
        AF2P af2p = automatasAF2PActuales.get(nombreAF2P);

        procesarCadenasAF2PConEsteAF2P(af2p, input);

    }

    // * Procesar una cadena con un AF2P determinado
    private static void procesarCadenasAF2PConEsteAF2P(AF2P af2p, Scanner input) {

        Boolean procesandoVariasCadenas = true;
        while (procesandoVariasCadenas){

            // Ingresar la cadena a procesar
            Boolean ingresandoCadena = true;
            String cadena = "";

            while (ingresandoCadena) {

                // Imprimir alfabeto de cinta
                StringBuilder alfabeto = new StringBuilder();
                alfabeto.append("{");
                Boolean masDeUnCaracter = false;
                for (Character simbolo : af2p.getSigma()) {
                    if (masDeUnCaracter) {
                        alfabeto.append("," + simbolo);
                    } else {
                        alfabeto.append(simbolo);
                        masDeUnCaracter = true;
                    }
                }
                alfabeto.append("}");
                System.out.println("Alfabeto: " + alfabeto.toString());
                System.out.println("lambda: $");
                System.out.println();

                // Pedir info necesaria
                System.out.println("Ingrese la cadena a procesar");
                cadena = input.next().replaceAll("\\s+","");
                System.out.println();
                Boolean cadenaValida = true;
                for (int i = 0; i < cadena.length(); i++) {
                    if (!af2p.getSigma().contains(cadena.charAt(i))) {
                        cadenaValida = false;
                        break;
                    }
                }
                if (!cadenaValida && !cadena.equals("$")) {
                    System.out.println("La cadena ingresada no es valida.");
                    System.out.println("Por favor intentelo nuevamente.");
                    System.out.println();

                } else {
                    if (cadena.equals("$")) {
                        cadena = "";
                    }
                    ingresandoCadena = false;

                }
            }

            // Procesar la cadena
            af2p.preprocesarCadena(cadena);

            // Setup menu seleccion de tipo de procesamiento
            Boolean procesandoCadenas = true;

            // Seleccion de procesamiento que se repite hasta volver
            while (procesandoCadenas) {

                // Limpiar consola para que se vea mas fancy
                System.out.print("\033c");

                // Espacio al inicio para no saturar e indicar menu
                System.out.println();
                System.out.println("Selección tipo de procesamiento");
                System.out.println();

                // Mostrar las opciones de selección de automata
                System.out.println("Presionando el entero seguido de la tecla enter, por favor");
                System.out.println("seleccione una opción:");
                System.out.println();
                System.out.println("1. Procesar cadena: \"" + cadena + "\"");
                System.out.println("2. Consultar todos los procesamiento posibles de la cadena: \"" + cadena + "\"");
                System.out.println("3. Consultar los procesamientos de aceptacion de la cadena: \"" + cadena + "\"");
                System.out.println("4. Consultar los procesamientos de rechazo de la cadena: \"" + cadena + "\"");
                System.out.println("5. Procesar una cadena distinta.");
                System.out.println("0. Volver");
                System.out.println();
                System.out.print("Ingrese su opción: ");
                Integer opcion = input.nextInt();
                System.out.println();

                // Redirigir a la opcion deseada
                switch (opcion) {

                    case 1:
                        if (af2p.getEsAceptada()) {
                            System.out.println("La cadena: \"" + cadena + "\" es aceptada.");
                            System.out.println("Un procesamiento aceptado es: ");
                            System.out.println(af2p.getProcesamientoAceptacion());
                        } else {
                            System.out.println("La cadena: \"" + cadena + "\" es rechazada.");
                            System.out.println("Uno de los procesamientos mas cortos es: ");
                            System.out.println(af2p.getProcesamientoRechazo());
                        }
                        
                        break;

                    case 2:
                        System.out.println(af2p.getStringListaProcesamientos());
                        break;

                    case 3:
                        System.out.println(af2p.getStringProcesamientosAceptacion());
                        break;

                    case 4:
                        System.out.println(af2p.getStringProcesamientosRechazo());
                        break;

                    case 5:
                        procesandoCadenas = false;
                        break;

                    case 0:
                        procesandoCadenas = false;
                        procesandoVariasCadenas = false;
                        break;

                    default:

                        System.out.println("Opción inválida");

                        try {

                            // Espera que lea el mensaje
                            TimeUnit.SECONDS.sleep(segundosEsperaLector);

                        } catch (InterruptedException e) {

                            e.printStackTrace();

                        }

                        break;

                }
                // Esperar hasta que se presione enter
                if (procesandoCadenas) {
                    System.out.println("Presione enter para continuar");
                    try {
                        System.in.read();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }
            }
        }
    }

    // * Procesar lista de cadenas AF2P
    private static void procesarListaDeCadenasAF2P(Scanner input) {

        // Verificar que sí existan
        if (automatasAF2PActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AF2P.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de cadenas por AF2P");
        System.out.println();

        if (automatasAF2PActuales.size() != 0) {

            // Listar AF2Ps
            imprimirListaNombresAF2P();

        }

        // Ingresar el AF2P con el que se desea trabajar
        Boolean ingresandoAF2P = true;
        String nombreAF2P = "";

        while (ingresandoAF2P) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del AF2P que desea utilizar");
            nombreAF2P = input.next();
            System.out.println();

            if (!automatasAF2PActuales.keySet().contains(nombreAF2P)) {

                System.out.println("No hay ningun AF2P con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoAF2P = false;

            }
        }
        AF2P af2p = automatasAF2PActuales.get(nombreAF2P);

        procesarListaDeCadenasAF2PConEsteAF2P(af2p, input, nombreAF2P);

    }
    
    private static void procesarListaDeCadenasAF2PConEsteAF2P(AF2P af2p, Scanner input, String nombreAF2P) {

        // Setup menu probar AF2P
        Boolean escogiendoLista = true;

        // Menu que muestra las opciones para af2p
        while (escogiendoLista) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Procesamiento de listas de cadenas AF2P");
            System.out.println();
            System.out.println("Automata: " + nombreAF2P);

            // Mostrar las opciones para probar AF2P
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar lista de cadenas aleatorias");
            System.out.println("2. Importar y procesar lista de cadenas");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            Iterable<String> listaCadenas;
            String nombreArchivo;
            String imprimirPantallaStr;
            boolean imprimirPantalla;

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir cantidad de cadenas
                    int numCadenas = 0;
                    while (numCadenas < 1) {
                        System.out.println("Por favor ingrese el numero de cadenas a generar");
                        while (!input.hasNext()) {
                            // Esperar a que haya un input y luego sí avanzas
                        }
                        numCadenas = input.nextInt();
                    }
                    listaCadenas = af2p.getAlfabeto().generarCadenas(numCadenas);

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("Ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    //procesar lista
                    af2p.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
                    // Esperar hasta que presione enter
                    System.out.println("\nPresione enter para continuar");
                    try {
                        System.in.read();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    break;

                case 2:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir ruta al archivo
                    System.out.println("Por favor ingrese la ruta al archivo que contiene la lista");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    String rutaArchivo = input.next();
                    System.out.println();

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    // Crear lista
                    try {
                        listaCadenas = leerListaCadenasDesdeArchivo(rutaArchivo);

                        //procesar lista
                        af2p.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
                        // Esperar hasta que presione enter
                        System.out.println("\nPresione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    } catch (Exception e) {
                        if (devMode) {
                            e.printStackTrace();
                        }
                        System.out.println("\nOcurrió un error al buscar el archivo.\n");
                        escogiendoLista = false;

                        // Esperar hasta que presione enter
                        System.out.println("Presione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                    break;

                case 0:
                    escogiendoLista = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }
    }

    private static void imprimirListaNombresAF2P() {
        Integer numActual = 1;

        System.out.println("Sus AF2P actuales son:");
        System.out.println();

        // Imprimir los AF2P contenidos en automatasActuales
        for (String strActual : automatasAF2PActuales.keySet()) {

            System.out.println(Integer.toString(numActual) + ". " + strActual + "\n");
            numActual++;

        }

        System.out.println();
    }

    // ** Probar MT
    private static void probarMT(Scanner input) {

        // Setup menu probar MT
        Boolean probandoMT = true;

        // Menu que muestra las opciones para la maquina de turing estandar
        while (probandoMT) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de MT");
            System.out.println();

            if (MTActuales.size() != 0) {

                // Listar MTs
                imprimirListaNombresMT();

            }

            // Mostrar las opciones para probar MT
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Editar MT");
            System.out.println("2. Procesar cadenas");
            System.out.println("3. Procesar listas de cadenas");
            System.out.println("4. Exportar MT");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    editarMT(input);
                    break;

                case 2:
                    procesarCadenasMT(input);
                    break;

                case 3:
                    procesarListaDeCadenasMT(input);
                    break;
                
                case 4:
                    exportarMT(input);
                    break;

                case 0:
                    probandoMT = false;
                    break;

                default:

                    System.out.println("Opción inválida.");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // * Editar MT
    private static void editarMT(Scanner input) {

        // Setup menu editar MT
        Boolean editandoMT = true;

        // Menu que muestra las opciones para editar MT
        while (editandoMT) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Edición de MTs");
            System.out.println();

            if (MTActuales.size() != 0) {

                // Listar MT
                imprimirListaNombresMT();

            }

            // Mostrar las opciones para editar MT
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear MT");
            System.out.println("2. Eliminar MT");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    crearMT(input);
                    break;

                case 2:
                    eliminarMT(input);
                    break;

                case 0:
                    editandoMT = false;
                    break;

                default:

                    System.out.println("Opción inválida.");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // Crear MT
    private static void crearMT(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de MT desde archivo");
        System.out.println();

        // Pedir informacion necesaria para crear el MT
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene a la MT");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del MT
        Boolean ingresandoNombre = true;
        String nombreMT = "";
        while (ingresandoNombre) {
            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para la MT");
            nombreMT = input.next().trim();
            System.out.println();

            // Verificar condición
            if (nombreMT.length() <= maxCharLenName && nombreMT.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido.");
                System.out.println("Por favor intentelo de nuevo.");
                System.out.println();

            }

        }

        // Crear la MT
        try {

            MT nuevaMT = new MT(rutaArchivo);
            MTActuales.put(nombreMT, nuevaMT);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo.");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }

        }

    }

    // Eliminar MT
    static void eliminarMT(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (MTActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ninguna MT.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Eliminación de MTs");
        System.out.println();

        // Mostrar MTs creadas
        if (MTActuales.size() != 0) {

            // Listar MTs
            imprimirListaNombresMT();

        }

        Boolean ingresandoMT = true;
        String nombreMT = "";

        while (ingresandoMT) {

            // Pedir info necesaria
            System.out.println("Indique el nombre de la MT que desea eliminar");
            nombreMT = input.next();
            System.out.println();

            if (!MTActuales.keySet().contains(nombreMT)) {

                System.out.println("No hay ninguna MT con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoMT = false;
                MTActuales.remove(nombreMT);

            }

        }

    }

    // * Exportar MT
    private static void exportarMT(Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (MTActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ninguna MT.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Exportar MT");
        System.out.println();

        // Mostrar automatas existentes
        if (MTActuales.size() != 0) {

            // Listar MTs
            imprimirListaNombresMT();

        }

        // Ingresar el MT a exportar
        Boolean ingresandoMT = true;
        String nombreMT = "";

        while (ingresandoMT) {

            // Pedir info necesaria
            System.out.println("Indique el nombre de la MT a exportar");
            nombreMT = input.next();
            System.out.println();

            if (!MTActuales.keySet().contains(nombreMT)) {

                System.out.println("No hay ninguna MT con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoMT = false;

            }

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Exportar el MT
        try {

            MTActuales.get(nombreMT).exportar(nombreArchivoSalida);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // * Procesar cadenas MT
    private static void procesarCadenasMT(Scanner input) {

        // Verificar que sí existan automatas
        if (MTActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("Todavía no se ha creado ninguna MT.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de una cadena por MT");
        System.out.println();

        if (MTActuales.size() != 0) {

            // Listar MTs
            imprimirListaNombresMT();

        }

        // Ingresar la MT con la que se desea trabajar
        Boolean ingresandoMT = true;
        String nombreMT = "";

        while (ingresandoMT) {

            // Pedir info necesaria
            System.out.println("Indique el nombre del MT que desea utilizar");
            nombreMT = input.next();
            System.out.println();

            if (!MTActuales.keySet().contains(nombreMT)) {

                System.out.println("No hay ninguna MT con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoMT = false;

            }
        }
        MT mt = MTActuales.get(nombreMT);

        procesarCadenasMTConEstaMT(mt, input);

    }

    // * Procesar una cadena con una MT determinado
    private static void procesarCadenasMTConEstaMT(MT mt, Scanner input) {

        Boolean procesandoVariasCadenas = true;
        while (procesandoVariasCadenas){

            // Ingresar la cadena a procesar
            Boolean ingresandoCadena = true;
            String cadena = "";

            while (ingresandoCadena) {

                // Imprimir alfabeto de cinta
                StringBuilder alfabeto = new StringBuilder();
                alfabeto.append("{");
                Boolean masDeUnCaracter = false;
                for (Character simbolo : mt.getSigma()) {
                    if (masDeUnCaracter) {
                        alfabeto.append("," + simbolo);
                    } else {
                        alfabeto.append(simbolo);
                        masDeUnCaracter = true;
                    }
                }
                alfabeto.append("}");
                System.out.println("Alfabeto: " + alfabeto.toString());
                System.out.println("lambda: $");
                System.out.println();

                // Pedir info necesaria
                System.out.println("Ingrese la cadena a procesar");
                cadena = input.next().replaceAll("\\s+","");
                System.out.println();
                Boolean cadenaValida = true;
                for (int i = 0; i < cadena.length(); i++) {
                    if (!mt.getSigma().contains(cadena.charAt(i)) && (cadena.charAt(i) != '!')) {
                        cadenaValida = false;
                        break;
                    }
                }
                if (!cadenaValida && !cadena.equals("$")) {
                    System.out.println("La cadena ingresada no es valida.");
                    System.out.println("Por favor intentelo nuevamente.");
                    System.out.println();

                } else {
                    if (cadena.equals("$")) {
                        cadena = "";
                    }
                    ingresandoCadena = false;

                }
            }

            // Procesar la cadena
            mt.preprocesarCadena(cadena);

            // Setup menu seleccion de tipo de procesamiento
            Boolean procesandoCadenas = true;

            // Seleccion de procesamiento que se repite hasta volver
            while (procesandoCadenas) {

                // Limpiar consola para que se vea mas fancy
                System.out.print("\033c");

                // Espacio al inicio para no saturar e indicar menu
                System.out.println();
                System.out.println("Selección tipo de procesamiento");
                System.out.println();

                // Mostrar las opciones de selección de procesamiento
                System.out.println("Presionando el entero seguido de la tecla enter, por favor");
                System.out.println("seleccione una opción:");
                System.out.println();
                System.out.println("1. Procesar cadena: \"" + cadena + "\"");
                System.out.println("2. Procesar como funcion con entrada: \"" + cadena + "\"");
                System.out.println("3. Procesar una cadena distinta.");
                System.out.println("0. Volver");
                System.out.println();
                System.out.print("Ingrese su opción: ");
                Integer opcion = input.nextInt();
                System.out.println();

                // Redirigir a la opcion deseada
                switch (opcion) {

                    case 1:
                        if (mt.getEsAceptada()) {
                            System.out.println("La cadena: \"" + cadena + "\" es aceptada:");
                        } else {
                            System.out.println("La cadena: \"" + cadena + "\" es rechazada:");
                        }
                        System.out.println(mt.getProcesamiento());
                        break;

                    case 2:
                        if (mt.getEsAceptada()) {
                            System.out.println("La cadena: \"" + cadena + "\" es aceptada, la saldia es:");
                        } else {
                            System.out.println("La cadena: \"" + cadena + "\" es rechazada, la saldia es:");
                        }
                        System.out.println(mt.procesarFuncion(cadena));
                        break;
                    case 3:
                        procesandoCadenas = false;
                        break;

                    case 0:
                        procesandoCadenas = false;
                        procesandoVariasCadenas = false;
                        break;

                    default:

                        System.out.println("Opción inválida");

                        try {

                            // Espera que lea el mensaje
                            TimeUnit.SECONDS.sleep(segundosEsperaLector);

                        } catch (InterruptedException e) {

                            e.printStackTrace();

                        }

                        break;

                }
                // Esperar hasta que se presione enter
                if (procesandoCadenas) {
                    System.out.println("Presione enter para continuar");
                    try {
                        System.in.read();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                }
            }
        }
    }

    // * Procesar lista de cadenas MT
    private static void procesarListaDeCadenasMT(Scanner input) {

        // Verificar que sí existan
        if (MTActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("Todavía no se ha creado ninguna MT.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de cadenas por MT");
        System.out.println();

        if (MTActuales.size() != 0) {

            // Listar MTa
            imprimirListaNombresMT();

        }

        // Ingresar la MT con la que se desea trabajar
        Boolean ingresandoMT = true;
        String nombreMT = "";

        while (ingresandoMT) {

            // Pedir info necesaria
            System.out.println("Indique el nombre de la MT que desea utilizar");
            nombreMT = input.next();
            System.out.println();

            if (!MTActuales.keySet().contains(nombreMT)) {

                System.out.println("No hay ninguna MT con ese nombre.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {

                ingresandoMT = false;

            }
        }
        MT mt = MTActuales.get(nombreMT);

        procesarListaDeCadenasMTConEstaMT(mt, input, nombreMT);

    }
    
    private static void procesarListaDeCadenasMTConEstaMT(MT mt, Scanner input, String nombreMT) {

        Boolean escogiendoLista = true;

        // Menu que muestra las opciones de listas
        while (escogiendoLista) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Procesamiento de listas de cadenas con MT");
            System.out.println();
            System.out.println("Automata: " + nombreMT);

            // Mostrar las opciones para probar MT
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Importar y procesar lista de cadenas");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            Iterable<String> listaCadenas;
            String nombreArchivo;
            String imprimirPantallaStr;
            boolean imprimirPantalla;

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir ruta al archivo
                    System.out.println("Por favor ingrese la ruta al archivo que contiene la lista");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    String rutaArchivo = input.next();
                    System.out.println();

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    // Crear lista
                    try {
                        listaCadenas = leerListaCadenasDesdeArchivo(rutaArchivo);

                        //procesar lista
                        mt.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
                        // Esperar hasta que presione enter
                        System.out.println("\nPresione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    } catch (Exception e) {
                        if (devMode) {
                            e.printStackTrace();
                        }
                        System.out.println("\nOcurrió un error al buscar el archivo.\n");
                        escogiendoLista = false;

                        // Esperar hasta que presione enter
                        System.out.println("Presione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                    break;

                case 0:
                    escogiendoLista = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }
    }

    private static void imprimirListaNombresMT() {
        Integer numActual = 1;

        System.out.println("Sus MTs actuales son:");
        System.out.println();

        // Imprimir las MT contenidas en MTActuales
        for (String strActual : MTActuales.keySet()) {

            System.out.println(Integer.toString(numActual) + ". " + strActual + "\n");
            numActual++;

        }

        System.out.println();
    }

    // ** Probar AFN Lambda
    private static void probarAFNLambda(Scanner input) throws IOException {
        System.out.print("Ingrese el nombre del archivo AFNLambda: ");
        Scanner scanner = new Scanner(System.in);
        String nombreArchivo = scanner.nextLine();
        AFNLambda automata;

        try {
            automata = new AFNLambda(nombreArchivo);
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo AFNLambda: " + e.getMessage());
            scanner.close();
            return;
        }

        Boolean probandoAFNLambda = true;
        TreeSet<AFD> automatasActuales = new TreeSet<>();

        while (probandoAFNLambda) {

            // Limpiar consola
            // System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de AFNLambda");
            System.out.println();

            System.out.println("Seleccione una opción:");
            System.out.println("1. Calcular λ-Clausura");
            System.out.println("2. Determinar estados inaccesibles");
            System.out.println("3. Imprimir autómata");
            System.out.println("4. Exportar autómata");
            System.out.println("5. Procesar cadena");
            System.out.println("6. Procesar cadena con detalles");
            System.out.println("7. Computar todos los procesamientos");
            System.out.println("8. Procesar lista de cadenas con detalles");
            System.out.println("9. Convertir AFN_Lambda a AFN");
            System.out.println("Escriba 'exit' para salir");
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();
            switch (opcion) {
                case 1:
                    try {
                        calcularYMostrarLambdaClausura(nombreArchivo);
                    } catch (IOException e) {
                        System.out.println("Error al calcular λ-Clausura: " + e.getMessage());
                    }
                    break;
                case 2:
                    determinarYMostrarEstadosInaccesibles(automata);
                    break;
                case 3:
                    try {
                        imprimirAFNLSimplificado(automata);
                    } catch (Exception e) {
                        System.out.println("Error:" + e.getMessage());
                    }
                case 4:
                    exportarAutomata(automata);
                    break;
                case 5:
                    procesarCadena(automata);
                    break;
                case 6:
                    procesarCadenaConDetalles(automata);
                    break;
                case 7:
                    computarTodosLosProcesamientos(automata);
                    break;
                case 8:
                    procesarListaCadenasConDetalles(automata);
                    break;
                case 9:
                    AFN afn = automata.AFN_LambdaToAFN(automata);
                    System.out.println("Ingrese el nombre del archivo de salida (AFN):");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String nombreArchivoSalida = reader.readLine();
                    // Exportar el autómata AFN a un archivo
                    afn.exportar(nombreArchivoSalida);
                    System.out.println("El autómata se ha exportado correctamente.");
                    break;
            }
        }

        scanner.close();
    }

    // Probar conversiones
    private static void probarAFNtoAFD(Scanner input) {

        // Verificar que sí existan
        if (automatasAFNActuales.size() == 0) {

            System.out.println();
            System.out.println("Todavía no se ha creado ningún AFN.");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Ingresar el AFN a exportar
        

        // Setup menu probar AFN to AFD
        Boolean probandoAFNtoAFD = true;

        // Menu que muestra las opciones para pruebas de conversion AFN a AFD
        while (probandoAFNtoAFD) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de Conversion: AFN a AFD");
            System.out.println();

            if (automatasAFNActuales.size() != 0) {

                // Listar AFNs
                System.out.println("AFNs guardados:\n");
                imprimirListaNombresAFN();

            }

            // Seleccionar AFN
            Boolean ingresandoAFN = true;
            String nombreAFN = "";

            while (ingresandoAFN) {

                // Pedir info necesaria
                System.out.println("Indique el nombre del AFN con el que desea trabajar");
                nombreAFN = input.next();
                System.out.println();

                if (!automatasAFNActuales.keySet().contains(nombreAFN)) {

                    System.out.println("No hay ningun AFN con ese nombre.");
                    System.out.println("Por favor intentelo nuevamente.");
                    System.out.println();

                } else {

                    ingresandoAFN = false;

                }

            }

            Boolean trabajandoConAFD = true;
            AFN afn = automatasAFNActuales.get(nombreAFN);
            AFD afd = AFN.AFNtoAFD(afn);

            while (trabajandoConAFD) {
                // Limpiar consola para que se vea mas fancy
                System.out.print("\033c");

                // Espacio al inicio para no saturar e indicar menu
                System.out.println();
                System.out.println("Prueba de Conversion: AFN a AFD");
                System.out.println();
                System.out.println("AFN seleccionado:" + nombreAFN + "\n");

                // Mostrar las opciones para probar AFN to AFD
                System.out.println("Presionando el entero seguido de la tecla enter, por favor");
                System.out.println("seleccione una opción:");
                System.out.println();
                System.out.println("1. Comparar procesamiento de una cadena");
                System.out.println("2. Procesar cadena con AFD equivalente");
                System.out.println("3. Procesar lista de cadenas con AFD equivalente");
                System.out.println("4. Exportar AFD equivalente");
                System.out.println("5. Seleccionar otro AFN");
                System.out.println("0. Volver");
                System.out.println();
                System.out.print("Ingrese su opción: ");
                Integer opcion = input.nextInt();
                System.out.println();

                // Redirigir a la opcion deseada
                switch (opcion) {

                    case 1:
                        compararAFNtoAFD(afn, afd, input);
                        break;

                    case 2:
                        procesarCadenasAFNtoAFD(afn, afd, input);
                        break;

                    case 3:
                        procesarListaDeCadenasAFNtoAFD(afn, afd, input, nombreAFN);
                        break;

                    case 4:
                        exportarAFNtoAFD(afd, input);
                        break;

                    case 5:
                        trabajandoConAFD = false;
                        break;

                    case 0:
                        trabajandoConAFD = false;
                        probandoAFNtoAFD = false;
                        break;

                    default:

                        System.out.println("Opción inválida");

                        try {

                            // Espera que lea el mensaje
                            TimeUnit.SECONDS.sleep(segundosEsperaLector);

                        } catch (InterruptedException e) {

                            e.printStackTrace();

                        }

                        break;

                }
            }
        }
    }

    
    // * Procesar cadenas AFN to AFD
    private static void compararAFNtoAFD(AFN afn, AFD afd, Scanner input) {

        // Ingresar la cadena a procesar
        Boolean ingresandoCadena = true;
        String cadena = "";

        while (ingresandoCadena) {

            // Imprimir alfabeto
            StringBuilder alfabeto = new StringBuilder();
            alfabeto.append("{");
            Boolean masDeUnCaracter = false;
            for (Character simbolo : afn.getSigma()) {
                if (masDeUnCaracter) {
                    alfabeto.append("," + simbolo);
                } else {
                    alfabeto.append(simbolo);
                    masDeUnCaracter = true;
                }
            }
            alfabeto.append("}");
            System.out.println("Alfabeto: " + alfabeto.toString());
            System.out.println("lambda: $");
            System.out.println();

            // Pedir info necesaria
            System.out.println("Ingrese la cadena a procesar");
            cadena = input.next().replaceAll("\\s+","");
            System.out.println();
            Boolean cadenaValida = true;
            for (int i = 0; i < cadena.length(); i++) {
                if (!afn.getSigma().contains(cadena.charAt(i))) {
                    cadenaValida = false;
                    break;
                }
            }
            if (!cadenaValida && !cadena.equals("$")) {
                System.out.println("La cadena ingresada no es valida.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {
                if (cadena.equals("$")) {
                    cadena = "";
                }
                ingresandoCadena = false;

            }
        }
        System.out.println("Pocesamiento via AFN:");
        Boolean esAceptadaPorAFN = afn.procesarCadenaConDetalles(cadena);
        System.out.println();
        System.out.println("Pocesamiento via AFD:");
        Boolean esAceptadaPorAFD = afd.procesarCadena(cadena, true);
        if (esAceptadaPorAFN && esAceptadaPorAFD) {
            System.out.println("\nLa cadena \"" + cadena + "\" es aceptada por ambos automatas.");
        } else if (!esAceptadaPorAFN && !esAceptadaPorAFD) {
            System.out.println("\nLa cadena \"" + cadena + "\" es rechazada por ambos automatas.");
        } else {
            System.out.println("\nLos automatas difieren en el procesamiento de la cadena \"" + cadena + "\".");
        }

        // Esperar hasta que se presione enter
        System.out.println("\nPresione enter para continuar");
        try {
            System.in.read();
        } catch (IOException er) {
            er.printStackTrace();
        }
    }

    // * Procesar cadenas AFN to AFD
    private static void procesarCadenasAFNtoAFD(AFN afn, AFD afd, Scanner input) {

        // Ingresar la cadena a procesar
        Boolean ingresandoCadena = true;
        String cadena = "";

        while (ingresandoCadena) {

            // Imprimir alfabeto
            StringBuilder alfabeto = new StringBuilder();
            alfabeto.append("{");
            Boolean masDeUnCaracter = false;
            for (Character simbolo : afn.getSigma()) {
                if (masDeUnCaracter) {
                    alfabeto.append("," + simbolo);
                } else {
                    alfabeto.append(simbolo);
                    masDeUnCaracter = true;
                }
            }
            alfabeto.append("}");
            System.out.println("Alfabeto: " + alfabeto.toString());
            System.out.println("lambda: $");
            System.out.println();

            // Pedir info necesaria
            System.out.println("Ingrese la cadena a procesar");
            cadena = input.next().replaceAll("\\s+","");
            System.out.println();
            Boolean cadenaValida = true;
            for (int i = 0; i < cadena.length(); i++) {
                if (!afn.getSigma().contains(cadena.charAt(i))) {
                    cadenaValida = false;
                    break;
                }
            }
            if (!cadenaValida && !cadena.equals("$")) {
                System.out.println("La cadena ingresada no es valida.");
                System.out.println("Por favor intentelo nuevamente.");
                System.out.println();

            } else {
                if (cadena.equals("$")) {
                    cadena = "";
                }
                ingresandoCadena = false;

            }
        }
        Boolean esAceptada = afd.procesarCadena(cadena, true);

        // Esperar hasta que se presione enter
        System.out.println("\nPresione enter para continuar");
        try {
            System.in.read();
        } catch (IOException er) {
            er.printStackTrace();
        }
    }

    // * Procesar lista de cadenas AFN
    private static void procesarListaDeCadenasAFNtoAFD(AFN afn, AFD afd, Scanner input, String nombreAFN) {

        // Setup menu probar AFN
        Boolean escogiendoLista = true;

        // Menu que muestra las opciones para afn
        while (escogiendoLista) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Procesamiento de listas de cadenas AFN");
            System.out.println();
            System.out.println("Automata: " + nombreAFN);

            // Mostrar las opciones para probar AFN
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar lista de cadenas aleatorias con AFD equivalente");
            System.out.println("2. Importar y procesar lista de cadenas con AFD equivalente");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            Iterable<String> listaCadenas;
            String nombreArchivo;
            String imprimirPantallaStr;
            boolean imprimirPantalla;

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir cantidad de cadenas
                    int numCadenas = 0;
                    while (numCadenas < 1) {
                        System.out.println("Por favor ingrese el numero de cadenas a generar");
                        while (!input.hasNext()) {
                            // Esperar a que haya un input y luego sí avanzas
                        }
                        numCadenas = input.nextInt();
                    }
                    listaCadenas = afn.getAlfabeto().generarCadenas(numCadenas);

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    //procesar lista
                    afn.procesarListaCadenasConversion(listaCadenas, nombreArchivo, imprimirPantalla, afd);
                    // Esperar hasta que presione enter
                    System.out.println("Presione enter para continuar");
                    try {
                        System.in.read();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    break;

                case 2:
                    // Pedir informacion necesaria para crear la lista
                    // Pedir ruta al archivo
                    System.out.println("Por favor ingrese la ruta al archivo que contiene la lista");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    String rutaArchivo = input.next();
                    System.out.println();

                    // Pedir info necesaria
                    System.out.println("Indique el nombre del archivo de salida");
                    nombreArchivo = input.next();
                    System.out.println();

                    System.out.println("ingrese \"p\" si desea imprimir los resultados en pantalla.");
                    while (!input.hasNext()) {
                        // Esperar a que haya un input y luego sí avanzas
                    }
                    imprimirPantallaStr = input.next();
                    System.out.println();
                    imprimirPantalla = imprimirPantallaStr.equalsIgnoreCase("p");

                    // Crear lista
                    try {
                        listaCadenas = leerListaCadenasDesdeArchivo(rutaArchivo);

                        //procesar lista
                        afn.procesarListaCadenasConversion(listaCadenas, nombreArchivo, imprimirPantalla, afd);
                        // Esperar hasta que presione enter
                        System.out.println("Presione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    } catch (Exception e) {
                        if (devMode) {
                            e.printStackTrace();
                        }
                        System.out.println("\nOcurrió un error al buscar el archivo.\n");
                        escogiendoLista = false;

                        // Esperar hasta que presione enter
                        System.out.println("Presione enter para continuar");
                        try {
                            System.in.read();
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                    }
                    break;

                case 0:
                    escogiendoLista = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }
    }

    // * Exportar AFN to AFD
    private static void exportarAFNtoAFD(AFD afd, Scanner input) {

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Exportar el AFD
        afd.exportar(nombreArchivoSalida);

    }

    private static void validarAFNtoAFD(Scanner input){

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Validar convercion de AFN a AFD");
        System.out.println();

        // Pedir informacion necesaria para crear el AFN
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta a un archivo que liste rutas a archivos .nfa");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();


        // Crear lista de AFNs

        try {

            Collection<AFN> listaAFNs = new ArrayDeque<AFN>();
            FileReader fileReader = new FileReader(rutaArchivo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                listaAFNs.add(new AFN(linea));
            }
            bufferedReader.close();
            ClaseValidacion.validarAFNtoAFD(listaAFNs);

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }



        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar un archivo.");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }


    private static void probarAFNLambdaToAFN() {

        // TODO

    }

    private static void probarAFNLambdaToAFD() {

        // TODO

    }

    private static void probarAFPN(Scanner input) {
        // Setup menu probar AFPD
        Boolean probandoAFPN = true;
        TreeSet<AFPN> automatasActuales = new TreeSet<>();
        // Menu que muestra las opciones para AFPD
        while (probandoAFPN) {
            // Limpiar la consola para que se vea más fancy
            System.out.print("\033c");
            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de AFPN");
            System.out.println();
            // Mostrar los automatas actuales
            if (automatasActuales.size() != 0) {
                Integer numActual = 1;
                System.out.println("Sus AFPN actuales son:");
                System.out.println();
                // Imprimir los AFPD contenidos en automatasActuales
                for (AFPN actual : automatasActuales) {
                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPN() + "\n");
                    numActual++;
                }
                System.out.println();
            }
            // Mostrar las opciones para probar AFPD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Editar AFPN's ");
            System.out.println("2. Procesar cadenas");
            System.out.println("3. Producto cartesiano con AFN");
            System.out.println("4. Exportar AFPN");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();
            // Redirigir a la opcion deseada
            switch (opcion) {
                case 1:
                    editarAFPN(automatasActuales, input);
                    break;
                case 2:
                    procesarCadenasAFPN(automatasActuales, input);
                    break;
                case 3:
                    // productoCartesianoConAFN(automatasActuales, input);
                    break;
                case 4:
                    exportarAFPN(automatasActuales, input);
                    break;
                case 0:
                    probandoAFPN = false;
                    break;
                default:
                    System.out.println("Opción inválida");
                    try {
                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private static void editarAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {

        // Setup menu editar AFPD
        Boolean editandoAFPD = true;

        // Menu que muestra las opciones para editar AFD
        while (editandoAFPD) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Edición de AFPN's");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFPN actuales son:");
                System.out.println();

                // Imprimir los AFPD contenidos en automatasActuales
                for (AFPN actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPN() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones para editar AFPD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear AFPN ");
            System.out.println("2. Eliminar AFPN");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();
            // Redirigir a la opcion deseada
            switch (opcion) {
                case 1:
                    crearAFPN(automatasActuales, input);
                    
                    break;
                case 2:
                    eliminarAFPN(automatasActuales, input);
                    break;
                case 0:
                    editandoAFPD = false;
                    break;
                default:
                    System.out.println("Opción inválida");
                    try {
                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            }
        }

    private static void crearAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {
        // Limpiar consola
        System.out.print("\033c");

        // Espacio al inicio para mostrar el menú
        System.out.println();
        System.out.println("Creación de AFPN desde archivo");
        System.out.println();

        // Pedir información necesaria para crear el AFPN
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene el AFPN");
        while (!input.hasNext()) {
            // Esperar a que haya un input y luego avanzar
    }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del AFPN
        boolean ingresandoNombre = true;
        String nombreAFPN = "";
        while (ingresandoNombre) {
            System.out.println("Por favor ingrese un nombre para el AFPN sin espacios");
            nombreAFPN = input.next().trim();
            System.out.println();

            // Verificar condición
            if (!nombreAFPN.contains(" ")) {
                ingresandoNombre = false;
            } else {
                System.out.println("El nombre ingresado no es válido");
                System.out.println("Por favor inténtelo de nuevo");
                System.out.println();
            }
        }

        // Crear el AFPN
        try {
            AFPN nuevoAFPN = new AFPN(rutaArchivo);
            nuevoAFPN.setNombreAFPN(nombreAFPN);
            automatasActuales.add(nuevoAFPN);
        } catch (Exception e) {
            if (devMode) {
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Espera a que el usuario presione Enter
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
    }

    private static void eliminarAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {
        // Limpiar consola
        System.out.print("\033c");

        // Verificar si existen AFPN
        if (automatasActuales.size() == 0) {
            System.out.println();
            System.out.println("No hay ningún autómata creado. Por favor, cree uno antes de usar esta opción");
            System.out.println();
            try {
                TimeUnit.SECONDS.sleep(segundosEsperaLector);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        // Espacio al inicio para mostrar el menú
        System.out.println();
        System.out.println("Eliminación de AFPN");
        System.out.println();

        // Mostrar los autómatas creados
        if (automatasActuales.size() != 0) {
            int numActual = 1;
            System.out.println("Sus AFPN actuales son:");
            System.out.println();

            // Imprimir los AFPN contenidos en automatasActuales
            for (AFPN actual : automatasActuales) {
                System.out.println(numActual + ". " + actual.getNombreAFPN() + "\n");
                numActual++;
            }

            System.out.println();
        }

        // Pedir indicación sobre el AFPN a eliminar
        boolean ingresandoAFPN = true;
        int numAFPN = 0;

        while (ingresandoAFPN) {
            // Pedir información necesaria
            System.out.println("Indique el número del AFPN que desea eliminar");
            numAFPN = input.nextInt();
            System.out.println();

            if (numAFPN <= 0 || numAFPN > automatasActuales.size()) {
                System.out.println("El número ingresado no es válido");
                System.out.println("Por favor inténtelo de nuevo");
                System.out.println();
            } else {
                ingresandoAFPN = false;
                numAFPN--;
            }
        }

        // Eliminar el AFPN
        ArrayList<AFPN> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPN afpnEliminado = listaAutomatas.get(numAFPN);
        automatasActuales.remove(afpnEliminado);
    } 

    // ** Procesar cadenas AFPN
    private static void procesarCadenasAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {

        if (automatasActuales.size() == 0) {
            System.out.print("\033c");
            System.out.println();
            System.out.println("No hay ningún autómata creado, por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();
            try {
                TimeUnit.SECONDS.sleep(segundosEsperaLector);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        Boolean procesandoCadenas = true;

        while (procesandoCadenas) {
            System.out.print("\033c");
            System.out.println();
            System.out.println("Selección tipo de procesamiento");
            System.out.println();

            if (automatasActuales.size() != 0) {
                Integer numActual = 1;
                System.out.println("Sus AFPN actuales son:");
                System.out.println();

                for (AFPN actual : automatasActuales) {
                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPN() + "\n");
                    numActual++;
                }
                System.out.println();
            }

            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar cadena");
            System.out.println("2. Procesar lista de cadenas desde archivo");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            switch (opcion) {
                case 1:
                    procesarCadenaAFPN(automatasActuales, input);
                    break;
                case 2:
                    procesarListaCadenasAFPN(automatasActuales, input);
                    break;
                case 0:
                    procesandoCadenas = false;
                    break;
                default:
                    System.out.println("Opción inválida");
                    try {
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                    }
        }
    }

    private static void procesarCadenaAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {

        System.out.print("\033c");
        System.out.println();
        System.out.println("Procesamiento de cadena");
        System.out.println();

        if (automatasActuales.size() != 0) {
            Integer numActual = 1;
            System.out.println("Sus AFPN actuales son:");
            System.out.println();

            for (AFPN actual : automatasActuales) {
                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPN() + "\n");
                numActual++;
            }
            System.out.println();
        }

        Boolean ingresandoAFPN = true;
        Integer numAFPN = 0;

        while (ingresandoAFPN) {
            System.out.println("Indique el número del AFPN a partir del cual quiere procesar la cadena");
            numAFPN = input.nextInt();
            System.out.println();

            if (numAFPN <= 0 || numAFPN > automatasActuales.size()) {
                System.out.println("El número ingresado no es válido. Por favor, inténtelo de nuevo.");
                System.out.println();
            } else {
                ingresandoAFPN = false;
                numAFPN--;
            }
        }

        System.out.println("Por favor, ingrese la cadena a procesar:");
        String cadenaEntrada = input.next();
        System.out.println();

        System.out.println("¿Desea imprimir los detalles del procesamiento?");
        System.out.println("Por favor, ingrese 1 para sí y 0 para no:");
        Integer detalles = input.nextInt();
        System.out.println();

        ArrayList<AFPN> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPN afpnBase = listaAutomatas.get(numAFPN);
        Boolean resultado = afpnBase.procesarCadena(cadenaEntrada, detalles == 1);
        System.out.println();

        if (resultado) {
            System.out.println("La cadena fue aceptada por el AFPN");
            System.out.println();
        } else {
            System.out.println("La cadena no fue aceptada por el AFPN");
            System.out.println();
        }

        System.out.println("Presione enter para continuar");
                    try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
                    }
    }

    private static void procesarListaCadenasAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {

        System.out.print("\033c");
        System.out.println();
        System.out.println("Procesamiento de lista de cadenas desde archivo");
        System.out.println();

        if (automatasActuales.size() != 0) {
            Integer numActual = 1;
            System.out.println("Sus AFPN actuales son:");
            System.out.println();

            for (AFPN actual : automatasActuales) {
                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPN() + "\n");
                numActual++;
            }
            System.out.println();
        }

        Boolean ingresandoAFPN = true;
        Integer numAFPN = 0;

        while (ingresandoAFPN) {
            System.out.println("Indique el número del AFPN a partir del cual quiere procesar la lista de cadenas");
            numAFPN = input.nextInt();
            System.out.println();

            if (numAFPN <= 0 || numAFPN > automatasActuales.size()) {
                System.out.println("El número ingresado no es válido. Por favor, inténtelo de nuevo.");
                System.out.println();
            } else {
                ingresandoAFPN = false;
                numAFPN--;
            }
        }

        System.out.println("Por favor, ingrese la ruta al archivo que contiene la lista de cadenas");
        String rutaArchivo = input.next();
        System.out.println();

        ArrayList<String> listaCadenas = new ArrayList<>();
                    try {
            File archivo = new File(rutaArchivo);
            Scanner lector = new Scanner(archivo);

            while (lector.hasNextLine()) {
                String lineaActual = lector.nextLine().trim();
                listaCadenas.add(lineaActual);
                        }

            lector.close();
                    } catch (FileNotFoundException e) {
            if (devMode) {
                e.printStackTrace();
                    }
            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();
            System.out.println("Presione enter para continuar");
            try {
                System.in.read();
            } catch (IOException err) {
                err.printStackTrace();
            }
            return;
        }

        System.out.println("Por favor, ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        System.out.println("Desea imprimir los detalles del procesamiento?");
        System.out.println("Por favor, digite 1 para sí y 0 para no:");
        Integer detalles = input.nextInt();
        System.out.println();

        ArrayList<AFPN> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPN afpnBase = listaAutomatas.get(numAFPN);
        afpnBase.procesarListaCadenas(listaCadenas, nombreArchivoSalida, detalles == 1);
        System.out.println();
        if (detalles == 1) {
            System.out.println("Presione enter para continuar");
                    try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
                    }
        }
    }

    private static void exportarAFPN(TreeSet<AFPN> automatasActuales, Scanner input) {

        if (automatasActuales.size() == 0) {
            System.out.print("\033c");
            System.out.println();
            System.out.println("No hay ningún autómata creado. Por favor, primero cree uno antes de usar esta opción.");
            System.out.println();
            try {
                TimeUnit.SECONDS.sleep(segundosEsperaLector);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        System.out.print("\033c");
        System.out.println();
        System.out.println("Exportar AFPN");
        System.out.println();

        if (automatasActuales.size() != 0) {
            Integer numActual = 1;
            System.out.println("Sus AFPN actuales son:");
            System.out.println();

            for (AFPN actual : automatasActuales) {
                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPN() + "\n");
                numActual++;
            }

            System.out.println();
        }

        Boolean ingresandoAFPN = true;
        Integer numAFPN = 0;

        while (ingresandoAFPN) {
            System.out.println("Indique el número del AFPN que desea exportar");
            numAFPN = input.nextInt();
            System.out.println();

            if (numAFPN <= 0 || numAFPN > automatasActuales.size()) {
                System.out.println("El número ingresado no es válido. Por favor, inténtelo de nuevo.");
                System.out.println();
            } else {
                ingresandoAFPN = false;
                numAFPN--;
            }
        }

        System.out.println("Por favor, ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        ArrayList<AFPN> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPN afpnExportado = listaAutomatas.get(numAFPN);
        afpnExportado.exportar(nombreArchivoSalida);
    }
    
    // ** Procesar cadenas
    private static void procesarCadenasAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {
        // Verificar que sí existan
        if (automatasActuales.size() == 0) {
            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");
            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();
            try {
                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        // Setup menu seleccion de tipo de procesamiento
        Boolean procesandoCadenas = true;
        // Seleccion de procesamiento que se repite hasta volver
        while (procesandoCadenas) {
            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");
            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Selección tipo de procesamiento");
            System.out.println();
            if (automatasActuales.size() != 0) {
                Integer numActual = 1;
                System.out.println("Sus AFPD actuales son:");
                System.out.println();
                // Imprimir los AFPD contenidos en automatasActuales
                for (AFPD actual : automatasActuales) {
                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                    numActual++;
                }
                System.out.println();
            }
            // Mostrar las opciones de selección de automata
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar cadena");
            System.out.println("2. Procesar lista de cadenas desde archivo");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();
            // Redirigir a la opcion deseada
            switch (opcion) {
                case 1:
                    procesarCadenaAFPD(automatasActuales, input);
                    break;
                case 2:
                    procesarListaCadenasAFPD(automatasActuales, input);
                    break;
                case 0:
                    procesandoCadenas = false;
                    break;
                default:
                    System.out.println("Opción inválida");
                    try {
                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    // ** Probar AFPD
    private static void probarAFPD(Scanner input) {

        // Setup menu probar AFPD
        Boolean probandoAFPD = true;
        TreeSet<AFPD> automatasActuales = new TreeSet<>();

        // Menu que muestra las opciones para AFPD
        while (probandoAFPD) {

            // Limpiar la consola para que se vea más fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Prueba de AFPD");
            System.out.println();

            // Mostrar los automatas actuales
            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFPD actuales son:");
                System.out.println();

                // Imprimir los AFPD contenidos en automatasActuales
                for (AFPD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones para probar AFPD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Editar AFPD's ");
            System.out.println("2. Procesar cadenas");
            System.out.println("3. Producto cartesiano con AFD");
            System.out.println("4. Exportar AFPD");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    editarAFPD(automatasActuales, input);
                    break;

                case 2:
                    procesarCadenasAFPD(automatasActuales, input);
                    break;

                case 3:
                    productoCartesianoConAFD(automatasActuales, input);
                    break;

                case 4:
                    exportarAFPD(automatasActuales, input);
                    break;

                case 0:
                    probandoAFPD = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    // ** Editar AFPD
    private static void editarAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Setup menu editar AFPD
        Boolean editandoAFPD = true;

        // Menu que muestra las opciones para editar AFD
        while (editandoAFPD) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Edición de AFPD's");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFPD actuales son:");
                System.out.println();

                // Imprimir los AFPD contenidos en automatasActuales
                for (AFPD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones para editar AFPD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Crear AFPD ");
            System.out.println("2. Eliminar AFPD");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    crearAFPD(automatasActuales, input);
                    break;

                case 2:
                    eliminarAFPD(automatasActuales, input);
                    break;

                case 0:
                    editandoAFPD = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    private static void crearAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Creación de AFPD desde archivo");
        System.out.println();

        // Pedir informacion necesaria para crear el AFPD
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene el AFPD");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del AFPD
        Boolean ingresandoNombre = true;
        String nombreAFPD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para el AFPD");
            nombreAFPD = input.next().trim();
            System.out.println();

            // Verificar condición
            if (nombreAFPD.length() <= maxCharLenName && nombreAFPD.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            }

        }

        // Crear el AFPD
        try {

            AFPD nuevoAFPD = new AFPD(rutaArchivo);
            nuevoAFPD.setNombreAFPD(nombreAFPD);
            automatasActuales.add(nuevoAFPD);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }

        }

    }

    static void eliminarAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            System.out.println();
            System.out.println("No hay ningún automata creado, por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Eliminación de AFPD");
        System.out.println();

        // Mostrar automatas creados
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFPD actuales son:");
            System.out.println();

            // Imprimir los AFPD contenidos en automatasActuales
            for (AFPD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Pedir indicacion sobre el AFPD a eliminar
        Boolean ingresandoAFPD = true;
        Integer numAFPD = 0;

        while (ingresandoAFPD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFPD que desea eliminar");
            numAFPD = input.nextInt();
            System.out.println();

            if (numAFPD <= 0 || numAFPD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFPD = false;
                numAFPD--;

            }

        }

        // Eliminar el AFPD
        ArrayList<AFPD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPD afpdEliminado = listaAutomatas.get(numAFPD);
        automatasActuales.remove(afpdEliminado);

    }

    // ** Procesar cadenas
    private static void procesarCadenasAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Setup menu seleccion de tipo de procesamiento
        Boolean procesandoCadenas = true;

        // Seleccion de procesamiento que se repite hasta volver
        while (procesandoCadenas) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            // Espacio al inicio para no saturar e indicar menu
            System.out.println();
            System.out.println("Selección tipo de procesamiento");
            System.out.println();

            if (automatasActuales.size() != 0) {

                Integer numActual = 1;

                System.out.println("Sus AFPD actuales son:");
                System.out.println();

                // Imprimir los AFPD contenidos en automatasActuales
                for (AFPD actual : automatasActuales) {

                    System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                    numActual++;

                }

                System.out.println();

            }

            // Mostrar las opciones de selección de automata
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("seleccione una opción:");
            System.out.println();
            System.out.println("1. Procesar cadena");
            System.out.println("2. Procesar lista de cadenas desde archivo");
            System.out.println("0. Volver");
            System.out.println();
            System.out.print("Ingrese su opción: ");
            Integer opcion = input.nextInt();
            System.out.println();

            // Redirigir a la opcion deseada
            switch (opcion) {

                case 1:
                    procesarCadenaAFPD(automatasActuales, input);
                    break;

                case 2:
                    procesarListaCadenasAFPD(automatasActuales, input);
                    break;

                case 0:
                    procesandoCadenas = false;
                    break;

                default:

                    System.out.println("Opción inválida");

                    try {

                        // Espera que lea el mensaje
                        TimeUnit.SECONDS.sleep(segundosEsperaLector);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                    break;

            }

        }

    }

    private static void procesarCadenaAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de cadena");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFPD actuales son:");
            System.out.println();

            // Imprimir los AFPD contenidos en automatasActuales
            for (AFPD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFPD a usar
        Boolean ingresandoAFPD = true;
        Integer numAFPD = 0;

        while (ingresandoAFPD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFPD a partir del cual quiere procesar");
            System.out.println("la cadena");
            numAFPD = input.nextInt();
            System.out.println();

            if (numAFPD <= 0 || numAFPD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFPD = false;
                numAFPD--;

            }

        }

        // Ingresar la cadena a procesar
        // Pedir info necesaria
        System.out.println("Por favor ingrese la cadena a procesar");
        String cadenaEntrada = input.next();
        System.out.println();

        // Preguntar si desea detalles
        System.out.println("Desea imprimir los detalles del procesamiento?");
        System.out.println("por favor digite 1 para sí y 0 para no:");
        Integer detalles = input.nextInt();
        System.out.println();

        // Procesar la cadena
        ArrayList<AFPD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPD afpdBase = listaAutomatas.get(numAFPD);
        Boolean resultado = afpdBase.procesarCadena(cadenaEntrada, detalles == 1);
        System.out.println();

        // Mostrar el resultado
        if (resultado) {

            System.out.println("La cadena fue aceptada por el AFPD");
            System.out.println();

        } else {

            System.out.println("La cadena no fue aceptada por el AFPD");
            System.out.println();

        }

        // Esperar hasta que presione enter
        System.out.println("Presione enter para continuar");
        try {
            System.in.read(); // Waits for user to press Enter
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void procesarListaCadenasAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Procesamiento de lista de cadenas desde archivo");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFPD actuales son:");
            System.out.println();

            // Imprimir los AFPD contenidos en automatasActuales
            for (AFPD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFPD a usar
        Boolean ingresandoAFPD = true;
        Integer numAFPD = 0;

        while (ingresandoAFPD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFPD a partir del cual quiere procesar");
            System.out.println("la lista de cadenas");
            numAFPD = input.nextInt();
            System.out.println();

            if (numAFPD <= 0 || numAFPD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFPD = false;
                numAFPD--;

            }

        }

        // Ingresar la ruta al archivo
        // Pedir info necesaria
        System.out.println("Por favor ingrese la ruta al archivo que contiene la lista de cadenas");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Obtener cadenas desde el archivo
        ArrayList<String> listaCadenas = new ArrayList<>();
        try {

            // Abrir el archivo
            File archivo = new File(rutaArchivo);
            Scanner lector = new Scanner(archivo);

            // Leer todas las lineas y agregarlas
            while (lector.hasNextLine()) {

                String lineaActual = lector.nextLine().trim();
                listaCadenas.add(lineaActual);

            }

            // Cerrar el lector
            lector.close();

        } catch (FileNotFoundException e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException err) {
                err.printStackTrace();
            }

            return;

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Preguntar si desea detalles
        System.out.println("Desea imprimir los detalles del procesamiento?");
        System.out.println("por favor digite 1 para sí y 0 para no:");
        Integer detalles = input.nextInt();
        System.out.println();

        // Procesar las cadenas
        ArrayList<AFPD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPD afpdBase = listaAutomatas.get(numAFPD);
        afpdBase.procesarListaCadenas(listaCadenas, nombreArchivoSalida, detalles == 1);
        System.out.println();

        // Si pidió detalles, esperar a que los lea
        if (detalles == 1) {

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // * Producto cartesiano con AFD
    private static void productoCartesianoConAFD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Producto cartesiano con AFD: interseccion");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFPD actuales son:");
            System.out.println();

            // Imprimir los AFPD contenidos en automatasActuales
            for (AFPD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFPD a usar
        Boolean ingresandoAFPD = true;
        Integer numAFPD = 0;

        while (ingresandoAFPD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFPD a partir del cual quiere hacer");
            System.out.println("el producto cartesiano");
            numAFPD = input.nextInt();
            System.out.println();

            if (numAFPD <= 0 || numAFPD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFPD = false;
                numAFPD--;

            }

        }

        // Obtener AFPD a usar
        ArrayList<AFPD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPD afpdBase = listaAutomatas.get(numAFPD);

        // Ingresar el AFD a usar
        // Pedir ruta al archivo
        System.out.println("Por favor ingrese la ruta al archivo que contiene el AFD");
        while (!input.hasNext()) {

            // Esperar a que haya un input y luego sí avanzas

        }
        String rutaArchivo = input.next();
        System.out.println();

        // Pedir nombre del AFPD
        Boolean ingresandoNombre = true;
        String nombreAFPD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de " + Integer.toString(maxCharLenName)
                            + " caracteres sin espacios para el nuevo AFPD");
            System.out.println("(Nombre del AFPD resultado del producto cartesiano)");
            nombreAFPD = input.next().trim();
            System.out.println();

            // Verificar condición
            if (nombreAFPD.length() <= maxCharLenName && nombreAFPD.split(" ").length == 1) {

                ingresandoNombre = false;

            } else {

                System.out.println("El nombre ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            }

        }

        // Crear el AFD y hacer el producto cartesiano
        try {

            AFD afdBase = new AFD(rutaArchivo);
            AFPD afpdProductoCartesiano = afpdBase.hallarProductoCartesianoConAFD(afdBase);
            afpdProductoCartesiano.setNombreAFPD(nombreAFPD);
            automatasActuales.add(afpdProductoCartesiano);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println();
            System.out.println("Ocurrió un error al buscar el archivo");
            System.out.println();

            // Esperar hasta que presione enter
            System.out.println("Presione enter para continuar");
            try {
                System.in.read(); // Waits for user to press Enter
            } catch (IOException er) {
                er.printStackTrace();
            }

        }

    }

    // * Exportar AFPD
    private static void exportarAFPD(TreeSet<AFPD> automatasActuales, Scanner input) {

        // Verificar que sí existan
        if (automatasActuales.size() == 0) {

            // Limpiar consola para que se vea mas fancy
            System.out.print("\033c");

            System.out.println();
            System.out.println("No hay ningún automata creado,por favor");
            System.out.println("primero cree uno antes de usar esta opción");
            System.out.println();

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return;

        }

        // Limpiar consola para que se vea mas fancy
        System.out.print("\033c");

        // Espacio al inicio para no saturar e indicar menu
        System.out.println();
        System.out.println("Exportar AFPD");
        System.out.println();

        // Mostrar automatas existentes
        if (automatasActuales.size() != 0) {

            Integer numActual = 1;

            System.out.println("Sus AFPD actuales son:");
            System.out.println();

            // Imprimir los AFPD contenidos en automatasActuales
            for (AFPD actual : automatasActuales) {

                System.out.println(Integer.toString(numActual) + ". " + actual.getNombreAFPD() + "\n");
                numActual++;

            }

            System.out.println();

        }

        // Ingresar el AFPD a exportar
        Boolean ingresandoAFPD = true;
        Integer numAFPD = 0;

        while (ingresandoAFPD) {

            // Pedir info necesaria
            System.out.println("Indique el número del AFPD que desea exportar");
            numAFPD = input.nextInt();
            System.out.println();

            if (numAFPD <= 0 || numAFPD > automatasActuales.size()) {

                System.out.println("El número ingresado no es valido");
                System.out.println("Por favor intentelo de nuevo");
                System.out.println();

            } else {

                ingresandoAFPD = false;
                numAFPD--;

            }

        }

        // Ingresar nombre del archivo de salida
        // Pedir info necesaria
        System.out.println("Por favor ingrese el nombre del archivo de salida");
        String nombreArchivoSalida = input.next();
        System.out.println();

        // Exportar el AFD
        ArrayList<AFPD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFPD afpdExportado = listaAutomatas.get(numAFPD);
        afpdExportado.exportar(nombreArchivoSalida);

    }

}
