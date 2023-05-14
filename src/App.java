import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Scanner;
import javax.print.event.PrintEvent;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        try {
            AFNLambda automata = new AFNLambda(args[0]);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            boolean salir = false;

            while (!salir) {
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

                try {
                    String opcion = reader.readLine();
                    switch (opcion) {
                        case "1":
                            calcularYMostrarLambdaClausura(automata);
                            break;
                        case "2":
                            determinarYMostrarEstadosInaccesibles(automata);
                            break;
                        case "3":
                            imprimirAFNLSimplificado(automata);
                            break;
                        case "4":
                            exportarAutomata(automata);
                            break;
                        case "5":
                            procesarCadena(automata);
                            break;
                        case "6":
                            procesarCadenaConDetalles(automata);
                            break;
                        case "7":
                            computarTodosLosProcesamientos(automata);
                            break;
                        case "8":
                            procesarListaCadenasConDetalles(automata);
                            break;
                        case "9":
                            System.out.println("Ingrese el nombre del archivo de entrada (AFN-λ):");
                            String nombreArchivoEntrada = reader.readLine();
                            // Crear instancia de AFNLambda a partir del archivo de entrada
                            AFNLambda afnLambda = new AFNLambda(nombreArchivoEntrada);
                            // Convertir el AFN-λ a AFN utilizando el método AFN_LambdaToAFN()
                            AFN afn = afnLambda.AFN_LambdaToAFN(afnLambda);
                            System.out.println("Ingrese el nombre del archivo de salida (AFN):");
                            String nombreArchivoSalida = reader.readLine();
                            // Exportar el autómata AFN a un archivo
                            afn.exportar(nombreArchivoSalida);
                            System.out.println("El autómata se ha exportado correctamente.");
                            break;
                        case "exit":
                            salir = true;
                            break;
                        default:
                            System.out.println("Opción inválida. Intente nuevamente.");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
            e.printStackTrace();
        }
    }

    private static void calcularYMostrarLambdaClausura(AFNLambda automata) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el estado para calcular la λ-Clausura:");
        String estado = reader.readLine();
        List<String> lambdaClausura = new ArrayList<>(automata.calcularLambdaClausura(estado));
        System.out.println("λ-Clausura de " + estado + ": " + lambdaClausura);
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
}
