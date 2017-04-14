package tk.strictlyconformist.medusa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class CreateServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_server);
    }

    public void connect(View view) throws IOException {
        EditText serverText = (EditText) findViewById(R.id.server);
        EditText portText = (EditText) findViewById(R.id.port);
        EditText userText = (EditText) findViewById(R.id.userName);
        EditText passText = (EditText) findViewById(R.id.password);
        final String hostname;
        final int  port;
        final Server myServer;
        if (serverText.getText().length() == 0) {
            hostname = "127.0.0.1";
        }else{
            hostname = serverText.getText().toString();
        }
        if (portText.getText().length() == 0){
            port = 21;
        }else{
            port = Integer.parseInt(portText.getText().toString());
        }
        myServer = new Server(hostname,port);
        if (userText.getText().length() == 0){
            myServer.userName = "anonymous";
        }else{
            myServer.userName = userText.getText().toString();
        }
        if (passText.getText().length() == 0){
            myServer.password = "guest";
        }else{
            myServer.password = passText.getText().toString();
        }

        new Thread(new Runnable(){
            public void run(){
                myServer.connect();
                myServer.logIn();
                myServer.connectData();
                myServer.retDirectory();
            }
        }).start();
    }
}
