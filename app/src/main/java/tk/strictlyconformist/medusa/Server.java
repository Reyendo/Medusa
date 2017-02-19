package tk.strictlyconformist.medusa;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by reyendo on 2/19/2017.
 */

public class Server {
    private String host;
    private int port;
    private String userName;
    private String password;
    private Socket commandSocket,dataSocket;

    public Server(String hostname, int portNumber){
        host = hostname;
        port = portNumber;
    }

    public Server(String hostname){
        host=hostname;
        port = 21;
    }

    public void connect(){
        try{
            commandSocket = new Socket(host,port);
        }catch(IOException except){
            System.err.println(except.getMessage());
        }
    }

    public void logIn(){
        try{
            PrintWriter out = new PrintWriter(commandSocket.getOutputStream(),true);
            out.print("USER"+userName+"\r\n");
            out.print("PASS"+password+"\r\n");
        }catch(IOException except){
            System.err.println(except.getMessage());
        }
    }
}
