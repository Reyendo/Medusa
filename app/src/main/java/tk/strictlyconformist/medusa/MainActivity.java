package tk.strictlyconformist.medusa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static Socket connect(String hostname, int port) throws IOException {
        Socket  targetSocket = new Socket(hostname, port);
        return targetSocket;
    }

    public static void logIn(String username, String password, Socket commandSocket) throws IOException{
        PrintWriter out =
                new PrintWriter(commandSocket.getOutputStream(),true);
        out.print("USER"+username+"\r\n");
        out.print("PASS"+password+"\r\n");
    }
}
