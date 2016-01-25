package com.ysd.phonehelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ysd.phonehelper.R;
import com.ysd.phonehelper.util.PreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/18.
 */
public class FSExplore extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG="FSExplore";
    ListView itemlist = null;
    List<Map<String, Object>> list;
     String path ="/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("文件浏览器");
        itemlist = (ListView) findViewById(R.id.itemlist);
        refreshListItems(path);
    }

    private void refreshListItems(String path) {
        setTitle("文件浏览器>" + path);
        list = buildListForSimpleAdapter(path);
        SimpleAdapter notes = new SimpleAdapter(this,list, R.layout.item_row,new String[]{"name","desc"},new int[]{R.id.name,R.id.desc});
        itemlist.setAdapter(notes);
        itemlist.setOnItemClickListener(this);
        itemlist.setSelection(0);
    }

    private List<Map<String,Object>> buildListForSimpleAdapter(String path) {
        File[] files = new File(path).listFiles();
        List<Map<String, Object>> list= new ArrayList<Map<String, Object>>(files.length);
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("name", "/");
            root.put("desc", "go to root directory");
            list.add(root);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "..");
            map.put("desc", "go to parent directory");
            list.add(map);
            for (File file : files) {
                Map<String, Object> maps = new HashMap<String,Object>();
         /*   if (file.isDirectory()) {
                map.put("desc", "文件夹");
            } else {
                map.put("desc", "文件");
            }*/
                maps.put("name", file.getName());
                maps.put("desc", file.getPath());
                list.add(maps);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "item clicked![" + position + "]");
        if (position==0) {
            path="/";
            refreshListItems(path);
        }else if (position == 1) {
            goToParent();
        }else {
            path = (String) list.get(position).get("desc");
            Log.i("path:", path);
            File file = new File(path);
            if (file.isDirectory()) {
                File[] files = new File(path).listFiles();
                if (files!=null){
                    refreshListItems(path);
                }else{
                    Toast.makeText(FSExplore.this, "该文件为空", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(FSExplore.this, "这个是文件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToParent() {
        File file = new File(path);
        Log.i("path:", path);
        File str_pa = file.getParentFile();
        if (str_pa == null) {
            Toast.makeText(FSExplore.this, "这是根目录", Toast.LENGTH_SHORT).show();
            refreshListItems(path);
        } else {
            path = str_pa.getAbsolutePath();
            refreshListItems(path);
        }
    }
}
