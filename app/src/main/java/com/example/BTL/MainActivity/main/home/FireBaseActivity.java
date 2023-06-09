package com.example.BTL.MainActivity.main.home;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseActivity extends AppCompatActivity {
    private static final String TAG ="FireBaseActivity";

    // Access a Cloud Firestore instance from your Activity
    public FirebaseFirestore mDb = FirebaseFirestore.getInstance();

}
