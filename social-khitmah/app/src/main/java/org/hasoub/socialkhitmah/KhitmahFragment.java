package org.hasoub.socialkhitmah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class KhitmahFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private AlarmManagerBroadcastReceiver alarm;

    public KhitmahFragment() {
    }
    CallbackManager callbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_khitmah, container, false);
        LoginButton authButton = (LoginButton) rootView.findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        authButton.setFragment(this);
        authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();

                loginResult.getAccessToken().get
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        uiHelper = new UiLifecycleHelper(getActivity(), callback);
//        uiHelper.onCreate(savedInstanceState);

//            // use this to start and trigger a service
//            Intent i= new Intent(getActivity(), MyService.class);
//            // potentially add data to the intent
//            getActivity().startService(i);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        alarm = new AlarmManagerBroadcastReceiver();
//        onetimeTimer();

    }

    public void startRepeatingTimer() {
        Context context = getActivity().getApplicationContext();
        if(alarm != null){
            alarm.SetAlarm(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void onetimeTimer(){
        Context context = getActivity().getApplicationContext();
        if(alarm != null){
            alarm.setOnetimeTimer(context);
        }else{
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }


}