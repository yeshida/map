package com.ysd.phonehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ysd.phonehelper.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/18.
 */
public class Software extends Activity implements Runnable {
    private static final String TAG="Software";
    public ProgressDialog pd;
    ListView itemlist = null;
    List<Map<String, Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("软件信息");
        itemlist = (ListView) findViewById(R.id.itemlist);
        pd = ProgressDialog.show(this, "请稍后。。。", "正在手机你已经安装好的软件信息。。",true,false);
        Thread thread = new Thread(this);
        thread.start();;
    }
    @Override
    public void run() {
    fetch_installed_apps();
        hander.sendEmptyMessage(0);
    }

    /**
     * 获取已经安装的应用信息
     * @return
     */
    private List fetch_installed_apps() {
        List<ApplicationInfo> packages =getPackageManager().getInstalledApplications(0);
        list = new ArrayList<Map<String, Object>>(packages.size());
        Iterator<ApplicationInfo> i = packages.iterator();
        while(i.hasNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            ApplicationInfo app = (ApplicationInfo) i.next();
            String packageName = app.packageName;
            String label = "";
            try {
                label = getPackageManager().getApplicationLabel(app).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("name", label);
            map.put("desc", packageName);
            list.add(map);
        }
        return list;
    }

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            refreshListItems();
            pd.dismiss();
        }
    };
    private void refreshListItems() {
        list = fetch_installed_apps();
        SimpleAdapter notes = new SimpleAdapter(this,list, R.layout.item_row,new String[]{"name","desc"},new int[]{R.id.name,R.id.desc});
        itemlist.setAdapter(notes);
        setTitle("软件信息，已经安装"+list.size()+"款应用。");
    }
}
