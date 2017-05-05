package com.osmond.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;
import static android.view.View.SYSTEM_UI_FLAG_VISIBLE;

public class StyledWidgets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TableLayout layout = (TableLayout)getLayoutInflater().inflate(R.layout.activity_styled_widgets,null);

        // add a new Button
        Button b3= new Button(this);
        b3.setText("add this button at runtime");
        layout.addView(b3,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(layout);

        findViewById(R.id.ToogleMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentVis = v.getSystemUiVisibility();
                int newVis;
                if((currentVis & SYSTEM_UI_FLAG_LOW_PROFILE) == SYSTEM_UI_FLAG_LOW_PROFILE ){
                    newVis =SYSTEM_UI_FLAG_VISIBLE  ;
                }else{
                    newVis = SYSTEM_UI_FLAG_LOW_PROFILE| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                }
                v.setSystemUiVisibility(newVis);
            }
        });
    }
}
