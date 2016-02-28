package builders.samudra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetUp extends AppCompatActivity
{

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
            mobileNumber = (EditText) findViewById(R.id.set_up_name);
            Button continueButton = (Button) findViewById(R.id.continue_button);
            continueButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(mobileNumber.getText()))
                        Toast.makeText(SetUp.this,"Please Enter Your Name/Mobile Number",Toast.LENGTH_LONG);
                    else
                    {
                        //Todo add the name and mobile number to Firebase
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("NAME",name.getText().toString());
                        editor.putString("NUMBER",mobileNumber.getText().toString());
                        editor.putBoolean("SETUP_COMPLETE", true);
                        editor.commit();
                        Toast.makeText(SetUp.this, "PSetUp complete", Toast.LENGTH_LONG);
                        startActivity(new Intent(SetUp.this,MainActivity.class));
                        finish();
                    }
                }
            });
        }
        else
        {
            startActivity(new Intent(SetUp.this,MainActivity.class));
            finish();
        }
    }

}
