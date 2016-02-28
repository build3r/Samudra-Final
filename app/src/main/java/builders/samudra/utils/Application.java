package builders.samudra.utils;

import android.content.Context;

import com.firebase.client.Firebase;
import com.parse.Parse;

/**
 * Created by Shabaz on 28-Nov-15.
 */
public class Application extends android.app.Application
{
    public static Context context;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Constants.REPEATED = false;
        this.context = this.getApplicationContext();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "GV7qkwQ7ITIa75MF1P0JxcsKu4MToseJt8wnGQa0", "Zo9ZgmurJFJKSCKo9BpsbD72lEEJ64DO134z810y");
        Firebase.setAndroidContext(this);
    }
}
