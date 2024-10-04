package com.example.cityquest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.cityquest.adapter.CompanionAdapter;
import com.example.cityquest.model.Trips;

public class CompanionsActivity extends AppCompatActivity {

    Button nextBtn,backBtn;
    Trips trip;
    CompanionAdapter adapter;
    private CardView selectedCard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_companions);


        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.secondary_color_dark));

        CardView soloCard = findViewById(R.id.going_solo_card);
        CardView partnerCard = findViewById(R.id.partner_card);
        CardView familyCard = findViewById(R.id.family_card);
        CardView friendsCard = findViewById(R.id.friends_card);

        TextView soloCardText = findViewById(R.id.solo_card_text);
        TextView partnerCardText = findViewById(R.id.partner_card_text);
        TextView familyCardText = findViewById(R.id.family_card_text);
        TextView friendsCardText = findViewById(R.id.firends_card_text);
        // Set click listeners for each card


        Shader textShader = new LinearGradient(
                0, 0, 0, soloCardText.getTextSize(),
                new int[]{
                        Color.parseColor("#f5bd45"),
                        Color.parseColor("#D6A53C")
                }, null, Shader.TileMode.CLAMP);
        // Apply the shader to the text
        soloCardText.getPaint().setShader(textShader);
        partnerCardText.getPaint().setShader(textShader);
        familyCardText.getPaint().setShader(textShader);
        friendsCardText.getPaint().setShader(textShader);


        // Apply the touch animation method to all cards
        applyTouchAnimation(soloCard);
        applyTouchAnimation(friendsCard);
        applyTouchAnimation(familyCard);
        applyTouchAnimation(partnerCard);




        // Apply the shader to the text
        soloCardText.getPaint().setShader(textShader);


        soloCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(soloCard);
            }
        });

        partnerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(partnerCard);
            }
        });

        familyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(familyCard);
            }
        });

        friendsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard(friendsCard);
            }
        });


        trip = getIntent().getParcelableExtra("trip");
        if (trip == null) {
            trip = new Trips();
        }

        nextBtn = findViewById(R.id.nextBtn_comp);
        nextBtn.setOnClickListener(v -> {
//            String selectedCompanion = adapter.getSelectedCompanion();
//            if (selectedCompanion != null) {
//                trip.setCompanion(selectedCompanion); // Store selected companion in trip
                Intent intent = new Intent(CompanionsActivity.this, InterestsActivity.class);
                intent.putExtra("trip", trip); // Pass trip object to next activity
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            } else {
//                // Optionally show a message that no companion is selected
//                Toast.makeText(this, "Please select a companion.", Toast.LENGTH_SHORT).show();
//            }

        });
        backBtn = findViewById(R.id.backBtn_comp);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CompanionsActivity.this,DateRangeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

    }

    // Method to handle card selection
    private void selectCard(CardView cardView) {
        if (selectedCard != null) {
            // Remove foreground from the previously selected card
            selectedCard.setForeground(null);
        }

        // Set foreground for the newly selected card
        cardView.setForeground(ContextCompat.getDrawable(this, R.drawable.companions_selected_background));

        // Update the selected card
        selectedCard = cardView;
    }

    private void applyTouchAnimation(CardView cardView) {
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Start scale-down animation
                    v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_animation));
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Start scale-up animation
                    v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.release_animation));
                }
                return false;
            }
        });
    }

}