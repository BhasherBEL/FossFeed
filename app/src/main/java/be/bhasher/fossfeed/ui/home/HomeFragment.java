package be.bhasher.fossfeed.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.utils.cache.AppDatabase;
import be.bhasher.fossfeed.utils.cache.FeedsDAO;

public class HomeFragment extends Fragment{

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        new RssParser(view).execute();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshFeeds);
        swipeRefreshLayout.setOnRefreshListener(() -> new RssParser(view, swipeRefreshLayout).execute());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}