package tk.strictlyconformist.medusa;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by reyendo on 2/19/2017.
 */

public class Server {
    private static final String TAG = "Server";
    private String host;
    private int port;
    public String userName;
    public String password;
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
            Log.e(TAG,except.getMessage());
        }
    }

    public void logIn(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            PrintWriter out = new PrintWriter(commandSocket.getOutputStream(),true);
            out.println("USER "+userName+"\r\n");
            out.println("PASS "+password+"\r\n");
            out.println("PASV\r\n");
            String inBuffer;
            while((inBuffer = in.readLine()) != null)
            {
                Log.i(TAG,inBuffer);
            }
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }

    public void connectData(){
        try {
            dataSocket = new Socket(host,port);
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }
}
