package com.ysd.phonehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.ysd.phonehelper.R;
import com.ysd.phonehelper.util.FetchData;
import com.ysd.phonehelper.util.PreferencesUtil;

/**
 * Created by Administrator on 2016/1/19.
 */
public class ShowInfo extends Activity implements Runnable {
    private static final String TAG="ShowInfo";
    TextView info;
    TextView title;
    private ProgressDialog pd;
    public String info_datas;
    public boolean is__valid = false;
    public int _id = 0;
    public String _name="";
    public int _position = 0;
    public int _ref=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinfo);
        revParas();
        info = (TextView) findViewById(R.id.info);
        title = (TextView) findViewById(R.id.title);
        setTitle("eoeinfosAssistant" + _name);
        title.setText(_name);
        load_data();
    }

    private void load_data() {
        pd = ProgressDialog.show(this, "please wait a moment..", "fetch into datas..", true, false);
        Thread thread = new Thread(this);
        thread.start();
    }

    private void revParas() {
        Log.i(TAG, "revParams");
        Intent startingIntent = getIntent();
        if (startingIntent != null) {
            Bundle infod = startingIntent.getBundleExtra("android.intent.extra.info");
            if (infod == null) {
                is__valid = false;
            } else {
                _id = infod.getInt("id");
                _name = infod.getString("name");
                _position = infod.getInt("position");
                is__valid = true;
            }
        } else {
            is__valid= false;
        }
        Log.i(TAG, "_name:" + _name + ",_id" + _id);
    }

    @Override
    public void run() {
        if (_ref > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        switch (_id) {
            case PreferencesUtil.CPU_INFO:
                info_datas=FetchData.fetch_cpu_info();
                break;
            case PreferencesUtil.VER_INFO:
                info_datas= FetchData.fetch_version_info();
                break;
            case PreferencesUtil.NET_STATUS:
                info_datas=FetchData.fetch_netstat_info();
                break;
            case PreferencesUtil.DISK_INFO:
                info_datas=FetchData.fetch_disk_info();
                break;
            case PreferencesUtil.DMESG_INFO:
                info_datas=FetchData.fetch_dmesg_info();
                break;
            case PreferencesUtil.RUNNING_PROCESS:
                info_datas=FetchData.fetch_process_info();
                break;
            case PreferencesUtil.NET_CONFIG:
                info_datas=FetchData.fetch_netcfg_info();
                break;
            case PreferencesUtil.MOUNT_INFO:
                info_datas=FetchData.fetch_mount_info();
                break;
            case PreferencesUtil.TEL_STATUS:
                info_datas=FetchData.fetch_tel_status(this);
                break;
            case PreferencesUtil.MEM_INFO:
                info_datas=FetchData.getMemoryInfo(this);
                break;
            case PreferencesUtil.SYSTEM_PROPERTY:
                info_datas=FetchData.getSystemProperty();
                break;
            case PreferencesUtil.DISPLAY_METRICS:
                info_datas=FetchData.getDisplayMetrics(this);
                break;
            case PreferencesUtil.RUNNING_SERVICE:
                info_datas=FetchData.getRunningServicesInfo(this);
                break;
            case PreferencesUtil.RUNNING_TASKS:
                info_datas=FetchData.getRunningTasksInfo(this);
                break;
        }
        handler.sendEmptyMessage(0);
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            pd.dismiss();
            info.setText(info_datas);
        }
    };
}
