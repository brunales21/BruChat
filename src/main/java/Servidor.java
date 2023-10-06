import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

@AllArgsConstructor
public class Servidor {
    List<Socket> sockets = new ArrayList<>();
    private ServerSocket serverSocket;
    @NonNull
    private int port;

    public Servidor(int port) {
        this.port = port;
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(port);
            int maxClientes = 2;
            for (int i = 0; i < maxClientes; i++) {
                System.out.println("Esperando cliente "+sockets.size()+"..");
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                System.out.println("Cliente conectado.");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("sockets: "+sockets);
        System.out.println("LISTO");
        process(sockets);
    }

    private void process(List<Socket> sockets) {
        do {
            try {
                for (Socket socket1: sockets) {
                    Scanner in = new Scanner(socket1.getInputStream());
                    String mensaje = in.nextLine();
                    System.out.println("Mensaje de "+socket1+": "+mensaje);
                    if (mensaje != null) {
                        for (Socket socket2: sockets) {
                            PrintStream out = new PrintStream(socket2.getOutputStream());
                            out.println("Mensaje: "+mensaje);
                        }
                    }
                }
            } catch (IOException | NoSuchElementException e) {
                //System.err.println(e.getMessage());
            }
        } while (true);
    }

    public static void main(String[] args) {
        Servidor s = new Servidor(5222);
        s.start();
    }

}
