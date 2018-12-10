package com.enescakir.auctioner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.enescakir.auctioner.Model.User;

public class BaseActivity extends AppCompatActivity {
    public static String username;
    public static int points;
    public SharedPreferences preferences;
    ProgressDialog progressDialog;

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showProgress(Context context) {
        progressDialog = ProgressDialog.show(context, "", "Loading. Please wait...", true);
    }

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public final void runOnThread(Thread action) {
        action.start();
    }
}
