package com.example.cityquest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.colormoon.readmoretextview.ReadMoreTextView;
import com.example.cityquest.Database.AppDatabase;
import com.example.cityquest.adapter.CommentAdapter;
import com.example.cityquest.adapter.DaysDetailsAdapter;
import com.example.cityquest.adapter.PostImageSliderAdapter;
import com.example.cityquest.model.Comment;
import com.example.cityquest.model.TravelStory;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.model.User;
import com.example.cityquest.utils.AndroidUtils;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.utils.PlayerManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPostCommentsActivity extends AppCompatActivity {

    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private ImageButton sendCommentButton;

    private List<Comment> commentsList = new ArrayList<>();
    private CommentAdapter commentsAdapter;
    private TextView userNameTV,timePostedTV,titleTV,likeCountTV,commentCountTV,contentTV;
    //private ReadMoreTextView contentTV;
    private ImageView profileImageView;
    private String userName,profileImageUrl;
    private String postId;
    private String userNameToReply;
    private String currentReplyToCommentId = null; // To track if the user is replying to a comment
    View postIncludedLayout;
    private LinearLayout replyToLayout;
    private TextView replyToUserNameTV;
    private ImageButton closeReplyToButton;
    FrameLayout mediaContainer;
    LinearLayout viewPagerLayout;
    private TabLayout tabLayout;
    ViewPager2 viewPagerPhotos;
    public PlayerView playerView; // Use PlayerView for ExoPlayer




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_post_comments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentEditText = findViewById(R.id.comment_edit_text);
        sendCommentButton = findViewById(R.id.send_comment_button);
        postIncludedLayout  = findViewById(R.id.post_item_include);

        userNameTV = postIncludedLayout.findViewById(R.id.user_name_post_item);
        timePostedTV = postIncludedLayout.findViewById(R.id.time_post_item);
        titleTV = postIncludedLayout.findViewById(R.id.post_title_post_item);
        contentTV = postIncludedLayout.findViewById(R.id.post_description_post_item);
        likeCountTV = postIncludedLayout.findViewById(R.id.like_count_post_item);
        commentCountTV = postIncludedLayout.findViewById(R.id.comment_count_post_item);
        profileImageView = postIncludedLayout.findViewById(R.id.profile_image_post_item);
        replyToLayout = findViewById(R.id.reply_to_user_layout);
        replyToUserNameTV = findViewById(R.id.reply_to_user_name);
        closeReplyToButton = findViewById(R.id.close_reply_button);

        mediaContainer = postIncludedLayout.findViewById(R.id.media_container);
        viewPagerLayout = postIncludedLayout.findViewById(R.id.view_pager_linearlayout);
        playerView = postIncludedLayout.findViewById(R.id.videoView_post_item);
        tabLayout = postIncludedLayout.findViewById(R.id.tabLayout);
        viewPagerPhotos = postIncludedLayout.findViewById(R.id.view_pager_photos_post_item);


        AppDatabase.databaseWriteExecutor.execute(() -> {

            User user = AppDatabase.getDatabase(getApplicationContext()).userDao().getUser();
            runOnUiThread(() -> {
                // Update UI with the retrieved user
                if (user != null) {
                    userName = user.getUserName();
                    profileImageUrl = user.getProfilePictureUrl();
                    Log.e("comments",userName);

                }
            });
        });

        postId = getIntent().getStringExtra("postId");
        if(postId != null){
            fetchPostInfoFromFirestore(postId);
            fetchCommentsFromFirestore();
        }

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        closeReplyToButton.setOnClickListener(v -> {
            currentReplyToCommentId = null; // Reset the state
            replyToLayout.setVisibility(View.GONE);
        });














        // Handle send button click
        sendCommentButton.setOnClickListener(v -> {

            String commentText = commentEditText.getText().toString().trim();

            if (!commentText.isEmpty()) {
                if (currentReplyToCommentId != null) {
                    // User is replying to a comment
                    addReplyToComment(currentReplyToCommentId, commentText);
                } else {
                    // User is posting a new comment
                    addCommentTofirestore(commentText);
                }
            }






        });

    }

    private void addCommentTofirestore(String commentText){



        String commentId = FirebaseFirestore.getInstance()
                .collection("posts")
                .document(postId)
                .collection("comments")
                .document()
                .getId();

        Map<String, Object> commentMap = new HashMap<>();
        commentMap.put("commentId", commentId);
        commentMap.put("username", userName);
        commentMap.put("profileImageUrl", profileImageUrl);
        commentMap.put("content", commentText);
        commentMap.put("likeCount", 0);
        commentMap.put("replyCount", 0);
        commentMap.put("isReply", false);
        commentMap.put("timePosted", FieldValue.serverTimestamp());
        commentMap.put("replies", new ArrayList<>()); // Add an empty list for replies
        ; // Firestore server timestamp

        FirebaseFirestore.getInstance()
                .collection("posts")
                .document(postId)
                .collection("comments")
                .document(commentId)
                .set(commentMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Comment added successfully");
                    commentEditText.setText(""); // Clear input field
                    FirebaseUtils.getPostsCollection()
                            .document(postId)
                            .update("comments", FieldValue.increment(1)) // Increment by 1
                            .addOnSuccessListener(aVoid2 -> Log.d("Firestore", "Comment count incremented successfully"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Error incrementing comment count", e));

                    fetchCommentsFromFirestore();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding comment", e);
                });
    }



    private void fetchCommentsFromFirestore() {
        commentsList.clear(); // Clear the existing list

        FirebaseFirestore.getInstance()
                .collection("posts")
                .document(postId)
                .collection("comments")
                .orderBy("timePosted", Query.Direction.ASCENDING) // Order by time
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {




                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {


                        // Deserialize the main comment
                        Comment comment = snapshot.toObject(Comment.class);
                        Log.d("Firestore", "replies before " + comment.getReplies().size());



                        if (comment != null) {
                            comment.setCommentId(snapshot.getId()); // Set the ID from the document snapshot

                            // Manually map replies, since nested lists aren't automatically deserialized
                            List<Map<String, Object>> repliesData = (List<Map<String, Object>>) snapshot.get("replies");


                            if (repliesData != null) {
                                List<Comment> replies = new ArrayList<>();
                                for (Map<String, Object> replyData : repliesData) {
                                    Comment reply = new Comment(
                                            (String) replyData.get("username"),
                                            (String) replyData.get("content"),
                                            (String) replyData.get("profileImageUrl"),
                                            true // isReply is true for replies

                                    );







                                    replies.add(reply);

                                }

                                comment.setReplies(replies); // Add replies to the comment


                            }
                            commentsList.add(comment); // Add the main comment to the list



                        }

                    }




                    // Setup RecyclerView


                updateCommentsSection(new ArrayList<>(commentsList));
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching comments", e);
                });
        Log.d("Firestore", "after " + commentsList.size());


    }



    private void fetchPostInfoFromFirestore(String postId) {

        FirebaseUtils.getPostsCollection().document(postId)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            TravelStory post = document.toObject(TravelStory.class);
                            if (post != null) {

                                userNameTV.setText(post.getUserName());
                                timePostedTV.setText(AndroidUtils.formatTimeAgo(post.getDatePosted()));
                                titleTV.setText(post.getTitle());
                                contentTV.setText(post.getDescription());
                                likeCountTV.setText(String.valueOf(post.getLikes()));
                                commentCountTV.setText(String.valueOf(post.getComments()));
                                Glide.with(this)
                                        .load(post.getUserProfilePicture())
                                        .placeholder(R.drawable.user_placholer_icon)
                                        .into(profileImageView);

                                if (post.getVideoUrl() != null && !post.getVideoUrl().isEmpty()) {
                                    playerView.setVisibility(View.VISIBLE);
                                    viewPagerLayout.setVisibility(View.GONE);

                                    // Attach Player only; don't play yet
                                    ExoPlayer player = PlayerManager.getInstance(this);
                                    playerView.setPlayer(player);

                                    // Set media item but don't start playback here
                                    MediaItem mediaItem = MediaItem.fromUri(post.getVideoUrl());
                                    player.setMediaItem(mediaItem);
                                    player.prepare();
                                } else if (post.getPhotoUrls() != null && !post.getPhotoUrls().isEmpty()) {
                                    // Show the ViewPager and hide the PlayerView
                                    playerView.setVisibility(View.GONE);
                                    viewPagerLayout.setVisibility(View.VISIBLE);


                                    // Set up the ViewPager2 adapter for photos
                                    PostImageSliderAdapter photosAdapter = new PostImageSliderAdapter(this, post.getPhotoUrls());
                                    viewPagerPhotos.setAdapter(photosAdapter);

                                    if(post.getPhotoUrls().size()>1){

                                        new TabLayoutMediator(tabLayout,viewPagerPhotos, (tab, p) -> {
                                            // Some implementation (if needed, e.g., set tab text)
                                        }).attach();

                                        tabLayout.setVisibility(View.VISIBLE);
                                    }else {
                                        tabLayout.setVisibility(View.GONE);

                                        mediaContainer.post(() -> {
                                            // Get the current height after layout pass
                                            ViewGroup.LayoutParams layoutParams = mediaContainer.getLayoutParams();
                                            int currentHeight = mediaContainer.getHeight();

                                            // Reduce height by 50dp (convert dp to pixels)
                                            int reductionInPx = (int) (50 * mediaContainer.getContext().getResources().getDisplayMetrics().density);

                                            // Apply the new height
                                            layoutParams.height = currentHeight - reductionInPx;
                                            mediaContainer.setLayoutParams(layoutParams);
                                        });
                                    }

                                } else {
                                    // Hide both PlayerView and ViewPager if no media is available
                                    playerView.setVisibility(View.GONE);
                                    viewPagerLayout.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            // Handle the case where the document doesn't exist
                        }
                    } else {
                        // Handle the failure
                    }
                });

    }

//    private void fillCommentList() {
//
//    for(int i = 0; i < 10; i++){
//        Comment newComment = new Comment(i+"","Ghassan Salmen", new Timestamp(new Date()), "sfsdfsdfs d;sd;flsddlkfj sdlkjflksd",0,0,null,profileImageUrl,false,false);
//        commentsList.add(newComment);
//
//
//
//        }
//
//    }
    public void showReplyInputDialog(String commentId,String userName) {
        currentReplyToCommentId = commentId;
        userNameToReply = userName;
        commentEditText.requestFocus();
        replyToUserNameTV.setText(userName);
        replyToLayout.setVisibility(View.VISIBLE);



    }
    private void addReplyToComment(String commentId, String replyText) {

        if (replyText.isEmpty()) {
            Log.e("ReplyError", "Reply text is empty!");
            return;
        }


        Map<String, Object> replayMap = new HashMap<>();


        replayMap.put("username", userName);
        replayMap.put("profileImageUrl", profileImageUrl);
        replayMap.put("content", replyText);
        replayMap.put("isReply", true);
//        replayMap.put("timePosted", FieldValue.serverTimestamp());


        FirebaseFirestore.getInstance()
                .collection("posts")
                .document(postId)
                .collection("comments")
                .document(commentId)
                .update(
                        "replies", FieldValue.arrayUnion(replayMap),
                        "replyCount", FieldValue.increment(1)
                )
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Reply added successfully");
                    commentEditText.setText(""); // Clear input field
                    currentReplyToCommentId = null; // Reset the state
                    replyToLayout.setVisibility(View.GONE);
                    fetchCommentsFromFirestore(); // Refresh the comments list
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding reply", e);
                });

        for (Comment comment : commentsList) {
            if (comment.getCommentId().equals(commentId)) {
                Comment newReply = new Comment(
                        userName,
                        replyText,
                        profileImageUrl,
                        true
                );
                comment.getReplies().add(newReply);
                comment.setReplyCount(comment.getReplyCount() + 1);
                commentsAdapter.notifyItemChanged(commentsList.indexOf(comment));
                break;
            }
        }
    }

    private void updateCommentsSection(List<Comment> comments) {
        Log.e("comments", "the comments in update section are: "+comments.size());
        if (commentsAdapter == null) {
            commentsAdapter = new CommentAdapter(this, comments,postId);
            commentsRecyclerView.setAdapter(commentsAdapter);
            Log.e("comments", "adapter created");
        } else {
            Log.e("comments", "adapter  already created with comments size :"+comments.size());
            commentsAdapter.updateComments(comments); // If necessary, update the adapter with new data

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        PlayerManager.releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayerManager.releasePlayer();
    }




}