package com.pauapps.pau.abrapalabra;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Pau on 19/6/2017.
 */

public class Principal extends AppCompatActivity {
    private DB db;

    private InterstitialAd mInterstitialAdFirst;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        db = new DB(this);

        db.numberOfRows();

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/SunriseInternationalDemo.otf");

        Button informacion = (Button) findViewById(R.id.informacion);
        informacion.setTypeface(type);

        Button jugar = (Button) findViewById(R.id.jugar);
        jugar.setTypeface(type);

        mInterstitialAdFirst = new InterstitialAd(this);
        mInterstitialAdFirst.setAdUnitId("ca-app-pub-8428748101355923/1999674042");
        mInterstitialAdFirst.loadAd(new AdRequest.Builder().build());

        mInterstitialAdFirst.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAdFirst.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {

            }

        });


    }

    public void onClickJugar(View v) {
        if (mInterstitialAdFirst.isLoaded()) {
            mInterstitialAdFirst.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
        startActivity(new Intent(Principal.this, Pictionary.class));
    }


    public void onClickInformación(View v) {
        startActivity(new Intent(Principal.this, Informacion.class));
        System.out.println("" + db.numberOfRows());
    }
}
