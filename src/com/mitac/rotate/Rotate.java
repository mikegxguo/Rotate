package com.mitac.rotate;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.AudioCapabilities;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecInfo.VideoCapabilities;
import static android.media.MediaCodecInfo.CodecProfileLevel.*;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import static android.media.MediaFormat.MIMETYPE_VIDEO_AVC;
import static android.media.MediaFormat.MIMETYPE_VIDEO_H263;
import static android.media.MediaFormat.MIMETYPE_VIDEO_HEVC;
import static android.media.MediaFormat.MIMETYPE_VIDEO_MPEG4;
import static android.media.MediaFormat.MIMETYPE_VIDEO_VP8;
import static android.media.MediaFormat.MIMETYPE_VIDEO_VP9;
import android.media.MediaPlayer;

//import com.android.compatibility.common.util.ApiLevelUtil;
//import com.android.compatibility.common.util.DynamicConfigDeviceSide;
//import com.android.compatibility.common.util.MediaUtils;





public class Rotate extends Activity {
    /** Called when the activity is first created. */
    private TextView mTextView01;
    private Thread mThread;
    private static final int MSG_AUTO_ROTATE = 0x1000;
    private static String VERSION = "1.2.0.0    2018/7/10 13:25";
    
    private static String TAG = "MIKE";
    
    private IntentFilter mIntentFilter;
    //private PendingIntent pi;
    //private AlarmManager am;
    private Context mContext;

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "mIntentReceiver");
            
            PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, new Intent("MIKE"),PendingIntent.FLAG_CANCEL_CURRENT);        
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);  
            am.cancel(pi);
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  SystemClock.elapsedRealtime()+60*60*1000,  pi);   
        }
    };

    public void printMediaCodecInfo() {
        int CodecCount = 0;
        Log.d(TAG, "printMediaCodecInfo 0000");
        try {
            CodecCount = MediaCodecList.getCodecCount();
        }catch (Exception e) {
            Log.e(TAG, "##### Failed to get codec count!");
            e.printStackTrace();
            return;
        }

        Log.d(TAG, "printMediaCodecInfo codec count:"+CodecCount);
        for (int i = 0; i < CodecCount; ++i) {
            MediaCodecInfo info = null;
            try {
                info = MediaCodecList.getCodecInfoAt(i);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Cannot retrieve decoder codec info", e);
            }
            if (info == null) {
                continue;
            }

            Log.d(TAG, "printMediaCodecInfo info:"+info.getName());
            String codecInfo = "MediaCodec, name="+info.getName()+", [";
            for (String mimeType : info.getSupportedTypes()) {
                codecInfo += mimeType + ",";
                MediaCodecInfo.CodecCapabilities capabilities;
                try {
                    capabilities = info.getCapabilitiesForType(mimeType);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Cannot retrieve decoder capabilities", e);
                    continue;
                }
                codecInfo += " max inst:"+capabilities.getMaxSupportedInstances()+",";
                String strColorFormatList = "";
                for (int colorFormat : capabilities.colorFormats) {
                    strColorFormatList += " 0x" + Integer.toHexString(colorFormat);
                }
                codecInfo += strColorFormatList + "] [";
            }
            Log.w(TAG, codecInfo);
        }

//        boolean bSupportHwVP8 = MediaCodecVideoEncoder.isVp8HwSupported();
//        boolean bSupportHwVP9 = MediaCodecVideoEncoder.isVp9HwSupported();
//        boolean bSupportHwH264 = MediaCodecVideoEncoder.isH264HwSupported();
//        String webrtcCodecInfo = "WebRTC codec support: HwVP8=" + bSupportHwVP8 + ", HwVP9=" + bSupportHwVP9
//            + ", Hw264=" + bSupportHwH264;
//        if(bSupportHwH264) {
//            webrtcCodecInfo += ", Hw264HighProfile=" + MediaCodecVideoEncoder.isH264HighProfileHwSupported();
//        }
//        Log.w(TAG, webrtcCodecInfo);
    }    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTextView01 = (TextView) findViewById(R.id.myTextView1);
        mTextView01.setText(VERSION);
        
        mContext = this;
        Log.d(TAG, "MIKE MIKE");
        printMediaCodecInfo();//FOR TEST
        
/*        
        Intent intent = new Intent("MIKE_MIKE_MIKE_MIKE");
        intent.putExtra("msg","Hello");  
        pi = PendingIntent.getBroadcast(this,0,intent,0);        
        am = (AlarmManager)getSystemService(ALARM_SERVICE);  
*/
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
//        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
//        registerReceiver(mIntentReceiver, mIntentFilter);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterReceiver(mIntentReceiver);
        super.onDestroy();
    }
    
    
    public void onAutoRotate(View v) {
        if (mThread == null) {
            mThread = new Thread(runnable);
            mThread.start();

        } else {
            Toast.makeText(getApplication(),
                    getApplication().getString(R.string.thread_started),
                    Toast.LENGTH_LONG).show();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_AUTO_ROTATE:
                onRotate(null);
                break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.obtainMessage(MSG_AUTO_ROTATE, null).sendToTarget();
            mHandler.postDelayed(this, 2000);
        }
    };

    public void onRotate(View v) {
        if (getRequestedOrientation() == -1) {
            mTextView01.setText(getResources().getText(R.string.str_err_1001));
        } else {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                Log.d(TAG, "SCREEN_ORIENTATION_PORTRAIT");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                Log.d(TAG, "SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                Log.d(TAG, "SCREEN_ORIENTATION_REVERSE_PORTRAIT");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                Log.d(TAG, "SCREEN_ORIENTATION_LANDSCAPE");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }
    
    public class MyReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // TODO Auto-generated method stub
            Log.d(TAG, "onclock......................");
            String msg = intent.getStringExtra("msg");
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }

    }    

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        // TODO Auto-generated method stub

        switch (requestedOrientation) {
        case (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE):
            // mMakeTextToast(
            // getResources().getText(R.string.str_msg1).toString(), false);
            break;
        case (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT):
            // mMakeTextToast(
            // getResources().getText(R.string.str_msg2).toString(), false);
            break;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    @Override
    public int getRequestedOrientation() {
        // TODO Auto-generated method stub

        return super.getRequestedOrientation();
    }

    public void mMakeTextToast(String str, boolean isLong) {
        if (isLong == true) {
            Toast.makeText(Rotate.this, str, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Rotate.this, str, Toast.LENGTH_SHORT).show();
        }
    }

}
