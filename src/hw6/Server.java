package hw6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        Socket clientsocket = null;
        Scanner scanner = new Scanner(System.in);
        try(ServerSocket serverSocket = new ServerSocket(8000)) {

            System.out.println("Сервер подключен");
            clientsocket = serverSocket.accept();
            System.out.println("Кклиент подключен");
            DataInputStream inputStream = new DataInputStream(clientsocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientsocket.getOutputStream());

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
                    System.out.println("Клиент отключился");
                    outputStream.writeUTF("/end");
                    break;
                } else {
                    System.out.println(" Клиент : " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                clientsocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
