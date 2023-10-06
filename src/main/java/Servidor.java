import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    private List<Socket> sockets = new ArrayList<>();
    private ServerSocket serverSocket;
    private int port;

    public Servidor(int port) {
        this.port = port;
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(port);
            int maxClientes = 2;
            for (int i = 0; i < maxClientes; i++) {
                System.out.println("Esperando cliente " + (i + 1) + "...");
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                System.out.println("Cliente conectado: " + socket.getInetAddress());
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("LISTO");
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                Scanner in = new Scanner(clientSocket.getInputStream());
                while (true) {
                    if (!in.hasNextLine()) {
                        continue;
                    }
                    String mensaje = in.nextLine();
                    System.out.println("Mensaje de " + clientSocket.getInetAddress() + ": " + mensaje);
                    for (Socket otherSocket : sockets) {
                        if (!otherSocket.equals(clientSocket)) {
                            PrintStream out = new PrintStream(otherSocket.getOutputStream());
                            out.println("Mensaje: " + mensaje);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Cliente desconectado: " + clientSocket.getInetAddress());
                sockets.remove(clientSocket);
            }
        }
    }

    public static void main(String[] args) {
        Servidor s = new Servidor(5222);
        s.start();
    }
}
