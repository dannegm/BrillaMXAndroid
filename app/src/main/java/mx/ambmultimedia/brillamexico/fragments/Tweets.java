package mx.ambmultimedia.brillamexico.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;


@SuppressLint("ValidFragment")
public class Tweets extends Fragment {
    Context ctx;
    Config config;


    public Tweets (Context _ctx) {
        ctx = _ctx;
    }
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout;
        layout = inflater.inflate(R.layout.fragment_tweets, container, false);
        config = new Config(ctx);
        return layout;
    }
}
