package builders.samudra.utils;

import android.util.Log;

import builders.samudra.BuildConfig;

/**
 * Created by Shabaz on 28-Nov-15.
 */
public class Logger
{
    String className ;
    public Logger(String clasName)
    {
       this.className= clasName;
    }

    public void d(String message)
    {
        if(BuildConfig.DEBUG)
        {
            Log.d(className, message);
        }
    }
}
