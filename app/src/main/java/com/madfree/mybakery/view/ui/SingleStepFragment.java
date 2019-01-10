package com.madfree.mybakery.view.ui;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.madfree.mybakery.R;
import com.madfree.mybakery.service.model.Step;
import com.madfree.mybakery.viewmodel.SingleStepViewModel;

public class SingleStepFragment extends Fragment {

    private static final String LOG_TAG = SingleStepFragment.class.getSimpleName();

    private int mStepUID;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView descTxtView;
    private String mStepVideoUrl;
    private String mStepDesc;
    private String mStepVideoThumb;
    private Application application;

    public SingleStepFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_step, container, false);
        mPlayerView = rootView.findViewById(R.id.player_view);
        descTxtView = rootView.findViewById(R.id.step_description_txt);
        setViewModel(application, mStepUID);
        return rootView;
    }


    private void setViewModel(final Application application, int stepUID) {
        SingleStepViewModel mSingleStepViewModel = ViewModelProviders.of(this, new SingleStepViewModel(application, stepUID)).get(SingleStepViewModel.class);
        mSingleStepViewModel.getStepListObservable().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                Log.d(LOG_TAG, "Updating step from LiveData in ViewModel with " + mStepUID);
                mStepVideoUrl = step.getVideoURL();
                Log.d(LOG_TAG, "This is the VideoUrl: " + step.getVideoURL());
                mStepVideoThumb = step.getThumbnailURL();
                Log.d(LOG_TAG, "This is the VideoThumb: " + step.getThumbnailURL());
                mStepDesc = step.getDescription();
                Log.d(LOG_TAG, "This is the description: " + step.getDescription());
                descTxtView.setText(mStepDesc);
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeFile(mStepVideoThumb));
                initializePlayer(mStepVideoUrl);
            }
        });
    }

    public void setStepId(int stepUID) {
        this.mStepUID = stepUID;
    }

    private void initializePlayer(String videoUrl) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "MyBakery");
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                    new DefaultDataSourceFactory(getContext(), userAgent), extractorsFactory, null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }

    }
}
