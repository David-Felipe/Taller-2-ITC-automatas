import java.util.*;

public class Alfabeto {

    // Atributos de la clase alfabeto
    public NavigableSet<Character> simbolos;

    // Constructor de la clase alfabeto para arreglos de caracteres
    public Alfabeto(Character[] simbolos) {

        // Convertimos simbolos a un conjunto
        this.simbolos = new TreeSet<Character>(Arrays.asList(simbolos));

    }

    // Constructor de la clase alfabeto para cadenas de caracteres
    public Alfabeto(String simbolos) {

        // Convertimos simbolos a un arreglo de caracteres y luego a un conjunto
        Character[] simbolosArray = simbolos.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        Collection<Character> simbolosCollection = Arrays.asList(simbolosArray);
        this.simbolos = new TreeSet<Character>(simbolosCollection);

    }

    // Método para generar cadenas aleatorias a partir del contenido de simbolos
    public String generarCadena(int longitud) {

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

}
