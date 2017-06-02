package tk.strictlyconformist.medusa;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;


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

    public String getHost()
    {return host;}

    public void saveToDisk(Context ctx){
        String FILENAME = "servers.dat";
        CharBuffer charBuffer = CharBuffer.allocate(userName.length()+host.length());
        charBuffer.put(userName+host);
        ByteBuffer converter = Charset.forName("ISO-8859-1").encode(charBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(charBuffer.length()+(Integer.SIZE/Byte.SIZE));
        byteBuffer.put(converter);
        byteBuffer.putInt(commandPort);
        byteBuffer.rewind();
        try {
            FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_APPEND);
            fos.write(byteBuffer.get());
            fos.close();
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }

    public static ArrayList<Server> readFromDisk(Context ctx){
        String FILENAME = "servers.dat";
        ArrayList<Server> serverList = null;
        try {
            FileInputStream fis = ctx.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String inBuffer;
            while((inBuffer = in.readLine()) != null)
            {
                String hostName = inBuffer.substring(0,255);
                int portNumber = Integer.parseInt(inBuffer.substring(255,260));
                String user = inBuffer.substring(262,294);
                Server newServer = new Server(hostName,portNumber);
                newServer.userName = user;
                serverList.add(newServer);
                Log.i(TAG,"SAVED "+inBuffer);
            }
            fis.close();
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
        return serverList;
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
                    break;
                }
            }
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }

    public void connectData(){
        try {
            dataSocket = new Socket(host,dataPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            String inBuffer;
            PrintWriter out = new PrintWriter(commandSocket.getOutputStream(),true);
            out.println("TYPE I\r\n");
            inBuffer = in.readLine();
            Log.i(TAG,inBuffer);
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }

    public void retDirectory(){
        try {
            PrintWriter out = new PrintWriter(commandSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            BufferedReader comIn = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            String inBuffer;
            String comBuffer;
            out.println("XPWD\r\n");
            comBuffer = comIn.readLine();
            Log.i(TAG,comBuffer);
            String tempString = "";
            for(int i=5;i<comBuffer.length();i++) {
                if (comBuffer.charAt(i) == '"') {
                    tempString = comBuffer.substring(5, i);
                    break;
                }
            }
            File cwd = new File(tempString);
            out.println("LIST\r\n");
            ArrayList<File> cwdContents = new ArrayList<>();
            while((inBuffer = in.readLine()) != null){
                Log.i(TAG,inBuffer);
                cwdContents.add(new File(cwd, inBuffer.substring(56)));
            }
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }
}
