package com.example.baads;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import  android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class addNewActivity extends BottomSheetDialogFragment {

        public static final String TAG = "addNewActivity";

        private TextView setduedate;
        private EditText Edit;
        private Button savebutton;
        private FirebaseFirestore firestore;
        private Context context;
        private String dueDate = "";
        private String id = "";
        private String dueDateUpdate = "";

        public static addNewActivity newInstance(){

            return new addNewActivity();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.add_new_activity , container , false);

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

           setduedate = view.findViewById(R.id.setduey);
            Edit = view.findViewById(R.id.edittexty);
            savebutton = view.findViewById(R.id.savebutton3);

            firestore = FirebaseFirestore.getInstance();

            boolean isUpdate = false;

            final Bundle bundle = getArguments();
            if (bundle != null){
                isUpdate = true;
                String task = bundle.getString("DAILY ACTIVITY");
                id = bundle.getString("ID");
                dueDateUpdate = bundle.getString("DUE DATE");

                Edit.setText(task);
                setduedate.setText(dueDateUpdate);

                if (task.length() > 0){
                    savebutton.setEnabled(false);
                    savebutton.setBackgroundColor(Color.GRAY);
                }
            }

            Edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals("")){
                        savebutton.setEnabled(false);
                        savebutton.setBackgroundColor(Color.GRAY);
                    }else{
                        savebutton.setEnabled(true);
                        savebutton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            setduedate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();

                    int MONTH = calendar.get(Calendar.MONTH);
                    int YEAR = calendar.get(Calendar.YEAR);
                    int DAY = calendar.get(Calendar.DATE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            setduedate.setText(dayOfMonth + "-" + month + "-" + year);
                            dueDate = dayOfMonth + "-" + month +"-"+year;

                        }
                    } , YEAR , MONTH , DAY);

                    datePickerDialog.show();
                }
            });

            boolean finalIsUpdate = isUpdate;
            savebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String daily = Edit.getText().toString();

                    if (finalIsUpdate){
                        firestore.collection("Dailyactivity").document(id).update("dailyactivity" , daily , "due" , dueDate);
                        Toast.makeText(context, "Daily Updated", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        if (daily.isEmpty()) {
                            Toast.makeText(context, "oopies!!!!! cant be empty. you did something today for sure ", Toast.LENGTH_SHORT).show();
                        } else {

                            Map<String, Object> dailyMap = new HashMap<>();

                            dailyMap.put("daily activity", daily);
                            dailyMap.put("due", dueDate);
                            dailyMap.put("status", 0);
                            dailyMap.put("time", FieldValue.serverTimestamp());


                            firestore.collection("Dailyactivity").add(dailyMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "yaayy!! activity Saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    dismiss();
                }
            });
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Override
        public void onDismiss(@NonNull DialogInterface dialog) {
            super.onDismiss(dialog);
            Activity activity = getActivity();
            if (activity instanceof closeListener){
                ((closeListener)activity).onDialogClose(dialog);
            }
        }
}


