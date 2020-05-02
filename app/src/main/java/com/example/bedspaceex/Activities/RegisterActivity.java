package com.example.bedspaceex.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bedspaceex.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";


    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button mBtnRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if (!isEmpty(mEmail.getText().toString())
                        && !isEmpty(mPassword.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())) {

                    //check if passwords match
                    if(doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())){

                        registerNewUser();

                    }else{
                        Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private void registerNewUser() {
        showDialogue();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString() ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "onComplete: onComplete: " + task.isSuccessful());
                if(task.isSuccessful()) {
                    Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    sendVerificationEmail();
                    FirebaseAuth.getInstance().signOut();
                    redirectToLoginScreen();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
                }
                hideDialogue();
            }
        });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(RegisterActivity.this, "Verification email sent", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(RegisterActivity.this, "couldn't send verification email", Toast.LENGTH_LONG).show();
                }
            }
        });
        }
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }

    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
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

    private void redirectToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}