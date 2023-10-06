import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Cliente {
    @NonNull
    private String host;
    @NonNull
    private int port;
    private Socket socket;
    private Scanner sc;
    private Scanner in;
    private PrintStream out;
    private ScheduledExecutorService executorService;

    public Cliente(String host, int port) {
        this.host = host;
        this.port = port;
        this.sc = new Scanner(System.in);
        this.executorService = Executors.newScheduledThreadPool(2); // 2 hilos para tareas programadas
    }

    private void connect() {
        try {
            this.socket = new Socket(host, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void enviar() {
        try {
            out.println(sc.nextLine());
        } catch (NoSuchElementException e) {
            //System.err.println(e.getMessage());
        }
    }

    private void recibir() {
        try {
            System.out.println(in.nextLine());
        } catch (NoSuchElementException e) {
            //throw new RuntimeException(e);
        }
    }

    private void initInputThread() {
        // Programa la ejecución de recibir() cada 200 milisegundos
        executorService.scheduleAtFixedRate(this::recibir, 0, 200, TimeUnit.MILLISECONDS);
    }

    private void initIOutputThread() {
        // Programa la ejecución de enviar() cada 200 milisegundos
        executorService.scheduleAtFixedRate(this::enviar, 0, 200, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 5222);
        cliente.connect();

        cliente.initIOutputThread();
        cliente.initInputThread();
    }
}
