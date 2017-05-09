package com.osmond.study;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager vp = (ViewPager) findViewById(R.id.aPager);
        vp.setAdapter(new ImagePagerAdapter());

    }
    
    class ImagePagerAdapter extends PagerAdapter {

        int[] Colors = {
                Color.BLUE,
                Color.RED,
                Color.GREEN,
                Color.BLACK
        };
        private Context getContext(){
            return ViewPagerActivity.this;
        }
        // create new View at this position
        @SuppressLint("SetTextI18n")
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(getContext());
            tv.setBackgroundColor(Colors[position]);
            tv.setTextColor(Color.WHITE);
            tv.setText(""+position);
            container.addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return Colors.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
