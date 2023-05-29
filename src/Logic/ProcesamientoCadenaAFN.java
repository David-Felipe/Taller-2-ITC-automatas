package Logic;

import java.util.*;

public class ProcesamientoCadenaAFN {

    private String cadena;
    private AFN afn;

    private Boolean esAceptada;
    private String procesamientoMasCorto;
    private String listaProcesamientos;
    private String listaProcesamientosAbortados;
    private String listaProcesamientosAceptacion;
    private String listaProcesamientosRechazados;
    private int numProcesamientosAbortados;
    private int numProcesamientosAceptacion;
    private int numProcesamientosRechazados;
    private int numProcesamientos;

    ProcesamientoCadenaAFN(String cadena, AFN afn) {
        this.cadena = cadena;
        this.afn = afn;

        // Crear cola de pilas de pasos computacionales (String[2])
        // Cada pila representa un procesamiento
        ArrayDeque<ArrayDeque<String[]>> q = new ArrayDeque<ArrayDeque<String[]>>();
        ArrayDeque<ArrayDeque<String[]>> procesamientosTerminados = new ArrayDeque<ArrayDeque<String[]>>();
        ArrayDeque<String[]> procesamientoInicial = new ArrayDeque<String[]>();

        // Encolar pila con el elemento [q0, cadena]
        String[] configuracionInicial = {afn.getq0(), cadena};
        procesamientoInicial.add(configuracionInicial);
        q.add(procesamientoInicial);

        // Remover cada pila del inicio de la cola y 
        // crear y encolar una nueva pila para cada paso
        // computacional posible desde la configuracion
        // en el tope de la pila original
        while (!q.isEmpty()) {
            ArrayDeque<String[]> procesamiento = q.remove();
            HashSet<String[]> nextProcesamientos = afn.darPasoComputacional(procesamiento.peekLast());
            if (nextProcesamientos!=null) {
                for (String[] nextProcesamiento : nextProcesamientos) {
                    ArrayDeque<String[]> nuevoStack = procesamiento.clone();
                    nuevoStack.add(nextProcesamiento);
                    q.add(nuevoStack);
                }
            } else {

                // Enlistar cada procesamiento terminado
                procesamientosTerminados.add(procesamiento);
            }
        }

        // Procesar la lista de procesamientos
        
        // String builders
        StringBuilder sb = new StringBuilder();
        StringBuilder sbAceptacion = new StringBuilder();
        StringBuilder sbAbortado = new StringBuilder();
        StringBuilder sbRechazo = new StringBuilder();

        // Variables
        String procesamientoRechazadoMasCorto = "";
        String procesamientoRechazado = "";
        int sizeProcesamientoRechazadoMasCorto = cadena.length();
        Boolean todaviaNoEsAceptada = true;
        Boolean todaviaNoEsRechazada = true;
        int contadorAceptadas = 0;
        int contadorRechazadas = 0;
        int contadorAbortadas = 0;

        for (ArrayDeque<String[]> procesamiento : procesamientosTerminados) {

            // Determinar el tipo de procesamiento
            String tipoDeProcesamiento = "";
            if (procesamiento.peekLast()[1].equals("")) {
                if (afn.esDeAceptacion(procesamiento.peekLast()[0])) {
                    tipoDeProcesamiento = "aceptado";
                    contadorAceptadas++;
                } else {
                    tipoDeProcesamiento = "rechazado";
                    contadorRechazadas++;
                }
            } else {
                tipoDeProcesamiento = "abortado";
                contadorAbortadas++;
            }

            // Almacenar procesamiento como StringBuilder
            StringBuilder sbUltimoProcesamiento = new StringBuilder();
            Boolean masDeUnaconfiguracion = false;

            // Recordar tamano del procesamiento
            int sizeProcesamiento = procesamiento.size() - 1; 
            
            // Crear listas de procesamientos via StringBuilders
            while (!procesamiento.isEmpty()){
                String[] configuracion = procesamiento.remove();

                // StringBuilder append para todos los procesamientos
                if (masDeUnaconfiguracion) {
                    sb.append(" -> [" + configuracion[0] + "," + configuracion[1] + "]");
                } else {
                    sb.append("[" + configuracion[0] + "," + configuracion[1] + "]");
                }

                // StringBuilder append para procesamiento actual
                if (masDeUnaconfiguracion) {
                    sbUltimoProcesamiento.append(" -> [" + configuracion[0] + "," + configuracion[1] + "]");
                } else {
                    sbUltimoProcesamiento.append("[" + configuracion[0] + "," + configuracion[1] + "]");
                }

                switch(tipoDeProcesamiento) {
                    case "aceptado":
                        if (masDeUnaconfiguracion) {
                            sbAceptacion.append(" -> [" + configuracion[0] + "," + configuracion[1] + "]");
                        } else {
                            sbAceptacion.append("[" + configuracion[0] + "," + configuracion[1] + "]");
                            masDeUnaconfiguracion = true;
                        }
                        break;
                    case "abortado":
                        if (masDeUnaconfiguracion) {
                            sbAbortado.append(" -> [" + configuracion[0] + "," + configuracion[1] + "]");
                        } else {
                            sbAbortado.append("[" + configuracion[0] + "," + configuracion[1] + "]");
                            masDeUnaconfiguracion = true;
                        }
                        break;
                    default:
                        if (masDeUnaconfiguracion) {
                            sbRechazo.append(" -> [" + configuracion[0] + "," + configuracion[1] + "]");
                        } else {
                            sbRechazo.append("[" + configuracion[0] + "," + configuracion[1] + "]");
                            masDeUnaconfiguracion = true;
                        }
                }      
            }


            switch(tipoDeProcesamiento) {
                case "aceptado":
                    sbAceptacion.append("\n");

                    // Almacenar un procesamiento aceptado
                    if (todaviaNoEsAceptada) {
                        this.esAceptada = true;
                        this.procesamientoMasCorto = sbUltimoProcesamiento.toString();;
                        todaviaNoEsAceptada = false;
                    }
                    break;
                case "abortado":
                    sbAbortado.append("\n");

                    // Almacenar si el procesamiento abortado es el mas corto
                    if (sizeProcesamiento < sizeProcesamientoRechazadoMasCorto) {
                        sizeProcesamientoRechazadoMasCorto = sizeProcesamiento;
                        procesamientoRechazadoMasCorto = sbUltimoProcesamiento.toString();
                    }
                    break;
                default:
                    sbRechazo.append("\n");
                    
                    // Almacenar un procesamiento rechazado
                    if (todaviaNoEsRechazada) {
                        procesamientoRechazado = sbUltimoProcesamiento.toString(); 
                        todaviaNoEsRechazada = false;
                    }
            }      
            sb.append(" " + tipoDeProcesamiento+"\n");
        }

        // Almacenar listas de procesamientos
        this.listaProcesamientos = sb.toString();
        this.listaProcesamientosAbortados = sbAbortado.toString();
        this.listaProcesamientosAceptacion = sbAceptacion.toString();
        this.listaProcesamientosRechazados = sbRechazo.toString();

        // Almacenar numero total de procesamientos posibles
        this.numProcesamientos = procesamientosTerminados.size();
        this.numProcesamientosAbortados = contadorAbortadas;
        this.numProcesamientosAceptacion = contadorAceptadas;
        this.numProcesamientosRechazados = contadorRechazadas;


        if (todaviaNoEsAceptada){
            this.esAceptada = false;
            if (procesamientoRechazadoMasCorto.equals("")) {
                this.procesamientoMasCorto = procesamientoRechazado;
            } else {
                this.procesamientoMasCorto = procesamientoRechazadoMasCorto;
            }
        }
    }

    // Getters

    public Boolean esAceptada() {
        return this.esAceptada;
    }

    public String procesamientoMasCorto() {
        return this.procesamientoMasCorto;
    }

    public String listaProcesamientos() {
        return this.listaProcesamientos;
    }

    public String listaProcesamientosAbortados() {
        return this.listaProcesamientosAbortados;
    }
    public String listaProcesamientosAceptacion() {
        return this.listaProcesamientosAceptacion;
    }

    public String listaProcesamientosRechazados() {
        return this.listaProcesamientosRechazados;
    }

    public int numProcesamientosAbortados() {
        return this.numProcesamientosAbortados;
    }

    public int numProcesamientosAceptacion() {
        return this.numProcesamientosAceptacion;
    }

    public int numProcesamientosRechazados() {
        return this.numProcesamientosRechazados;
    }

    public int numProcesamientos() {
        return this.numProcesamientos;
    }
}