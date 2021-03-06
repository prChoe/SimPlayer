package com.example.massivcode.simplayer.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.massivcode.simplayer.R;

/**
 * Created by massivCode on 2015-10-10.
 */
public class PlayerFragment extends android.support.v4.app.Fragment{

    private static final String TAG = PlayerFragment.class.getSimpleName();
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mAlbumArtImageVIew;
    private ImageView mAlbumArtBigImageView;
    private SeekBar mSeekBar;
    private TextView mCurrentTimeTextView, mDurrationTextView;

    public static PlayerFragment getInstance(Parcelable parcelable) {
        PlayerFragment fragment = new PlayerFragment();
        if (parcelable == null) {
            return fragment;
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable("info", parcelable);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.mini_player_title_tv);
        mArtistTextView = (TextView) view.findViewById(R.id.mini_player_artist_tv);
        mAlbumArtImageVIew = (ImageView)view.findViewById(R.id.mini_player_album_art_iv);
        mAlbumArtBigImageView = (ImageView)view.findViewById(R.id.player_album_art_iv);
        mSeekBar = (SeekBar)view.findViewById(R.id.player_seekbar);
        mCurrentTimeTextView = (TextView)view.findViewById(R.id.player_current_time_tv);
        mDurrationTextView = (TextView)view.findViewById(R.id.player_duration_tv);

//        if (getArguments() != null) {
//            MusicInfo info = getArguments().getParcelable("info");
//            mTitleTextView.setText(info.getTitle());
//            mArtistTextView.setText(info.getArtist());
//            mAlbumArtImageVIew.setImageBitmap(MusicInfoUtil.getBitmap(getActivity(), info.getAlbumArt(), 4));
//            mAlbumArtBigImageView.setImageBitmap(MusicInfoUtil.getBitmap(getActivity(), info.getAlbumArt(), 1));
//            mDurrationTextView.setText(MusicInfoUtil.getTime(info.getDuration()));
//        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
