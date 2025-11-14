package com.untidar.kkntrack.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * Small utility for showing in-app notifications consistently.
 * Prefers Snackbar anchored to the activity root view, falls back to Toast if needed.
 */
public final class UiUtils {

    private UiUtils() {}

    public static void showSnack(Activity activity, String message) {
        View root = activity.findViewById(android.R.id.content);
        final int DURATION_MS = 1000;
        if (root != null) {
            final Snackbar sb = Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE);
            sb.setDuration(DURATION_MS);
            sb.show();

            // Dismiss when user touches anywhere outside the snackbar
            View.OnTouchListener touchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        sb.dismiss();
                        v.setOnTouchListener(null);
                    }
                    return false;
                }
            };

            root.setOnTouchListener(touchListener);

            sb.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    // remove touch listener when snackbar dismisses
                    root.setOnTouchListener(null);
                }
            });
        } else {
            final Toast t = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
            t.show();
            // Cancel the toast after custom duration
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    t.cancel();
                }
            }, DURATION_MS);
        }
    }

    public static void showSnackLong(Activity activity, String message) {
        final int DURATION_MS = 3000; // ~3 seconds
        View root = activity.findViewById(android.R.id.content);
        if (root != null) {
            final Snackbar sb = Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE);
            sb.setDuration(DURATION_MS);
            sb.show();

            View.OnTouchListener touchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        sb.dismiss();
                        v.setOnTouchListener(null);
                    }
                    return false;
                }
            };

            root.setOnTouchListener(touchListener);

            sb.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    root.setOnTouchListener(null);
                }
            });
        } else {
            final Toast t = Toast.makeText(activity, message, Toast.LENGTH_LONG);
            t.show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    t.cancel();
                }
            }, DURATION_MS);
        }
    }

    public static void showSnackWithAction(Activity activity, String message, String actionText, View.OnClickListener action) {
        View root = activity.findViewById(android.R.id.content);
        if (root != null) {
            Snackbar.make(root, message, Snackbar.LENGTH_LONG)
                    .setAction(actionText, action)
                    .show();
        } else {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }
}
