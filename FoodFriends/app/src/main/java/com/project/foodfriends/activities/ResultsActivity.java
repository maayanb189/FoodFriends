package com.project.foodfriends.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.foodfriends.R;
import com.project.foodfriends.entities.User;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    private User mUser;
    private List<User> mResultUsers;
    private ListView mListView;
    private ArrayAdapter<User> mCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        // Initialize Firebase Auth
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUser = (User)getIntent().getSerializableExtra("user");
        if (mFirebaseUser == null || mUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        mResultUsers = new ArrayList<>();
        this.addDbListener();
        final ResultsActivity thisActivity = this;
        mListView = (ListView)findViewById(R.id.results_list);
        // get data from the table by the ListAdapter
        mCustomAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, mResultUsers);
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(mCustomAdapter.getItem(position).getEmail()) +
                        "?subject=" + Uri.encode("mail from FoodFriends") +
                        "&body=" + Uri.encode("let's eat together!");
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));
            }
        });
    }

    private void addDbListener() {
        final ResultsActivity thisActivity = this;
        ValueEventListener myUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User currUser = userSnapshot.getValue(User.class);
                    if(thisActivity.isUserResult(currUser)){
                        mResultUsers.add(currUser);
                        mCustomAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").addValueEventListener(myUserListener);
    }

    private boolean isUserResult(User currUser) {

        if(!currUser.getId().equals(mUser.getId())){
            int myStartTimestamp = getTimestamp(mUser.getStartHour(), mUser.getStartMinute());
            int myEndTimestamp = getTimestamp(mUser.getEndHour(), mUser.getEndMinute());
            int otherStartTimestamp = getTimestamp(currUser.getStartHour(), currUser.getStartMinute());
            int otherEndTimestamp = getTimestamp(currUser.getEndHour(), currUser.getEndMinute());

            // my start-time is between other's, or other's start-time is between mine
            if((myStartTimestamp >= otherStartTimestamp && myStartTimestamp <= otherEndTimestamp) ||
               (otherStartTimestamp >= myStartTimestamp && otherStartTimestamp <= myEndTimestamp)){
                if(currUser.getPreferredFood() == mUser.getPreferredFood()) {
                    return true;
                }
            }
        }

        return false;
    }

    private int getTimestamp(int hour, int minute){
        return hour * 100 + minute;
    }

    private void setUserValuesOnViews(){

    }
}
