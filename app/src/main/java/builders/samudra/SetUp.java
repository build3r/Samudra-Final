package builders.samudra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import builders.samudra.model.Expert;
import builders.samudra.utils.Constants;

public class SetUp extends AppCompatActivity
{
    private static final String LOG_TAG = SetUp.class.getSimpleName();
    Firebase mFirebaseRef;

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseRef = new Firebase(Constants.FIREBASE_BASE_URI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Set Up");
        final SharedPreferences mSharedPreferences = getSharedPreferences("DETAILS",MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean("SETUP_COMPLETE",false))
        {
            final EditText name,mobileNumber;
            name = (EditText) findViewById(R.id.set_up_name);
            mobileNumber = (EditText) findViewById(R.id.set_up_number);
            Button continueButton = (Button) findViewById(R.id.continue_button);
            continueButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(mobileNumber.getText()))
                        Toast.makeText(SetUp.this, "Please Enter Your Name/Mobile Number", Toast.LENGTH_LONG).show();
                    else
                    {
                        //Todo add the name and mobile number to Firebase
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("NAME",name.getText().toString());
                        editor.putString("NUMBER", mobileNumber.getText().toString());
                        editor.putBoolean("SETUP_COMPLETE", true);
                        editor.commit();

                        // Create a new expert object
                        Expert expert = new Expert();
                        expert.setMobileNumber(mobileNumber.getText().toString());
                        expert.setName(name.getText().toString());

                        mFirebaseRef.child("expert").child(expert.getMobileNumber()).setValue(expert, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError == null) {
                                    Toast.makeText(SetUp.this, "PSetUp complete", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SetUp.this, "PSetUp incomplete. Contact admin", Toast.LENGTH_LONG).show();
                                    Log.d(LOG_TAG, "Issue while setting up: " + firebaseError.getMessage());
                                }
                            }
                        });

                        startActivity(new Intent(SetUp.this, MainActivity.class));
                        finish();
                    }
                }
            });
        }
        else
        {
            startActivity(new Intent(SetUp.this, MainActivity.class));
            finish();
        }
    }
}
