package com.example.Activities;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.Classes.GoogleUser;
import com.example.ecohelp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Registration";
    private static final int RC_SIGN_IN = 9001;

    private EditText mEmailField;
    private EditText mPasswordField;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.forgetPassword).setOnClickListener(this);
        findViewById(R.id.signInGoogle).setOnClickListener(this);
        mEmailField = findViewById(R.id.emaiField);
        mPasswordField = findViewById(R.id.passwordField);


        // Кнопки
        findViewById(R.id.signUp).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Авторизация");
        }






        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "Вход" + email);
        if (validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        hideProgressDialog();
                        Log.d(TAG, "Вход через почту успешен");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        hideProgressDialog();
                        updateUI(null);
                    }


                });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Пусто");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Пусто");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return !valid;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                Log.w(TAG, "Ошибка входа", e);

                updateUI(null);

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "Аутентификация" + acct.getId());

         String account = acct.getEmail();



        showProgressDialog();


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference userNameRef = rootRef.child("users").child(getUid());
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    writeNewUser(account);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.v(TAG,""+databaseError);
                            }
                        };
                        userNameRef.addListenerForSingleValueEvent(valueEventListener);
                        Log.d(TAG, "Успешно");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                        }

                    else{

                            Log.w(TAG, "Ошибка", task.getException());
                            updateUI(null);
                        }



                    hideProgressDialog();

                });
        }






    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void writeNewUser( String email) {

        GoogleUser user = new GoogleUser(email, 0);

        mDatabase.child("users").child(getUid()).setValue(user);
        mDatabase.child("users").child(getUid()).child("Avatar").setValue("1");

        mDatabase.child("users").child(getUid()).child("Coupons").child("NumberCoupons").setValue(0);

    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signInGoogle) {
            signIn();

        } else if (i == R.id.signUp) {
            Intent intent = new Intent(MainActivity.this, RegistationActivity.class);
            startActivity(intent);

        } else if (i == R.id.emailSignInButton) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        else if(i==R.id.forgetPassword){
            Intent intent = new Intent(MainActivity.this,newPasswordActivity.class);
            startActivity(intent);
        }
    }
}
