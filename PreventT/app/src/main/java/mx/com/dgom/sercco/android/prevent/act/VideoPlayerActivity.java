package mx.com.dgom.sercco.android.prevent.act;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import mx.com.dgom.sercco.android.prevent.Constantes;

public class VideoPlayerActivity extends Activity {

    VideoView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoPlayer = (VideoView) findViewById(R.id.video_view);

        Intent intent = getIntent();

        String videoUrl = intent.getStringExtra(Constantes.VIDEO_URL);


//Use a media controller so that you can scroll the video contents
//and also to pause, start the video.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoPlayer);
        videoPlayer.setMediaController(mediaController);
        videoPlayer.setVideoURI(Uri.parse(videoUrl));
        videoPlayer.start();
    }
}
