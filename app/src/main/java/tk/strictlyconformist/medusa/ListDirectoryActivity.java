package tk.strictlyconformist.medusa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListDirectoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_directory);
        ListView directoryListView = (ListView) findViewById(R.id.directory_list_view);
        ArrayList<String> cwdContents = getIntent().getStringArrayListExtra("cwdContents");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cwdContents);
        assert directoryListView != null;
        directoryListView.setAdapter(adapter);
    }
}