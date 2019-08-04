package com.project.foodfriends.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodfriends.R;

import com.project.foodfriends.daos.UserDao;
import com.project.foodfriends.entities.FoodType;
import com.project.foodfriends.entities.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    private User mUser;
    private Spinner mFoodTypeSpinner;
    private RadioGroup mRadioGroup;
    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mSaveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        final MainActivity thisActivity = this;
        this.addDbListener();

        mFoodTypeSpinner = (Spinner) findViewById(R.id.food_type_spinner);
        mFoodTypeSpinner.setAdapter(new ArrayAdapter<FoodType>(this, android.R.layout.simple_spinner_item, FoodType.values()));
        mFoodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mUser != null){
                    mUser.setPreferredFood((FoodType) mFoodTypeSpinner.getItemAtPosition(position));
                }
//                thisActivity.setUserValuesOnViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMaleRadio = (RadioButton)findViewById(R.id.main_radio_male);
        mFemaleRadio = (RadioButton)findViewById(R.id.main_radio_female);
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
            if(id == mMaleRadio.getId()){
                mUser.setPreferredMale(true);
            }
            else{
                mUser.setPreferredMale(false);
            }
//            thisActivity.setUserValuesOnViews();
            }
        });

        mStartTimeButton = (Button)findViewById(R.id.start_time_button);
        mStartTimeButton.setOnClickListener(this);

        mEndTimeButton = (Button)findViewById(R.id.end_time_button);
        mEndTimeButton.setOnClickListener(this);

        mSaveButton = (Button)findViewById(R.id.save_main_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                UserDao.getInstance().insertUser(user.getUid(), mUser);
                Intent intent = new Intent(thisActivity, ResultsActivity.class);
                intent.putExtra("user", mUser);
                startActivity(intent);
            }
        });
    }

    private void addDbListener() {
        final MainActivity thisActivity = this;
        ValueEventListener myUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                thisActivity.setUserValuesOnViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(mFirebaseUser.getUid()).addValueEventListener(myUserListener);
    }

    private void setUserValuesOnViews(){
        int pos = ((ArrayAdapter<FoodType>)this.mFoodTypeSpinner.getAdapter())
                .getPosition(this.mUser.getPreferredFood());
        this.mFoodTypeSpinner.setSelection(pos);

        mRadioGroup.check(mUser.isPreferredMale() ? R.id.main_radio_male : R.id.main_radio_female);

        mStartTimeButton.setText(this.getTimeFormat(mUser.getStartHour(), mUser.getStartMinute()));
        mEndTimeButton.setText(this.getTimeFormat(mUser.getEndHour(), mUser.getEndMinute()));
    }

    private String getTimeFormat(int hour, int minute){
        String stringTime = "" + (hour > 9 ? hour : "0" + hour);
        stringTime += ":" + (minute > 9 ? minute : "0" + minute);
        return stringTime;
    }

    @Override
    public void onClick(View view) {
        TimePickerDialog.OnTimeSetListener listener = null;
        Integer hour = null, minute = null;
        final MainActivity thisActivity = this;
        switch (view.getId()){
            case R.id.start_time_button:
                listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        mUser.setStartHour(h);
                        mUser.setStartMinute(m);
                        thisActivity.setUserValuesOnViews();
                    }
                };
                hour = mUser.getStartHour();
                minute = mUser.getStartMinute();
                break;
            case R.id.end_time_button:
                listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        mUser.setEndHour(h);
                        mUser.setEndMinute(m);
                        thisActivity.setUserValuesOnViews();
                    }
                };
                hour = mUser.getEndHour();
                minute = mUser.getEndMinute();
                break;
        }
        TimePickerDialog tpd = new TimePickerDialog(this, listener, hour, minute, true);
        tpd.show();
    }
}
