package com.osmond.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.osmond.study.onedrive.OAuth2Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button)findViewById(R.id.toStyledWidget);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StyledWidgets.class));
            }
        });

        Button b2 = (Button)findViewById(R.id.toBullsEye);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BullsEye.class));
            }
        });

        findViewById(R.id.toSimpleListView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SimpleListView.class));
            }
        });

        findViewById(R.id.toDropboxTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DropBoxTest.class));
            }
        });
        findViewById(R.id.toGoogleDriveTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GoogleDriveTest.class));
            }
        });

        findViewById(R.id.toOneDrive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OAuth2Activity.class));
            }
        });

        findViewById(R.id.toWebView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WebViewActivity.class));
            }
        });


    }

    public void onUIButtonClick(View view){
        if(view.getId() == R.id.toViewPager){
            toActiviy(ViewPagerActivity.class);
            return;
        }
    }

    private void toActiviy(Class<?> cls){
        startActivity(new Intent(MainActivity.this,cls));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_viewpager){
            startActivity(new Intent(this,ViewPagerActivity.class));
        }
        return true;
    }
}
