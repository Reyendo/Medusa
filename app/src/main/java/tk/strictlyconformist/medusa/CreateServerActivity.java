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

    public void save(final View view)
    {
        EditText addressText = (EditText) findViewById(R.id.address);
        EditText portText = (EditText) findViewById(R.id.port);
        EditText userText = (EditText) findViewById(R.id.userName);
        EditText passText = (EditText) findViewById(R.id.password);
        final String address;
        final int  port;
        final Server myServer;
        if (addressText.getText().length() == 0) {
            address = "127.0.0.1";
        }else{
            address = addressText.getText().toString();
        }
        if (portText.getText().length() == 0){
            port = 21;
        }else{
            port = Integer.parseInt(portText.getText().toString());
        }
        myServer = new Server(address,port);
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
        myServer.saveToDisk(view.getContext());
    }

    public void connect(final View view) throws IOException {
        EditText addressText = (EditText) findViewById(R.id.address);
        EditText portText = (EditText) findViewById(R.id.port);
        EditText userText = (EditText) findViewById(R.id.userName);
        EditText passText = (EditText) findViewById(R.id.password);
        final String address;
        final int  port;
        final Server myServer;
        if (addressText.getText().length() == 0) {
            address = "127.0.0.1";
        }else{
            address = addressText.getText().toString();
        }
        if (portText.getText().length() == 0){
            port = 21;
        }else{
            port = Integer.parseInt(portText.getText().toString());
        }
        myServer = new Server(address,port);
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
                myServer.readFromDisk(view.getContext());
                myServer.connect();
                myServer.logIn();
                myServer.connectData();
                myServer.retDirectory();
            }
        }).start();
    }
}
