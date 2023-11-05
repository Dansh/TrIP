package com.example.trip;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class ShowPlan extends AppCompatActivity implements View.OnClickListener {


    LinearLayout showTripLayout;
    ImageButton showPlanMenu;
    Dialog menuDialog;
    TripPlanData tripPlanData;
    TextView dayTitleTv;
    Button allTripBtn, saveTripBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_plan);

        menuDialog = new Dialog(this);
        menuDialog.setContentView(R.layout.show_plan_dialog);

        allTripBtn = menuDialog.findViewById(R.id.allTripBtn);
        allTripBtn.setOnClickListener(this);

        showPlanMenu = findViewById(R.id.showPlanMenu);
        showPlanMenu.setOnClickListener(this);



        tripPlanData = TripPlanData.getInstance();

        // Access the data from the TripPlanData class
        Map<String, Object> planHashMap = tripPlanData.getHashMap();
        System.out.println(planHashMap);


        showTripLayout = findViewById(R.id.showTripLayout);

        // Creating the first TextView dynamically
        dayTitleTv = new TextView(getApplicationContext());
        dayTitleTv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        dayTitleTv.setTextSize(30);
        dayTitleTv.setText((String) tripPlanData.get("plan-trip"));

        dayTitleTv.setTextColor(getResources().getColor(R.color.light_blue));
        dayTitleTv.setPadding(0, 0, 0, 16); // Adding bottom padding
        showTripLayout.addView(dayTitleTv);

        saveTripBtn = findViewById(R.id.saveTripBtn);
        saveTripBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        createMenuDialog();
    }


    @Override
    public void onClick(View view) {
        if (view == showPlanMenu) {
            menuDialog.show();
        } else if (view == allTripBtn) {
            String currentText = (String) tripPlanData.get("plan-trip");
            dayTitleTv.setText(currentText);
            menuDialog.dismiss();
        } else if (view == saveTripBtn) {
            String fileName = TripData.getInstance().getAnswer("Destination") + "_" + mAuth.getUid();
            PDFCreator.createPdf(tripPlanData.get("plan-trip").toString(), fileName);
            savePDF(fileName);
        } else {
            String currentText = (String) tripPlanData.getDays().get(view.getTag().toString());
            dayTitleTv.setText(currentText);
            menuDialog.dismiss();
        }
    }

    private void createMenuDialog() {

        LinearLayout menuDialogLayout = menuDialog.findViewById(R.id.menuDialogLayout);

        int dayNum = 1;
        for (Map.Entry<String, String> entry : ((Map<String, String>) tripPlanData.get("days")).entrySet()) {
            String day = entry.getKey();
            String description = entry.getValue();

            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setTextSize(30);
            button.setTag(day);
            button.setOnClickListener(this);
            button.setText("Day " + dayNum);
            button.setTextColor(Color.rgb(48, 168, 255));
            button.setBackgroundResource(R.drawable.cool_button);
            // button.setBackgroundColor(Color.rgb(0,30,54));
            // Get the existing layout parameters of the button
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
            // Set the top margin
            int topMarginInPixels = (int) getResources().getDimension(R.dimen.button_top_margin);
            layoutParams.setMargins(0, topMarginInPixels, 0, 0);
            int paddingInPixels = (int) getResources().getDimension(R.dimen.button_padding);
            button.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
            // Apply the updated layout parameters to the button
            button.setLayoutParams(layoutParams);
            // Add the button to the container
            menuDialogLayout.addView(button);
            dayNum++;
        }


    }

    private void savePdfUrlAndTripNameToDatabase(String pdfUrl, String tripName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://trip-database-6d7f8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usersRef = database.getReference("users");

        // Assuming you have the user ID of the currently signed-in user
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a new child node with a unique key under the user's node
        DatabaseReference tripId = usersRef.child(userId).push();

        // Save the PDF URL and trip name as child nodes under the unique key
        tripId.child("pdfUrl").setValue(pdfUrl);
        tripId.child("tripName").setValue(tripName);
        Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
        startActivity(profileIntent);

    }



    public void savePDF(String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Assuming you have the PDF file URI
        Uri fileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileName + ".pdf"));

        // Create a reference to the location where you want to store the PDF file
        StorageReference pdfRef = storageRef.child("pdfs/" + fileUri.getLastPathSegment());

        // Upload the file to Firebase Storage
        UploadTask uploadTask = pdfRef.putFile(fileUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // File uploaded successfully
                // Get the download URL of the uploaded file
                pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        // Save the download URL along with the trip name in Firebase Database
                        savePdfUrlAndTripNameToDatabase(downloadUrl, fileName);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occurred during the upload
                // e.g., show an error message to the user
            }
        });
    }
}



