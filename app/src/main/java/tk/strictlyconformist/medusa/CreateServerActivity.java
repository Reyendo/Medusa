package tk.strictlyconformist.medusa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class CreateServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_server);
    }

    private Server grabInput()
    {
        EditText addressText = (EditText) findViewById(R.id.address);
        EditText portText = (EditText) findViewById(R.id.port);
        EditText userText = (EditText) findViewById(R.id.userName);
        EditText passText = (EditText) findViewById(R.id.password);
        final String address;
        final int  port;
        final Server myServer;
        if(addressText != null && !TextUtils.isEmpty(addressText.getText()))
        { address = addressText.getText().toString(); } else { address = "127.0.0.1"; }
        if(portText != null && !TextUtils.isEmpty(portText.getText()))
        {
            int tempPort;
            try { tempPort = Integer.parseInt(portText.getText().toString()); }
            catch (NumberFormatException except) { tempPort = 21; }
            port = tempPort;
        } else { port = 21; }
        myServer = new Server(address,port);
        if(userText != null && !TextUtils.isEmpty(userText.getText()))
        { myServer.userName = userText.getText().toString(); }
        if(passText != null && !TextUtils.isEmpty(passText.getText()))
        { myServer.password = passText.getText().toString(); }
        return myServer;
    }

    public void save(final View view)
    {
        Server myServer = grabInput();
        myServer.saveToDisk(view.getContext());
    }

    public void connect(final View view) {
        final Server myServer = grabInput();

        new Thread(new Runnable(){
            public void run(){
                myServer.logIn();
                myServer.returnDirectory(view.getContext());
            }
        }).start();
    }
}