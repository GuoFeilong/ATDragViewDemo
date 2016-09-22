package com.asiatravel.atdragviewdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.asiatravel.atdragviewdemo.R;
import com.asiatravel.atdragviewdemo.customview.ATDragView;
import com.asiatravel.atdragviewdemo.customview.CountDownCircleView;
import com.asiatravel.atdragviewdemo.customview.MyElongVerticalTextView;
import com.asiatravel.atdragviewdemo.customview.SwitchView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        test();

        List<String> data = new ArrayList<>();
        data.add("0元");
        data.add("400元");
        data.add("800元");
        data.add("1600元");
        data.add("无限");
        final CountDownCircleView countDownCircleView = (CountDownCircleView) findViewById(R.id.countdown_circle_view);


        ATDragView atDragView = (ATDragView) findViewById(R.id.at_dragView);
        atDragView.setData(data, new ATDragView.OnDragFinishedListener() {
            @Override
            public void dragFinished(int leftPostion, int rightPostion) {
                Toast.makeText(MainActivity.this, "回调数据Left-->" + leftPostion + "--Right-->" + rightPostion, Toast.LENGTH_SHORT).show();

                countDownCircleView.startCountDown(new CountDownCircleView.OnCountDownFinishedListener() {
                    @Override
                    public void countDownStop() {
                        Toast.makeText(MainActivity.this, "计时结束", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        SwitchView switchView1 = (SwitchView) findViewById(R.id.switch_view1);
        SwitchView switchView2 = (SwitchView) findViewById(R.id.switch_view2);

        switchView1.setOpenState(false);
        switchView2.setOpenState(true);

        MyElongVerticalTextView verticalTextView = (MyElongVerticalTextView) findViewById(R.id.vt_textView);
//        verticalTextView.setText("QQ专属");

        TextView jump = (TextView) findViewById(R.id.tv_test);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void test() {
        for (int a = 0, b = 0; a < 2; b = ++a) {
            Logger.e("a===" + a + "---b=" + b);
        }
    }
}
