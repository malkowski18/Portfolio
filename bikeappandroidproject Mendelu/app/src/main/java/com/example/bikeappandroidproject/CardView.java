package com.example.bikeappandroidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;


import java.util.ArrayList;
import java.util.List;

public class CardView extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.bike1,"Ecofriendly","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Integer malesuada. Nullam justo entiam egestas wisi a erat. "));
        models.add(new Model(R.drawable.bike2,"Cheap","Lorem ipsum dolor sit amet, consect Nullam justo enim, consectetut. Etiam egestas wisi a erat. "));
        models.add(new Model(R.drawable.bike3,"Healthy","orem ipsum dolor sit amet, consect Nullam justo enim, consectetut. Etiam egestas wisi a erat."));
        models.add(new Model(R.drawable.bike4,"Fast","orem ipsum dolor sit amet, consect Nullam justo enim, consectetut. Etiam egestas wisi a erat."));

        adapter = new Adapter(models,this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < (adapter.getCount() -1) && position < (colors.length -1)){
                    viewPager.setBackgroundColor(
                            (Integer)argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position+1])
                    );
                } else{
                    viewPager.setBackgroundColor(colors[colors.length -1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void towebpage(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.cz/"));
        startActivity(browserIntent);
    }

}
