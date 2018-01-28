package com.study.mallr.studycatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ricky on 1/27/2018.
 */

public class Questions_Call extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            Intent intent1 = new Intent(context,Questions.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }

}
