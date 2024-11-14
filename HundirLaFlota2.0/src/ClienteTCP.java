import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteTCP {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private char[][] tablero;

    public void iniciar(String direccion, int puerto) throws IOException {
        socket = new Socket(direccion, puerto);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        tablero = new char[10][10]; 
        inicializarTablero();
        manejarJuego();
    }

    //el barco es la b
    private void inicializarTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = 'A'; 
            }
        }
        tablero[3][4] = 'B'; 
        tablero[4][4] = 'B'; 
        tablero[8][7] = 'B';
        tablero[8][6] = 'B'; 
        tablero[8][5] = 'B'; 
    }

    private void manejarJuego() throws IOException {
        Scanner sc = new Scanner(System.in);
        String coordenada;
        while (true) {
            //Ataque del cliente
            System.out.print("Introduce coordenada para atacar (importante la coma entre las coordenadas) (x,y): ");
            coordenada = sc.nextLine();
            out.println(coordenada);
            String respuesta = in.readLine();
            System.out.println(respuesta);
            if (respuesta.equals("Enhorabuena, ganaste!")) {
                break;
            }

            //se recibe el ataque del servidor 
            coordenada = in.readLine();
            System.out.println("el misil del servidor va hacia  " + coordenada);
            String[] partes = coordenada.split(",");
            int x = Integer.parseInt(partes[0]);
            int y = Integer.parseInt(partes[1]);
            if (tablero[x][y] == 'B') {
                out.println("Enhorabuena, ganaste!");
                break;
            } else {
                out.println("Fallaste!");
            }
        }
        detener();
    }

    public void detener() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) {
        ClienteTCP cliente = new ClienteTCP();
        try {
            cliente.iniciar("localhost", 5555);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
