package com.osmond.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> data = new ArrayList<String>(Arrays.asList("Osmond","Allen","Henry","Jack"));

        ListView listView =new ListView(this);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,
                R.layout.custom_row,R.id.line1,data);

        data.add("dddd");

        listView.setAdapter(adapter);

        setContentView(listView);
    }
}
