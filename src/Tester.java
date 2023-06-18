import java.util.*;

import Logic.AFN;
import Logic.Alfabeto;
import Logic.AFD;
import Logic.ClaseValidacion;
import Logic.AF2P;

public class Tester {
    public static void main(String[] args) throws Exception {
        System.setProperty("console.encoding", "UTF-8");

        Scanner scanner = new Scanner(System.in);
        //System.out.println("Ingrese el nombre del archivo de entrada:");
        //String nombreArchivoEntrada = scanner.nextLine();

        //AFN automata = new AFN(nombreArchivoEntrada);
        //AFN automata = new AFN("C:\\Users\\jjezc\\Documents\\GitHub\\automatons\\Taller-2-ITC-automatas\\src\\testerfile.nfa");
        //System.out.println(automata.delta("q5", 'b'));
        //System.out.println("A");
        //System.out.println(automata.procesarCadenaConDetalles("abca"));
        //System.out.println(automata.toString());
        //automata.imprimirAFNSimplificado();
        //automata.exportar("Cocuyo");
        //AFD automata2 = automata.AFNtoAFD(automata);

        //System.out.println("AA");
        //System.out.println(automata.computarTodosLosProcesamientos("ab", "pop"));

        //String[] asd = {"abc", "abca", "a", "b"};
        //automata.procesarListaCadenas(asd, "asdo!", true);
        
        //System.out.println(automata.procesarCadenaConversion(""));
        //System.out.println(automata.procesarCadenaConDetallesConversion("b"));
        
        //automata.procesarListaCadenasConversion(asd, "asda", true);
        //AFD automata2 = automata.AFNtoAFD(automata);
        //automata2.exportar("afd1");

        //AFD automata3 = new AFD("C:\\Users\\jjezc\\Documents\\GitHub\\automatons\\afd1.afd");
        //System.out.println(automata3.toString());
        //automata3.procesarListaCadenas(Arrays.asList(asd), "asda", true);

        //automata.computarTodosLosProcesamientos("abca", "pop");
        //System.out.println(automata.getAlfabeto().generarCadenas(10));

        //Collection<AFN> listaAFNs = new ArrayDeque<AFN>();
        //listaAFNs.add(automata);
        //ClaseValidacion.validarAFNtoAFD(listaAFNs);

        // Test creacion, adicion a delta y lectura delta must be public
        Alfabeto alf = new Alfabeto("ab");
        Alfabeto alfG = new Alfabeto("ab");
        HashSet<String> Q = new HashSet<>();
        HashSet<String> F = new HashSet<>();
        Q.add("q0");
        Q.add("q1");
        F.add("q1");
        HashMap<String, HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>>> delta = new HashMap<String, HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>>>();
        AF2P stacker = new AF2P(alf, alfG, Q, "q0", F, delta);
        stacker.addTransition("q0", 'a', '$', '$', "q0", 'a', '$');
        //HashSet<String[]> img = stacker.delta.get("q0").get('a').get('$').get('$');
        //for (String[] lista : img) {
        //    System.out.println(lista[0] + lista[1] + lista[2]);
        //}
        // fin test
    }
}
