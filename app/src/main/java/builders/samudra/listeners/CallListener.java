package builders.samudra.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import builders.samudra.MainActivity;
import builders.samudra.Network.ParseGetData;
import builders.samudra.utils.Helper;
import builders.samudra.utils.Logger;

/**
 * Created by Shabaz on 28-Nov-15.
 */
public class CallListener extends BroadcastReceiver
{
    Logger mLog = new Logger(CallListener.class.getSimpleName());

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent
     * broadcast.  During this time you can use the other methods on
     * BroadcastReceiver to view/modify the current result values.  This method
     * is always called within the main thread of its process, unless you
     * explicitly asked for it to be scheduled on a different thread using
     * {@link Context#(BroadcastReceiver,
     * , String, Handler)}. When it runs on the main
     * thread you should
     * never perform long-running operations in it (there is a timeout of
     * 10 seconds that the system allows before considering the receiver to
     * be blocked and a candidate to be killed). You cannot launch a popup dialog
     * in your implementation of onReceive().
     * <p/>
     * <p><b>If this BroadcastReceiver was launched through a &lt;receiver&gt; tag,
     * then the object is no longer alive after returning from this
     * function.</b>  This means you should not perform any operations that
     * return a result to you asynchronously -- in particular, for interacting
     * with services, you should use
     * {@link Context#startService(Intent)} instead of
     * {@link Context#(Intent, , int)}.  If you wish
     * to interact with a service that is already running, you can use
     * {@link #peekService}.
     * <p/>
     * <p>The Intent filters used in {@link Context#registerReceiver}
     * and in application manifests are <em>not</em> guaranteed to be exclusive. They
     * are hints to the operating system about how to find suitable recipients. It is
     * possible for senders to force delivery to specific recipients, bypassing filter
     * resolution.  For this reason, {@link #onReceive(Context, Intent) onReceive()}
     * implementations should respond only to known actions, ignoring any unexpected
     * Intents that they may receive.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        if(null == bundle)
            return;
        mLog.d("Call Listener");
        String state = bundle.getString(TelephonyManager.EXTRA_STATE);
        mLog.d("State: "+ state);


        if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))
        {
            Helper.phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            mLog.d("Incomng Number: " +  Helper.phoneNumber);
            String info = "Incoming number: " +  Helper.phoneNumber;
            Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            new ParseGetData().execute(Helper.phoneNumber);

        }
        else
        if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            mLog.d("Picked Number: " +  Helper.phoneNumber);
            String info = "Picked number: " +  Helper.phoneNumber;
            Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            Intent mIntent = new Intent(context,MainActivity.class);
            mIntent.putExtra("NUMBER", Helper.phoneNumber);
            Helper.phoneNumber="";
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
