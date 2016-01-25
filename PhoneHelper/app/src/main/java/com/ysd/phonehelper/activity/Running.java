package com.ysd.phonehelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ysd.phonehelper.R;
import com.ysd.phonehelper.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/18.
 */
public class Running extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG="Running";
    ListView itemlist = null;
    List<Map<String, Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("运行时信息");
        itemlist = (ListView) findViewById(R.id.itemlist);
        refreshListItems();
    }

    private void refreshListItems() {
        list = buildListForSimpleAdapter();
        SimpleAdapter notes = new SimpleAdapter(this,list, R.layout.item_row,new String[]{"name","desc"},new int[]{R.id.name,R.id.desc});
        itemlist.setAdapter(notes);
        itemlist.setOnItemClickListener(this);
        itemlist.setSelection(0);
    }

    private List<Map<String,Object>> buildListForSimpleAdapter() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(3);
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("id", PreferencesUtil.RUNNING_SERVICE);
        map.put("name", "运行的service");
        map.put("desc", "获取正在运行的后台服务");
        list.add(map);


        map = new HashMap<String,Object>();
        map.put("id", PreferencesUtil.RUNNING_TASKS);
        map.put("name", "运行的task");
        map.put("desc", "获取正在运行的任务。");
        list.add(map);
        map = new HashMap<String,Object>();
        map.put("id",PreferencesUtil.RUNNING_PROCESS);
        map.put("name", "进程信息");
        map.put("desc", "获取正在运行的进程。");
        list.add(map);
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Log.i(TAG, "item clicked![" + position + "]");
        Bundle info = new Bundle();
        Map<String, Object> map = list.get(position);
        info.putInt("id", (Integer) map.get("id"));
        info.putString("name", (String) map.get("name"));
        info.putString("desc", (String) map.get("desc"));
        info.putInt("position", position);
        intent.putExtra("android.intent.extra.info", info);
        intent.setClass(Running.this, ShowInfo.class);
        startActivityForResult(intent,0);
    }
}
