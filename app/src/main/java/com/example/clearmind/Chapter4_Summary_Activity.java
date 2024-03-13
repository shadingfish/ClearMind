package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter4_Summary_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private String txt_answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4_summary);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        answer2 = findViewById(R.id.input2);
        answer3 = findViewById(R.id.input3);
        answer4 = findViewById(R.id.input4);

        // Retrieve and Display user input from the database
        db.child("Chapter4").child("summary").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_summary = (HashMap<String, String>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_summary != null){
                        answer2.setText(hashmap_summary.get("answer2"));
                        answer3.setText(hashmap_summary.get("answer3"));
                        answer4.setText(hashmap_summary.get("answer4"));

                        String current_answer1 = hashmap_summary.get("answer1");
                        switch (current_answer1) {
                            case "1":
                                radiogroup1.check(R.id.radiobtn_1);
                                break;
                            case "2":
                                radiogroup1.check(R.id.radiobtn_2);
                                break;
                            case "3":
                                radiogroup1.check(R.id.radiobtn_3);
                                break;
                            case "4":
                                radiogroup1.check(R.id.radiobtn_4);
                                break;
                            case "5":
                                radiogroup1.check(R.id.radiobtn_5);
                                break;
                        };
                    }
                }
            }
        });

        // get user's new radio button chose
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button = (RadioButton) findViewById(checkedId);
//                selected_button.setChecked(true);
                txt_answer1 = selected_button.getText().toString();
//                Toast.makeText(getApplicationContext(), "your choice is " + selected_button.getText(), Toast.LENGTH_LONG).show();
            }
        });


        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter4();
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
                // Read user's input and button chose, and write them to database
                String txt_answer2 = answer2.getText().toString();
                String txt_answer3 = answer3.getText().toString();
                String txt_answer4 = answer4.getText().toString();

                if (txt_answer1 == null || txt_answer2.isEmpty() || txt_answer3.isEmpty() || txt_answer3.isEmpty()){
                    Toast.makeText(Chapter4_Summary_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    // Get all answers from user
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("answer1", txt_answer1);
                    map.put("answer2", txt_answer2);
                    map.put("answer3", txt_answer3);
                    map.put("answer4", txt_answer4);

                    db.child("Chapter4").child("summary").child(username).setValue(map);

                    //Done: Do we need to check the progress status at this point? - No need
                    Map<String, Object> update = new HashMap<>();
                    update.put("chapter4", "2");
                    db.child("progress").child(username).updateChildren(update);

                    // update Chapter progress
                    Map<String, Object> chapter_progress_update = new HashMap<>();
                    chapter_progress_update.put("7_Summary", "1");
                    db.child("Chapter4").child("progress").child(username).updateChildren(chapter_progress_update);

                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter4_Activity5_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4() {
        Intent intent = new Intent(this,Chapter4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
