package com.study.mallr.studycatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.widget.Toast.LENGTH_LONG;
import static java.lang.Thread.sleep;


public class Questions extends AppCompatActivity  {

    String response_bearer;
    Vector<Integer> set_ids_in_use = new Vector<>();
    Button answerbtn_1, answerbtn_2, answerbtn_3, answerbtn_4;
    String definition;
    Vector<Button> answer_buttons = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int shared_entries_size = sharedPref.getAll().size();
        answerbtn_1 = (Button)findViewById(R.id.anwser1_btn);
        answerbtn_2 = (Button)findViewById(R.id.anwser2_btn);
        answerbtn_3 = (Button)findViewById(R.id.anwser3_btn);
        answerbtn_4 = (Button)findViewById(R.id.anwser4_btn);

        //Get set_ids from Shared Preferences
        for(int i = 0; i<shared_entries_size; i++){
            int entry = sharedPref.getInt("setId_"+i, 0);
            if(entry != 0){
                set_ids_in_use.add(entry);
                System.out.println("DATABASE: "+entry);
            }
        }

        getQuestions(set_ids_in_use);
        buttonHandler();
    }

    public void getQuestions(Vector<Integer> possible_sets){
        //get a random set
        Collections.shuffle(possible_sets);
        final Integer set_id = possible_sets.get(0);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String token = sharedPref.getString("access_token", "");

        //HTTP Request to Pull JSON
        Thread thread = new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://api.quizlet.com/2.0/sets/"+set_id+"?client_id=xW5p9usgDg&access_token="+token)
                        .build();
                System.out.println("URL: "+"https://api.quizlet.com/2.0/sets/"+set_id+"?client_id=xW5p9usgDg&access_token="+token);
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject data = null;
                    Log.e("json", json);
                    try {
                        data = new JSONObject(json);
                        JSONArray jsonArray = data.getJSONArray("terms");

                        //get Random question in JSON
                        ArrayList<Integer> rand_index_list = new ArrayList();
                        for(int i = 0; i<jsonArray.length()-1; i++){
                            rand_index_list.add(i);
                        }
                        Collections.shuffle(rand_index_list);

                        JSONObject random_question = jsonArray.getJSONObject(rand_index_list.get(0));
                        String term = random_question.optString("term");
                        definition = random_question.optString("definition");

                        //get random answers
                        random_question = jsonArray.getJSONObject(rand_index_list.get(1));
                        String wrong_definition_1 = random_question.optString("definition");
                        random_question = jsonArray.getJSONObject(rand_index_list.get(2));
                        String wrong_definition_2 = random_question.optString("definition");
                        random_question = jsonArray.getJSONObject(rand_index_list.get(3));
                        String wrong_definition_3 = random_question.optString("definition");

                        System.out.println("QUESTION: "+term);
                        System.out.println("ANSWER: "+definition);
                        System.out.println("FAKE: "+wrong_definition_1);
                        System.out.println("FAKE: "+wrong_definition_2);
                        System.out.println("FAKE: "+wrong_definition_3);

                        TextView question_title = (TextView)findViewById(R.id.question_title);
                        question_title.setText(term);
                        ArrayList<String> questions = new ArrayList();
                        questions.add(definition);
                        questions.add(wrong_definition_1);
                        questions.add(wrong_definition_2);
                        questions.add(wrong_definition_3);

                        Collections.shuffle(questions);
                        answerbtn_1.setText(questions.get(0));
                        answerbtn_2.setText(questions.get(1));
                        answerbtn_3.setText(questions.get(2));
                        answerbtn_4.setText(questions.get(3));

                        answer_buttons.add(answerbtn_1);
                        answer_buttons.add(answerbtn_2);
                        answer_buttons.add(answerbtn_3);
                        answer_buttons.add(answerbtn_4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        //get random question from set w/Answer
        //get 3 other random answers
    }

    public void buttonHandler(){
        answerbtn_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setButtonColor(answerbtn_1);
            }

        });
        answerbtn_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setButtonColor(answerbtn_2);
            }

        });
        answerbtn_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setButtonColor(answerbtn_3);
            }

        });
        answerbtn_4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setButtonColor(answerbtn_4);
                //answerbtn_2.setText(answerbtn_2.getText()+" (✔)");
                //answerbtn_4.setText(answerbtn_4.getText()+" (✘)");
            }

        });
    }

    public void setButtonColor(Button b){
        try{
            String answer = b.getText()+"";
            if(answer.equals(definition)){
                b.setBackgroundColor(Color.rgb(0,255,0));
                b.setText(b.getText()+" (✔)");
            } else {
                b.setBackgroundColor(Color.rgb(255,0,0));
                b.setText(b.getText()+" (✘)");
                for(int i = 0; i<4; i++){
                    if(answer_buttons.get(i).getText().equals(definition)){
                        answer_buttons.get(i).setBackgroundColor(Color.rgb(0,255,0));
                        answer_buttons.get(i).setText(answer_buttons.get(i).getText()+" (✔)");
                    }
                }
            }

            //add a 2 second pause before starting this

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

