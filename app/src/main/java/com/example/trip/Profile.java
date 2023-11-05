package com.example.trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.parser.Line;

public class Profile extends AppCompatActivity implements View.OnClickListener {


    Button planTripBtn, disconnectBtn;
    FirebaseAuth mAuth;
    LinearLayout scrollViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        planTripBtn = findViewById(R.id.planTripBtn);
        planTripBtn.setOnClickListener(this);

        disconnectBtn = findViewById(R.id.disconnectBtn);
        disconnectBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        getTripNamesByUserId(mAuth.getUid());

        scrollViewLayout = findViewById(R.id.scrollViewLayout);
    }


    @Override
    public void onClick(View view) {
        if (view == planTripBtn)
        {
            Intent planTripIntent = new Intent(getApplicationContext(), QuizPage.class);
            startActivity(planTripIntent);
        }
        else if (view == disconnectBtn)
        {
            Intent disconnectIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(disconnectIntent);
        }
        else
        {
            downloadPDF(view.getTag().toString(), "trip-plan");
        }

    }

    private void getTripNamesByUserId(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://trip-database-6d7f8-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usersRef = database.getReference("users");

        // Retrieve the child node under the user's node based on the user ID
        DatabaseReference userTripsRef = usersRef.child(userId);

        // Add a listener to fetch the trip names
        userTripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                    String tripName = tripSnapshot.child("tripName").getValue(String.class);
                    tripName = tripName.replace("_" + mAuth.getUid(), "");
                    String url = tripSnapshot.child("pdfUrl").getValue(String.class);
                    createTripLinear(tripName, url);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void createTripLinear(String tripName, String url){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(layoutParams);

        TextView textView = new TextView(this);
        textView.setText(tripName);
        textView.setTextSize(30);
        textView.setTextColor(getResources().getColor(R.color.light_blue));
        textView.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textViewParams.setMargins(0, 0, 30, 0);
        textView.setLayoutParams(textViewParams);

        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageResource(android.R.drawable.stat_sys_download);
        imageButton.setTag(url);
        imageButton.setOnClickListener(this);

        linearLayout.addView(textView);
        linearLayout.addView(imageButton);

        scrollViewLayout.addView(linearLayout);
    }

    private void downloadPDF(String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("Downloading PDF")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

}