package tk.strictlyconformist.medusa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView serverListView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverListView = (ListView) findViewById(R.id.server_list_view);
        ArrayList<Server> serverList = Server.readFromDisk(getApplicationContext());
        String[] listHosts = new String[serverList.size()];
        for (int i = 0; i < serverList.size(); i++) {
            Server tempServer = serverList.get(i);
            listHosts[i] = tempServer.getHost();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHosts);
        assert serverListView != null;
        serverListView.setAdapter(adapter);
    }

    public void createServer(final View view){
        Intent intent = new Intent(view.getContext(), CreateServerActivity.class);
        startActivity(intent);
    }
}
