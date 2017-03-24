package com.android.margintop.customspinner;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by L on 2017/3/13.
 *
 * @描述 ${TODO}
 */

public class ToastUtil {

    private static Toast sToast;

    public static void showToast(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

}
