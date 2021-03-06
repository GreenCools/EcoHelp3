package com.example.Activities;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.example.Classes.User;
import com.example.ecohelp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistationActivity extends BaseActivity {
    private static final String TAG = "EmailPassword";
    //Инициализация всего
    private EditText EmailField;
    private EditText PasswordField;
    private EditText RepeatPassword;


    private DatabaseReference mDatabase;



    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);
        EmailField = findViewById(R.id.emailRegistration);
        PasswordField = findViewById(R.id.passwordRegistration);
        RepeatPassword = findViewById(R.id.repeatPasswordRegistration);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Регистрация");
        }



    }

    private void createAccount(String email, String password) {


        Log.d(TAG, "Создание аккаунта" + email);
            if (validateForm()) {
                return;
            }


            showProgressDialog();

            //Регистрация через емайл

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    hideProgressDialog();
                    Log.d(TAG, "Аккаунт успешно создан");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    assert user != null;

                    writeNewUser(email);
                } else {
                    hideProgressDialog();

                    updateUI(null);
                }

            });
        }







    private boolean validateForm() {
        boolean valid = true;

        String email = EmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            EmailField.setError("Пусто");
            valid = false;
        } else {
            EmailField.setError(null);
        }

        String password = PasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            PasswordField.setError("Пусто");
            valid = false;
        } else {
            PasswordField.setError(null);
        }
        String repeatPassword = RepeatPassword.getText().toString();
        if(TextUtils.isEmpty(repeatPassword)){
            RepeatPassword.setError("Пусто");
        }
        else{
            RepeatPassword.setError(null);
        }


        return !valid;
    }


    private void writeNewUser(String email) {

        User user = new User(email, 0);

        mDatabase.child("users").child(getUid()).setValue(user);
        mDatabase.child("users").child(getUid()).child("Avatar").setValue("1");
        mDatabase.child("users").child(getUid()).child("Coupons").child("NumberCoupons").setValue(0);
    }
    public void Onclick(View v){
        String repeatPassword =  RepeatPassword.getText().toString();
        String password =  PasswordField.getText().toString();
        if (password.equals(repeatPassword)){

            createAccount(EmailField.getText().toString(), PasswordField.getText().toString());

    }
    else {
            PasswordField.setError("Пароли не совпадают");
            RepeatPassword.setError("Пароли не совпадают");
        }

    }
}
