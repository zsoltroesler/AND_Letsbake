package com.example.android.letsbake.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.example.android.letsbake.models.Recipe;
import com.example.android.letsbake.models.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.android.letsbake.fragments.RecipeDetailsFragment.STEP_RECIPE_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepsFragment extends Fragment implements Player.EventListener {

    private static final String LOG_TAG = RecipeStepsFragment.class.getSimpleName();
    private static final String PLAYER_POSITION = "exoPlayer position";


    private Context context;
    private ArrayList<Step> stepsList = new ArrayList<>();
    private Step step;
    private int stepId;
    private String stepDescription;
    private String stepVideoUrl;
    private String stepThumbnailUrl;
    private Unbinder unbinder;
    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private long exoPlayerPosition;

    @BindView(R.id.rl_player)
    RelativeLayout playerRelativeLayout;
    @BindView(R.id.player_step)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.iv_step_placeholder)
    ImageView placeholderImage;
    @BindView(R.id.tv_step_description)
    TextView descriptionText;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        unbinder = ButterKnife.bind(this, view);

        // Get the data from the Bundle object
        step = getArguments().getParcelable(STEP_RECIPE_KEY);

        getActivity().setTitle(step.getStepShortDescription());

        descriptionText.setText(step.getStepDescription());

        stepVideoUrl = step.getStepVideoUrl();
        stepThumbnailUrl = step.getStepThumbnailUrl();

        exoPlayerPosition = C.TIME_UNSET;

        if (savedInstanceState == null) {

            if (!stepVideoUrl.isEmpty()) {
                // Hide the placeholder image
                placeholderImage.setVisibility(View.GONE);

                // Initialize the Media Session.
                initializeMediaSession();

                // Initialize the ExoPlayer.
                initializePlayer(Uri.parse(stepVideoUrl));
            } else {
                // Hide ExoPlayer
                exoPlayerView.setVisibility(View.GONE);

                // Load the corresponding thumbnail image url or if it is empty load the placeholder image
                Picasso.with(getActivity())
                        .load(stepThumbnailUrl)
                        .placeholder(R.drawable.ic_baking_placeholder)
                        .error(R.drawable.ic_baking_placeholder)
                        .into(placeholderImage);
            }
        } else {
            exoPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION, C.TIME_UNSET);
        }
        return view;
}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PLAYER_POSITION, exoPlayerPosition);
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param videoUri The URI of the sample to play.
     */
    private void initializePlayer(Uri videoUri) {

        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), LOG_TAG);
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (exoPlayerPosition != C.TIME_UNSET) exoPlayer.seekTo(exoPlayerPosition);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            //exoPlayer.seekTo(exoPlayerPosition);
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     * This code snippet was used from course material of Udacity
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mediaSession = new MediaSessionCompat(getActivity(), LOG_TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(playbackStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSession.setCallback(new MediaSessionCallback());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);

    }

/**
 * Media Session Callbacks, where all external clients control the player.
 */
private class MediaSessionCallback extends MediaSessionCompat.Callback {
    @Override
    public void onPlay() {
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
        exoPlayer.seekTo(0);
    }

}

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (exoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayerPosition = exoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stepVideoUrl != null)
            initializePlayer(Uri.parse(stepVideoUrl));
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
