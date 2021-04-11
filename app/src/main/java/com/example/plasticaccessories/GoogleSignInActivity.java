package com.example.plasticaccessories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInActivity extends AppCompatActivity
{

    private static final int RC_SIGN_IN = 22;
    private static final String TAG = "TAG";
    SignInButton btnSignIn;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_signin_activity_layout);

        btnSignIn = findViewById(R.id.btnGoogleSignIn);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        if (mAuth.getCurrentUser() != null){
            Intent i = new Intent(GoogleSignInActivity.this, BottomNavigationActivity.class);
            startActivity(i);
            finish();
        }else {
            super.onStart();
        }
    }

    private  void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG,"firebaseAuthWithGoogle : "+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                //Google Sign In failed, update UI appropriately
                Log.w(TAG,"Google Sign In Failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //sign in success, update UI with the signed-in user's information
                    Log.d(TAG,"signInWithCredential : Success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    Intent i = new Intent(GoogleSignInActivity.this,BottomNavigationActivity.class);
                    startActivity(i);
                }else {
                    //if sign in fails, display a message to the user
                    Log.w(TAG,"signInWithCredential : Failure",task.getException());
                }
            }
        });
    }

}
