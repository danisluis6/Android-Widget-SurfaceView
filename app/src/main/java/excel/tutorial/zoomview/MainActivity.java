package excel.tutorial.zoomview;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    ZoomView mZoomView;
    private MediaPlayer mediaPlayer;
    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mZoomView = this.findViewById(R.id.surfaceView);
        mZoomView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(path), holder);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            public void onPrepared(MediaPlayer arg0) {
                mediaPlayer.start();
            }} );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
