package com.study.mallr.studycatch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static android.widget.Toast.LENGTH_LONG;


public class Questions extends AppCompatActivity  {

    String response_bearer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        //ArrayList ids_needed = (ArrayList) getIntent().getSerializableExtra("set_ids_to_use");

        //Toast.makeText(getApplicationContext(), String.valueOf(ids_needed.get(0)), Toast.LENGTH_SHORT).show();




        final Button answerbtn_1 = (Button)findViewById(R.id.anwser1_btn);
        final Button answerbtn_2 = (Button)findViewById(R.id.anwser2_btn);
        final Button answerbtn_3 = (Button)findViewById(R.id.anwser3_btn);
        final Button answerbtn_4 = (Button)findViewById(R.id.anwser4_btn);
        answerbtn_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               // String path = URI.uri.toString();
              //  Toast.makeText(getApplicationContext(), "RESPONSE_BEARER: " + /*response_bearer*/ , Toast.LENGTH_LONG).show();
                answerbtn_2.setBackgroundColor(Color.rgb(00,255,00));
                answerbtn_2.setText(answerbtn_2.getText()+" (✔)");
                answerbtn_1.setBackgroundColor(Color.rgb(255,00,00));
                answerbtn_1.setText(answerbtn_1.getText()+" (✘)");
            }

        });
        answerbtn_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // String path = URI.uri.toString();
                //  Toast.makeText(getApplicationContext(), "RESPONSE_BEARER: " + /*response_bearer*/ , Toast.LENGTH_LONG).show();
                answerbtn_2.setBackgroundColor(Color.rgb(00,255,00));
                answerbtn_2.setText(answerbtn_2.getText()+" (✔)");
                //answerbtn_1.setBackgroundColor(Color.rgb(99,00,00));
            }

        });
        answerbtn_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // String path = URI.uri.toString();
                //  Toast.makeText(getApplicationContext(), "RESPONSE_BEARER: " + /*response_bearer*/ , Toast.LENGTH_LONG).show();
                answerbtn_2.setBackgroundColor(Color.rgb(00,255,00));
                answerbtn_2.setText(answerbtn_2.getText()+" (✔)");
                answerbtn_3.setBackgroundColor(Color.rgb(255,00,00));
                answerbtn_3.setText(answerbtn_3.getText()+" (✘)");
            }

        });
        answerbtn_4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // String path = URI.uri.toString();
                //  Toast.makeText(getApplicationContext(), "RESPONSE_BEARER: " + /*response_bearer*/ , Toast.LENGTH_LONG).show();
                answerbtn_2.setBackgroundColor(Color.rgb(00,255,00));
                answerbtn_2.setText(answerbtn_2.getText()+" (✔)");
                answerbtn_4.setBackgroundColor(Color.rgb(255,00,00));
                answerbtn_4.setText(answerbtn_4.getText()+" (✘)");
            }

        });
    }

}

