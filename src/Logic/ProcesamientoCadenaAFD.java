package Logic;

import java.util.*;

public class ProcesamientoCadenaAFD {

    private String cadena;
    private Boolean esAceptada;
    private LinkedList<PasoComputacional> pasosComputacionales;
    Character[] cadenaSeparada;
    private AFD afdProcesador;

    ProcesamientoCadenaAFD(String cadena, AFD afdProcesador) {
        this.cadena = cadena;
        this.afdProcesador = afdProcesador;
        this.pasosComputacionales = new LinkedList<>();
        this.cadenaSeparada = cadena.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
    }

    public class PasoComputacional {

        private String estadoActual;
        private Character simboloLeido;
        private String estadoSiguiente;

        PasoComputacional(String estadoActual, Character simboloLeido, String estadoSiguiente) {
            this.estadoActual = estadoActual;
            this.simboloLeido = simboloLeido;
            this.estadoSiguiente = estadoSiguiente;
        }

        public String getEstadoActual() {
            return estadoActual;
        }

        public Character getSimboloLeido() {
            return simboloLeido;
        }

        public String getEstadoSiguiente() {
            return estadoSiguiente;
        }

    }

    AFD getAfdProcesador() {
        return afdProcesador;
    }

    void agregraPasoComputacional(String estadoActual, Character simboloLeido, String estadoSiguiente) {

        pasosComputacionales.add(new PasoComputacional(estadoActual, simboloLeido, estadoSiguiente));

    }

    void setEsAceptada(Boolean esAceptada) {
        this.esAceptada = esAceptada;
    }

    Boolean getEsAceptada() {

        return esAceptada;

    }

    public String getCadena() {
        return cadena;
    }

}
