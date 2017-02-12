package tk.strictlyconformist.medusa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public Socket connect(View view) throws IOException {
        EditText serverText = (EditText) findViewById(R.id.server);
        EditText portText = (EditText) findViewById(R.id.port);
        String hostname = serverText.getText().toString();
        int port = Integer.parseInt(portText.getText().toString());
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
