package com.example.cityquest;

import android.os.Bundle;


import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cityquest.adapter.PostsAdapter;
import com.example.cityquest.model.TravelStory;
import com.example.cityquest.utils.FirebaseUtils;
import com.example.cityquest.utils.PlayerManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommunityActivity extends Fragment {

    private RecyclerView postsRecyclerView;

    private LottieAnimationView loadingAnim;


    private PostsAdapter postsAdapter;
    private List<TravelStory> travelStories;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int lastVisibleItemPosition = RecyclerView.NO_POSITION; // Keeps track of the last visible item

    // Variables for pagination
    private DocumentSnapshot lastVisiblePost; // Keeps track of the last document

    private boolean isLoading = false; // Flag to avoid multiple clicks
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the fragment layout
        LayoutInflater themedInflater = inflater.cloneInContext(new ContextThemeWrapper(getActivity(), R.style.TripsPage));
        View view = themedInflater.inflate(R.layout.activity_community, container, false);
        // Inflate the layout for this fragment

        loadingAnim = view.findViewById(R.id.loading_anim_community_page);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_community);
        swipeRefreshLayout.setColorSchemeResources(R.color.secondary_color, R.color.background_color);
        // Initialize RecyclerView
        postsRecyclerView = view.findViewById(R.id.community_posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        // Set up the menu item click listeners



        travelStories = new ArrayList<>();
        // Load mock data for posts
        //uploadTravelStories();

        loadPosts(true);

        // Initialize and set adapter
        postsAdapter = new PostsAdapter(getContext(), travelStories);
        postsRecyclerView.setAdapter(postsAdapter);
        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Simulate data refresh
            loadPosts(false);
        });

        // Set the "Load More" button click listener
        postsAdapter.setLoadMoreClickListener(v -> {
            if (!isLoading) {
                isLoading = true;
                loadPosts(false); // Your method to load more posts
            }
        });
        postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int currentlyPlayingPosition = RecyclerView.NO_POSITION;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // When scrolling stops
                    Log.e("PostsAdapter", "player Position " + currentlyPlayingPosition);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        // Find the first visible item
                        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                        // Ensure valid position
                        if (firstVisibleItem != RecyclerView.NO_POSITION) {
                            // Check if the item to play has changed
                            if (currentlyPlayingPosition != firstVisibleItem) {
                                // Stop the previously playing video
                                if (currentlyPlayingPosition != RecyclerView.NO_POSITION) {
                                    Log.e("PostsAdapter", "STOPPED for item " + currentlyPlayingPosition);
                                    stopVideoForPosition(currentlyPlayingPosition);
                                }

                                // Play video for the new first visible item
                                TravelStory story = travelStories.get(firstVisibleItem);
                                if (story.getVideoUrl() != null) {
                                    ExoPlayer player = PlayerManager.getInstance(getContext());
                                    MediaItem mediaItem = MediaItem.fromUri(story.getVideoUrl());
                                    player.setMediaItem(mediaItem);
                                    player.prepare();
                                    player.play();
                                    Log.e("PostsAdapter", "the video is played for item " + firstVisibleItem);

                                    // Update the currently playing position
                                    currentlyPlayingPosition = firstVisibleItem;
                                }
                            }
                        }
                    }
                }
            }
            private void stopVideoForPosition(int position) {
                // Find the ViewHolder for the given position
                RecyclerView.ViewHolder viewHolder = postsRecyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder instanceof PostsAdapter.ViewHolder) {
                    PostsAdapter.ViewHolder holder = (PostsAdapter.ViewHolder) viewHolder;
                    if (holder.playerView.getPlayer() != null) {
                        Log.e("PostsAdapter", "player is released for item " + position);
                        holder.playerView.getPlayer().stop(); // Stop the video
                        holder.playerView.setPlayer(null);    // Detach player
                    }
                }
            }
        });



        return view;
    }


//    private void uploadTravelStories() {
//        travelStories = Arrays.asList(
//                // Photo-based posts
//                new TravelStory(
//                        "John Doe",
//                        "2023-12-01",
//                        "Goa Beach Adventure",
//                        "Exploring the serene beaches of Goa.",
//                        Arrays.asList("https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg"),
//                        null, // No video URL provided
//                        "Goa, India",
//                        1200, // Views
//                        350, // Likes
//                        45,  // Comments
//                        "https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "Alice Smith",
//                        "2023-11-28",
//                        "Sunset at Palolem Beach",
//                        "A breathtaking sunset view at Palolem Beach.",
//                        Arrays.asList("https://images.pexels.com/photos/460672/pexels-photo-460672.jpeg"),
//                        null, // No video URL provided
//                        "Goa, India",
//                        900, // Views
//                        250, // Likes
//                        30,  // Comments
//                        "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "David Brown",
//                        "2023-11-15",
//                        "Historical Fort Aguada",
//                        "Exploring the ancient Fort Aguada.",
//                        Arrays.asList("https://images.pexels.com/photos/618079/pexels-photo-618079.jpeg"),
//                        null, // No video URL provided
//                        "Goa, India",
//                        800, // Views
//                        200, // Likes
//                        25,  // Comments
//                        "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "Emily Davis",
//                        "2023-11-10",
//                        "Nightlife in Goa",
//                        "Experiencing the vibrant nightlife.",
//                        Arrays.asList("https://images.pexels.com/photos/5062343/pexels-photo-5062343.jpeg"),
//                        null, // No video URL provided
//                        "Goa, India",
//                        1000, // Views
//                        300, // Likes
//                        35,  // Comments
//                        "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "Michael Lee",
//                        "2023-11-05",
//                        "Goan Cuisine",
//                        "Tasting the delicious seafood in Goa.",
//                        Arrays.asList("https://images.pexels.com/photos/4676439/pexels-photo-4676439.jpeg"),
//                        null, // No video URL provided
//                        "Goa, India",
//                        750, // Views
//                        220, // Likes
//                        20,  // Comments
//                        "https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg" // User profile picture
//                ),
//
//                // Video-based posts
//                new TravelStory(
//                        "Sophia Wilson",
//                        "2023-11-01",
//                        "Watersports Adventure",
//                        "Thrilling water sports activities in Goa.",
//                        null, // No photo URL provided
//                        "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                        "Goa, India",
//                        950, // Views
//                        280, // Likes
//                        40,  // Comments
//                        "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "Chris Taylor",
//                        "2023-10-28",
//                        "Parasailing Experience",
//                        "Soaring through the skies of Goa.",
//                        null, // No photo URL provided
//                        "https://samplelib.com/lib/preview/mp4/sample-5s.mp4",
//                        "Goa, India",
//                        1100, // Views
//                        400, // Likes
//                        50,  // Comments
//                        "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "Laura Green",
//                        "2023-10-20",
//                        "Scuba Diving Adventure",
//                        "Exploring the underwater world.",
//                        null, // No photo URL provided
//                        "https://www.appsloveworld.com/wp-content/uploads/2018/10/640.mp4",
//                        "Goa, India",
//                        1300, // Views
//                        500, // Likes
//                        60,  // Comments
//                        "https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "James White",
//                        "2023-10-15",
//                        "Goa's Carnival Festivities",
//                        "Enjoying the vibrant carnival celebrations.",
//                        null, // No photo URL provided
//                        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
//                        "Goa, India",
//                        1500, // Views
//                        550, // Likes
//                        70,  // Comments
//                        "https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg" // User profile picture
//                ),
//                new TravelStory(
//                        "Megan Brown",
//                        "2023-10-10",
//                        "Jungle Trekking",
//                        "Venturing into Goa's lush jungles.",
//                        null, // No photo URL provided
//                        "https://www.w3schools.com/html/mov_bbb.mp4",
//                        "Goa, India",
//                        1400, // Views
//                        530, // Likes
//                        65,  // Comments
//                        "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg" // User profile picture
//                )
//        );
    //}


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer(); // Release the singleton ExoPlayer when the fragment is paused
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer(); // Additional safety to ensure the player is released when the view is destroyed
    }

    private void releasePlayer() {
        if (postsAdapter != null) {
            postsAdapter.releasePlayer(); // Call the adapter's releasePlayer method
        }
    }

    // Method to load posts


    // Method to load posts
    private void loadPosts(boolean isInitialLoad) {
        postsRecyclerView.setVisibility(View.GONE);
        if (!swipeRefreshLayout.isRefreshing()) {
            loadingAnim.setVisibility(View.VISIBLE);
        }
        Query query = FirebaseUtils.getPostsCollection()
                .orderBy("datePosted", Query.Direction.DESCENDING)
                .limit(10);

        // If not the first load, start after the last visible post
        if (!isInitialLoad && lastVisiblePost != null) {
            query = query.startAfter(lastVisiblePost);
        }

        isLoading = true; // Mark loading as true

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                if (!documents.isEmpty()) {
                    // Update the last visible post
                    lastVisiblePost = documents.get(documents.size() - 1);

                    for (DocumentSnapshot document : documents) {
                        TravelStory post = document.toObject(TravelStory.class);
                        // Add the post to the adapter's data list

                        postsAdapter.addPost(post); // You should define this method in your adapter
                    }
                } else {
                    Toast.makeText(getContext(), "No more posts to load", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Firestore", "Error fetching posts", task.getException());
            }

            isLoading = false; // Mark loading as false
            postsRecyclerView.setVisibility(View.VISIBLE);
            loadingAnim.setVisibility(View.GONE);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void fetchLikedPosts() {

        String userId = FirebaseUtils.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUtils.getLikedPostsCollection(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Set<String> likedPostIds = new HashSet<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            likedPostIds.add(document.getId());
                        }
                        postsAdapter.updateLikedPosts(likedPostIds);
                    } else {
                        Log.w("Firestore", "Error getting saved trips.", task.getException());
                    }
                });

    }




}
