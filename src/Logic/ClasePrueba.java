package Logic;

import java.io.*;
import java.util.*;

public class ClasePrueba {

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
            System.out.println("Menu de selección de automata");
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
                    break;

            }

        }

    }

    // Menu para elegir que conversion probar
    private static void probarConversiones(Scanner input) {

        // TODO

    }

    // Probar automatas
    private static void probarAFD(Scanner input) {

        // TODO

    }

    private static void probarAFN(Scanner input) {

        // TODO

    }

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
