package com.example.cityquest.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.colormoon.readmoretextview.ReadMoreTextView;
import com.example.cityquest.R;
import com.example.cityquest.SignInActivity;
import com.example.cityquest.SignUpActivity;
import com.example.cityquest.ViewPostCommentsActivity;
import com.example.cityquest.model.ReadyTrips;
import com.example.cityquest.model.TravelStory;
import com.example.cityquest.utils.AndroidUtils;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.utils.PlayerManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;


import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;


import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final Context context;
    private final List<TravelStory> travelStories;
    private View.OnClickListener loadMoreClickListener;
    private Set<String> likedPostIds;
    private  Map<Integer, Boolean> expandedState = new HashMap<>();
    private Map<Integer, Integer> linesCount = new HashMap<>();



    private boolean isLoading = false;


    private final Map<Integer, Long> playbackPositions = new HashMap<>(); // Store playback positions for each video

    // Constructor
    public PostsAdapter(Context context, List<TravelStory> travelStories) {
        this.context = context;
        this.travelStories = travelStories;
        this.likedPostIds = new HashSet<>(); // Initialize with an empty set

    }
    public void updateLikedPosts(Set<String> savedTripIds) {
        this.likedPostIds.clear();
        this.likedPostIds.addAll(savedTripIds);
        notifyDataSetChanged(); // Refresh UI
    }

    // Set the listener for the "Load More" button click
    public void setLoadMoreClickListener(View.OnClickListener listener) {
        this.loadMoreClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coummunity_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelStory story = travelStories.get(position);

        // Bind text data
        holder.titleTextView.setText(story.getTitle());
        holder.userNameTV.setText(story.getUserName());

        // Get Firestore Timestamp
        Timestamp timestamp = story.getDatePosted();


// Set the time in your TextView
        holder.timeTV.setText(AndroidUtils.formatTimeAgo(timestamp));


        holder.viewsCountTV.setText(String.valueOf(story.getViews()));
        holder.likesCountTV.setText(String.valueOf(story.getLikes()));
        holder.commentsCountTV.setText(String.valueOf(story.getComments()));

        // Load user profile picture using Glide
        Glide.with(context)
                .load(story.getUserProfilePicture())
                .placeholder(R.drawable.user_placholer_icon)
                .into(holder.userImageView);

        if (position == travelStories.size() - 1) {
            holder.loadMoreButton.setVisibility(View.VISIBLE);
            holder.loadMoreButton.setOnClickListener(v -> {
                // Trigger loading more posts
                if (loadMoreClickListener != null) {
                    loadMoreClickListener.onClick(v);
                }
            });
        } else {
            holder.loadMoreButton.setVisibility(View.GONE);
        }

        if (story.getVideoUrl() != null && !story.getVideoUrl().isEmpty()) {
            holder.playerView.setVisibility(View.VISIBLE);
//            holder.viewPagerLayout.setVisibility(View.GONE);
            holder.viewPagerPhotos.setVisibility(View.GONE);
            holder.tabLayout.setVisibility(View.GONE);


            // Attach Player only; don't play yet
            ExoPlayer player = PlayerManager.getInstance(context);
            holder.playerView.setPlayer(player);

            // Set media item but don't start playback here
            MediaItem mediaItem = MediaItem.fromUri(story.getVideoUrl());
            player.setMediaItem(mediaItem);
            player.prepare();
        } else if (story.getPhotoUrls() != null && !story.getPhotoUrls().isEmpty()) {
            // Show the ViewPager and hide the PlayerView
            holder.playerView.setVisibility(View.GONE);
//            holder.viewPagerLayout.setVisibility(View.VISIBLE);
            holder.viewPagerPhotos.setVisibility(View.VISIBLE);
            holder.tabLayout.setVisibility(View.VISIBLE);


            // Set up the ViewPager2 adapter for photos
            PostImageSliderAdapter photosAdapter = new PostImageSliderAdapter(context, story.getPhotoUrls());
            holder.viewPagerPhotos.setAdapter(photosAdapter);

            if(story.getPhotoUrls().size()>1){

                holder.leftArrow.setVisibility(View.VISIBLE);
                holder.rightArrow.setVisibility(View.VISIBLE);
                new TabLayoutMediator(holder.tabLayout, holder.viewPagerPhotos, (tab, p) -> {
                    // Some implementation (if needed, e.g., set tab text)
                }).attach();

                // Update arrow visibility based on the current photo position
                holder.viewPagerPhotos.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        // Show/hide left arrow
                        if (position == 0) {
                            holder.leftArrow.setVisibility(View.GONE);
                        } else {
                            holder.leftArrow.setVisibility(View.VISIBLE);
                        }

                        // Show/hide right arrow
                        if (position == story.getPhotoUrls().size() - 1) {
                            holder.rightArrow.setVisibility(View.GONE);
                        } else {
                            holder.rightArrow.setVisibility(View.VISIBLE);
                        }
                    }
                });
                // Arrow click listeners for manual navigation
                holder.leftArrow.setOnClickListener(v -> {
                    int currentItem = holder.viewPagerPhotos.getCurrentItem();
                    if (currentItem > 0) {
                        holder.viewPagerPhotos.setCurrentItem(currentItem - 1);
                    }
                });

                holder.rightArrow.setOnClickListener(v -> {
                    int currentItem = holder.viewPagerPhotos.getCurrentItem();
                    if (currentItem < story.getPhotoUrls().size() - 1) {
                        holder.viewPagerPhotos.setCurrentItem(currentItem + 1);
                    }
                });

                holder.tabLayout.setVisibility(View.VISIBLE);
            }else {
                holder.tabLayout.setVisibility(View.GONE);

                // Hide arrows if only one photo
                holder.leftArrow.setVisibility(View.GONE);
                holder.rightArrow.setVisibility(View.GONE);

//                holder.mediaContainer.post(() -> {
//                    // Get the current height after layout pass
//                    ViewGroup.LayoutParams layoutParams = holder.mediaContainer.getLayoutParams();
//                    int currentHeight = holder.mediaContainer.getHeight();
//
//                    // Reduce height by 50dp (convert dp to pixels)
//                    int reductionInPx = (int) (50 * holder.mediaContainer.getContext().getResources().getDisplayMetrics().density);
//
//                    // Apply the new height
//                    layoutParams.height = currentHeight - reductionInPx;
//                    holder.mediaContainer.setLayoutParams(layoutParams);
//                });
            }

        } else {
            // Hide both PlayerView and ViewPager if no media is available
            holder.playerView.setVisibility(View.GONE);
//            holder.viewPagerLayout.setVisibility(View.GONE);
            holder.viewPagerPhotos.setVisibility(View.GONE);
            holder.tabLayout.setVisibility(View.GONE);
        }

        holder.commentLinearLayout.setOnClickListener(v->{
            Intent intent = new Intent(context, ViewPostCommentsActivity.class);
            intent.putExtra("postId", story.getPostId());
            context.startActivity(intent);

        });


        if (likedPostIds.contains(story.getPostId())) {
            holder.likeButton.setImageResource(R.drawable.thumbs_up_fill);
        } else {
            holder.likeButton.setImageResource(R.drawable.thumbs_up);
        }

        // Handle like button click
        holder.likeButton.setOnClickListener(v -> {
            if (likedPostIds.contains(story.getPostId())) {
                unlikePost(story.getPostId());
            } else {
                likePost(story.getPostId());
            }
        });

        // Fetch the current expanded state for this position
        boolean isExpanded = expandedState.getOrDefault(position, false);

        // Always set the text first (to avoid view recycling issues)
        holder.descriptionTV.setText(story.getDescription());

        // Use ViewTreeObserver to calculate the actual line count dynamically

        holder.descriptionTV.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Remove the listener to avoid repeated calls
                holder.descriptionTV.getViewTreeObserver().removeOnPreDrawListener(this);

                int lineCount = holder.descriptionTV.getLineCount();


                // Show or hide "Read More" based on the line count

                if (lineCount > 2) {
                    holder.readMoreButton.setVisibility(View.VISIBLE);
                } else {
                   // holder.readMoreButton.setVisibility(View.GONE);
                }

                // Apply initial collapsed or expanded state dynamically
                if (isExpanded) {
                    holder.descriptionTV.setMaxLines(Integer.MAX_VALUE); // Expand text
                    holder.descriptionTV.setEllipsize(null);
                    holder.readMoreButton.setText("Read Less");
                } else {
                    holder.descriptionTV.setMaxLines(2); // Collapse text
                    holder.descriptionTV.setEllipsize(TextUtils.TruncateAt.END);
                    holder.readMoreButton.setText("Read More");
                }


                return true; // Allow the drawing process to continue
            }
        });
//        if (linesCount.get(position) > 2) {
//            holder.readMoreButton.setVisibility(View.VISIBLE);
//        } else {
//            // holder.readMoreButton.setVisibility(View.GONE);
//        }

        // Handle "Read More / Read Less" button click
        holder.readMoreButton.setOnClickListener(v -> {
            // Toggle the current expanded state
            boolean currentState = expandedState.getOrDefault(position, false);
            expandedState.put(position, !currentState);

            // Update the UI for the toggled state
            if (!currentState) {
                // Expand the text
                holder.descriptionTV.setMaxLines(Integer.MAX_VALUE);
                holder.descriptionTV.setEllipsize(null);
                holder.readMoreButton.setText("Read Less");
            } else {
                // Collapse the text
                holder.descriptionTV.setMaxLines(2);
                holder.descriptionTV.setEllipsize(TextUtils.TruncateAt.END);
                holder.readMoreButton.setText("Read More");
            }


        });
//
//        holder.readMoreTextView.setCollapsedText("Read More");
//        holder.readMoreTextView.setCollapsedText("Read Less");
//        holder.readMoreTextView.setTrimLines(2);



    }

    @Override
    public int getItemCount() {
        return travelStories.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);

        // Detach the singleton player when the ViewHolder is recycled

        if (holder.playerView.getPlayer() != null) {
            Log.e("PostsAdapter", "player is released"+holder.playerView.getPlayer().toString());
            holder.playerView.getPlayer().stop();
            holder.playerView.setPlayer(null); // Detach PlayerView
        }
    }

    // Release resources when adapter is destroyed
    public void releasePlayer() {
        PlayerManager.releasePlayer();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, userNameTV, timeTV,descriptionTV, viewsCountTV, likesCountTV, commentsCountTV;
        ImageView userImageView,likeButton;
        ViewPager2 viewPagerPhotos;
        public PlayerView playerView; // Use PlayerView for ExoPlayer
        Button loadMoreButton;
        LinearLayout commentLinearLayout;
//        LinearLayout viewPagerLayout;
        private TabLayout tabLayout;
        TextView readMoreButton;
        FrameLayout mediaContainer;
        ImageView leftArrow, rightArrow;

        //ReadMoreTextView readMoreTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title_post_item);
            userNameTV = itemView.findViewById(R.id.user_name_post_item);
            timeTV = itemView.findViewById(R.id.time_post_item);
//            descriptionTV = itemView.findViewById(R.id.post_description_post_item);
            viewsCountTV = itemView.findViewById(R.id.view_count_post_item);
            likesCountTV = itemView.findViewById(R.id.like_count_post_item);
            commentsCountTV = itemView.findViewById(R.id.comment_count_post_item);
            loadMoreButton = itemView.findViewById(R.id.load_more_btn);

            likeButton = itemView.findViewById(R.id.like_button_post_item);
            playerView = itemView.findViewById(R.id.videoView_post_item); // Replace videoView
            viewPagerPhotos = itemView.findViewById(R.id.view_pager_photos_post_item);
            userImageView = itemView.findViewById(R.id.profile_image_post_item);
            commentLinearLayout = itemView.findViewById(R.id.comment_linearLayout_post_item);
           // viewPagerLayout = itemView.findViewById(R.id.view_pager_linearlayout);
            tabLayout = itemView.findViewById(R.id.tabLayout);
            //readMoreTextView = itemView.findViewById(R.id.tvReadMoreLess_post_item);
            descriptionTV = itemView.findViewById(R.id.post_description_post_item);
            mediaContainer = itemView.findViewById(R.id.media_container);
            leftArrow = itemView.findViewById(R.id.left_arrow);
            rightArrow = itemView.findViewById(R.id.right_arrow);

           readMoreButton = itemView.findViewById(R.id.read_more_button);
        }
    }

    public void addPost(TravelStory post) {
        this.travelStories.add(post);
        notifyItemInserted(travelStories.size() - 1);
    }

    public void addPosts(List<TravelStory> newPosts) {
        int initialSize = travelStories.size();
        this.travelStories.addAll(newPosts);
        notifyItemRangeInserted(initialSize, newPosts.size());
    }

    private void likePost(String postId) {
        String userId = FirebaseUtils.getCurrentUser().getUid();


        // Add to user's liked posts
        FirebaseUtils.getLikedPostsCollection(userId).document(postId).set(new HashMap<>())
                .addOnSuccessListener(aVoid -> {
                    // Add postId to local set
                    likedPostIds.add(postId);

                    // Increment the likes count in the post document
                    FirebaseUtils.getPostsCollection().document(postId)
                            .update("likes", FieldValue.increment(1))
                            .addOnSuccessListener(aVoid1 -> notifyDataSetChanged())
                            .addOnFailureListener(e -> Log.e("Firestore", "Failed to update likes count", e));
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to like post", e));
    }



    private void unlikePost(String postId) {
        String userId = FirebaseUtils.getCurrentUser().getUid();


        // Remove from user's liked posts
        FirebaseUtils.getLikedPostsCollection(userId).document(postId).delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove postId from local set
                    likedPostIds.remove(postId);

                    // Decrement the likes count in the post document
                    FirebaseUtils.getPostsCollection().document(postId)
                            .update("likes", FieldValue.increment(-1))
                            .addOnSuccessListener(aVoid1 -> notifyDataSetChanged())
                            .addOnFailureListener(e -> Log.e("Firestore", "Failed to update likes count", e));
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to unlike post", e));
    }






}
