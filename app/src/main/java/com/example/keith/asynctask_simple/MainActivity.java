package com.example.keith.asynctask_simple;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Integer myInt=100000000;
    private TextView tv;
    private Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.textView2);
        but = (Button)findViewById(R.id.button);
    }

    public void doClick(View view) {
        but.setEnabled(false);
        AddTask myTask = new AddTask();
        myTask.execute(myInt);
    }

    /*
    If the following class is non static then it will have a hidden reference to its
    parent, MainActivity.  If the device is rotated while the following
    thread is running then this reference will keep the garbage collector from
    collecting the parent activity.  If the thread runs long enough and the
    device keeps rotating, its memory footprint grows and grows.
    If its static then no hidden reference, so no memory problems, but its harder to
    manipulate activity UI.
     */
    private class AddTask extends AsyncTask<Integer,Void,Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            //occurs in new thread
            Integer res = 0;
            Integer ival = integers[0];

            while(ival>0){
                res+=ival--;
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //reset the text view
            MainActivity.this.tv.setText("Awaiting results...");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //occurs in main thread, called upon completion of doInBackground
            super.onPostExecute(integer);
            but.setEnabled(true);
            MainActivity.this.tv.setText("Result is "+ Integer.toString(integer));
            
        }
    }
    
}
