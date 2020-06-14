package com.example.bedspaceex.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bedspaceex.R;
import com.example.bedspaceex.ResendVerificationDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPassword;
    private Button mSigninBtn;
    private TextView mRegTxtView;
    private TextView mForgotPass;
    private TextView mResendVerEmail;
    private ProgressBar mProgressBar;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mSigninBtn = (Button) findViewById(R.id.btn_signin);
        mRegTxtView = (TextView) findViewById(R.id.tv_register);
        mForgotPass = (TextView) findViewById(R.id.tv_forgot_password);
        mResendVerEmail = (TextView) findViewById(R.id.tv_resend_ver_email);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        setUpFirebaseAuth();


        mRegTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue();
                //check if the fields are filled out
                if(!isEmpty(mEmailEditText.getText().toString())
                        && !isEmpty(mPassword.getText().toString())){
                    Log.d(TAG, "onClick: attempting to authenticate.");
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmailEditText.getText().toString(), mPassword.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    hideDialogue();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Authentication failed \nCreate an account before signing in",
                                    Toast.LENGTH_LONG).show();
                            hideDialogue();
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                    hideDialogue();
                }
            }
        });

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mResendVerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendVerificationDialog resendVerificationDialog = new ResendVerificationDialog();
                resendVerificationDialog.show(getSupportFragmentManager(), "dialog_resend_verification_email");
            }
        });
        hideSoftKeybpoard();
    }

    private void hideSoftKeybpoard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /******************************
     ***************************************************** FireBaseAuth Setup********************************
     */

    private void setUpFirebaseAuth() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    if(user.isEmailVerified()) {
                        Log.d(TAG, "onAuthStateChanged: signed in " + user.getUid());
                        Toast.makeText(LoginActivity.this, "Welcome " + user.getEmail(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(LoginActivity.this, "check your email inbox for verification link", Toast.LENGTH_LONG)
                                .show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
                else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null)
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    private void showDialogue() {
        int colorCodeDark = Color.parseColor("#F0673AB7");
        mProgressBar.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialogue() {
        if(mProgressBar.getVisibility() == View.VISIBLE)
            mProgressBar.setVisibility(View.INVISIBLE);
    }
}
