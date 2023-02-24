package exercicis;
import java.util.*;
import java.util.Random;
import java.util.Scanner;

// TODO: 24/2/23 mirar que se marquen las casillas tocadas y hundidas en cada uno de los tableros
//poner niveles vs maquina, colores para barcos, arraylist para modificacion tamaño
public class HundirLaFlota {
    public static final int FILAS = 10;
    public static final int COLUMNAS = 10;
    public static final int NUM_BARCOS = 5;
    public static final int LONG_BARCOS[] = { 5, 4, 3, 3, 2 };

    public static final int AGUA = 0;
    public static final int BARCO = 1;
    public static final int TOCADO = 2;
    public static final int HUNDIDO = 3;

    public static final int FALLADO = 4;

    public static void main(String[] args) {
        int[][] tableroJugador = new int[FILAS][COLUMNAS];
        int[][] tableroMaquina = new int[FILAS][COLUMNAS];
        System.out.println("Tablero jugador:");
        inicializarTablero(tableroJugador);
        // colocarBarcos(tableroJugador);
        colocarBarcosAleatorio(tableroJugador);//aixo es treu pero per debbugging va be
        mostrarTablero(tableroJugador,false);
        System.out.println("Tablero Maquina:");
        inicializarTablero(tableroMaquina);
        colocarBarcosAleatorio(tableroMaquina);
        mostrarTablero(tableroMaquina, false);




        boolean juegoTerminado = false;
        boolean turnoJugador = true;

        while (!juegoTerminado) {
            if (turnoJugador) {

                System.out.println("Turno del jugador:");
                mostrarTablero(tableroMaquina, true);
                atacar(tableroMaquina);
                juegoTerminado = todosBarcosHundidos(tableroMaquina);
            } else {

                System.out.println("Turno de la máquina:");
                // para hacer un rango para que el tiro sea mas inteligente para cuando toque un
                // barco
                int ultimaFila = 0;//no entinedo pk falla aqui
                int ultimaColumna = 0;//y aqui

                int[] coordenadas = atacarAleatorio(tableroJugador, ultimaFila, ultimaColumna);
                ultimaFila = coordenadas[0];
                ultimaColumna = coordenadas[1];
               // atacarAleatorio(tableroJugador, ultimaFila, ultimaColumna);
                juegoTerminado = todosBarcosHundidos(tableroJugador);
                mostrarTablero(tableroMaquina, true);
            }
            turnoJugador = !turnoJugador;
        }

        if (turnoJugador) {
            System.out.println("¡Has ganado!");
        } else {
            System.out.println("¡Has perdido!");
        }
    }

    // INICIALIZARTABLERO funciona
    /**
     * inicializar tablero
     */
    public static void inicializarTablero(int[][] tablero) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = AGUA;
            }
        }
    }

    // ATACARALEATORIO quizas funciona pero va bien MAKINA

    /**
     * Función con la posibilidad de elegir un rango de posiciones alrededor
     * de la última posición tocada en caso de que haya tocado un barco en la jugada
     * anterior.
     * la función genera dos números aleatorios para la fila y la columna del ataque
     * y mira de que la posición no haya sido atacada antes.
     * Si ataca a una posición que contiene un barco, se indica que el barco ha sido
     * tocado y se marca en el tablero del jugador.
     * Si se hunde el barco, se muestra un mensaje correspondiente.
     * En el caso contrario, se muestra un mensaje que indica que la máquina ha
     * fallado.
     *
     * @param tableroJugador el tablero de juego del jugador al que se le realiza el
     *                       ataque
     * @param ultimaFila     la ultima fila tocada en la jugada anterior
     * @param ultimaColumna  la ultima columna tocada en la jugada anterior
     * @return void
     */
    // TODO: 24/2/23 para nivel dificil cambiar fallado por agua y asi se vueve inmortal
    // TODO: 24/2/23 poner modos: automatico(maquina contra maquina) superdificil cada vez que la maquina aciert ael jugador tiene dos turnos
    
    private static int[] atacarAleatorio(int[][] tableroJugador, int ultimaFila, int ultimaColumna) {
        Random random = new Random();
        int fila;
        int columna;
        int rango = 1; // rango de posiciones alrededor de la última posición tocada

        // si en la jugada anterior ha tocado una posición con 'TOCADO', se escoge un
        // número aleatorio en un rango de posiciones alrededor de la última posición
        // tocada
        if (tableroJugador[ultimaFila][ultimaColumna] == TOCADO) {
            do {
                fila = ultimaFila + random.nextInt(2 * rango + 1) - rango; // escoge un número aleatorio en un rango de
                // posiciones alrededor de la última fila
                // tocada
                columna = ultimaColumna + random.nextInt(2 * rango + 1) - rango; // escoge un número aleatorio en un
                // rango de posiciones alrededor de la
                // última columna tocada
            } while (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS
                    || tableroJugador[fila][columna] == FALLADO || tableroJugador[fila][columna] == TOCADO); // para
            // comprobar el rango de limites y que no sea una posicion hundida en fallado hi habia agua
        } else {
            // numero random dentro del limite de filas y columnas
            do {
                fila = random.nextInt(FILAS);
                columna = random.nextInt(COLUMNAS);
            } while (tableroJugador[fila][columna] == FALLADO || tableroJugador[fila][columna] == TOCADO);//en fallado anava agua
        }
        // si el numero le da en un valor que equivale a barco
        if (tableroJugador[fila][columna] == BARCO) {
            System.out.println(
                    "Damn! la maquina te ha pillado un barco en la posición " + fila + "," + columna + " ,rebientalo!");
            tableroJugador[fila][columna] = TOCADO;
            // en el caso de que el tiro hunda un barco
            if (barcoHundido(tableroJugador, fila, columna)) {
                System.out.println("Bro, la maquina ha reventado tu nave xd...");
            }
        } else {
            // si el numero le da en un valor que equivale a agua
            System.out.println("Nah, un disparo fallido de la maquina en la posicion " + fila + "," + columna);
            tableroJugador[fila][columna] = FALLADO;
        }
        // guarda los valores para la proxima jugada en el caso que sea tocado poder
        // tirar de
        // forma mas inteligente
        int[] coordenadas = new int[2];
        coordenadas[0] = fila;
        coordenadas[1] = columna;
        return coordenadas;
    }

    // BARCOHUNDIDO quizas funciona
    /**
     * Determina si un barco ha sido hundido en la posición dada y actualiza el
     * tablero del jugador
     *
     * @param tablero el tablero del jugador
     * @param fila           la fila en la que se encuentra el barco
     * @param columna        la columna en la que se encuentra el barco
     * @return true si el barco ha sido hundido, false en caso contrario
     */
    private static boolean barcoHundido(int[][] tablero, int fila, int columna) {
        if (fila > 0 && tablero[fila - 1][columna] == BARCO) {
            return false;
        }

        if (fila < FILAS - 1 && tablero[fila + 1][columna] == BARCO) {
            return false;
        }

        if (columna > 0 && tablero[fila][columna - 1] == BARCO) {
            return false;
        }

        if (columna < COLUMNAS - 1 && tablero[fila][columna + 1] == BARCO) {
            return false;
        }

        tablero[fila][columna] = HUNDIDO;
        return true;

    }

    // ATACAR quizas funciona
    private static void atacar(int[][] tableroMaquina) {
        Scanner scanner = new Scanner(System.in);

        int fila, columna;
        String mensaje = "Coordenadas no válidas o ya has disparado allí. Inténtalo de nuevo.";

        do {
            System.out.print("Fila (1-" + FILAS + "): ");
            fila = scanner.nextInt() - 1;
            System.out.print("Columna (1-" + COLUMNAS + "): ");
            columna = scanner.nextInt() - 1;

            if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
                System.out.println("Coordenadas no válidas. Inténtalo de nuevo.");
            } else if (tableroMaquina[fila][columna] == FALLADO || tableroMaquina[fila][columna] == TOCADO
                    || tableroMaquina[fila][columna] == HUNDIDO) {
                System.out.println("Ya has disparado allí. Inténtalo de nuevo.");
            }
        } while (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS
                || tableroMaquina[fila][columna] == FALLADO
                || tableroMaquina[fila][columna] == TOCADO || tableroMaquina[fila][columna] == HUNDIDO);

        if (tableroMaquina[fila][columna] == BARCO) {
            System.out.println("¡Tocado!");
            tableroMaquina[fila][columna] = TOCADO;
            if (barcoHundido(tableroMaquina, fila, columna)) {
                System.out.println("¡Hundido!");
            }
        } else if (tableroMaquina[fila][columna] == AGUA) {
            System.out.println("Fallaste, mala suerte bro.");
            tableroMaquina[fila][columna] = FALLADO;
        } else {
            System.out.println("¡Agua!");

        }
    }

    // COLOCARBARCOSALEATORIO------------------------
    public static void colocarBarcosAleatorio(int[][] tablero) {
        Random random = new Random();
        int filaInicio = 0;
        int columnaInicio = 0;
        int filaFin = 0;
        int columnaFin ;
        int direccion ;
        int sentido;

        for (int i = 0; i < NUM_BARCOS; i++) {

            do {
                filaInicio = random.nextInt(FILAS);
                columnaInicio = random.nextInt(COLUMNAS);
                direccion = random.nextInt(2);
                sentido = random.nextInt(2);

                if (direccion == 0) {
                    if (sentido == 0) {
                        columnaFin = columnaInicio + (LONG_BARCOS[i] -1);
                    } else {
                        columnaFin = columnaInicio - (LONG_BARCOS[i] -1);
                    }
                    filaFin = filaInicio;

                    // colocar barco horizontal

                } else {
                    if (sentido == 0) {
                        filaFin = filaInicio + (LONG_BARCOS[i] -1);
                    } else {
                        filaFin = filaInicio - (LONG_BARCOS[i] -1);
                    }
                    columnaFin = columnaInicio;

                    // colocar barco vertical
                }
            } while (!verificarBarco(tablero, filaInicio, columnaInicio, filaFin, columnaFin, LONG_BARCOS[i]));
            tablero[filaFin][columnaFin] = BARCO;
            tablero[filaInicio][columnaInicio] = BARCO;
            if (direccion == 0) {
                if (sentido == 0) {
                    for (int j = 0; j < LONG_BARCOS[i]; j++) {
                        tablero[filaFin][columnaFin-j] = BARCO;
                    }
                } else {
                    for (int j = 0; j < LONG_BARCOS[i]; j++) {
                        tablero[filaFin][columnaFin+j] = BARCO;
                    }
                }


                // colocar barco horizontal

            } else {
                if (sentido == 0) {
                    for (int j = 0; j < LONG_BARCOS[i]; j++) {
                        tablero[filaFin-j][columnaFin] = BARCO;
                    }
                } else {
                    for (int j = 0; j < LONG_BARCOS[i]; j++) {
                        tablero[filaFin+j][columnaFin] = BARCO;
                    }
                }
//SUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU

                // colocar barco vertical
            }
        }

    }

    // COLOCARBARCOS funciona
    /**
     * Esta funcion permite al ususario colocar sus barcos al tablero del juego
     * El usuario tiene que poner las cordenadas de los barcos para cada barco que
     * van a colocar
     * El numero y la longitud de los barcos que el usuario puede colocar esta
     * determinado por
     * el numero de barcos que se definen en la variable NUM_BARCOS y la longitud de
     * cada barco
     * que esta definido en la variable LONG_BARCOS
     * La funcion utiliza las funciones auxiliares 'verificarBarco' y
     * 'mostrarTAblero' para validar
     * y mostrar los barcos colocados al tablero
     * LAs cordenadas se validas para asegurarnos de que el barco este dentro del
     * tablero y no
     * tenga conflicto con otros barcos y si las cordenadas son validas se añade el
     * barco al tablero
     * con la posicion establecida.
     *
     * @param tablero matriz bidimensional de numeros enteros que representa el
     *                tablero del juego
     *                El valor BARCO se pondra en las posiciones donde se añada un
     *                barco
     * @return void
     */
    private static void colocarBarcos(int[][] tablero) {
        Scanner scanner = new Scanner(System.in);
        // todo cambiar las filas de int por char y hacer diccionario conversor de num a
        // letras
        for (int i = 0; i < NUM_BARCOS; i++) {
            int filaInicio, columnaInicio, filaFin, columnaFin;

            do {
                System.out.println("Coloca el barco de longitud " + LONG_BARCOS[i]);
                System.out.print("Fila horizontal de inicio (1-" + FILAS + "): ");
                filaInicio = scanner.nextInt() - 1;
                System.out.print("Columna vertical de inicio (1-" + COLUMNAS + "): ");
                columnaInicio = scanner.nextInt() - 1;
                System.out.print("Fila horizontal de fin (1-" + FILAS + "): ");
                filaFin = scanner.nextInt() - 1;
                System.out.print("Columna vertical de fin (1-" + COLUMNAS + "): ");
                columnaFin = scanner.nextInt() - 1;

                if (!verificarBarco(tablero, filaInicio, columnaInicio, filaFin, columnaFin, LONG_BARCOS[i])) {
                    System.out.println("Barco no válido. Inténtalo de nuevo.");
                }
            } while (!verificarBarco(tablero, filaInicio, columnaInicio, filaFin, columnaFin, LONG_BARCOS[i]));
            // para piner barcos en horizontal o vertical
            for (int j = 0; j < LONG_BARCOS[i]; j++) {
                if (filaInicio == filaFin) {
                    // Barco horizontal
                    tablero[filaInicio][columnaInicio + j] = BARCO;
                } else {
                    // Barco vertical
                    tablero[filaInicio + j][columnaInicio] = BARCO;
                }
            }
            // todo: mantener en false para poder ver los barcps, se deberia esconder en los
            // barcos maquina NO AQUI!
            mostrarTablero(tablero, false);// es true pero per debuhgejar es false
        }
    }

    // COLOCARBARCOS funciona
    /**
     * verifica si las cordenadas de inicio y fin del barco son validas y si el
     * barco cabe
     * dentro de los limites del tablero y si no se junta con otros barcos que ya
     * existan
     *
     * @param tablero       La matriz que representa el tablero del juego
     * @param filaInicio    La fila donde se encuentra la casilla de inicio del
     *                      barco
     * @param columnaInicio la columna donde se encuentra la casilla de inicio del
     *                      barco
     * @param filaFin       la fila donde se encuentra la casilla de fin del barco
     * @param columnaFin    la columna donde se encuentra la casilla de fin del
     *                      barco.
     * @param longBarco     la longitud del barco que se colocara.
     * @return un valor boleano que indica si las cordenadas con correctas y si no
     *         se junta con ningun otro barco
     */
    public static boolean verificarBarco(int[][] tablero, int filaInicio, int columnaInicio, int filaFin,
                                         int columnaFin, int longBarco) {
        if (filaInicio < 0 || filaInicio >= FILAS || columnaInicio < 0 || columnaInicio >= COLUMNAS ||
                filaFin < 0 || filaFin >= FILAS || columnaFin < 0 || columnaFin >= COLUMNAS) {
            return false;
        }

        if (filaInicio != filaFin && columnaInicio != columnaFin) {
            return false;
        }

        if (Math.abs(filaInicio - filaFin) + Math.abs(columnaInicio - columnaFin) != longBarco - 1) {
            return false;
        }

        for (int i = Math.min(filaInicio, filaFin); i <= Math.max(filaInicio, filaFin); i++) {
            for (int j = Math.min(columnaInicio, columnaFin); j <= Math.max(columnaInicio, columnaFin); j++) {
                if (tablero[i][j] != AGUA) {
                    return false;
                }
            }
        }

        return true;
    }

    // TODOSBARCOSHUNDIDOS ---------------------------------
    private static boolean todosBarcosHundidos(int[][] tablero) {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int columna = 0; columna < COLUMNAS; columna++) {
                if (tablero[fila][columna] == BARCO) {
                    return false;
                }
            }
        }
        return true;
    }


    // MOSTRARTABLERO funciona
    /**
     *
     * Mostra el contenido del tablero, tanto el agua como los barcos tocados y
     * hundidos
     *
     * @param tablero       Matriz de numeros que compone el tablero (dependiendo de
     *                      si es agua,barco,tocado,hundido)
     *
     * @param ocultarBarcos Valor booleano que indica si los barcos tienen que estar
     *                      ocultos o no(para la maquina)
     */
    public static void mostrarTablero(int[][] tablero, boolean ocultarBarcos) {
        System.out.print("  ");
        for (int i = 1; i < COLUMNAS + 1; i++) {
            System.out.print(" " + i + " "); // per momstrar els numeros de la primera columna
        }
        System.out.println();

        for (int i = 0; i < FILAS; i++) {
            System.out.print((char) ('A' + i) + " ");// per poder mostrar les lletres de la primera fila
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == AGUA) {
                    System.out.print("\033[34m" + " 〰 " + "\u001B[0m");
                } else if (tablero[i][j] == BARCO) {
                    if (ocultarBarcos) {
                        System.out.print("\033[34m" + " 〰 " + "\u001B[0m");
                    } else {
                        System.out.print("\033[32m" + " ⛴ " + "\u001B[0m");
                    }
                } else if (tablero[i][j] == TOCADO) {
                    System.out.print("\033[33m" + " X " + "\u001B[0m");
                } else if (tablero[i][j] == HUNDIDO) {
                    System.out.print("\033[31m" + " X " + "\u001B[0m");
                } else if (tablero[i][j] == FALLADO) {
                    System.out.print("\033[37m" + " 〰 " + "\u001B[0m");
                }
            }
            System.out.println();
        }
    }

}
