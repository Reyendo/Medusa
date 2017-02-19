package tk.strictlyconformist.medusa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.Socket;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) throws IOException{
        EditText serverText = (EditText) findViewById(R.id.server);
        EditText portText = (EditText) findViewById(R.id.port);
        final String hostname = serverText.getText().toString();
        final int  port = Integer.parseInt(portText.getText().toString());
        /**
        if(portText.getText() != null) {
            port = Integer.parseInt(portText.getText().toString());
         */
        new Thread(new Runnable(){
            public void run(){
                try {
                    new Socket(hostname,port);
                }
                catch(IOException except){
                    System.err.println(except.getMessage());
                }
            }
        }).start();
    }
}
