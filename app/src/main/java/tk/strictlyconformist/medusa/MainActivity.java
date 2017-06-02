package tk.strictlyconformist.medusa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    private ListView serverListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverListView = (ListView) findViewById(R.id.server_list_view);
        ArrayList<Server> serverList = null;
        if((serverList = Server.readFromDisk(getApplicationContext())) == null)
        {
            Intent intent = new Intent(this, CreateServerActivity.class);
            startActivity(intent);
        }else {
            String[] listHosts = new String[serverList.size()];
            for (int i = 0; i < serverList.size(); i++) {
                Server tempServer = serverList.get(i);
                listHosts[i] = tempServer.getHost();
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listHosts);
            serverListView.setAdapter(adapter);
        }
    }

    public void createServer(View view){
        Intent intent = new Intent(this, CreateServerActivity.class);
        startActivity(intent);
    }
}
