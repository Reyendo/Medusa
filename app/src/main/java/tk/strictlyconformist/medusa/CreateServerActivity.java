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
        final String hostname = serverText.getText().toString();
        final int  port = Integer.parseInt(portText.getText().toString());
        final Server myServer = new Server(hostname,port);
        myServer.userName = userText.getText().toString();
        myServer.password = passText.getText().toString();
        new Thread(new Runnable(){
            public void run(){
                myServer.connect();
                myServer.logIn();
            }
        }).start();
    }
}
