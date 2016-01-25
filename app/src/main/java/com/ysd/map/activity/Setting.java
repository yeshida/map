package com.ysd.map.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ysd.map.R;

/**
 * Created by Administrator on 2016/1/25.
 */
public class Setting extends Activity {
    private static final String TAG = "Setting";
    //定义菜单需要的常亮
    private static final int MENU_MAIN= Menu.FIRST+1;
    private static final int MENU_NEW= MENU_MAIN+1;
    private static final int MENU_BACK= MENU_NEW+1;
    //保存个性化设置
    private static final String SETTING_INFOS = "SETTING_Infos";
    private static final String SETTING_GPS = "SETTING_Gps";
    private static final String SETTING_MAP = "SETTING_Map";
    private static final String SETTING_GPS_POSITON = "SETTING_Gps_p";
    private static final String SETTING_MAP_POSITON = "SETTING_Map_p";

    private Button button_setting_submit;
    private Spinner field_setting_gps;
    private Spinner field_setting_map_level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        setTitle(R.string.menu_setting);
        findViews();
        setListeners();
        retorePrefs();
    }
        private void findViews() {
            Log.i(TAG, "find views");
            button_setting_submit = (Button) findViewById(R.id.setting_submit);
            field_setting_gps = (Spinner) findViewById(R.id.setting_gps);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gps, android.R.layout.simple_spinner_item);
            field_setting_gps.setAdapter(adapter);
            field_setting_map_level = (Spinner) findViewById(R.id.setting_map_level);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.map, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            field_setting_map_level.setAdapter(adapter2);

        }
    //Listen for button clicks
    private void setListeners () {
        Log.d(TAG, "set listeners");
        button_setting_submit.setOnClickListener(setting_submit);
    }
    private Button.OnClickListener setting_submit=new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onclick new_track..");
            try {
                String gps = (field_setting_gps.getSelectedItem().toString());
                String map = (field_setting_map_level.getSelectedItem().toString());
                if (gps.equals("") || map.equals("")) {
                    Toast.makeText(Setting.this, getString(R.string_setting_null), Toast.LENGTH_SHORT).show();
                } else {
                    //保存设定
                    storePrefs();
                    Toast.makeText(Setting.this,getString(R.string.setting_ok),Toast.LENGTH_SHORT).show();
                    //跳转到主界面
                    Intent intent = new Intent();
                    intent.setClass(Setting.this, ITracks.class);
                    startActivity(intent);
                }
            } catch (Exception err) {
                Log.e(TAG, "error:" + err.toString());
                Toast.makeText(Setting.this,getString(R.string.setting_fail),Toast.LENGTH_SHORT).show();
            }
        }
    };
    //restore preferences
    private void restorePrefs() {
        SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
    }
}