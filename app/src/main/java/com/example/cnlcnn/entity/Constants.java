package com.example.cnlcnn.entity;
/*
 *  项目名：  WallPaper
 *  文件名:   Constants
 *  创建者:   LiChuang
 *  创建时间:  2017/5/15
 *  描述：    常量类
 */

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.Gravity;

import com.example.cnlcnn.utils.L;
import com.example.cnlcnn.view.CustomDialog;
import com.example.cnlcnn.wallpaper.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Constants {

    //传递图片的ID值
    public static final String PICTURE_IMAGE_ID = "picture_image_id";
    //传递的标题或是类型
    public static final String TYPE_NAME = "type_name";

    //来自网络图片标志
    public static final String NET_SIGN = "NET_PICTURE";

    //来自网络图片的路径
    public static final String NET_PICTURE_PATH = "NET_PICTURE_PATH";

    //从本地找到的图片路径
    public static final String LOCAL_PICTURE_PATH = "LOCAL_PICTURE_PATH";

    //本地图片缓存文件夹
    public static final String WALLPAPER_CACHE = "/wallPaperlocal/";
    //下载图片文件夹
    public static final String WALLPAPER_DOWNLOAD = "/WPDownload/";

    public static final String WALLPAPER_BASE_URL = "http://open.lovebizhi.com/";

    //QQ下载地址
    public static final String URL_DOWNLOAD_QQ = "http://app.sina.cn/appdetail.php?appID=100928";
    //微博下载地址
    public static final String URL_DOWNLOAD_SINA = "http://app.sina.cn/appdetail.php?appID=84560";
    //微信下载地址
    public static final String URL_DOWNLOAD_WECHAT = "http://app.sina.cn/appdetail.php?appID=93134";

    //停止动画
    public static final int HANDLER_STOP_ANIMATION = 1003;

    //封装dialog
    public static CustomDialog showDialog(Context mContext, int layout) {
        //初始化提示框
        CustomDialog dialog = new CustomDialog(mContext, 0, 0,
                layout, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        return dialog;
    }

    //获取版本号
    public static String getAppVersion(Context mContext) {
        String version = "";
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            version = "获取失败";
        }
        return version;
    }

    //获取手机IP
    public static String getPhoneIp(Context mContext) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    //地址算法
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    //获取MAC地址
    public static String getMacAddress(Context mContext) {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    //获取内存卡可用内存
    public static String getSdAvailableMemory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            // 获取block的SIZE
            long blocSize = statfs.getBlockSize();
            // 获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            // 空闲的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            // 计算总空间大小和空闲的空间大小
            long availableSize = blocSize * availaBlock;
            long allSize = blocSize * totalBlocks;
            return "可用：" + availableSize / 1024 / 1024 / 1024 + "GB"
                    + "  总共：" + allSize / 1024 / 1024 / 1024 + "GB";
        } else {
            return "SD卡不可用";
        }
    }

    //获取当前网络状态
    public static String getNetworkState(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return "无网络连接";
        } else {
            return "网络正常";
        }
    }

    //获得可用内存
    public static String getAvailMemory(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(mContext, mi.availMem);
    }

    //获取总内存
    public static String getTotalMemory(Context mContext) {
        // 系统内存信息文件
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            // 读取meminfo第一行，系统总内存大小
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            // 获得系统总内存，单位是KB，乘以1024转换为Byte
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();

        } catch (IOException e) {
            L.e("error:" + e.toString());
        }
        // Byte转换为KB或者MB，内存大小规格化
        return Formatter.formatFileSize(mContext, initial_memory);
    }

    // 1-cpu型号 2-cpu频率
    public static String[] cpuInfo = {"", ""};

    //获取CPU属性
    public static void getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            L.i("error:" + e.toString());
        }
    }

    //获取IMEI
    public static String getImei(Context mContext) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) mContext.
                getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        return imei;
    }

    //系统分享
    public static void intentSystemShare(Context mContext, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(intent);
    }

    //跳转QQ 可指定好友
    public static void intentStartQQ(final Context mContext, String text) {
        if (isInstall(mContext, "com.tencent.mobileqq")) {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=100000&version=1";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            mContext.startActivity(intent);
        } else {
            showNoInstallDialog(mContext, mContext.getString(R.string.is_install_qq), URL_DOWNLOAD_QQ);
        }
    }

    //跳转微博 可分享图片等
    public static void intentStartSina(final Context mContext, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String packageName = "com.sina.weibo";
        ResolveInfo info = null;
        for (ResolveInfo each : matches) {
            String pkgName = each.activityInfo.applicationInfo.packageName;
            if (packageName.equals(pkgName)) {
                info = each;
                break;
            }
        }
        if (info == null) {
            showNoInstallDialog(mContext, mContext.getString(R.string.is_install_sina), URL_DOWNLOAD_SINA);
        } else {
            intent.setClassName(packageName, info.activityInfo.name);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            mContext.startActivity(intent);
        }
    }

    //跳转到微信
    public static void intentStartWechat(Context mContext, String text) {
        if (isInstall(mContext, "com.tencent.mm")) {
            Intent intent = new Intent();
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.setAction(Intent.ACTION_VIEW);
            mContext.startActivity(intent);
        } else {
            showNoInstallDialog(mContext, mContext.getString(R.string.is_install_wechat), URL_DOWNLOAD_WECHAT);
        }
    }

    //判断程序是否安装
    public static boolean isInstall(Context mContext, String packageName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        ResolveInfo info = null;
        for (ResolveInfo each : matches) {
            String pkgName = each.activityInfo.applicationInfo.packageName;
            if (packageName.equals(pkgName)) {
                info = each;
                break;
            }
        }
        if (info == null) {
            return false;
        } else {
            return true;
        }
    }

    //提示未安装
    public static void showNoInstallDialog(final Context mContext, String message, final String url) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse(url);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                mContext.startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }
}
