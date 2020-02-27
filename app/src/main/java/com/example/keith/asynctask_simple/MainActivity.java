package com.example.keith.asynctask_simple;

import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int P_BAR_MAX = 100;
    private Integer myInt=100;
    private TextView tv;
    private Button butStart;
    private Button butCancel;
    ProgressBar pBar;

    //persists accross config changes
    DataVM myVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.textView2);
        butStart = (Button)findViewById(R.id.bStart);
        butCancel= (Button)findViewById(R.id.bCancel);
        pBar = (ProgressBar) findViewById(R.id.progressBar1);
        pBar.setMax(P_BAR_MAX);

        // Create a ViewModel the first time the system calls an activity's
        // onCreate() method.  Re-created activities receive the same
        // MyViewModel instance created by the first activity.
        myVM = new ViewModelProvider(this).get(DataVM.class);

        //if we have a thread running then attach this activity
        if (myVM.myTask != null) {
            myVM.myTask.set(new WeakReference<MainActivity>(this));

            //a thread is running have the UI show that
            setUIState(false);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void doStart(View view) {
        myVM.myTask = new DataVM.AddTask(new WeakReference<MainActivity>(this));
        myVM.myTask.execute(myInt);
     }

    public void setUIState(boolean b){
        setUIState(b, null);
    }
    public void setUIState(boolean b, String s) {
        butStart.setEnabled(b);
        butCancel.setEnabled(!b);
        if(s != null)
            tv.setText(s);
        pBar.setProgress(0);
    }

    public void doCancel(View view) {
         //try to cancel thread
        if (myVM.myTask != null) {
            myVM.myTask.cancel(true);
        }
    }

}
