package com.study.mallr.studycatch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button sign_in = (Button)findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url = "https://quizlet.com/authorize?response_type=code&client_id=xW5p9usgDg&scope=read&state=RANDOM_STRING";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

        });
    }
}
