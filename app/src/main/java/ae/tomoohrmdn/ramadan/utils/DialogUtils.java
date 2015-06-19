package ae.tomoohrmdn.ramadan.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ae.tomoohrmdn.ramadan.R;

/**
 * Created by tareq on 06/11/2015.
 */
public class DialogUtils {

    public  static void   OkDialog(Activity activity,String title,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if(!title.isEmpty())
            builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public  static void   OkDialog(Activity activity,String title,String message,final DialogInterface.OnClickListener listener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog,which);
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public  static void   YesNoDialog(Activity activity,String title,String message,final DialogInterface.OnClickListener listener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }

        });

        builder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
}