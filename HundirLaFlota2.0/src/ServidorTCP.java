import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServidorTCP {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private char[][] tablero;

    public void iniciar(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("Buscando partida...");
        clientSocket = serverSocket.accept();
        System.out.println("Partida enconttranda.");
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        tablero = new char[10][10]; 
        inicializarTablero();
        manejarJuego();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = 'A'; 
            }
        }
        tablero[1][2] = 'B';
        tablero[1][3] = 'B';
        tablero[1][4] = 'B';
        tablero[7][9] = 'B';
        tablero[6][9] = 'B';
    }

    private void manejarJuego() throws IOException {
        Scanner sc = new Scanner(System.in);
        String coordenada;
        while (true) {
            // Recibir coordenada del cliente
            coordenada = in.readLine();
            if (coordenada == null) break;
            System.out.println("El misisl va hacia las coordenadas: " + coordenada);
            String[] partes = coordenada.split(",");
            int x = Integer.parseInt(partes[0]);
            int y = Integer.parseInt(partes[1]);
            if (tablero[x][y] == 'B') {
                out.println("Enhorabuena, ganaste!");
                break;
            } else {
                out.println("Fallaste!");
            }

            
            System.out.print("Introduce coordenada para atacar (importante la coma entre las coordenadas) (x,y): ");
            coordenada = sc.nextLine();
            out.println(coordenada);
            String respuesta = in.readLine();
            System.out.println(respuesta);
            if (respuesta.equals("Enhorabuena, ganaste!")) {
                break;
            }
        }
        detener();
    }

    public void detener() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) {
        ServidorTCP servidor = new ServidorTCP();
        try {
            servidor.iniciar(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
