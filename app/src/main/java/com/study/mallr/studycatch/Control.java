package com.study.mallr.studycatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Control extends AppCompatActivity {

    String response_bearer;
    String access_token;
    int set_id;
    String set_name, set_creator;
    Vector<Switch> switches = new Vector<>();
    Vector<Integer> set_ids_in_use = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get current URI
        setContentView(R.layout.activity_control);
        Uri data = this.getIntent().getData();
        final String secret_code = data.getQueryParameter("code");
        System.out.println("the param is: "+data);
        Toast.makeText(this, "Code: "+secret_code, Toast.LENGTH_LONG).show();

        //Temporary solution to network threads
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //POST/Get-Authorization
        getAuthenticated(secret_code);

        //Update UI
        list_sets();
        submit();
    }

    //get authentication used for recently viewed sets
    public void getAuthenticated(final String secret_code_copy) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .addHeader("Host", "api.quizlet.com")
                        .addHeader("Authorization", "Basic " + "eFc1cDl1c2dEZzp0c1gyMnplY0ZYUzl1N0NnTmQ5eTNu")
                        .url("https://api.quizlet.com/oauth/token")
                        .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                                "grant_type=authorization_code&code=" + secret_code_copy +
                                        "&redirect_uri=" + getResources().getString(R.string.redirect_uri)))
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject data = null;
                    Log.e("json", json);
                    try {
                        data = new JSONObject(json);
                        String accessToken = data.optString("access_token");
                        String userName = data.optString("user_id");
                        System.out.println("JSONDATA******: " + data);
                        System.out.println("AccessToken: " + accessToken);
                        System.out.println("UserName: " + userName);
                        access_token = accessToken;
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
    }

    public void list_sets(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://api.quizlet.com/2.0/feed/studied?access_token="+access_token)
                        .build();
                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject data = null;
                    Log.e("json", json);
                    try{
                        //response
                        Integer dataRange = 10;
                        data = new JSONObject(json);
                        System.out.println(data);

                        JSONArray jsonArray = data.getJSONArray("items");
                        if(jsonArray.length() < dataRange){
                            dataRange = jsonArray.length();
                        }

                        LinearLayout layout = (LinearLayout) findViewById(R.id.setsToStudyLinearLayout);
                        LayoutInflater inflater = (LayoutInflater) Control.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        for(int i = 0; i<dataRange; i++) {
                            JSONObject set = jsonArray.getJSONObject(i).getJSONObject("item_data").getJSONObject("set");
                            set_id = Integer.parseInt(set.optString("id"));
                            set_name = set.optString("title");
                            set_creator = set.optString("created_by");

                            System.out.println("SET: " + set_id);
                            System.out.println("TITLE: " + set_name);
                            System.out.println("BY: " + set_creator);
                            System.out.println("-------------------------------");

                            Switch mySwitch = (Switch) inflater.inflate(R.layout.set_switch_view, null);
                            mySwitch.setText(set_name);
                            mySwitch.setId(set_id);
                            switches.add(mySwitch);
                            layout.addView(mySwitch);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void submit(){
        Button submit_btn = (Button)findViewById(R.id.setsToStudySubmitBtn);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<switches.size(); i++){
                    if(switches.get(i).isChecked()){
                        set_ids_in_use.add(switches.get(i).getId());
                        editor.putInt("setId_"+i, switches.get(i).getId());
                        editor.commit();
                        System.out.println("ADDING TO DATABASE: "+switches.get(i).getId()+" | SIZE: "+sharedPref.getAll().size());
                    }
                }
                editor.putString("access_token", access_token);
                editor.commit();
                Toast.makeText(getApplicationContext(), "You may close this application.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Application will start next time you unlock your phone!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
