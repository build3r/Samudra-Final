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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import builders.samudra.listeners.RecorderService;
import builders.samudra.utils.Helper;
import builders.samudra.utils.Logger;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView mTextView;
    Button submitButton;
    EditText etMobileNumber;
    EditText etName;
    EditText etDob;
    RadioGroup etGender;
    EditText etEducationQualification;
    EditText etNameOfInstitution;
    EditText etWorkExprience;
    EditText etPermanentAddress;
    EditText etResidentialAddress;
    EditText etSecondaryMobile;
    EditText etEmail;
    RadioButton etRelationshipStatus;
    EditText etHobbies;
    EditText etFatherName;
    EditText etFatherQual;
    EditText etFatherOcu;
    EditText etMothersName;
    EditText etMotherQual;
    EditText etMotherOcu;
    RadioGroup etParentsMaritialStatus;
    EditText etTotalMember;
    EditText etFamilyIncome;
    EditText etFamilyHistory;
    EditText etChildhoodBehavior;
    EditText etWhyYouNeedCounseling;
    EditText etWhatYouExpectFromCounselor;
    CheckBox etIssuesBotherYou;
    String mMobileNumber;
    String mName;
    String mDOB;
    String mGender;
    String mEducationQualification;
    String mNameOfInstitution;
    String mWorkExperience;
    String mEmailID;
    String mPermanentAddress;
    String mResidentialAddress;
    String mSecondaryMobile;
    String mRelationshipStatus;
    String mHobbies;
    String mFathersDetails;
    String mMothersDetails;
    String mParentsMaritialStatus;
    String mTotalMember;
    String mFamilyIncome;
    String mFamilyHistory;
    String mChildhoodBehavior;
    String mWhyYouNeedCounseling;
    String mWhatYouExpectFromCounselor;
    String mIssuesBotherYou;
    Logger mLog = new Logger(MainActivity.class.getSimpleName());
    String phNumber;
    boolean currentlyRecording = false;
    Timer t;
    int hours = 0, minutes = 0, seconds = 0;
    String fileName = "";
    private Menu mMenu;
    String dateStamp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etMobileNumber = (EditText) findViewById(R.id.mobile_no_id);
        etName = (EditText) findViewById(R.id.name_id);
        etDob = (EditText) findViewById(R.id.dob_id);
        etEducationQualification = (EditText) findViewById(R.id.education_qual_id);
        etNameOfInstitution = (EditText) findViewById(R.id.institute_id);
        etWorkExprience = (EditText) findViewById(R.id.work_exp_id);
        etPermanentAddress = (EditText) findViewById(R.id.per_address_id);
        etResidentialAddress = (EditText) findViewById(R.id.temp_address_id);
        etSecondaryMobile = (EditText) findViewById(R.id.secndary_num_id);
        etEmail = (EditText) findViewById(R.id.email_id);
        etHobbies = (EditText) findViewById(R.id.hobbies_id);
        etFatherName = (EditText) findViewById(R.id.father_name_id);
        etFatherQual = (EditText) findViewById(R.id.father_qual_id);
        etFatherOcu = (EditText) findViewById(R.id.father_occ_id);
        etMothersName = (EditText) findViewById(R.id.mother_name_id);
        etMotherQual = (EditText) findViewById(R.id.mother_qualifications_id);
        etMotherOcu = (EditText) findViewById(R.id.mother_occupation_id);
        etFamilyIncome = (EditText) findViewById(R.id.family_income_id);
        etFamilyHistory = (EditText) findViewById(R.id.family_history_id);
        etChildhoodBehavior = (EditText) findViewById(R.id.childhood_behavioral_issues_id);
        etWhyYouNeedCounseling = (EditText) findViewById(R.id.Why_you_need_counseling_id);
        etWhatYouExpectFromCounselor = (EditText) findViewById(R.id.what_you_expect_id);
        submitButton = (Button) findViewById(R.id.submit_id);
        submitButton.setOnClickListener(this);
        phNumber = getIntent().getStringExtra("NUMBER");
        EventBus.getDefault().registerSticky(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    public void onEvent(ParseObject details)
    {
        mLog.d("Got Event");
        //Repeadted Caller
        mLog.d("Event was ok, Repeated Caller");
        mTextView.setText(details.getString("mobileNumber") + "  " + details.getString("name"));
    }

    public void onEvent(String phNumber)
    {       //New Caller
        mLog.d("Event was null AKA New USER");
        mTextView.setText("New User " + phNumber);
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
                Snackbar.make(findViewById(R.id.new_caller_layout), "Data Sent To DeskTop", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                sendFormDataToParse("TEMPCOOR");
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
                        findViewById(R.id.recorderStatus).setVisibility(View.VISIBLE);
                        mMenu.findItem(R.id.action_record).setIcon(R.drawable.stop);
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

    @Override
    public void onClick(View v) {
        sendFormDataToParse("DETAILS");
    }

    private void sendFormDataToParse(String tableName)
    {
        RadioGroup radioSexGroup,radioStatusGroup,radioParentStatusGroup;
        RadioButton radioSexButton, radioStatusButton, radioParentButton;
        CheckBox acoholic_id,lack_of_confidence_id,difficulty_in_communication_id,lack_of_opportunities_id,unhealthy_and_unnecessary_peer_competition_id,physical_handicap_id,OCD_By_polar_disorder_id,
                ADHD_id,psychological_issues_id,Sexuality_related_Issues_id,Family_related_issues_id,Peer_related_id,academic_related_id,health_and_fitness_id,relationship_issues_id,stress_id,
                career_confusions_id,Addictions_id;

        StringBuffer checkedList = new StringBuffer();
        ArrayList<String> checkList = new ArrayList<>();
        String temp = null;
        String mMobileNumber;
        temp = etMobileNumber.getText().toString();
        if(temp==null || temp.isEmpty())
        {
            Snackbar.make(findViewById(R.id.new_caller_layout), "Mobile Number is Mandatory", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        else
        {
            mMobileNumber = temp;
        }
        temp = etName.getText().toString();
        if(temp==null)
            mName="";
        else mName=temp;
        temp = etDob.getText().toString();
        if(temp==null)
            mDOB="";
        else mDOB=temp;
        temp = etEducationQualification.getText().toString();
        if(temp==null)
            mEducationQualification="";
        else mEducationQualification=temp;
        temp = etNameOfInstitution.getText().toString();
        if(temp==null)
            mNameOfInstitution="";
        else mNameOfInstitution=temp;
        temp = etWorkExprience.getText().toString();
        if(temp==null)
            mWorkExperience="";
        else mWorkExperience=temp;
        temp = etEmail.getText().toString();
        if(temp==null)
            mEmailID="";
        else mEmailID=temp;
        temp = etPermanentAddress.getText().toString();
        if(temp==null)
            mPermanentAddress="";
        else mPermanentAddress=temp;
        temp = etResidentialAddress.getText().toString();
        if(temp==null)
            mResidentialAddress="";
        else mResidentialAddress=temp;
        temp = etSecondaryMobile.getText().toString();
        if(temp==null)
            mSecondaryMobile="";
        else mSecondaryMobile=temp;
        temp = etSecondaryMobile.getText().toString();
        if(temp==null)
            mSecondaryMobile="";
        else mSecondaryMobile=temp;
        temp = etHobbies.getText().toString();
        if(temp==null)
            mHobbies="";
        else mHobbies=temp;

        temp = etFatherName.getText().toString();
        if(temp==null)
            mFathersDetails ="N/A";
        else mFathersDetails=temp;
        temp = etFatherQual.getText().toString();
        if(temp==null)
            mFathersDetails +="\nN/A";
        else mFathersDetails+="\n"+temp;
        temp = etFatherOcu.getText().toString();
        if(temp==null)
            mFathersDetails +="\nN/A";
        else mFathersDetails+="\n"+temp;

        temp = etMothersName.getText().toString();
        if(temp==null)
            mMothersDetails ="N/A";
        else mMothersDetails=temp;
        temp = etMotherQual.getText().toString();
        if(temp==null)
            mMothersDetails +="\nN/A";
        else mMothersDetails+="\n"+temp;
        temp = etMotherOcu.getText().toString();
        if(temp==null)
            mMothersDetails +="\nN/A";
        else mMothersDetails+="\n"+temp;
        temp = etFamilyIncome.getText().toString();
        if(temp==null)
            mFamilyIncome ="";
        else mFamilyIncome=temp;
        temp = etFamilyHistory.getText().toString();
        if(temp==null)
            mFamilyHistory ="";
        else mFamilyHistory=temp;
        temp = etChildhoodBehavior.getText().toString();
        if(temp==null)
            mChildhoodBehavior ="";
        else mChildhoodBehavior=temp;
        temp = etWhyYouNeedCounseling.getText().toString();
        if(temp==null)
            mWhyYouNeedCounseling ="";
        else mWhyYouNeedCounseling=temp;
        temp = etWhatYouExpectFromCounselor.getText().toString();
        if(temp==null)
            mWhatYouExpectFromCounselor ="";
        else mWhatYouExpectFromCounselor=temp;
        try
        {
           /*Toast.makeText(this,
                   "radioStatusButton.getText()", Toast.LENGTH_SHORT).show();*/

            checkedList.append("CheckedItems:");
            acoholic_id = (CheckBox) findViewById(R.id.acoholic_id);
            lack_of_confidence_id = (CheckBox) findViewById(R.id.lack_of_confidence_id);
            difficulty_in_communication_id = (CheckBox) findViewById(R.id.difficulty_in_communication_id);
            lack_of_opportunities_id = (CheckBox) findViewById(R.id.lack_of_opportunities_id);
            unhealthy_and_unnecessary_peer_competition_id = (CheckBox) findViewById(R.id.unhealthy_and_unnecessary_peer_competition_id);
            physical_handicap_id = (CheckBox) findViewById(R.id.physical_handicap_id);
            OCD_By_polar_disorder_id =  (CheckBox) findViewById(R.id.OCD_By_polar_disorder_id);
            ADHD_id = (CheckBox) findViewById(R.id.ADHD_id);
            psychological_issues_id = (CheckBox) findViewById(R.id.psychological_issues_id);
            Sexuality_related_Issues_id = (CheckBox) findViewById(R.id.Sexuality_related_Issues_id);
            Family_related_issues_id = (CheckBox) findViewById(R.id.Family_related_issues_id);
            Peer_related_id = (CheckBox) findViewById(R.id.Peer_related_id);
            academic_related_id = (CheckBox) findViewById(R.id.academic_related_id);
            health_and_fitness_id = (CheckBox) findViewById(R.id.health_and_fitness_id);
            relationship_issues_id = (CheckBox) findViewById(R.id.relationship_issues_id);
            stress_id = (CheckBox) findViewById(R.id.stress_id);
            career_confusions_id = (CheckBox) findViewById(R.id.career_confusions_id);
            Addictions_id = (CheckBox) findViewById(R.id.Addictions_id);

            if(acoholic_id.isChecked()) {
                checkedList.append("alcoholic");
                checkList.add("alcoholic");
            }
            if(lack_of_confidence_id.isChecked()) {
                checkedList.append("Lack Of Confidence");
                checkList.add("Lack Of Confidence");
            }
            if(difficulty_in_communication_id.isChecked()) {
                checkedList.append("Communication Difficulty");
                checkList.add("Communication Difficulty");
            }
            if(lack_of_opportunities_id.isChecked()) {
                checkedList.append("Lack Of Oppurtunities");
                checkList.add("Lack Of Oppurtunities");
            }
            if(unhealthy_and_unnecessary_peer_competition_id.isChecked()) {
                checkedList.append("Peer Pressure");
                checkList.add("Peer Pressure");
            }
            if(physical_handicap_id.isChecked()) {
                checkedList.append("Specially Abled");
                checkList.add("Specially Abled");
            }
            if(OCD_By_polar_disorder_id.isChecked()) {
                checkedList.append("OCD");
                checkList.add("OCD");
            }
            if(ADHD_id.isChecked()) {
                checkedList.append("ADHD");
                checkList.add("ADHD");
            }
            if(psychological_issues_id.isChecked()) {
                checkedList.append("Psychological Issues");
                checkList.add("Psychological Issues");
            }
            if(Sexuality_related_Issues_id.isChecked()) {
                checkedList.append("Sexuality Related Issues");
                checkList.add("Sexuality Related Issues");
            }
            if(Family_related_issues_id.isChecked()) {
                checkedList.append("Family Related Issues");
                checkList.add("Family Related Issues");
            }
            if(Peer_related_id.isChecked()) {
                checkedList.append("Peer Related");
                checkList.add("Peer Related");
            }
            if(academic_related_id.isChecked()) {
                checkedList.append("Academic Pressure");
                checkList.add("Academic Pressure");
            }
            if(health_and_fitness_id.isChecked()) {
                checkedList.append("Health Issues");
                checkList.add("Health Issues");
            }
            if(relationship_issues_id.isChecked()) {
                checkedList.append("Relationship Issues");
                checkList.add("Relationship Issues");
            }
            if(career_confusions_id.isChecked()) {
                checkedList.append("Career confusions");
                checkList.add("Career confusions");
            }
            if(stress_id.isChecked()) {
                checkedList.append("alcoholic");
                checkList.add("alcoholic");
            }
            if(Addictions_id.isChecked()) {
                checkedList.append("Addiction Issues");
                checkList.add("Addiction Issues");
            }
            if(checkedList==null)
                mIssuesBotherYou = "";
            else mIssuesBotherYou = checkedList.toString();
            mLog.d("Checked = "+checkedList.toString());

            radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
            radioStatusGroup = (RadioGroup) findViewById(R.id.radioStatus);
            radioParentStatusGroup = (RadioGroup) findViewById(R.id.radioParent);


            int selectedSexId = radioSexGroup.getCheckedRadioButtonId();
            int selectedStatusId = radioStatusGroup.getCheckedRadioButtonId();
            int selectedParentId = radioParentStatusGroup.getCheckedRadioButtonId();


            radioSexButton = (RadioButton) findViewById(selectedSexId);

            mGender =radioSexButton.getText().toString();


            radioStatusButton = (RadioButton) findViewById(selectedStatusId);
            mRelationshipStatus = radioStatusButton.getText().toString();


            radioParentButton = (RadioButton) findViewById(selectedParentId);

            mParentsMaritialStatus=radioParentButton.getText().toString();

            EditText tempB = (EditText) findViewById(R.id.brothers_no_id);


            mTotalMember = "";
            String tempValue = tempB.getText().toString();
            if(tempValue!=null)
                mTotalMember += "Brothers = "+tempValue;



            EditText tempF = (EditText) findViewById(R.id.sister_no_id);

            String tempValueF = tempF.getText().toString();
            if(tempValueF!=null)
                mTotalMember += "  Sisters = "+tempValueF;

            EditText tempO = (EditText) findViewById(R.id.other_no_id);

            String tempValueO = tempO.getText().toString();
            if(tempValueO!=null)
                mTotalMember += "  Others = "+tempValueF;


            Toast.makeText(this, mTotalMember, Toast.LENGTH_LONG  ).show();
            Log.i("Check", tempValue);

            ParseObject mParseObject = new ParseObject(tableName  );

            mParseObject.put("MobileNumber",mMobileNumber);
            mParseObject.put("Name", mName);
            mParseObject.put("DOB", mDOB);
            mParseObject.put("Gender", mGender);
            mParseObject.put("EducationQualification",mEducationQualification );
            mParseObject.put("NameOfInstitution", mNameOfInstitution);
            mParseObject.put("WorkExperience",mWorkExperience);
            mParseObject.put("EmailID",mEmailID);
            mParseObject.put("PermanentAddress", mPermanentAddress);
            mParseObject.put("ResidentialAddress",mResidentialAddress);
            mParseObject.put("SecondaryMobile",mSecondaryMobile);
            mParseObject.put("RelationshipStatus",mRelationshipStatus);
            mParseObject.put("Hobbies",mHobbies);
            mParseObject.put("FathersDetails",mFathersDetails);
            mParseObject.put("MothersDetails",mMothersDetails);
            mParseObject.put("ParentsMaritialStatus",mParentsMaritialStatus);
            mParseObject.put("TotalMember",mTotalMember);
            mParseObject.put("FamilyIncome",mFamilyIncome);
            mParseObject.put("FamilyHistory",mFamilyHistory);
            mParseObject.put("ChildhoodBehavior",mChildhoodBehavior);
            mParseObject.put("WhyYouNeedCounseling",mWhyYouNeedCounseling);
            mParseObject.put("WhatYouExpectFromCounselor",mWhatYouExpectFromCounselor);
            mParseObject.put("IssuesBotherYou",mIssuesBotherYou);
            mParseObject.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    if(e==null)
                    {
                        mLog.d("Save Successfull");
                    }
                    else
                    {
                        mLog.d("Save UNSuccessfull");

                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
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
