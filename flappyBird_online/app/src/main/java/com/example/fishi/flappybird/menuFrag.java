package com.example.fishi.flappybird;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class menuFrag extends androidx.fragment.app.Fragment{

    public menuFrag(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Log.i("menuFrag", "Created");
        final View rootView = inflater.inflate(R.layout.menu_layout, container, false);
        Button playButton = (Button) rootView.findViewById(R.id.play);
        Button aiPlayButton = (Button) rootView.findViewById(R.id.aiplay);

        playButton.setOnClickListener(play);
        aiPlayButton.setOnClickListener(aiPlay);

        return rootView;
    }



    //switches to game fragment
    View.OnClickListener play = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameFragment gameFrag = new gameFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame, gameFrag).addToBackStack(null).commit();
        }
    };

    View.OnClickListener aiPlay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameFragment gameFrag = new gameFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frame, gameFrag).addToBackStack(null).commit();
            gameFrag.ai();
        }
    };

}
