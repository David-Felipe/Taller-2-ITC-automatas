package Logic;
import java.util.*;

public class ClaseValidacion {

    public static void validarAFNtoAFD(Collection<AFN> listaAFNs) {

        int contadorAutomatas = 1;
        for (AFN afn : listaAFNs) {

            AFD afd = AFN.AFNtoAFD(afn);
            int contadorIguales = 0;
            int contadorDistintos = 0;
            Collection<String> cadenasDiferencia = new ArrayDeque<String>();

            Collection<String> cadenas = afn.getAlfabeto().generarCadenas(500);

            for (String cadena : cadenas) {

                Boolean esAceptada1 = afn.procesarCadena(cadena);
                Boolean esAceptada2 = afd.procesarCadena(cadena);

                if (esAceptada1 == esAceptada2){
                    contadorIguales++;
                } else {
                    contadorDistintos++;
                    cadenasDiferencia.add(cadena);
                }

            }
            System.out.println("Automata " + contadorAutomatas + ": \n");
            System.out.println("Numero de casos con el mismo resultado: " + contadorIguales + "\n");
            System.out.println("Numero de casos con resultados distintos: " + contadorDistintos + "\n");
            if (contadorDistintos > 0){
                System.out.println("Cadenas que generan resultados distintos:");
                if (cadenasDiferencia.isEmpty()){
                    System.out.println("Ninguna.\n");
                } else {
                    for (String cadena : cadenasDiferencia) {
                        System.out.println(cadena);
                    }
                    System.out.println();
                }
            }
            System.out.println("\n\n");
            contadorAutomatas++;

        }
        

    }

    static void validarAFNLambdaToAFN() {

        // TODO
        // * la clase alfabeto puede generar cadenas aleatorias, por fa tenganlo en
        // cuenta

    }

}
