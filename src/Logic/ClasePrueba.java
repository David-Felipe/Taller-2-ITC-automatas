package Logic;

// Paquetes propios de java
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// Paquetes creados por nosotros
import Logic.AFD;

public class ClasePrueba {

    static Integer segundosEsperaLector = 3;
    static Boolean devMode = true;
    static Integer maxCharLenName = 15;

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
                    probarAFNLambda(input);
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

        // TODO

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

    static void eliminarAFD(TreeSet<AFD> automatasActuales, Scanner input) {

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
        System.out.println("Selección de operación Producto Cartesiano");
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

        // TODO

    }

    // ** Probar AFN Lambda
    private static void probarAFNLambda(Scanner input) {

        // TODO

    }

    // Probar conversiones
    private static void probarAFNtoAFD() {

        // TODO

    }

    private static void probarAFNLambdaToAFN() {

        // TODO

    }

    private static void probarAFNLambdaToAFD() {

        // TODO

    }

}
