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
            System.out.println("Numero de casos con el mismo resultado: " + contadorIguales);
            System.out.println("Numero de casos con resultados distintos: " + contadorDistintos);
            if (contadorDistintos > 0){
                System.out.println("Cadenas que generan resultados distintos:");
                System.out.println(cadenasDiferencia);
            }
            System.out.println("\n\n");


        }
        

    }

    static void validarAFNLambdaToAFN() {

        // TODO
        // * la clase alfabeto puede generar cadenas aleatorias, por fa tenganlo en
        // cuenta

    }

}
