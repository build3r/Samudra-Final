package builders.samudra;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Timer;
import java.util.TimerTask;

import builders.samudra.listeners.RecorderService;
import builders.samudra.utils.Helper;
import builders.samudra.utils.Logger;
import de.greenrobot.event.EventBus;

public class RepeatedCallerActivity extends AppCompatActivity
{
    Logger mLog = new Logger(RepeatedCallerActivity.class.getSimpleName());
    FloatingActionButton fab;
    Menu mMenu;
    Timer t;
    int hours = 0, minutes = 0, seconds = 0;
    boolean currentlyRecording = false;
    String phNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeated_caller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.repeated_save);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Data Updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        phNumber = getIntent().getStringExtra("NUMBER");
        if(phNumber==null || phNumber.isEmpty())
        {
            phNumber = Helper.phoneNumber;
        }

    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(ParseObject reParseObject)
    {
        mLog.d("Got Event");
        //Repeadted Caller
        mLog.d("Event was ok, Repeated Caller");

        String reMobileNumber = reParseObject.get("MobileNumber").toString();
        String reName = reParseObject.get("Name").toString();
        String reGender = reParseObject.get("Gender").toString();
        //ToDo Query Experts Table,Notes Table,Category Table
        String reExpertAssigned = "Dr. Lalith";//reParseObject.get("reExpertAssign").toString();
        String rePermanentAddress = reParseObject.get("PermanentAddress").toString();

        TextView reMobileNumberText,reNameText,reGenderText,reExpertAssignedText,rePermanentAddressText;

        reMobileNumberText = (TextView) findViewById(R.id.re_mobile_no_id);
        reNameText = (TextView) findViewById(R.id.re_name_id);
        reGenderText = (TextView) findViewById(R.id.re_gender_id);
        reExpertAssignedText = (TextView) findViewById(R.id.reexpert_id);

        reMobileNumberText.setText(reMobileNumber);
        reNameText.setText(reName);
        reGenderText.setText(reGender);
        reExpertAssignedText.setText(reExpertAssigned);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }
    public void onEvent(Boolean succesfullySaved)
    {
        if (succesfullySaved)
        {
            if (currentlyRecording)
            {
                if (t != null)
                {
                    t.cancel();
                }
                currentlyRecording = !currentlyRecording;
                mMenu.findItem(R.id.action_record).setIcon(R.drawable.record);
            }
            findViewById(R.id.heading).setVisibility(View.GONE);
            findViewById(R.id.timer).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.status)).setText("Call Recording Saved");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_desktop:
                //ToDo send What ever filled data to tempTable same as inTake
                Snackbar.make(findViewById(R.id.repeat_layout), "Data Synced With DeskTop", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //sendFormDataToParse("TEMPCOOR");
                break;
            case R.id.action_record:
                if (Helper.PHONE_STATE.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                {
                    currentlyRecording = !currentlyRecording;
                    if (currentlyRecording)
                    {
                        Intent callIntent = new Intent(this, RecorderService.class);

                        callIntent.putExtra("NUMBER", phNumber);
                        ComponentName name = startService(callIntent);
                        if (null == name)
                        {
                            Log.e("CallRecorder", "startService for RecordService returned null ComponentName");
                        } else
                        {
                            Log.i("CallRecorder", "startService returned " + name.flattenToString());
                        }
                        startTimer();

                        mMenu.findItem(R.id.action_record).setIcon(R.drawable.stop);
                        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.recorderStatus);
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        mRelativeLayout.findViewById(R.id.heading).setVisibility(View.VISIBLE);
                        mRelativeLayout.findViewById(R.id.timer).setVisibility(View.VISIBLE);
                        ((TextView) mRelativeLayout.findViewById(R.id.status)).setText("Call Recording : ");
                    } else
                    {
                        t.cancel();

                        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.recorderStatus);
                        mRelativeLayout.findViewById(R.id.heading).setVisibility(View.GONE);
                        mRelativeLayout.findViewById(R.id.timer).setVisibility(View.GONE);
                        ((TextView) mRelativeLayout.findViewById(R.id.status)).setText("Saving the Recorded Call...");

                        Boolean stopped = stopService(new Intent(this, RecorderService.class));
                        Log.i("CallRecorder", "stopService for RecordService returned " + stopped);
                        mMenu.findItem(R.id.action_record).setIcon(R.drawable.record);
                    }

                }


                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer()
    {
        //Set the schedule function and rate
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask()
        {

            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        TextView tv = (TextView) findViewById(R.id.timer);
                        tv.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                        seconds += 1;

                        if (seconds == 60)
                        {
                            seconds = 0;
                            minutes = minutes + 1;

                        }
                        if (minutes == 60)
                        {
                            minutes = 0;
                            hours += 1;
                        }
                    }

                });
            }

        }, 0, 1000);

    }

}
