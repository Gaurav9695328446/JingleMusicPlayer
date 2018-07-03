package com.example.suneel.musicapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by suneel on 29/5/18.
 */

public class Utils {

    public static boolean isInternetAvaliable(Context context) {
        if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            return true;
        }
        return false;
    }

    public static void showNetworkAlertDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("Please check your internet connection")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
