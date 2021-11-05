package be.bhasher.fossfeed.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.ui.home.db.FeedDB;
import be.bhasher.fossfeed.ui.home.db.ItemsDB;

public class HomeFragment extends Fragment{

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //new RssParser(view).execute();
        new FeedDB.UpdateAllFeedItems().execute();
        new FeedManager.UpdateDisplayedFeedItems(view, null).execute();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshFeeds);
        swipeRefreshLayout.setOnRefreshListener(() -> new FeedManager.UpdateDisplayedFeedItems(view, swipeRefreshLayout).execute());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}