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

import com.google.android.exoplayer2.C;
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

    private int mStepId;
    private int mStepUID;
    private int mRecipeId;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView descTxtView;
    private String mStepVideoUrl;
    private String mStepDesc;
    private String mStepVideoThumb;
    private Application application;
    private long playerPosition;
    private boolean mTwoPane;

    public SingleStepFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mTwoPane) {
            mStepId = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_single_step, container, false);
        mPlayerView = rootView.findViewById(R.id.player_view);
        descTxtView = rootView.findViewById(R.id.step_description_txt);
        setViewModel(application, mRecipeId, mStepId, mTwoPane);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            playerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStepVideoUrl != null)
            initializePlayer(mStepVideoUrl);
    }


    private void setViewModel(final Application application, int mRecipeId, final int mStepId, boolean mTwoPane) {
        SingleStepViewModel mSingleStepViewModel = ViewModelProviders.of(this,
                new SingleStepViewModel(application, mRecipeId, mStepId, mTwoPane)).get(SingleStepViewModel.class);
        mSingleStepViewModel.getStepListObservable().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {

                Log.d(LOG_TAG, "Updating step from LiveData in ViewModel with " + mStepId);
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

    public void setRecipeAndStepId(int recipeId, int stepId) {
        this.mRecipeId = recipeId;
        this.mStepId = stepId;
    }

    private void initializePlayer(String videoUrl) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector,
                    loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "MyBakery");
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                    new DefaultDataSourceFactory(getContext(), userAgent), extractorsFactory,
                    null, null);
            // ToDo: Current issue - upon rotation the video starts twice!!
            if (playerPosition != C.TIME_UNSET) {
                mExoPlayer.seekTo(playerPosition);
            }
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }

    }

    public void setTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }
}
