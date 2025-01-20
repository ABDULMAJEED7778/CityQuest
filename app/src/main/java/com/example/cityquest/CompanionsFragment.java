package com.example.cityquest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.Trips;

public class CompanionsFragment extends Fragment {

    private Button nextBtn, backBtn;
    private ReadyTrips trip;
    private CardView selectedCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_companions, container, false);

        CardView soloCard = view.findViewById(R.id.going_solo_card);
        CardView partnerCard = view.findViewById(R.id.partner_card);
        CardView familyCard = view.findViewById(R.id.family_card);
        CardView friendsCard = view.findViewById(R.id.friends_card);

        TextView soloCardText = view.findViewById(R.id.solo_card_text);
        TextView partnerCardText = view.findViewById(R.id.partner_card_text);
        TextView familyCardText = view.findViewById(R.id.family_card_text);
        TextView friendsCardText = view.findViewById(R.id.friends_card_text);

        // Apply gradient shader to text
        Shader textShader = new LinearGradient(
                0, 0, 0, soloCardText.getTextSize(),
                new int[]{
                        Color.parseColor("#f5bd45"),
                        Color.parseColor("#D6A53C")
                }, null, Shader.TileMode.CLAMP);

        soloCardText.getPaint().setShader(textShader);
        partnerCardText.getPaint().setShader(textShader);
        familyCardText.getPaint().setShader(textShader);
        friendsCardText.getPaint().setShader(textShader);

        // Apply touch animations to cards
        applyTouchAnimation(soloCard);
        applyTouchAnimation(partnerCard);
        applyTouchAnimation(familyCard);
        applyTouchAnimation(friendsCard);

        // Set click listeners for card selection
        soloCard.setOnClickListener(v -> selectCard(soloCard));
        partnerCard.setOnClickListener(v -> selectCard(partnerCard));
        familyCard.setOnClickListener(v -> selectCard(familyCard));
        friendsCard.setOnClickListener(v -> selectCard(friendsCard));

        trip = getArguments() != null ? getArguments().getParcelable("trip") : new ReadyTrips();

        nextBtn = view.findViewById(R.id.nextBtn_comp);
        nextBtn.setOnClickListener(v -> {
            if (selectedCard != null) {
                // Proceed to the next fragment/activity
//                Intent intent = new Intent(getActivity(), InterestsActivity.class);
//                intent.putExtra("trip", trip);
//                startActivity(intent);
//                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                InterestsFragment interestsFragment = new InterestsFragment();
                Bundle args = new Bundle();
                args.putParcelable("trip", trip);
                interestsFragment.setArguments(args);
                ((MainActivity) requireActivity()).switchToNextFragment(interestsFragment);
            } else {
                Toast.makeText(getContext(), "Please select a companion.", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn = view.findViewById(R.id.backBtn_comp);
        backBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        return view;
    }

    private void selectCard(CardView cardView) {
        if (selectedCard != null) {
            // Remove foreground from previously selected card
            selectedCard.setForeground(null);
        }
        // Set foreground for newly selected card
        cardView.setForeground(ContextCompat.getDrawable(requireContext(), R.drawable.companions_selected_background));
        selectedCard = cardView;

        // Determine the companion type based on the selected card
        String companionType = "";
        if (cardView.getId() == R.id.going_solo_card) {
            companionType = "Solo";
        } else if (cardView.getId() == R.id.partner_card) {
            companionType = "Partner";
        } else if (cardView.getId() == R.id.family_card) {
            companionType = "Family";
        } else if (cardView.getId() == R.id.friends_card) {
            companionType = "Friends";
        }

        // Set the companion type in the trip object
        trip.setCompanionType(companionType);
    }

    private void applyTouchAnimation(CardView cardView) {
        cardView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Start scale-down animation
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shrink_animation));
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                // Start scale-up animation
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.release_animation));
            }
            return false;
        });
    }
}
