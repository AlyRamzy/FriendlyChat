package com.google.firebase.udacity.friendlychat.repositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.udacity.friendlychat.FriendlyMessage;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Aly on 7/10/2019.
 */

public class FriendlyMessageRepository {
    private static FriendlyMessageRepository instance;
    protected FirebaseDatabaseRepositoryCallback<FriendlyMessage> firebaseCallback;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStoarge;
    private StorageReference mChatPhotoStoargeReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    public static FriendlyMessageRepository getInstance(){
        if(instance == null){
            instance = new FriendlyMessageRepository();

        }
        return instance;
    }
    public void init(){
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseStoarge=FirebaseStorage.getInstance();
        mFirebaseRemoteConfig=FirebaseRemoteConfig.getInstance();

        mMessageDatabaseReference=mFirebaseDatabase.getReference().child("messages");
        mChatPhotoStoargeReference=mFirebaseStoarge.getReference().child("chat_photos");

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.e(TAG, "Triger onChildAdded" );
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    if(firebaseCallback!=null){
                        Log.e(TAG, "Triger onChildAdded callback" );
                        firebaseCallback.onSuccess(friendlyMessage);
                    }
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessageDatabaseReference.addChildEventListener(mChildEventListener);

        }
    }
    public void addListener(FirebaseDatabaseRepositoryCallback<FriendlyMessage> firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
       
    }
    public void clean(){
        firebaseCallback=null;
        mMessageDatabaseReference.removeEventListener(mChildEventListener);
    }
    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(T result);

        void onError(Exception e);
    }

}
