package com.example.baads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class newdailyActivity extends AppCompatActivity {
    EditText dailyEditText;
    ImageButton savedailybutton;
    TextView pageDailyTextView;
    String daily, docId;
    boolean isEditMode

 = false;
    TextView deleteActivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdaily);
        dailyEditText = findViewById(R.id.activity_daily_text);
        savedailybutton = findViewById(R.id.saveButtondaily);
        pageDailyTextView = findViewById(R.id.page_daily);
        deleteActivityTextView = findViewById(R.id.deleteactivitybtn);

        daily = getIntent().getStringExtra("daily");
       docId = getIntent().getStringExtra("docId");
       if (docId!=null && !docId.isEmpty()) {
           isEditMode = true;
       }

       dailyEditText.setText(daily);
       if(isEditMode) {
           pageDailyTextView.setText("Edit your activity");
           deleteActivityTextView.setVisibility(View.VISIBLE);
       }


        savedailybutton.setOnClickListener((v) -> saveActivity());
        deleteActivityTextView.setOnClickListener((v)-> deleteactivityfromfirestore());
    }

    void saveActivity() {
        String activityDaily = dailyEditText.getText().toString();
        if (activityDaily==null || activityDaily.isEmpty() ) {
            dailyEditText.setError("Daily activity is required");
            return;
        }
        Activity activity = new Activity();
        activity.setDaily(activityDaily);
        activity.setTimestamp(Timestamp.now());

        saveActivityToFirebase(activity);
    }

    void saveActivityToFirebase(Activity activity){
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForActivities().document(docId);
        }else {
            documentReference = Utility.getCollectionReferenceForActivities().document();
        }

        documentReference.set(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(newdailyActivity.this, "Activity added successfully");
                    finish();
                } else {
                    Utility.showToast(newdailyActivity.this, "Activity couldnt be added");
                }
            }
        });

    }
    void deleteactivityfromfirestore(){
        DocumentReference documentReference;


        documentReference = Utility.getCollectionReferenceForActivities().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(newdailyActivity.this, " Activity deleted successfully ");
                    finish();

                } else {
                    Utility.showToast(newdailyActivity.this, "sorry couldn't delete ");
                }
            }
        });

    }

}