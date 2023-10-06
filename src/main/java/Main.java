import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "C:\\Users\\2DAM\\IdeaProjects\\BruChat\\target\\classes", "Cliente");
        try {
            Process p = pb.start();
            Scanner sc = new Scanner(p.getInputStream());
            do {
                System.out.println(sc.nextLine());
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
