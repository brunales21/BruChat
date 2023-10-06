import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

@AllArgsConstructor
public class Cliente {
    @NonNull
    private String host;
    @NonNull
    private int port;
    private Socket socket;
    private Scanner sc = new Scanner(System.in);

    public Cliente(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void connect() {
        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void enviar() {
        try (PrintStream out = new PrintStream(socket.getOutputStream())) {
            out.println(sc.nextLine());
        } catch (IOException | NoSuchElementException e) {
            //System.err.println(e.getMessage());
        }
    }

    private void recibir() {
        try (Scanner in = new Scanner(socket.getInputStream())) {
            System.out.println(in.nextLine());
        } catch (IOException | NoSuchElementException e) {
            //throw new RuntimeException(e);
        }
    }

    private void initInputThread() {
        timerReader = new Timer();
        timerReader.scheduleAtFixedRate(inputThread, 0, 200);
    }

    private void initIOutputThread() {
        timerWriter = new Timer();
        timerWriter.scheduleAtFixedRate(outputThread, 0, 200);
    }

    private Timer timerReader;
    private Timer timerWriter;

    private TimerTask inputThread = new TimerTask() {
        @Override
        public void run() {
            recibir();
        }
    };

    private TimerTask outputThread = new TimerTask() {
        @Override
        public void run() {
            enviar();

        }
    };



    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 5222);
        cliente.connect();

        cliente.initIOutputThread();
        cliente.initInputThread();

    }
}
