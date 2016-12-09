package com.zetaphase.betago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReloadListActivity extends AppCompatActivity {

    public String message;
    public BetaGoActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("INRELOADACTIVITY", "INRELOAD");
        setContentView(R.layout.activity_reload_list);

        Intent intent = getIntent();
        message = intent.getStringExtra(BetaGoActivity.EXTRA_MESSAGE);
        //TextView textView = new TextView(this);
        //textView.setTextSize(10);
        //textView.setText(message);

        //ViewGroup layout = (ViewGroup) findViewById(R.id.activity_reload_list);
        //layout.addView(textView);

        populateListView();
        Log.d("REACHEDHERE", "here");
        registerClickCallback();
    }

    private void populateListView() {
        String[] myItems = {"Blue", "Green", "Purple", "Red"};
        String lines[] = message.split("\\r?\\n");
        String[] myLines = new String[lines.length];
        Log.d("LINES", String.valueOf(lines));
        Log.d("LINES", String.valueOf(lines.length));
        for (int i = 0; i < lines.length; i++) {
            myLines[i] = lines[i].substring(12);
        }
        Log.d("MYLINES", String.valueOf(myLines));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.da_item, myLines);
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position + ", which is string: " + textView.getText().toString();
                Toast.makeText(ReloadListActivity.this, message, Toast.LENGTH_LONG).show();
                Log.d("SETRESULT", "beforeresult");
                Intent intent = new Intent();
                intent.putExtra("Mydata", textView.getText().toString());
                setResult(BetaGoActivity.MY_REQUEST_CODE, intent);
                Log.d("SETRESULT", "setresult");
                finish();
            }
        });
    }
}
