package builders.samudra.listeners;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import builders.samudra.RepeatedCallerActivity;
import builders.samudra.utils.Constants;
import builders.samudra.utils.Helper;
import builders.samudra.utils.Logger;
import de.greenrobot.event.EventBus;

import static builders.samudra.utils.Helper.normalizeNumber;

/**
 * Created by Shabaz on 29-Nov-15.
 */
public class ParseDataService extends Service
{

    Logger mLog = new Logger(ParseDataService.class.getSimpleName());
    String phNumber=Helper.phoneNumber;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        mLog.d("Parse Fetch Data Called");
        phNumber = normalizeNumber(Helper.phoneNumber);
        mLog.d("PhoneNumber = "+phNumber);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DETAILS");
        query.whereEqualTo("MobileNumber", phNumber);
        try
        {
            ParseObject details = query.getFirst();
            mLog.d("Successful Fetch");
            mLog.d("Details name = "+details.getString("Name")); //new user
            Intent mIntent = new Intent(this, RepeatedCallerActivity.class);
            intent.putExtra("NUMBER",phNumber);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            EventBus.getDefault().postSticky(details);
            startActivity(mIntent);
            Constants.REPEATED = true;


        } catch (ParseException e)
        {
            mLog.d("UNSuccessful Fetch");
            //e.printStackTrace();
            Constants.REPEATED = false;
            EventBus.getDefault().postSticky(phNumber);
        }
        catch (Exception e)
        {
            Intent mIntent = new Intent(this, RepeatedCallerActivity.class);
            intent.putExtra("NUMBER",phNumber);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            Constants.REPEATED = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy()
    {
        mLog.d("Parse Fetch Data STOPPED");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
