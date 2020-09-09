package hw6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String serverAdress = "localhost";
    private static final int serverPort = 8100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        try {
            socket = new Socket(serverAdress, serverPort);
            System.out.println("Клиент подключился к серверу");
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            Thread read = new Thread(() -> {
                try {
                    outputStream.writeUTF(scanner.nextLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            read.setDaemon(true);
            read.start();
            while (true) {
                String str = inputStream.readUTF();
                if (str.equals("/end")) {
                    System.out.println("Сервер отключился");
                    outputStream.writeUTF("/end");
                    break;
                } else {
                    System.out.println(" Сервер : " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
