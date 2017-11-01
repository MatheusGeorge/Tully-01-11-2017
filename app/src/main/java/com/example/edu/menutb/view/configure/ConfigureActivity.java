package com.example.edu.menutb.view.configure;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ConfigureController;
import com.example.edu.menutb.view.login.LoginActivity;
/**
 * Created by Edu on 18/07/2017.
 */

public class ConfigureActivity extends Fragment {
    Button buttonChangePassword;
    Button buttonInativate;
    Button buttonTerms;
    Button buttonAbout;
    UserDeleteTask mDeleteTask;

    View myView;

    String tokenString;
    String idString;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.configuration, container, false);

        SharedPreferences idBusca = getContext().getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = idBusca.getString("id", "");
        getActivity().setTitle("");
        SharedPreferences token = getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
        buttonChangePassword = (Button) myView.findViewById(R.id.changePasswordButton);

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), ChangePasswordActivity.class);
                myIntent.putExtra("token", tokenString);
                myIntent.putExtra("id", idString);
                startActivity(myIntent);
            }
        });

        buttonInativate = (Button) myView.findViewById(R.id.buttonInativate);
        buttonInativate.setOnClickListener(listenerInativar);

        buttonTerms = (Button) myView.findViewById(R.id.buttonTerms);
        buttonTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TermsActivity.class);
                intent.putExtra("aparecer","false");
                startActivity(intent);
            }
        });

        buttonAbout = (Button) myView.findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);

            }
        });
        return myView;
    }

    private final View.OnClickListener listenerInativar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
            final View alterarView = layoutInflaterAndroid.inflate(R.layout.configuration_dialog_inactivate, null);
            AlertDialog.Builder dialogName = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
            dialogName.setView(alterarView);
            dialogName.setCancelable(false).setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    mDeleteTask = new UserDeleteTask();
                    mDeleteTask.execute();
                }
            }).setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    dialogBox.cancel();
                }
            });
            AlertDialog dialogFinalName = dialogName.create();
            dialogFinalName.show();
            dialogFinalName.getButton(dialogFinalName.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBranco));
            dialogFinalName.getButton(dialogFinalName.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBranco));
        }
    };

    private class UserDeleteTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {
            try {
                new ConfigureController().inativaAccount(idString, tokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mDeleteTask = null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
