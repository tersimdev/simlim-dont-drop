package com.simlim.mobileMod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Test extends AppCompatActivity {

    private static final String TAG = "Test";

    private static final String KEY_NAME = "name";
    private static final String KEY_KINK = "kink";

    private EditText editTextName;
    private EditText editTextKink;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collRef = db.collection("People");
    private DocumentReference lastDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test);

        editTextName = findViewById(R.id.edit_text_name);
        editTextKink = findViewById(R.id.edit_text_kink);
        textViewData = findViewById(R.id.text_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        collRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    String name = queryDocumentSnapshots.getDocuments().get(0).getString(KEY_NAME);
                    String kink = queryDocumentSnapshots.getDocuments().get(0).getString(KEY_KINK);

                    textViewData.setText("Name: " + name + "\n" + "Kink: " + kink + "\n");
                }
            }
        });
    }

    public void saveKink(View v) {
        String name = editTextName.getText().toString();
        String kink = editTextKink.getText().toString();
        Map<String, Object> saveObj = new HashMap<>();
        saveObj.put(KEY_NAME, name);
        saveObj.put(KEY_KINK, kink);

        lastDocRef = collRef.document(name);
        collRef.document(name).set(saveObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Test.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Test.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadKink(View v) {
        lastDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString(KEY_NAME);
                            String kink = documentSnapshot.getString(KEY_KINK);

                            //Map<String, Object> map = documentSnapshot.getData();

                            textViewData.setText("Name: " + name + "\n" + "Kink: " + kink + "\n");
                        } else {
                            Toast.makeText(Test.this, "No previous entry!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Test.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainMenu.class);
        startActivity(intent);
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
}
