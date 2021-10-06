package com.example.keith.asynctask_simple;

import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;

public class DataVM extends ViewModel {
    AddTask myTask;

    @Override
    protected void onCleared() {
        super.onCleared();
        if(myTask != null)
            myTask.cancel(true);
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
    public static class AddTask extends AsyncTask<Integer,Integer,String> {
        // if an object can only be reached by a weak reference then its
        // eligible for garbage collection.  So on a confgurationchanged
        // event when the activity is destroyed, it can be GCed even
        // though ma has a weak reference to it
        private WeakReference<MainActivity> ma;
        public AddTask(WeakReference<MainActivity> ma) {
            set(ma);
        }
        public void set(WeakReference<MainActivity> ma) {
            //hold onto this for activity manip
            this.ma = ma;
        }

        /**
         * @param integers varargs- array of ints passed in
         * @return
         */
        @Override
        protected String doInBackground(Integer... integers) {
            //runs in new thread
            Integer imaxval = integers[0];

            for (int i=0;i<imaxval;i++){

                //simulate some work sleep for .5 seconds
                SystemClock.sleep(100);

                //let main thread know how we are doing
                publishProgress(i);

                //periodically check if the user canceled
                if (isCancelled())
                    return ("Canceled");
            }
            return "Completed";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //see if the weak ref still exists (
            try {
                ma.get().setUIState(false, "Launching async task...");
            } catch (NullPointerException npe) {
                //what to do here?
            }

            //is there a race condition here?)
//            if (ma.get()!=null) {
//                ma.get().setUIState(false, "Launching async task...");
//            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer progress = values[0] * 1;

            //set the UI
            try {
                ma.get().pBar.setProgress(progress);
            } catch (NullPointerException npe) {
                //what to do here?
            }
        }

        @Override
        protected void onPostExecute(String retval) {
            //occurs in main thread, called upon completion of doInBackground
            super.onPostExecute(retval);

            //set the UI
            try {
                ma.get().setUIState(true, retval);
            } catch (NullPointerException npe) {
                //what to do here?
            }
        }

        @Override
        protected void onCancelled(String retval) {
            //occurs in main thread, called upon completion of doInBackground
            super.onCancelled();

            //set the UI
            try {
                ma.get().setUIState(true, retval);
            } catch (NullPointerException npe) {
                //what to do here?
            }
        }
    }
}
