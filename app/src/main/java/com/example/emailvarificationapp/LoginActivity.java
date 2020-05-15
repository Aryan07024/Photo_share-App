package com.example.emailvarificationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    EditText unl,passl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        unl = (EditText)findViewById(R.id.unl);
        passl = (EditText)findViewById(R.id.passl);
        passl.setOnKeyListener(this);
        ConstraintLayout cls =(ConstraintLayout)findViewById(R.id.cll);
        ImageView ivs = (ImageView)findViewById(R.id.ivl);
        ivs.setOnClickListener(this);
        cls.setOnClickListener(this);
    }

    public void shift(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void login1(View view)
    {


// Login with Parse
        ParseUser.logInInBackground(unl.getText().toString(), passl.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser parseUser, ParseException e) {
            if (parseUser != null) {
                if(parseUser.getBoolean("emailVerified")) {
                    alertDisplayer("Login Sucessful", "Welcome, " + unl.getText().toString() + "!", false);
                }
                else
                {
                    ParseUser.logOut();
                    alertDisplayer("Login Fail", "Please Verify Your Email first", true);
                }
            } else {
                ParseUser.logOut();
                alertDisplayer("Login Fail", e.getMessage() + " Please re-try", true);
            }
        }
    });



    }
    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent = new Intent(LoginActivity.this, inside.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
       if(keyCode==66&&event.getAction()==KeyEvent.ACTION_DOWN)
       {
           login1(v);
       }

        return false;
    }
    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }


}
