package com.ysd.phonehelper.util;

import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ysd.phonehelper.activity.ShowInfo;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/1/19.
 */
public class FetchData {
    public static  StringBuffer buffer;

    /**
     * 获取操作系统的版本
     * @return
     */
    public static String fetch_version_info() {
        String result = null;
        CMDExecute cmdexe= new CMDExecute();
        String[] args = {"/system/bin/cat","/proc/version"};
        try {
            result= cmdexe.run(args,"/system/bin/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取手机系统信息
     * @return
     */
    public static String getSystemProperty() {
        buffer = new StringBuffer();
        initporperty("java.vendor.url", "java.vendor.url");
        initporperty("java.class.path", "java.class.path");
        initporperty("user.home","user.home");
        initporperty("java.class.version","java.class.version");
        initporperty("os.version","os.version");
        initporperty("java.vendor","java.vendor");
        initporperty("user.dir","user.dir");
        initporperty("user.time.zone","user.time.zone");
        initporperty("path.separator","path.separator");
        initporperty("os.name","os.name");
        initporperty("os.arch","os.arch");
        initporperty("line.separator","line.separator");
        initporperty("file.separator","file.separator");
        initporperty("user.name","user.name");
        initporperty("java.version","java.version");
        initporperty("java.home","java.home");
        return buffer.toString();
    }

    /**
     * 获取运营商信息
     * @param context
     * @return
     */
    public static String fetch_tel_status(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String result = null;
        result="DeviceId(imei)="+tm.getDeviceId()+"\n";
        result += "DeviceSoftwareVersion=" + tm.getDeviceSoftwareVersion() + "\n";
        result+="Line1number="+tm.getLine1Number()+"\n";
        result += "NetworkCountyIos=" + tm.getNetworkCountryIso() + "\n";
        result += "NetworkOperator=" + tm.getNetworkOperator() + "\n";
        result+="NetworkType="+tm.getNetworkType()+"\n";
        result += "PhoneType=" + tm.getPhoneType() + "\n";
        result += "SimCountyIos=" + tm.getSimCountryIso() + "\n";
        result += "SimOperator=" + tm.getSimOperator() + "\n";
        result += "SimOperatorName" + tm.getSimOperatorName() + "\n";
        result += "SimserialNumber=" + tm.getSimSerialNumber() + "\n";
        result += "SimState=" + tm.getSimState() + "\n";
        result += "SubscriberId(IMSI)=" + tm.getSubscriberId() + "\n";
        result += "VoiceMailNumber=" + tm.getVoiceMailNumber() + "\n";
        int mcc = context.getResources().getConfiguration().mcc;
        int mnc = context.getResources().getConfiguration().mnc;
        result += "IMSI MCC（mobile county code）:" + String.valueOf(mcc) + "\n";
        result += "IMSI MNC(mobile county code):" + String.valueOf(mnc);

        return result;
    }
    private static String initporperty(String path, String str) {
        buffer.append(path);
        buffer.append(":");
        buffer.append(System.getProperty(path)+"\n");
        return buffer.toString();
    }

    /**
     * 获取cpu信息
     * @return
     */
    public static String fetch_cpu_info() {
        String result= null;
        CMDExecute cmdExecute = new CMDExecute();
        String[] args  ={"/system/bin/cat","/proc/cpuinfo"};
        try {
            result = cmdExecute.run(args, "/system/bin/");
            Log.i("result", "result=" + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取网络信息
     * @return
     */
    public static String fetch_netstat_info() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        String[] args ={"/system/bin/netstat"};
        try {
            result = cmdexe.run(args, "/system/bin/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取硬盘信息
     * @return
     */
    public static String fetch_disk_info() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        String[] args = {"/system/bin/df"};
        try {
            result = cmdexe.run(args, "/system/bin/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String fetch_dmesg_info() {
        return null;
    }

    public static String fetch_process_info() {
        Log.i("fetch_process_info", "start...");
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        String[] args = {"/system/bin/top","-n","1"};
        try {
            result = cmdexe.run(args, "/system/bin/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String fetch_netcfg_info() {
        return null;
    }

    public static String fetch_mount_info() {
        return null;
    }



    /**
     * 获取内存信息   也可以读取/proc/meminfo信息
     * @param context
     * @return
     */
    public static String getMemoryInfo(Context context) {
        StringBuffer memryinfo = new StringBuffer();
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        memryinfo.append("\nTotal available memory:").append(outInfo.availMem >> 10).append("k");
        memryinfo.append("\nTotal available memory:").append(outInfo.availMem >> 20).append("M");
        memryinfo.append("\nIn low memory situation:").append(outInfo.lowMemory);

        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        String[] args ={"/system/bin/cat","/proc/meminfo"};
        try {
            result = cmdexe.run(args, "/system/bin/");
        } catch (IOException e) {
           e.printStackTrace();
        }
        return memryinfo.toString()+"\n\n"+result;
    }


    public static String getDisplayMetrics(Context context) {
        String str= "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWith = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        str += "The absolute width:" + String.valueOf(screenWith) + "pixels\n";
        str += "The absolute height:" + String.valueOf(screenHeight) + "pixels\n";
        str += "The logical density of the display:" + String.valueOf(density)+"\n";
        str += "X dimension:" + String.valueOf(xdpi) + "pixels per inch\n";
        str += "Y dimension:" + String.valueOf(ydpi) + "pixels per inch\n";
        return str;
    }

    public static String getRunningServicesInfo(Context context) {
        StringBuffer serviceInfo = new StringBuffer();
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(100);
        Iterator<ActivityManager.RunningServiceInfo> i = services.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo si = (ActivityManager.RunningServiceInfo) i.next();
            serviceInfo.append("pid:").append(si.pid);
            serviceInfo.append("/nprocess:").append(si.process);
            serviceInfo.append("\ncrashCount:").append(si.crashCount);
            serviceInfo.append("\nclientCount:").append(si.clientCount);
            serviceInfo.append("\nactiveSince:").append(si.activeSince);
            serviceInfo.append("\nlastActivityTime:").append(si.lastActivityTime);
            serviceInfo.append("\n\n");

        }
        return serviceInfo.toString();
    }

    public static String getRunningTasksInfo(Context context) {
        StringBuffer sinfo = new StringBuffer();
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(100);
        Iterator<ActivityManager.RunningTaskInfo> i = tasks.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningTaskInfo ti = (ActivityManager.RunningTaskInfo) i.next();
            sinfo.append("id:").append(ti.id);
            sinfo.append("\nbaseActivity:").append(ti.baseActivity.flattenToString());
            sinfo.append("\nnumActivities").append(ti.numActivities);
            sinfo.append("\nnumRunning:").append(ti.numRunning);
            sinfo.append("\ndescription:").append(ti.description);
            sinfo.append("\n\n");
        }
        return sinfo.toString();
    }
}
