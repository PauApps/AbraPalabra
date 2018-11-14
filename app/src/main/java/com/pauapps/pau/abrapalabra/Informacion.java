package com.pauapps.pau.abrapalabra;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Pau on 20/6/2017.
 */

public class Informacion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/SunriseInternationalDemo.otf");

        String info = "informacion.txt";
        String str;
        StringBuilder buf = new StringBuilder();
        InputStream is = null;
        try {
            is = this.getAssets().open(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert is != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            while ((str = reader.readLine()) != null) {
                buf.append(str).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.information);
        tv.setText(buf.toString());

        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(type);
        tv.setTypeface(type);

    }
}
