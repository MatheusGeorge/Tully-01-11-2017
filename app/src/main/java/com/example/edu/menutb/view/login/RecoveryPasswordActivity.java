package com.example.edu.menutb.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.edu.menutb.R;


public class RecoveryPasswordActivity extends AppCompatActivity{

    Button buttonAlreadyCode;
    Button buttonSendCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_options);


        buttonAlreadyCode = (Button) findViewById(R.id.buttonAlreadyCode);
        buttonAlreadyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alreadyCode();
            }
        });

        buttonSendCode = (Button) findViewById(R.id.buttonSendCode);
        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
    }

    private void sendCode(){
        Intent intent = new Intent(getApplicationContext(), SendCodeActivity.class);
        startActivity(intent);
    }

    private void alreadyCode(){
        Intent intent = new Intent(getApplicationContext(), AlreadyHaveCodeActivity.class);
        startActivity(intent);
    }
}
