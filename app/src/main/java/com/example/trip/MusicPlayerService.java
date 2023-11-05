package com.example.trip;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicPlayerService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.come_and_get_your_love); // Replace with your music file resource
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start(); // Start playing the music
        return START_NOT_STICKY; // The service will not be automatically restarted if it's killed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop(); // Stop playing the music
        mediaPlayer.release(); // Release the resources used by the MediaPlayer
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // We don't need to bind to this service, so return null
    }
}
