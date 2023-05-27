package Logic;

// Paquetes propios de java
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

// Paquetes creados por nosotros
import Logic.AFD;

public class ClasePrueba {

    static Integer segundosEsperaLector = 5;
    static Boolean devMode = false;
    static Integer maxCharLenName = 15;

    // Funcion de control, menu inicial
    public static void main() {

        // Setup para el main()
        Boolean probando = true;
        Scanner input = new Scanner(System.in, "utf-8");

        // Espacio al inicio para no saturar
        System.out.println();
        System.out.println();
        // Mensaje de bienvenida
        System.out.println("Bienvenido al programa de prueba de automatas");

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
            System.out.println("Seleccione una opción:");
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
            System.out.println("Seleccione una opción:");
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

                }

                System.out.println();

                numActual++;

            }

            // Mostrar las opciones para probar AFD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("Seleccione una opción:");
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
                    procesarCadenas(automatasActuales, input);
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

                }

                System.out.println();

                numActual++;

            }

            // Mostrar las opciones para editar AFD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("Seleccione una opción:");
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

                }

                System.out.println();

                numActual++;

            }

            // Mostrar las opciones para crear AFD
            System.out.println("Presionando el entero seguido de la tecla enter, por favor");
            System.out.println("Seleccione una opción:");
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
        String rutaArchivo = input.nextLine();
        System.out.println();

        // Pedir nombre del AFD
        Boolean ingresandoNombre = true;
        String nombreAFD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de maxCharLenName caracteres sin espacios para el AFD");
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
        try {

            AFD nuevoAFD = new AFD(rutaArchivo);
            nuevoAFD.setNombreAFD(nombreAFD);
            automatasActuales.add(nuevoAFD);

        } catch (Exception e) {

            if (devMode) {

                e.printStackTrace();

            }

            System.out.println("Ocurrio un error al buscar el archivo");

            try {

                // Espera que lea el mensaje
                TimeUnit.SECONDS.sleep(segundosEsperaLector);

            } catch (InterruptedException intrrptd) {

                intrrptd.printStackTrace();

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

            }

            System.out.println();

            numActual++;

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

            }

        }

        // Pedir nombre del AFD
        Boolean ingresandoNombre = true;
        String nombreAFD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de maxCharLenName caracteres sin espacios para el AFD");
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
        automatasActuales.add(afdBase);

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

            }

            System.out.println();

            numActual++;

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

            }

        }

        // Pedir nombre del AFD
        Boolean ingresandoNombre = true;
        String nombreAFD = "";
        while (ingresandoNombre) {

            System.out.println(
                    "Por favor ingrese un nombre de menos de maxCharLenName caracteres sin espacios para el AFD");
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
        automatasActuales.add(afdBase);

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

            }

            System.out.println();

            numActual++;

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

            }

        }

        // Eliminar el AFD
        ArrayList<AFD> listaAutomatas = new ArrayList<>(automatasActuales);
        AFD afdEliminado = listaAutomatas.get(numAFD);
        automatasActuales.remove(afdEliminado);

    }

    private static void procesarCadenas(TreeSet<AFD> automatasActuales, Scanner input) {

        // TODO

    }

    private static void productoCartesiano(TreeSet<AFD> automatasActuales, Scanner input) {

        // TODO

    }

    private static void exportarAFD(TreeSet<AFD> automatasActuales, Scanner input) {

        // TODO

    }

    // * Probar AFN
    private static void probarAFN(Scanner input) {

        // TODO

    }

    // * Probar AFN Lambda
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

    // Probar operaciones sobre AFD
    private static void probarComplemento() {

        // TODO

    }

    private static void probarProductoCartesiano() {

        // TODO

    }

    private static void probarSimplificacion() {

        // TODO

    }

}
