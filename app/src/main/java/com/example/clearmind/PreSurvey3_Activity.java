package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PreSurvey3_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button button_back;
    private Button button_next;

    private String scale_answer6;
    private String scale_answer7;
    private String scale_answer8;
    private String scale_answer9;
    private String scale_answer10;

    private long pageOpenTime;
    private long pageCloseTime;

    private DatabaseReference activityRef;
    private String activityId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_3);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        RadioGroup radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        RadioGroup radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);

        activityRef = db.child("userActivity").child(username).child("PreSurvey_3");
        activityId = intent.getStringExtra("activityId");

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer6 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer7 = selected_button_1.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer8 = selected_button_1.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer9 = selected_button_1.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer10 = selected_button_1.getText().toString();
            }
        });


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean toNextPage = false;

                if (scale_answer6==null || scale_answer7==null || scale_answer8==null || scale_answer9==null || scale_answer10==null){
                    Toast.makeText(PreSurvey3_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PreSurvey").child(username).child("6").setValue(scale_answer6);
                    db.child("PreSurvey").child(username).child("7").setValue(scale_answer7);
                    db.child("PreSurvey").child(username).child("8").setValue(scale_answer8);
                    db.child("PreSurvey").child(username).child("9").setValue(scale_answer9);
                    db.child("PreSurvey").child(username).child("10").setValue(scale_answer10);

                    toNextPage = true;
                }

                if (toNextPage){
                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,PreSurvey2_Activity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,PreSurvey4_Activity.class);
        intent.putExtra("username", username);
        intent.putExtra("activityId", activityId);
        Log.d("activityId: ", activityId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageOpenTime = System.currentTimeMillis(); // get the page open time
    }

    @Override
    protected void onPause() {
        super.onPause();
        pageCloseTime = System.currentTimeMillis(); // get the page close time

//        if (pageCloseTime - pageOpenTime > 1999){   // Only if the view time >= 2 seconds
//            sendTimeStampsToFirebase(); // store the Time Stamp to Firebase
//        }
        sendTimeStampsToFirebase();
    }

    private void sendTimeStampsToFirebase() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        Date resultdate_open = new Date(pageOpenTime);
        Date resultdate_close = new Date(pageCloseTime);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("openTime_ms", pageOpenTime);
        activityData.put("openTime_str", String.valueOf(resultdate_open));

        activityData.put("closeTime_ms", pageCloseTime);
        activityData.put("closeTime_str", String.valueOf(resultdate_close));

        activityData.put("duration", pageCloseTime - pageOpenTime);

        if (activityId != null) {
            activityRef.child(activityId).setValue(activityData)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Activity time recorded successfully"))
                    .addOnFailureListener(e -> Log.d("Firebase", "Failed to record activity time", e));

//            db.child("userActivity").child(username).child("PreSurvey_0_All").child(activityId).child("openTime_ms").setValue(pageOpenTime);
//            db.child("userActivity").child(username).child("PreSurvey_0_All").child(activityId).child("openTime_str").setValue(String.valueOf(resultdate_open));
        }
    }

}
