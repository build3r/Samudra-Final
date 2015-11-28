package builders.samudra.Network;

import android.os.AsyncTask;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import builders.samudra.utils.Helper;
import builders.samudra.utils.Logger;
import de.greenrobot.event.EventBus;

/**
 * Created by Shabaz on 28-Nov-15.
 */
public class ParseGetData extends AsyncTask<String,Integer,ParseObject>
{
    Logger mLog = new Logger(ParseGetData.class.getSimpleName());
    String phNumber=Helper.phoneNumber;
    @Override
    protected ParseObject doInBackground(String... params)
    {
        phNumber = Helper.normalizeNumber(params[0]);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("intakeForm");
        query.whereEqualTo("mobileNumber", phNumber);
        try
        {
            ParseObject details = query.getFirst();
            mLog.d("Successful Fetch");
            mLog.d("Details name = "+details.getString("name"));
            return details;

        } catch (ParseException e)
        {
            mLog.d("UNSuccessful Fetch");
            //e.printStackTrace();
            return null;
        }

    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param details The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(ParseObject details)
    {
        super.onPostExecute(details);
        mLog.d("Posting Event");
        if(details==null)
        {
            //new user

            EventBus.getDefault().postSticky(phNumber);
        }
        else {

            EventBus.getDefault().postSticky(details);
        }
    }
}
