package com.example.vincius.myapplication;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Connect {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authstateListener;
    private static FirebaseUser firebaseUser;

    private Connect() {
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            startFireebaseAuth();
        }
        return firebaseAuth;
    }

    private static void startFireebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        authstateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    firebaseUser = user;
                }
            }
        };

        firebaseAuth.addAuthStateListener(authstateListener);
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }


    public static void logOut() {
        firebaseAuth.signOut();
    }


}

