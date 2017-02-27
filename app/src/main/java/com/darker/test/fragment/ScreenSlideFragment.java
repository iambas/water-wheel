package com.darker.test.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.darker.test.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.lang.reflect.Array;

public class ScreenSlideFragment extends Fragment{

    private int page;

    // youtube id
    private static final String DEVELOPER_KEY = "AIzaSyB9jU54eqpsIfgbj3kADXqA7OfzegSjSfk";
    private static final String VIDEO_ID = "JfYKTTYn9As";

    public ScreenSlideFragment() {}

    public static ScreenSlideFragment newInstance(int page) {
        ScreenSlideFragment fragment = new ScreenSlideFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.page = getArguments().getInt("page");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_screen_slide, container, false);
        if (page == 1){
            return inflater.inflate(R.layout.fragment_history, container, false);
        }else if (page == 2){
            return inflater.inflate(R.layout.fragment_factor, container, false);
        }else if (page == 3){
            return inflater.inflate(R.layout.fragment_benefit, container, false);
        }else if (page == 4){

            // YouTube
            YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

            youTubePlayerFragment.initialize(DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    if (!wasRestored) {
                        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        player.loadVideo(VIDEO_ID);
                        player.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                    // YouTube error
                    String errorMessage = error.toString();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.d("errorMessage:", errorMessage);
                }
            });

            return inflater.inflate(R.layout.fragment_video, container, false);
        }else {
            return inflater.inflate(R.layout.fragment_history, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView;
        String[] text;
        String msg;

        switch (page){
            case 1:
                textView = (TextView) view.findViewById(R.id.ct_history);
                text = getResources().getStringArray(R.array.text_history);
                msg = new String();
                for (int i = 0; i < Array.getLength(text); i++)
                    msg += String.format("          %s\n\n", text[i]);
                textView.setText(msg);
                break;
            case 2:
                textView = (TextView) view.findViewById(R.id.ct_factor);
                text = getResources().getStringArray(R.array.text_factor);
                msg = new String();
                for (int i = 0; i < Array.getLength(text); i++)
                    msg += String.format("%s\n\n", text[i]);
                textView.setText(msg);
                break;
            case 3:
                textView = (TextView) view.findViewById(R.id.ct_benefit);
                text = getResources().getStringArray(R.array.text_benefit);
                msg = new String();
                for (int i = 0; i < Array.getLength(text); i++)
                    msg += String.format("%s\n\n", text[i]);
                textView.setText(msg);
                break;
            case 4:
                textView = (TextView) view.findViewById(R.id.text_video);
                text = getResources().getStringArray(R.array.video);
                msg = String.format("%s\n\n          %s", text[0], text[1]);
                textView.setText(msg);
                break;
            default:    break;
        }
    }
}