package org.scorelab.soundcom;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lasith on 9/17/16.
 */

public class RecordingService extends Service {

    private MediaRecorder myAudioRec;
    private String outFile=null;

    public IBinder onBind(Intent arg0){
        return null;
    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Log.d("Service test", "from the onHandleIntent");
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy_hhmmss");
        String format = s.format(new Date());
        outFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording_"+format+".mp4";

        myAudioRec = new MediaRecorder();
        myAudioRec.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRec.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRec.setOutputFile(outFile);
        try {
            myAudioRec.prepare();
            myAudioRec.start();
        } catch (IllegalStateException e){
            e.printStackTrace();
        } catch (IOException  e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Sound recording activated.", Toast.LENGTH_SHORT).show();
        Timer myTimer;

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    myAudioRec.stop();
                    myAudioRec.reset();
                    myAudioRec.release();
                    myAudioRec = null;

                    stopSelf();
                    //onDestroy();  I added this one to test it out. not sure.

                } catch (IllegalStateException e) {
                    //  it is called before start()
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    // no valid audio/video data has been received
                    e.printStackTrace();
                }
            }

        }, 60000 * 5); //5 minutes timer to stop the recording after 90 minutes

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        //onCreate();
        // return 1;
        return START_STICKY; //not sure about this one
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
//what about this one ?
    // @Override
    // public void onDestroy() {
//  myRecorder.stop();
//  myRecorder.reset();
//  myRecorder.release();
    //}

}
