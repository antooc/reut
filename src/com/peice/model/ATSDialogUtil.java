
package com.peice.model;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

public class ATSDialogUtil {
    public static void showDialog(final Context ctx, String msg, boolean closeSelf) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
            .setMessage(msg).setCancelable(false);

        if (closeSelf) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity)ctx).finish();
                }
            });
        }
        else {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        builder.create().show();
    }

    public static void showDialog(Context ctx, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setView(view).setCancelable(false)
                .setPositiveButton("确定", null);

        builder.create().show();
    }
}
