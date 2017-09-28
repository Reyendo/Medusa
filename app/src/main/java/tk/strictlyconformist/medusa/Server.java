package tk.strictlyconformist.medusa;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


class Server {
    private static final String TAG = "Server";
    private String host;
    private int commandPort,dataPort;
    String userName;
    String password;
    private Socket commandSocket,dataSocket;

    Server(String hostname, int portNumber){
        host = hostname;
        commandPort = portNumber;
        userName = "anonymous";
        password = "anonymous@domain.com";
    }

    String getHost()
    { return host; }

    void saveToDisk(Context ctx){
        String FILENAME = "servers.dat";
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(ctx.openFileOutput(FILENAME, Context.MODE_APPEND)));
            out.writeUTF(userName);
            out.writeUTF(host);
            out.writeInt(commandPort);
            out.close();
        }catch(IOException except) {
            Log.e(TAG, except.getMessage());
        }
    }

    // Needs serious work. I need to fix Server's constructors and privacy settings and I need to turn this into a loop.
    // Variable names need to be normalized and corrected, and I need to cut down on all the temp variables.
    static ArrayList<Server> readFromDisk(Context ctx){
        String FILENAME = "servers.dat";
        ArrayList<Server> serverList = new ArrayList<>();
        try{
            DataInputStream in = new DataInputStream(new BufferedInputStream(ctx.openFileInput(FILENAME)));
            String userString = in.readUTF();
            String hostString = in.readUTF();
            int portTemp = in.readInt();
            in.close();
            Server serverTemp = new Server(hostString,portTemp);
            serverTemp.userName = userString;
            serverList.add(serverTemp);
        }catch(IOException except) {
            except.printStackTrace();
        }
        return serverList;
    }

    void logIn(){
        try{
            commandSocket = new Socket(host,commandPort);
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
            dataSocket = new Socket(host,dataPort);
            out.println("TYPE I\r\n");
            inBuffer = in.readLine();
            Log.i(TAG,inBuffer);
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }

   void returnDirectory(Context ctx){
        try {
            PrintWriter out = new PrintWriter(commandSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            BufferedReader comIn = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            String inBuffer;
            String comBuffer;
            out.println("XPWD\r\n");
            comBuffer = comIn.readLine();
            Log.i(TAG,comBuffer);
            out.println("LIST\r\n");
            ArrayList<String> cwdContents = new ArrayList<>();
            while((inBuffer = in.readLine()) != null)
            { cwdContents.add(inBuffer.substring(56)); }
            Intent directory_content_intent = new Intent(ctx, ListDirectoryActivity.class);
            directory_content_intent.putStringArrayListExtra("cwdContents",cwdContents);
            for(int i=0;i<cwdContents.size();i++)
            { Log.i(TAG,cwdContents.get(i)); }
            ctx.startActivity(directory_content_intent);
        }catch(IOException except){
            Log.e(TAG,except.getMessage());
        }
    }
}
