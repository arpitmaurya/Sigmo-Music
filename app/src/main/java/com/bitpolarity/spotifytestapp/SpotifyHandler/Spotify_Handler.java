package com.bitpolarity.spotifytestapp.SpotifyHandler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.os.TokenWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bitpolarity.spotifytestapp.Bottom_Nav_Files.MainHolder;
import com.bitpolarity.spotifytestapp.R;
import com.bitpolarity.spotifytestapp.database_related.TempDataHolder;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.Arrays;


public class Spotify_Handler {

    // 0F:9B:82:31:61:7F:F9:DA:DC:F9:C5:B8:E1:74:E4:90:4C:85:30:83
    private static final String CLIENT_ID = "84b37e8b82e2466c9f69a2e41b100476";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    Context context;
    SharedPreferences prefs;
    TempDataHolder tempDataHolder;

    public Spotify_Handler(Context context){
        this.context = context;
        prefs=  context.getSharedPreferences("com.bitpolarity.spotifytestapp",Context.MODE_PRIVATE);
        onStart();
        tempDataHolder = new TempDataHolder();

    }


     public void onStart() {


        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("Spotify_Handler", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("Spotify_Handler", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }


    public void onStop() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }


    public void connected() {

       //TempDataHolder mDetail_holder = new TempDataHolder();

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()

                .subscribeToPlayerState()
                .setEventCallback( playerState -> {
                    final Track track = playerState.track;

                    if (track != null) {

                        String trackName = track.name;
                        String trackArtist = String.valueOf(track.artist.name);

                        Log.d("MainActivity", trackName + " by " + trackArtist);
                        Log.d("MainActivity", String.valueOf(track.imageUri));
                        Log.d("MainActivity", track.uri);
                        Log.d("MainActivity", String.valueOf(track.album));
                        Log.d("MainActivity Paused ? ", String.valueOf(playerState.isPaused));
                        Log.d("MainActivity", String.valueOf(playerState.playbackPosition));
                        Log.d("MainActivity", String.valueOf(playerState.playbackOptions));
                        String url = "https://" + "i.scdn.co/image/" + track.imageUri.toString().substring(22, track.imageUri.toString().length() - 2);

                        mDetail_Holder appDetails = mDetail_Holder.getInstance();
                        appDetails.setSong_Title(trackName);


                    }

                });

    }
}
