package com.study.mallr.studycatch;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Questions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

    }

    String response_auth_code = "";
    String redirect_uri = "";
    String basic_authorization = "Basic "+"xW5p9usgDg"+"Rs008354!";
    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(myString)
    String post_url = "https://api.quizlet.com/oauth/token";
    String response_bearer = "";

    StringRequest sr = new StringRequest(Request.Method.POST,post_url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            response_bearer = response;
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "Error has occurred.", Toast.LENGTH_LONG).show();
        }
    }){
        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("grant_type","authorization_code");
            params.put("code",userAccount.getPassword());
            params.put("comment", Uri.encode(comment));
            params.put("comment_post_ID",String.valueOf(postId));
            params.put("blogId",String.valueOf(blogId));

            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> params = new HashMap<String, String>();
            params.put("Content-Type","application/x-www-form-urlencoded");
            return params;
        }
    };

}
