package com.example.massivcode.simplayer.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class PlayerActivity extends FragmentActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, MusicService.TestListener {

    private static final String TAG = PlayerActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mAlbumArtImageVIew;
    private ImageView mAlbumArtBigImageView;
    private SeekBar mSeekBar;
    private TextView mCurrentTimeTextView, mDurrationTextView;

    private Button mRepeatButton, mPreviousButton, mPlayButton, mNextButton, mShuffleButton;


    private MusicService mService = null;
    private MusicInfo mMusicInfo;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
            mSeekBar.setMax(mService.getMediaPlayer().getDuration());
            if(mService.getMediaPlayer().isPlaying()) {
                mPlayButton.setSelected(true);
            }
            mService.setOnTestListener(PlayerActivity.this);
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
        mAlbumArtImageVIew.setImageBitmap(MusicInfoUtil.getBitmap(this, mMusicInfo.getAlbumArt(), 4));
        mAlbumArtBigImageView.setImageBitmap(MusicInfoUtil.getBitmap(this, mMusicInfo.getAlbumArt(), 1));
        mDurrationTextView.setText(MusicInfoUtil.getTime(mMusicInfo.getDuration()));



        mSeekBar.setOnSeekBarChangeListener(this);





    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mCurrentTimeTextView.setText(MusicInfoUtil.getTime(String.valueOf(progress)));
            mService.getMediaPlayer().pause();
            mService.getMediaPlayer().seekTo(progress);
            mService.getMediaPlayer().start();
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
                break;

            case R.id.player_play_btn:

                if(mService.getMediaPlayer().isPlaying()) {
                    mPlayButton.setSelected(false);
                } else {
                    mPlayButton.setSelected(true);
                }

                Intent pauseMusic = new Intent(PlayerActivity.this, MusicService.class);
                pauseMusic.setAction(MusicService.ACTION_PAUSE);
                pauseMusic.setData(null);
                startService(pauseMusic);

                break;

            case R.id.player_next_btn:
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
    public void getCurrentPosition(final int currentPosition) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "" + currentPosition);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private class UpdateAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "case1");
            while(true) {
                publishProgress();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                   break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d(TAG, "case2");
            if(mSeekBar.getProgress() == mService.getMediaPlayer().getDuration()) {
                Log.d(TAG, "case3");
                mCurrentTimeTextView.setText(MusicInfoUtil.getTime(String.valueOf(mService.getMediaPlayer().getCurrentPosition())));
                mSeekBar.setProgress(mService.getMediaPlayer().getCurrentPosition());
            }
            super.onProgressUpdate(values);
        }
    }
}
