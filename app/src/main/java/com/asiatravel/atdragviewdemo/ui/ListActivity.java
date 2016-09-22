package com.asiatravel.atdragviewdemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.asiatravel.atdragviewdemo.R;

/**
 * Created by user on 16/9/21.
 */

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ListAdapter listAdapter = new ListAdapter();
        ListView listView = (ListView) findViewById(R.id.lv_test);
        listView.setAdapter(listAdapter);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(ListActivity.this).inflate(R.layout.layout_test, null, false);
            return convertView;
        }
    }
}
