package org.hasoub.socialkhitmah;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    private static final String SERVER_URL = "http://social-khitmah.appspot.com/verse";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        try {
            showAya(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Release the lock
        wl.release();

    }

    class GetAyaAsyncTask extends AsyncTask<String, String, String> {

        private Context context;
        private OkHttpClient client;

        public GetAyaAsyncTask(Context context) {
            this.context = context;
            this.client = new OkHttpClient();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject json = getRequest(params[0]);
                String aya = queryDB(json.getString("chapter"), json.getString("verse"));
                return aya;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            sendNotification(s);
        }

        private JSONObject getRequest(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            try {
                return new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String queryDB(String chapter, String verse) {
            DataBaseHelper myDbHelper = new DataBaseHelper(context);

            try {
                myDbHelper.createDataBase();
            } catch (IOException ioe) {
                throw new Error("Unable to create database");
            }

            try {
                myDbHelper.openDataBase();
                SQLiteDatabase database = myDbHelper.getReadableDatabase();
                String query = "SELECT ar FROM ar WHERE chapter=" + chapter + " AND verse=" + verse;
                Cursor cursor = database.rawQuery(query, null);
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex("ar"));
            } catch(SQLException sqle) {
                throw sqle;
            }
        }

        private void sendNotification(String s) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText("A new aya has arrived!").setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, AyaActivity.class);
            resultIntent.putExtra(AyaActivity.AYA_TEXT, s);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(AyaActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(5, mBuilder.build());

        }
    }

    private void showAya(Context context) throws IOException {
        new GetAyaAsyncTask(context).execute(SERVER_URL);
    }

    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }


    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}

