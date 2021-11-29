package be.bhasher.fossfeed.ui.home;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeController());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if(swipeBack){
                    System.out.println(event.getX());
                    if(event.getX() <= 1){
                        System.out.println("SUCCESS");
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerFeeds);
        FeedAdapter adapter = (FeedAdapter) recyclerView.getAdapter();
        if(adapter != null && FeedItem.LAST_INDEX >= 0) adapter.notifyAsRead(FeedItem.LAST_INDEX);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

class SwipeController extends Callback {

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
}