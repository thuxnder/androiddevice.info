package info.androiddevice.deviceinventory;

import android.content.Context;
import android.content.Intent;

import info.androiddevice.deviceinventory.submission.R;

public class Application extends android.app.Application {
    static Context context = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable t)
            {
                handleUncaughtException (thread, t);
            }
        });
    }

    private void handleUncaughtException (Thread thread, Throwable t)
    {
        try {
            android.content.pm.PackageInfo packageinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.email)});
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.subject));
            intent.putExtra(android.content.Intent.EXTRA_TEXT, new Error(packageinfo, t).toString());

            Intent mail = Intent.createChooser(intent, context.getString(R.string.crashtitle));
            mail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mail);
        } catch(Exception e) {
        } finally {
            Runtime.getRuntime().exit(0);
        }

    }

    public static Context getContext() {
        return context;
    }
}
