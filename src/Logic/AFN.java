package Logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AFN {
    private Set<Character> alphabet;
    private Set<String> states;
    private String initialState;
    private Set<String> acceptingStates;
    private Map<String, Map<Character, Set<String>>> transitionFunction;

    public AFN(Set<Character> alphabet, Set<String> states, String initialState, Set<String> acceptingStates,
            Map<String, Map<Character, Set<String>>> transitionFunction) {
        this.alphabet = alphabet;
        this.states = states;
        this.initialState = initialState;
        this.acceptingStates = acceptingStates;
        this.transitionFunction = transitionFunction;
    }

    public void exportar(String archivo) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
        writer.write("#!nfe");
        writer.newLine();
        writer.write("#alphabet");
        writer.newLine();
        for (char symbol : alphabet) {
            writer.write(Character.toString(symbol));
            writer.newLine();
        }
        writer.write("#states");
        writer.newLine();
        for (String state : states) {
            writer.write(state);
            writer.newLine();
        }
        writer.write("#initial");
        writer.newLine();
        writer.write(initialState);
        writer.newLine();
        writer.write("#accepting");
        writer.newLine();
        for (String acceptingState : acceptingStates) {
            writer.write(acceptingState);
            writer.newLine();
        }
        writer.write("#transitions");
        writer.newLine();
        for (String fromState : transitionFunction.keySet()) {
            Map<Character, Set<String>> transitions = transitionFunction.get(fromState);
            for (Character symbol : transitions.keySet()) {
                Set<String> toStates = transitions.get(symbol);
                String toStatesStr = String.join(",", toStates);
                String transitionStr = String.format("%s:%c>%s", fromState, symbol, toStatesStr);
                writer.write(transitionStr);
                writer.newLine();
            }
        }
        writer.close();
    }
}