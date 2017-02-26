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
    private int commandPort,dataPort;
    public String userName;
    public String password;
    private Socket commandSocket,dataSocket;

    public Server(String hostname, int portNumber){
        host = hostname;
        commandPort = portNumber;
    }

    public Server(String hostname){
        host=hostname;
        commandPort = 21;
    }

    public void connect(){
        try{
            commandSocket = new Socket(host,commandPort);
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
                // Messy code below. Clean up later.
                if(Integer.parseInt(inBuffer.substring(0,3)) == 227) {
                    int p1=0,p2=0,x=0;
                    for (int i = inBuffer.length()-1;i>0;i--) {
                        if(inBuffer.charAt(i) == ')'){
                           x = i;
                        }
                        if(inBuffer.charAt(i) == '('){
                            String tempString = inBuffer.substring(i+1,x);
                            for(int j=tempString.length()-1;j>0;j--) {
                                if (tempString.charAt(j) == ',')
                                {
                                    p2 = Integer.parseInt(tempString.substring(j+1));
                                    for(int k = j-1;k > 0;k--){
                                        if(tempString.charAt(k) == ',')
                                        {
                                            p1 = Integer.parseInt(tempString.substring(k+1,j));
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    dataPort = (p1*256)+p2;
                }
            }
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }

    public void connectData(){
        try {
            dataSocket = new Socket(host,dataPort);
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }
}
