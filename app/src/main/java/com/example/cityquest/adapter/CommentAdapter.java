package com.example.cityquest.adapter;

import android.content.Context;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.ViewPostCommentsActivity;
import com.example.cityquest.model.Comment;
import com.example.cityquest.model.TripDay;
import com.example.cityquest.utils.AndroidUtils;
import com.google.firebase.Timestamp;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> items; // Unified list of comments and replies
    private List<Boolean> expandedStates; // Track expanded/collapsed states for each day
    private String postId;


    public CommentAdapter(Context context, List<Comment> comments, String postId) {
        this.context = context;
        this.items = comments;
        this.postId = postId;
        this.expandedStates = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            expandedStates.add(false); // By default, all items are collapsed
        }

    }



    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = items.get(position);

        holder.usernameTextView.setText(comment.getUsername());
        holder.contentTextView.setText(comment.getContent());
        holder.likeCountTextView.setText(String.valueOf(comment.getLikeCount()));
        holder.replyCountTextView.setText(String.format("%d replies", comment.getReplyCount()));



        ReplaysAdapter replaysAdapter = new ReplaysAdapter(context, comment.getReplies());

        holder.replyRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.replyRecyclerView.setAdapter(replaysAdapter);

        if(!items.isEmpty()){
            boolean isExpanded = expandedStates.get(position);
            holder.replyRecyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        }




        // Format timestamp
        String timeAgo = AndroidUtils.formatTimeAgo(comment.getTimePosted());
        holder.timeTextView.setText(timeAgo);

        // Load profile image
        Glide.with(context)
                .load(comment.getProfileImageUrl())
                .placeholder(R.drawable.user_placholer_icon)
                .into(holder.profileImageView);




        // Handle reply clicks
        holder.replyButton.setOnClickListener(v -> {
            toggleCommentExpansion(holder, position);

            if (context instanceof ViewPostCommentsActivity) {
                ((ViewPostCommentsActivity) context).showReplyInputDialog(items.get(position).getCommentId(),items.get(position).getUsername());
            }

        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImageView;
        private TextView usernameTextView;
        private TextView contentTextView;
        private TextView timeTextView;
        private TextView likeCountTextView;
        private TextView replyCountTextView;
        private TextView replyButton;
        private RecyclerView replyRecyclerView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image_comment_item);
            usernameTextView = itemView.findViewById(R.id.user_name_comment_item);
            contentTextView = itemView.findViewById(R.id.content_comment_item);
            timeTextView = itemView.findViewById(R.id.time_comment_item);
            likeCountTextView = itemView.findViewById(R.id.likes_count_comment_item);
            replyCountTextView = itemView.findViewById(R.id.replay_count_comment_item);
            replyButton = itemView.findViewById(R.id.replay_btn_comment_item);
            replyRecyclerView = itemView.findViewById(R.id.replays_recycler_view);
        }
    }

    private void toggleCommentExpansion(CommentAdapter.CommentViewHolder holder, int position) {
        boolean currentlyExpanded = expandedStates.get(position);

        // Toggle the expanded state
        expandedStates.set(position, !currentlyExpanded);

        // Animate the layout changes
        TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);

        // Notify the adapter to refresh the item
        notifyItemChanged(position);
    }
    public void updateComments(List<Comment> newComments) {

        Log.d("comments", "Before clear, items size: " + this.items.size());
        this.items.clear();
        Log.d("comments", "After clear, items size: " + this.items.size());


        this.items.addAll(newComments);




         //Reset the expanded states
        expandedStates.clear();
        for (int i = 0; i < newComments.size(); i++) {
            expandedStates.add(false);
        }


        // Notify the adapter that the data has changed
        notifyDataSetChanged();
        Log.d("Comments", "Adapter is notified...");


    }


}

