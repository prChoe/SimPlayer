package com.example.massivcode.simplayer.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.massivcode.simplayer.Database.Model.MusicInfo;
import com.example.massivcode.simplayer.R;
import com.example.massivcode.simplayer.Service.MusicService;
import com.example.massivcode.simplayer.Util.MusicInfoUtil;

/**
 * Created by junsuk on 2015. 10. 12..
 */
public class PlayerActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, MusicService.CurrentInfoCommunicator, MusicService.Test {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mAlbumArtImageVIew;
    private ImageView mAlbumArtBigImageView;
    private SeekBar mSeekBar;
    private TextView mCurrentTimeTextView, mDurrationTextView;

    private Button mRepeatButton, mPreviousButton, mPlayButton, mNextButton, mShuffleButton;

    private Timer mTimer;


    private MusicService mMusicService = null;
    private MusicInfo mMusicInfo;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mMusicService = binder.getService();


            if(mMusicService.getMediaPlayer().isPlaying()) {
                mPlayButton.setSelected(true);
            }

            mMusicService.setOnCurrentInfoToPlayerActivity(PlayerActivity.this);
            mMusicService.setOnTest(PlayerActivity.this);
           mTimer =  new Timer(mMusicService.getMediaPlayer().getDuration(), 1000);
            mTimer.start();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_player);

        bindService(new Intent(PlayerActivity.this, MusicService.class), mConnection, BIND_AUTO_CREATE);

        mMusicInfo = getIntent().getParcelableExtra("info");

        mTitleTextView = (TextView) findViewById(R.id.mini_player_title_tv);
        mArtistTextView = (TextView) findViewById(R.id.mini_player_artist_tv);
        mAlbumArtImageVIew = (ImageView)findViewById(R.id.mini_player_album_art_iv);
        mAlbumArtBigImageView = (ImageView)findViewById(R.id.player_album_art_iv);
        mSeekBar = (SeekBar)findViewById(R.id.player_seekbar);
        mCurrentTimeTextView = (TextView)findViewById(R.id.player_current_time_tv);
        mDurrationTextView = (TextView)findViewById(R.id.player_duration_tv);


        mRepeatButton = (Button)findViewById(R.id.player_repeat_btn);
        mPreviousButton = (Button)findViewById(R.id.player_previous_btn);
        mPlayButton = (Button)findViewById(R.id.player_play_btn);
        mNextButton = (Button)findViewById(R.id.player_next_btn);
        mShuffleButton = (Button)findViewById(R.id.player_shuffle_btn);

        mRepeatButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mShuffleButton.setOnClickListener(this);


        mTitleTextView.setText(mMusicInfo.getTitle());
        mArtistTextView.setText(mMusicInfo.getArtist());
        mAlbumArtImageVIew.setImageBitmap(MusicInfoUtil.getBitmap(this, mMusicInfo.getUri(), 4));
        mAlbumArtBigImageView.setImageBitmap(MusicInfoUtil.getBitmap(this, mMusicInfo.getUri(), 1));
        mDurrationTextView.setText(MusicInfoUtil.getTime(mMusicInfo.getDuration()));



        mSeekBar.setOnSeekBarChangeListener(this);




    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mCurrentTimeTextView.setText(MusicInfoUtil.getTime(String.valueOf(progress)));
            mMusicService.getMediaPlayer().pause();
            mMusicService.getMediaPlayer().seekTo(progress);
            mMusicService.getMediaPlayer().start();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_repeat_btn:
                break;

            case R.id.player_previous_btn:
                if(mMusicService != null & mMusicService.isReady()) {

                    int position = mMusicService.getCurrentPosition();

                    if(position > 0) {
                        position -= 1;
                    } else {
                        position = mMusicService.getCurrentPlaylistSize();
                    }

                    Intent nextIntent = new Intent(PlayerActivity.this, MusicService.class);
                    nextIntent.setAction(MusicService.ACTION_PLAY_PREVIOUS);
                    nextIntent.putExtra("position", position);
                    startService(nextIntent);
                }
                break;

            case R.id.player_play_btn:

                Intent pauseIntent = new Intent(PlayerActivity.this, MusicService.class);
                pauseIntent.setAction(MusicService.ACTION_PAUSE);
                startService(pauseIntent);

                if(mMusicService.getMediaPlayer().isPlaying()) {
                    mPlayButton.setSelected(false);
                } else {
                    mPlayButton.setSelected(true);
                }

                break;

            case R.id.player_next_btn:
                if(mMusicService != null & mMusicService.isReady()) {

                    int position = mMusicService.getCurrentPosition();

                    if(position < mMusicService.getCurrentPlaylistSize()) {
                        position += 1;
                    } else {
                        position = 0;
                    }

                    Intent nextIntent = new Intent(PlayerActivity.this, MusicService.class);
                    nextIntent.setAction(MusicService.ACTION_PLAY_NEXT);
                    nextIntent.putExtra("position", position);
                    startService(nextIntent);
                }
                break;

            case R.id.player_shuffle_btn:
                break;

        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }


    @Override
    public void transferData(MusicInfo info) {
        mMusicInfo = info;
        mTitleTextView.setText(info.getTitle());
        mArtistTextView.setText(info.getArtist());
        mAlbumArtImageVIew.setImageBitmap(MusicInfoUtil.getBitmap(this, info.getUri(), 4));
        mAlbumArtBigImageView.setImageBitmap(MusicInfoUtil.getBitmap(this, info.getUri(), 1));
        mDurrationTextView.setText(MusicInfoUtil.getTime(info.getDuration()));
    }

    @Override
    public void test(MediaPlayer mediaPlayer) {
        mTimer.cancel();
        mTimer =  new Timer(mMusicService.getMediaPlayer().getDuration(), 1000);
        mTimer.start();

    }

    public class Timer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d(TAG, "현재 위치 : " + mMusicService.getMediaPlayer().getCurrentPosition() + "\r\n음원 길이 : " + mMusicService.getMediaPlayer().getDuration());
            mCurrentTimeTextView.setText(MusicInfoUtil.getTime(String.valueOf(mMusicService.getMediaPlayer().getCurrentPosition())));
            mSeekBar.setMax(mMusicService.getMediaPlayer().getDuration());
            mSeekBar.setProgress(mMusicService.getMediaPlayer().getCurrentPosition());
        }

        @Override
        public void onFinish() {

        }
    }

}
