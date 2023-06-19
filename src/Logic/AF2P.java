package Logic;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.*;
import java.util.*;
import java.io.*;

public class AF2P {

    // Atributos clase AF2P
    private Alfabeto sigma;
    private Alfabeto gama;
    private HashSet<String> Q;
    private String q0;
    private HashSet<String> F;
    private HashSet<String> estadosInaccesibles;
    private HashMap<String, HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>>> delta;
    private String procesamientoAceptacion = null;
    private String procesamientoRechazo = null;
    private HashSet<String> procesamientos = null;
    private HashSet<String> procesamientosAceptacion = null;
    private HashSet<String> procesamientosRechazo = null;
    private int numPasosProcesamientoAceptacion;
    private int numPasosProcesamientoRechazo;
    private Boolean esAceptado = false;
    private Stack<Character> pila1 = new Stack<Character>();
    private Stack<Character> pila2 = new Stack<Character>();
    private int numProcesamientos = 0;


    // Constructores de la clase AFN
    // Constructor de la clase AFN para conjuntos
    public AF2P(Alfabeto sigma, Alfabeto gama, HashSet<String> Q, String q0, HashSet<String> F,
            HashMap<String, HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>>> delta) {
        this.sigma = sigma;
        this.gama = gama;
        this.Q = Q;
        this.q0 = q0;
        this.F = F;
        this.delta = delta;
        this.estadosInaccesibles = new HashSet<>();

        // Hallar estados inasequibles y guardarlos
        this.hallarEstadosInaccesibles();
    }

    // Hallar los estados inasequibles del automata y guardarlos en
    // "this.estadosInasequibles"
    private void hallarEstadosInaccesibles() {
        HashSet<String> estados = new HashSet<String>();
        estados.add(this.q0);
        int oldSizeEstados = 0;

        NavigableSet<Character> alfabeto = this.sigma.getAlfabeto();
        NavigableSet<Character> alfabetoStack = this.gama.getAlfabeto();
        while (oldSizeEstados != estados.size()) {
            oldSizeEstados = estados.size();
            Iterator<String> iterator = estados.iterator();
            HashSet<String> copyEstados = new HashSet<String>();
            copyEstados.addAll(estados);
            while (iterator.hasNext()) {
                String estado = iterator.next();
                for (Character simbolo : alfabeto) {
                    for (Character top1 : alfabetoStack) {
                        for (Character top2 : alfabetoStack) {
                            HashSet<String[]> imagen = this.delta(estado, simbolo, top1, top2);

                            // Agregar cada estado de llegada (elemento[0] para todo elemento en imagen)
                            if (imagen != null) {
                                for (String[] elemento : imagen) {
                                    copyEstados.add(elemento[0]);
                                }
                            }
                        }
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
    public HashSet<String[]> delta(String estado, Character simbolo, Character top1, Character top2) {
        if (this.Q.contains(estado) && 
        (this.sigma.contains(simbolo) || simbolo == '$') && 
        (this.gama.contains(top1) || top1 == '$') &&
        (this.gama.contains(top2) || top2 == '$') &&
        this.delta.get(estado) != null &&
        this.delta.get(estado).get(simbolo) != null &&
        this.delta.get(estado).get(simbolo).get(top1) != null) {
            return this.delta.get(estado).get(simbolo).get(top1).get(top2);
        } else {
            return null;
        }
    }

    public void addTransition(String fromState, Character symbol, Character fromTop1, Character fromTop2, String toState,  Character toTop1, Character toTop2) {
        if (this.Q.contains(fromState) && 
        this.Q.contains(toState) && 
        (this.sigma.contains(symbol) || symbol == '$') &&
        (this.gama.contains(fromTop1) || fromTop1 == '$') &&
        (this.gama.contains(fromTop2) || fromTop2 == '$') &&
        (this.gama.contains(toTop1) || toTop1 == '$') &&
        (this.gama.contains(toTop2) || toTop2 == '$')) {
            String[] toConfig = {toState, String.valueOf(toTop1), String.valueOf(toTop2)};
            HashMap<Character, HashMap<Character, HashMap<Character, HashSet<String[]>>>> stateTransitions = this.delta.computeIfAbsent(fromState,
                    k -> new HashMap<>());
            HashMap<Character, HashMap<Character, HashSet<String[]>>> toStatesTop1 = stateTransitions.computeIfAbsent(symbol,
                    k -> new HashMap<>());
            HashMap<Character, HashSet<String[]>> toStatesTop2 = toStatesTop1.computeIfAbsent(fromTop1,
                    k -> new HashMap<>());
            HashSet<String[]> toStates = toStatesTop2.computeIfAbsent(fromTop2,
                    k -> new HashSet<>());
            toStates.add(toConfig);
        }
    }

    public void modificarPila(Stack<Character> pila, String operacion, String newTop) {
        CharacterIterator iterator = new StringCharacterIterator(newTop);
        switch (operacion){
            case "insertar":
                while (iterator.current() != CharacterIterator.DONE) {
                    Character nextChar = iterator.current();
                    if (!nextChar.equals('$')) {
                        pila.push(iterator.current());
                    }
                    iterator.next();
                }
                break;
            case "reemplazar":
                if (!pila.empty()) {
                    pila.pop();
                    while (iterator.current() != CharacterIterator.DONE) {
                        pila.push(iterator.current());
                        iterator.next();
                    }
                }
                break;
            case "eliminar":
                if (!pila.empty()) {
                    pila.pop();
                }
                break;
            default:
                System.out.println("La operacion para pilas \"" + operacion + "\" no esta definida.");
        }
    }

    public void modificarPila(Stack<Character> pila, String operacion) {
        if (!pila.empty()) {
            pila.pop();
        }
    }

    public void preprocesarCadena(String estado, String cadena, Stack<Character> pila1, Stack<Character> pila2, String str, int numPasos) {
        if (cadena.isEmpty()) {
            
            this.numProcesamientos++;

            if (pila1.empty() && pila2.empty() && this.F.contains(estado)){
                this.esAceptado = true;
                str += ">>accepted";
                this.procesamientosAceptacion.add(str);
                this.procesamientos.add(str);
                if (this.numPasosProcesamientoAceptacion < 0) {
                    this.numPasosProcesamientoAceptacion = numPasos;
                    this.procesamientoAceptacion = str;
                } else {
                    if (this.numPasosProcesamientoAceptacion > numPasos) {
                        this.numPasosProcesamientoAceptacion = numPasos;
                        this.procesamientoAceptacion = str;
                    }
                }
            } else {
                str += ">>rejected";
                this.procesamientosRechazo.add(str);
                this.procesamientos.add(str);
                if (this.numPasosProcesamientoRechazo < 0) {
                    this.numPasosProcesamientoRechazo = numPasos;
                    this.procesamientoRechazo = str;
                } else {
                    if (this.numPasosProcesamientoRechazo > numPasos) {
                        this.numPasosProcesamientoRechazo = numPasos;
                        this.procesamientoRechazo = str;
                    }
                }
            }
            
            
        } else {

            List<Character> possibleTops1 = new ArrayList<Character>();
            possibleTops1.add('$');
            List<Character> possibleTops2 = new ArrayList<Character>();
            possibleTops2.add('$');
            
            if (!pila1.empty()) {
                possibleTops1.add(pila1.peek());
            } 
            if (!pila2.empty()) {
                possibleTops2.add(pila2.peek());
            }
            Boolean abortado = true;
            for (Character top1 : possibleTops1) {
                for (Character top2 : possibleTops2) {
                    if (this.delta.get(estado) != null &&
                        this.delta.get(estado).get(cadena.charAt(0)) != null &&
                        this.delta.get(estado).get(cadena.charAt(0)).get(top1) != null) {
                            
                            HashSet<String[]> imagenes = this.delta.get(estado).get(cadena.charAt(0)).get(top1).get(top2);

                            if (imagenes != null){

                                abortado = false;

                                for (String[] imagen : imagenes) {
                                    Stack<Character> copiaPila1 = new Stack<Character>();
                                    Stack<Character> copiaPila2 = new Stack<Character>();
                                    copiaPila1.addAll(pila1);
                                    copiaPila2.addAll(pila2);

                                    // Actualizar pila 1
                                    if (top1.equals('$')) {
                                        if (!imagen[1].equals("$")) {
                                            this.modificarPila(copiaPila1, "insertar", imagen[1]);
                                        }
                                    } else {
                                        if (imagen[1].equals("$")) {
                                            this.modificarPila(copiaPila1, "eliminar");
                                        } else {
                                            this.modificarPila(copiaPila1, "reemplazar", imagen[1]);
                                        }
                                    }
                                    
                                    // Actualizar pila 2
                                    if (top2.equals('$')) {
                                        if (!imagen[2].equals("$")) {
                                            this.modificarPila(copiaPila2, "insertar", imagen[2]);
                                        }
                                    } else {
                                        if (imagen[2].equals("$")) {
                                            this.modificarPila(copiaPila2, "eliminar");
                                        } else {
                                            this.modificarPila(copiaPila2, "reemplazar", imagen[2]);
                                        }
                                    }
                                    
                                    String newCadena = cadena.substring(1);

                                    String strPila1 = copiaPila1.toString().replaceAll(", |\\[|\\]","");
                                    
                                    strPila1 = new StringBuilder(strPila1).reverse().toString();
                                    

                                    String strPila2 = copiaPila2.toString().replaceAll(", |\\[|\\]","");
                                    strPila2 = new StringBuilder(strPila2).reverse().toString();
                                    
                                    String newStr = str + "->(" + imagen[0] + ",";
                                    if (newCadena.isEmpty()) {
                                        newStr += "$,";
                                    } else {
                                        newStr += newCadena + ",";
                                    }
                                    if (strPila1.isEmpty()) { 
                                        newStr += "$,";
                                    } else {
                                        newStr += strPila1 + ",";
                                    }
                                    if (strPila2.isEmpty()) { 
                                        newStr += "$)";
                                    } else {
                                        newStr += strPila2 + ")";
                                    }
                                    

                                    preprocesarCadena(imagen[0], 
                                                    newCadena, 
                                                    copiaPila1, 
                                                    copiaPila2,
                                                    newStr,
                                                    numPasos + 1);
                                }
                            }
                        }
                }
            }
            

            if (abortado){
                this.numProcesamientos++;
                str += ">>aborted";
                this.procesamientosRechazo.add(str);
                this.procesamientos.add(str);
                if (this.numPasosProcesamientoRechazo < 0) {
                    this.numPasosProcesamientoRechazo = numPasos;
                    this.procesamientoRechazo = str;
                } else {
                    if (this.numPasosProcesamientoRechazo > numPasos) {
                        this.numPasosProcesamientoRechazo = numPasos;
                        this.procesamientoRechazo = str;
                    }
                }
            }
        }

        List<Character> possibleTops1 = new ArrayList<Character>();
        possibleTops1.add('$');
        List<Character> possibleTops2 = new ArrayList<Character>();
        possibleTops2.add('$');
        
        if (!pila1.empty()) {
            possibleTops1.add(pila1.peek());
        } 
        if (!pila2.empty()) {
            possibleTops2.add(pila2.peek());
        }
        for (Character top1 : possibleTops1) {
            for (Character top2 : possibleTops2) {
                
                if (this.delta.get(estado) != null &&
                this.delta.get(estado).get('$') != null &&
                this.delta.get(estado).get('$').get(top1) != null) {

                    HashSet<String[]> imagenes = this.delta.get(estado).get('$').get(top1).get(top2);
                    
                    if (imagenes != null){

                        for (String[] imagen : imagenes) {
                            Stack<Character> copiaPila1 = new Stack<Character>();
                            Stack<Character> copiaPila2 = new Stack<Character>();
                            copiaPila1.addAll(pila1);
                            copiaPila2.addAll(pila2);

                            // Actualizar pila 1
                            if (top1.equals('$')) {
                                if (!imagen[1].equals("$")) {
                                    this.modificarPila(copiaPila1, "insertar", imagen[1]);
                                }
                            } else {
                                if (imagen[1].equals("$")) {
                                    this.modificarPila(copiaPila1, "eliminar");
                                } else {
                                    this.modificarPila(copiaPila1, "reemplazar", imagen[1]);
                                }
                            }
                            
                            // Actualizar pila 2
                            if (top2.equals('$')) {
                                if (!imagen[2].equals("$")) {
                                    this.modificarPila(copiaPila2, "insertar", imagen[2]);
                                }
                            } else {
                                if (imagen[2].equals("$")) {
                                    this.modificarPila(copiaPila2, "eliminar");
                                } else {
                                    this.modificarPila(copiaPila2, "reemplazar", imagen[2]);
                                }
                            }

                            String strPila1 = copiaPila1.toString().replaceAll(", |\\[|\\]","");
                            strPila1 = new StringBuilder(strPila1).reverse().toString();
                            String strPila2 = copiaPila2.toString().replaceAll(", |\\[|\\]","");
                            strPila2 = new StringBuilder(strPila2).reverse().toString();
                            String newStr = str + "->(" + imagen[0] + "," + cadena + ",";
                            if (strPila1.isEmpty()) { 
                                newStr += "$,";
                            } else {
                                newStr += strPila1 + ",";
                            }
                            if (strPila2.isEmpty()) { 
                                newStr += "$)";
                            } else {
                                newStr += strPila2 + ")";
                            }

                            preprocesarCadena(imagen[0], 
                                            cadena, 
                                            copiaPila1, 
                                            copiaPila2,
                                            newStr,
                                            numPasos + 1);
                            
                        }
                    }
                    
                }
            }
        }
    }

    public void preprocesarCadena(String cadena) {
        this.procesamientoAceptacion = "";
        this.procesamientoRechazo = "";
        this.numPasosProcesamientoAceptacion = -1;
        this.numPasosProcesamientoRechazo = -1;
        this.procesamientos = new HashSet<String>();
        this.procesamientosAceptacion = new HashSet<String>();
        this.procesamientosRechazo = new HashSet<String>();
        this.esAceptado = false;
        this.numProcesamientos = 0;
        String str = "(" + this.q0 + "," + cadena + ",$,$)";
        preprocesarCadena(this.q0, cadena, this.pila1, this.pila2, str, 0);
        System.out.println(this.procesamientoAceptacion);
        System.out.println(this.procesamientoRechazo);
        System.out.println(this.procesamientos);
        System.out.println(this.procesamientosAceptacion);
        System.out.println(this.procesamientosRechazo);
        System.out.println(this.numPasosProcesamientoAceptacion);
        System.out.println(this.numPasosProcesamientoRechazo);
        System.out.println(this.esAceptado);
        System.out.println(this.numProcesamientos);

    }
}