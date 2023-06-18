package Logic;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
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
        System.out.println(this.estadosInaccesibles);
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
}