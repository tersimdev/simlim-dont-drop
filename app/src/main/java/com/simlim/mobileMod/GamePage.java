package com.simlim.mobileMod;

// Created by TanSiewLan2019
// Create a GamePage is an activity class used to hold the GameView which will have a surfaceview

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Share;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GamePage extends Activity {

    public static GamePage Instance = null;

    private GameView gameView;
    private ConstraintLayout container;

    public enum UI {
        TXT_SCORE,
        TXT_HSCORE,
        BTN_LEADERBOARD,
        BTN_SHARE,
        BTN_PAUSE,
        TXT_DRAWLINE
    };

    //UI
    private TextView scoreText, highscoreText;
    private Button btnLeaderboard, btnShare, btnPause;
    private TextView drawALine;

    //alert for exit
    private AlertDialog quitDialog;

    //Motion sensing
    private SensorManager sensorManager;
    private Sensor gravSensor;
    private SensorEventListener gravSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //Log.d("MY_APP", Double.toString(event.values[0]) + "," + Double.toString(event.values[1])+ "," + Double.toString(event.values[2]));
            gravity.set(-event.values[0], event.values[1]);
            //System.out.println(gravity);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Log.d("MY_APP", sensor.toString() + " - " + accuracy);
        }
    };
    private PointF gravity = new PointF(0, 1);

    //shared prefs
    private final String SHARED_PREF_ID = "GameSaveData";
    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor sharedPrefEditor = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    CallbackManager callbackManager;
    AccessToken accessToken;
    ShareDialog shareDialog;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide titlebar
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);  // Hide topbar

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Instance = this;

        setContentView(R.layout.activity_game_scene);
        container = findViewById(R.id.container);

        scoreText = findViewById(R.id.score);
        highscoreText = findViewById(R.id.highscore);
        btnLeaderboard = findViewById(R.id.btn_leaderboard);
        btnShare = findViewById(R.id.btn_share);
        btnPause = findViewById(R.id.btn_pause);
        drawALine = findViewById(R.id.drawaline);

        final int childSize = container.getChildCount();
        for (int i = 0; i < childSize; ++i) {
            container.getChildAt(i).setElevation(1);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Quit Game?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        quitDialog = builder.create();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sharedPref = getSharedPreferences(SHARED_PREF_ID, 0);

        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        shareDialog = new ShareDialog(this);
        // this part is optional
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() { ... })

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                System.out.println("Success");
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            System.out.println("facebook - profile " + currentProfile.getName());
                            mProfileTracker.stopTracking();
                        }
                    };
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    System.out.println("facebook - profile " + profile.getName());
                }
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("Error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        container.removeView(gameView);
        sensorManager.unregisterListener(gravSensorListener);
    }

    @Override
        protected void onResume() {
        super.onResume();
        gameView = new GameView(this);
        gameView.setId(View.generateViewId());
        gameView.setElevation(0);
        container.addView(gameView, 0);
        final int id = gameView.getId();
        ConstraintSet c = new ConstraintSet();
        c.clone(container);
        c.connect(id, ConstraintSet.START, container.getId(), ConstraintSet.START);
        c.applyTo(container);
        sensorManager.registerListener(gravSensorListener, gravSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onBackPressed() {
        quitDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // WE are hijacking the touch event into our own system
        int x = (int) event.getX();
        int y = (int) event.getY();

        TouchManager.Instance.Update(x, y, event.getAction());

        return true;
    }

    public void backToMainMenu(View view) {
        StateManager.Instance.ChangeState("Mainmenu");
    }

    public boolean isLoggedIn() {
        return accessToken != null && !accessToken.isExpired();
    }

    public Bitmap takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = gameView.getRootView();// getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            v1.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            return bitmap;
//
//            File imageFile = new File(mPath);
//
//            System.out.println(4);
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            System.out.println(3);
//
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            System.out.println("Screenshot taken");
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            return null;
        }
    }

    public void HandleOnClick(View _v) {
        if (_v.getId() == R.id.btn_leaderboard) {
            Intent intent = new Intent();
            intent.setClass(this, Leaderboard.class);
            StateManager.Instance.ChangeState("Leaderboard");
            startActivity(intent);
        }
        else if (_v.getId() == R.id.btn_pause) {
            GameSystem.Instance.TogglePause();
        }
        else if (_v.getId() == R.id.btn_share) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(takeScreenshot())
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)) {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }
    }

    public void UpdateUIText(final UI type, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TXT_SCORE:
                        scoreText.setText(text);
                        break;
                    case TXT_HSCORE:
                        highscoreText.setText(text);
                        break;
                    case TXT_DRAWLINE:
                        drawALine.setText(text);
                        break;
                }
            }
        });
    }


    public void UpdateUITextColor(final UI type, final int color) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TXT_SCORE:
                        scoreText.setTextColor(color);
                        break;
                    case TXT_HSCORE:
                        highscoreText.setTextColor(color);
                        break;
                    case TXT_DRAWLINE:
                        drawALine.setTextColor(color);
                        break;
                }
            }
        });
    }

    public void ShowUI(final UI type, final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case TXT_SCORE:
                        scoreText.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case TXT_HSCORE:
                        highscoreText.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case BTN_LEADERBOARD:
                        btnLeaderboard.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case BTN_SHARE:
                        btnShare.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case BTN_PAUSE:
                        btnPause.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case TXT_DRAWLINE:
                        drawALine.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                        break;
                }
            }
        });
    }

    public PointF UpdateGravity() {
        return gravity;
    }

    public void SaveEditBegin() {
        if (sharedPrefEditor != null)
            return;
        sharedPrefEditor = sharedPref.edit();
    }

    public void SaveEditEnd() {
        if (sharedPrefEditor == null)
            return;
        sharedPrefEditor.commit();
        sharedPrefEditor = null;
    }

    public void SaveInt(String _key, int _val) {
        if (sharedPrefEditor == null)
            return;
        sharedPrefEditor.putInt(_key, _val);
    }

    public int GetSavedInt(String _key, int _defVal) {
        return sharedPref.getInt(_key, _defVal);
    }

    public void SaveToFireStore(String _name, int _score) {
        Map<String, Object> saveObj = new HashMap<>();
        saveObj.put("name", UpdateThread.deviceId);
        saveObj.put("score", _score);
        db.collection("HighScores").document(UpdateThread.deviceId).set(saveObj);
    }
}

