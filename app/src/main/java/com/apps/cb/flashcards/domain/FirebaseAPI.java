package com.apps.cb.flashcards.domain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apps.cb.flashcards.entities.FlashCard;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class FirebaseAPI {
    private DatabaseReference dataReference;
    private FirebaseAuth firebaseAuth;
    private ChildEventListener flashCardEventListener;

    public static final String FLASH_CARDS_KEY = "flash_card";

    public FirebaseAPI(FirebaseAuth auth, DatabaseReference databaseReference) {
        this.firebaseAuth = auth;
        this.dataReference = databaseReference;
    }

    public void checkForData(final FirebaseActionListenerCallback listener) {


        dataReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listener.onSuccess();
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void subscribe(final FirebaseEventListenerCallback listener) {
        if (flashCardEventListener == null) {
            flashCardEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listener.onChildChanged(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };
            dataReference.child(FLASH_CARDS_KEY).addChildEventListener(flashCardEventListener);
        }
    }

    public void unsubscribe() {
        dataReference.removeEventListener(flashCardEventListener);
    }

    public String create(FlashCard card) {
        dataReference.child(FLASH_CARDS_KEY).push().setValue(card);
        return dataReference.push().getKey();
    }

    public void update(FlashCard card) {
        DatabaseReference databaseReference = this.dataReference.child(FLASH_CARDS_KEY).child(card.getId());
        databaseReference.setValue(card);
    }

    public void remove(FlashCard card, FirebaseActionListenerCallback listener) {
        dataReference.child(FLASH_CARDS_KEY).child(card.getId()).removeValue();
        listener.onSuccess();
    }

    public String getUserId() {
        String userId = null;
        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        return userId;
    }

    public void signUp(String email, String password, final FirebaseActionListenerCallback listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            listener.onSuccess();
                        else
                            listener.onError(task.getException().getMessage());
                    }
                });
    }

    public void login(String email, String password, final FirebaseActionListenerCallback listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            listener.onSuccess();
                        else
                            listener.onError(task.getException().getMessage());
                    }
                });
    }

    public void login(AccessToken token, final FirebaseActionListenerCallback listener) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onError(task.getException().getLocalizedMessage());
                }

            }
        });
    }

    public void checkForSession(FirebaseActionListenerCallback listener) {
        if (firebaseAuth.getCurrentUser() != null) {
            listener.onSuccess();
        } else {
            listener.onError(null);
        }
    }

    public void logout() {
        firebaseAuth.signOut();
    }
}
