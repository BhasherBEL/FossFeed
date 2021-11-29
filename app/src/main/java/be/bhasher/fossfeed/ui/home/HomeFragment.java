package be.bhasher.fossfeed.ui.home;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.Objects;

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.ui.home.db.FeedDB;

public class HomeFragment extends Fragment{

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        new FeedDB.UpdateAllFeedItems().execute();
        new FeedManager.UpdateDisplayedFeedItems(view, null).execute();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshFeeds);
        swipeRefreshLayout.setOnRefreshListener(() -> new FeedManager.UpdateDisplayedFeedItems(view, swipeRefreshLayout).execute());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerFeeds);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                FeedAdapter.ViewHolderFeed feed = (FeedAdapter.ViewHolderFeed) viewHolder;
                FeedAdapter adapter = (FeedAdapter) recyclerView.getAdapter();
                if(adapter != null) adapter.markAsRead(feed.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //ImageView imageView = requireView().findViewById(R.id.toolbarIcon);

        /*imageView.setOnClickListener(v -> {
            FeedManager.hideRead = !FeedManager.hideRead;
            //new FeedManager.UpdateDisplayedFeedItems(view, swipeRefreshLayout).execute();
        });*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerFeeds);
        FeedAdapter adapter = (FeedAdapter) recyclerView.getAdapter();
        if(adapter != null && FeedItem.LAST_INDEX >= 0) adapter.notifyAsRead(FeedItem.LAST_INDEX);

        ((MainActivity) requireActivity()).setSubActionBarIcon(R.drawable.eye);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}