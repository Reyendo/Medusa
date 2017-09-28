package tk.strictlyconformist.medusa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListDirectoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_directory);
        ListView directoryListView = (ListView) findViewById(R.id.directory_list_view);
        ArrayList<String> cwdContents = getIntent().getStringArrayListExtra("cwdContents");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cwdContents);
        assert directoryListView != null;
        directoryListView.setAdapter(adapter);
        directoryListView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",position);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}