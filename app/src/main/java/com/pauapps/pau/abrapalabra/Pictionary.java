package com.pauapps.pau.abrapalabra;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Pictionary extends AppCompatActivity {

    private DB db;

    private InterstitialAd   mInterstitialAd;

    //test comment

    private int iClicks = -1;
    private int aciertos = 0;
    private final int timeStop = 60;
    private boolean sonido = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictionary);
        db = new DB(this);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/SunriseInternationalDemo.otf");


        TextView text = (TextView) findViewById(R.id.text);
        text.setTypeface(type);

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-8428748101355923~5729639689");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                stop();
            }
        });


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8428748101355923/4871675974");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                stop();
            }

        });

        final MediaPlayer sound = MediaPlayer.create(this, R.raw.reloj);
        final MediaPlayer soundfinal = MediaPlayer.create(this, R.raw.sfinals);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView txtClicks = (TextView) findViewById(R.id.txtClicks);
                        // task to be done every 1000 milliseconds
                        if (iClicks < 0) {
                            txtClicks.setText("60");
                        } else {
                            if (sonido) {
                                if (iClicks <= 49) {
                                    sound.start();
                                } else {
                                    soundfinal.start();
                                }
                            }
                            iClicks = iClicks + 1;
                            txtClicks.setText(String.valueOf(iClicks));
                        }
                        if (iClicks == timeStop) {
                            reset();
                            sound.stop();
                            soundfinal.stop();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }

    public void onClickSi(View v) {
        if (sonido) {
            final MediaPlayer sound = MediaPlayer.create(this, R.raw.point);
            sound.start();
        }
        aciertos += 1;
        TextView taciertos = (TextView) findViewById(R.id.naciertos);
        taciertos.setText("" + aciertos);
        doRandom();
    }

    public void onClickNo(View v) {
        reset();
    }

    public void random(View v) {
        doRandom();
    }

    public void doRandom() {
        ArrayList<String> array_list = db.getPalabras();
        String randomText = array_list.get(new Random().nextInt(array_list.size()));
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(randomText);
        iClicks = 0;
        TextView txtClicks = (TextView) findViewById(R.id.txtClicks);
        txtClicks.setText(String.valueOf(iClicks));
        System.out.println("" + db.numberOfRows());
    }

    private void reset() {
        RelativeLayout pic = (RelativeLayout) findViewById(R.id.activity_pictionary);
        pic.setBackgroundColor(Color.RED);
        if (sonido) {
            final MediaPlayer sound = MediaPlayer.create(this, R.raw.gong);
            sound.start();
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            stop();
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    private void stop() {
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("Click para empezar!");
        iClicks = -1;
        TextView txtClicks = (TextView) findViewById(R.id.txtClicks);
        txtClicks.setText("");
        aciertos = 0;
        TextView taciertos = (TextView) findViewById(R.id.naciertos);
        taciertos.setText("" + aciertos);
        RelativeLayout pic = (RelativeLayout) findViewById(R.id.activity_pictionary);
        pic.setBackgroundColor(Color.WHITE);
    }

    public void sound(View v) {
        ImageView so;
        if (sonido) {
            so = (ImageView) findViewById(R.id.so);
            so.setImageResource(R.drawable.soundoff);
            sonido = false;
        } else {
            so = (ImageView) findViewById(R.id.so);
            so.setImageResource(R.drawable.soundon);
            sonido = true;
        }
    }
}
