package builders.samudra;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseObject;

import builders.samudra.utils.Logger;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity
{
    TextView mTextView ;
    Logger mLog = new Logger(MainActivity.class.getSimpleName());
    private Menu mMenu;
    boolean currentlyRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextView = (TextView) findViewById(R.id.textView);
        EventBus.getDefault().registerSticky(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void onEvent(ParseObject details)
    {
        mLog.d("Got Event");
            //Repeadted Caller
            mLog.d("Event was ok, Repeated Caller");
            mTextView.setText(details.getString("mobileNumber")+"  "+details.getString("name"));
    }
    public void onEvent(String phNumber)
    {


            //New Caller
            mLog.d("Event was null AKA New USER");
            mTextView.setText("New User "+ phNumber);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
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
                break;
            case R.id.action_record:
                currentlyRecording = !currentlyRecording;
                if(currentlyRecording)
                {
                    //ToDo call record function to start recording
                    mMenu.findItem(R.id.action_record).setIcon(R.drawable.stop);
                }
                else
                {
                    mMenu.findItem(R.id.action_record).setIcon(R.drawable.record);
                }
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
