package Logic;

import java.util.*;

public class Alfabeto {

    // Atributos de la clase alfabeto
    private NavigableSet<Character> simbolos;
    private Random rand = new Random();

    // Constructor de la clase alfabeto para conjuntos
    public Alfabeto(Set<Character> simbolos) {

        // Convertimos simbolos a un conjunto
        this.simbolos = new TreeSet<Character>(simbolos);
        // Verificamos que lambda no esté en el alfabeto
        isLambdaHere();

    }

    // Constructor de la clase alfabeto para arreglos de caracteres
    public Alfabeto(Character[] simbolos) {

        // Convertimos simbolos a un conjunto
        this.simbolos = new TreeSet<Character>(Arrays.asList(simbolos));
        // Verificamos que lambda no esté en el alfabeto
        isLambdaHere();

    }

    // Constructor de la clase alfabeto para cadenas de caracteres
    public Alfabeto(String simbolos) {

        // Convertimos simbolos a un arreglo de caracteres y luego a un conjunto
        Character[] simbolosArray = simbolos.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        Collection<Character> simbolosCollection = Arrays.asList(simbolosArray);
        this.simbolos = new TreeSet<Character>(simbolosCollection);
        // Verificamos que lambda no esté en el alfabeto
        isLambdaHere();

    }

    // Metodo para verificar si un simbolos está en el alfabeto
    public boolean contiene(Character simbolo) {

        // Regresamos si el simbolo está en el alfabeto
        return simbolos.contains(simbolo);

    }

    // Método para verificar si una cadena está en el alfabeto
    public boolean contiene(String cadena) {

        // Convertimos la cadena a un arreglo de caracteres
        Character[] cadenaArray = cadena.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        Collection<Character> cadenaCollection = Arrays.asList(cadenaArray);

        // Regresamos si la cadena está en el alfabeto
        return simbolos.containsAll(cadenaCollection);

    }

    // Getter del alfabeto
    public NavigableSet<Character> getAlfabeto() {

        // Regresamos el alfabeto
        return simbolos;

    }

    // Método para generar una cadena aleatoria a partir del contenido de simbolos
    public String generarCadena2(int longitud) {

        // Creamos un objeto de tipo StringBuilder
        StringBuilder cadena = new StringBuilder();

        // Generamos una cadena aleatoria de longitud "longitud"
        for (int i = 0; i < longitud; i++) {

            Character simbolo = ' ';

            // Hacer que las chances de floor o ceiling sean las mismas
            if (Math.random() < 0.5) {

                // Obtenemos un símbolo aleatorio de simbolos con floor
                simbolo = (char) simbolos.floor((char) (simbolos.first().hashCode()
                        + Math.random() * (simbolos.last().hashCode() - simbolos.first().hashCode())));

            } else {

                // Obtenemos un símbolo aleatorio de simbolos con ceiling
                simbolo = (char) simbolos.ceiling((char) (simbolos.first().hashCode()
                        + Math.random() * (simbolos.last().hashCode() - simbolos.first().hashCode())));

            }

            // Agregamos el símbolo a la cadena
            cadena.append(simbolo);

        }

        // Regresamos la cadena generada
        return cadena.toString();

    }

    // Método para generar una cadena aleatoria a partir del contenido de simbolos
    public String generarCadena(int longitud) {

        // Creamos un objeto de tipo StringBuilder
        StringBuilder cadena = new StringBuilder();

        if (longitud == 0) {
            return "$";
        }

        // Generamos una cadena aleatoria de longitud "longitud"
        for (int i = 0; i < longitud; i++) {

            // Tomamos un símbolo aleatorio del alfabeto
            Character simbolo = ' ';

            int size = this.simbolos.size();
            int randInt = rand.nextInt(size);
            int iterInt = 0;
            for (Character caracter : this.simbolos) {
                if (iterInt == randInt) {
                    simbolo = caracter;
                    break;
                }
                iterInt++;
            }

            // Agregamos el símbolo a la cadena
            cadena.append(simbolo);

        }

        // Regresamos la cadena generada
        return cadena.toString();

    }

    // Método para generar cantidad cadenas aleatorias a partir del contenido de
    // simbolos
    // y de lungitud aleatoria menor estricta que maxLen
    public Collection<String> generarCadenas(int cantidad, int maxLen) {

        Collection<String> listaCadenas = new ArrayDeque<String>();
        listaCadenas.add("$");

        for (int i = 1; i < cantidad; i++) {

            int randInt = rand.nextInt(1, maxLen);
            String cadena = this.generarCadena(randInt);
            listaCadenas.add(cadena);

        }
        return listaCadenas;
    }

    public Collection<String> generarCadenas(int cantidad) {

        int size = this.simbolos.size();
        if (size == 1) {
            return this.generarCadenas(cantidad, 50);
        }
        int maxLen = (int) Math.ceil(Math.log(500) / Math.log(size));
        return this.generarCadenas(cantidad, maxLen);
    }

    // Método para verificar que el caracter '$' no esté en el alfabeto
    private void isLambdaHere() {

        // Verificamos que el caracter '$' no esté en el alfabeto
        if (simbolos.contains('$')) {

            // Si está, lanzamos una excepción
            throw new IllegalArgumentException("El caracter '$' no puede estar en el alfabeto");

        }

    }

    public Boolean contains(char simbolo) {
        return this.simbolos.contains(simbolo);
    }

    // Metodo para retornar el tamaño del alfabeto
    public int size() {
        return this.simbolos.size();
    }

}
