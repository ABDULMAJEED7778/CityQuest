package com.example.cityquest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.Comment;
import com.example.cityquest.utils.AndroidUtils;

import java.util.List;

public class ReplaysAdapter extends RecyclerView.Adapter<ReplaysAdapter.ReplyViewHolder> {

    private Context context;
    private List<Comment> replies; // List of replies for a comment

    public ReplaysAdapter(Context context, List<Comment> replies) {
        this.context = context;
        this.replies = replies;
        Log.d("Firestore", "replise on instialized adapter "+ replies.size());
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_comment_item, parent, false);
        return new ReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Comment reply = replies.get(position);

        Log.d("Firestore", "replise on instialized adapter "+ replies.get(0).getContent());
        holder.usernameTextView.setText(reply.getUsername());
        holder.contentTextView.setText(reply.getContent());


        // Format timestamp
//        String timeAgo = AndroidUtils.formatTimeAgo(reply.getTimePosted());
       holder.timeTextView.setText("");

        // Load profile image
        Glide.with(context)
                .load(reply.getProfileImageUrl())
                .placeholder(R.drawable.user_placholer_icon)
                .into(holder.profileImageView);

        // Hide reply button and reply count for replies
        holder.likeReplayLayout.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImageView;
        private TextView usernameTextView;
        private TextView contentTextView;
        private TextView timeTextView;



        private LinearLayout likeReplayLayout;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image_comment_item);
            usernameTextView = itemView.findViewById(R.id.user_name_comment_item);
            contentTextView = itemView.findViewById(R.id.content_comment_item);
            timeTextView = itemView.findViewById(R.id.time_comment_item);

            likeReplayLayout = itemView.findViewById(R.id.replay_like_layout_comment_item);
        }
    }
}

