package tk.strictlyconformist.medusa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class CreateServerActivity extends AppCompatActivity {
    ArrayList<String> cwdContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_server);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.i("DirectoryListItem",cwdContents.get(data.getIntExtra("result",0)));
            }
        }
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
                cwdContents = myServer.returnDirectory();
                Intent directory_content_intent = new Intent(view.getContext(), ListDirectoryActivity.class);
                directory_content_intent.putStringArrayListExtra("cwdContents",cwdContents);
                startActivityForResult(directory_content_intent,1);
            }
        }).start();
    }
}