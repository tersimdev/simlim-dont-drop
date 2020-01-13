package com.simlim.mobileMod;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ListItem {
    ListItem(String _username, String _score) {
        username = _username;
        score = _score;
    }
    public String username;
    public String score;
};

public class Leaderboard extends Activity implements StateBase {

    private LayoutInflater inflater;
    private View loading;
    private ViewGroup insertPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.activity_leaderboard);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        insertPoint = (ViewGroup) findViewById(R.id.insertPoint);

        loading = findViewById(R.id.loading_lb);
        loading.setVisibility(View.VISIBLE);

        ReadScores(10);
    }

    public void BackToGame(View _v) {
        Intent intent = new Intent();
        intent.setClass(this, GamePage.class);
        StateManager.Instance.ChangeState("MainGame");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, GamePage.class);
        StateManager.Instance.ChangeState("MainGame");
        startActivity(intent);
    }

    @Override
    public void Render(Canvas _canvas) {
    }

    @Override
    public void OnEnter(SurfaceView _view) {
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void Update(float _dt) {
    }

    @Override
    public String GetName() {
        return "Leaderboard";
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void ReadScores(int limit) {
        final List<ListItem> ret = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("HighScores").orderBy("score", Query.Direction.ASCENDING).limit(limit).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Toast.makeText(Leaderboard.this, "Success", Toast.LENGTH_SHORT).show();
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : documents) {
                            Map<String, Object> data = snapshot.getData();
                            ret.add(new ListItem(data.get("name").toString(), data.get("score").toString()));
                        }
                        DisplayScores(ret);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Leaderboard.this, "Error! Can't Find Leaderboard.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void DisplayScores(final List<ListItem> scores) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.INVISIBLE);
                TextView name, score;
                for (ListItem item : scores) {
                    View v = inflater.inflate(R.layout.leaderboard_item, null);
                    insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    name = v.findViewById(R.id.name);
                    score = v.findViewById(R.id.score);
                    name.setText(item.username);
                    score.setText(item.score);
                }
            }
        });
    }
}
