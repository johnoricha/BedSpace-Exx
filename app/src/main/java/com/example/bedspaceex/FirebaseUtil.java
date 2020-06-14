package com.example.bedspaceex;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bedspaceex.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class  FirebaseUtil {

    private static final String TAG = "FirebaseUtil";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    public boolean isAdmin;
    private  ChildEventListener mChildEventListener;
    private Activity caller;



    private static FirebaseStorage mFirebaseStorage;
    private static StorageReference mStorageReference;

    public FirebaseUtil(Activity caller) {
        this.caller =caller;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("sellers");
        Log.d(TAG, "FirebaseUtil: " + isAdmin);
        Log.d(TAG, "FirebaseUtil: " + FirebaseAuth.getInstance().getUid());
    }

    public void checkAdmin() {
        mChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (FirebaseAuth.getInstance().getUid().equals(dataSnapshot.getKey())) {
                    isAdmin = true;
                    showMenu();
                    Log.d(TAG, "onChildAdded: you are an admin " + isAdmin);
                } else {
                    Log.d(TAG, "onChildAdded: you are not an admin");
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
        Log.d(TAG, "checkAdmin: " + isAdmin);
    }

    private void showMenu() {
        caller.invalidateOptionsMenu();
    }

    public static FirebaseStorage getFirebaseStorage() {
        if(mFirebaseStorage == null) {
            mFirebaseStorage = FirebaseStorage.getInstance();
        }
        return mFirebaseStorage;
    }

    public static StorageReference getStorageReference() {
        if (mStorageReference == null) {
            mStorageReference = mFirebaseStorage.getReference().child("offers_pictures");
        }
        return mStorageReference;
    }
}
