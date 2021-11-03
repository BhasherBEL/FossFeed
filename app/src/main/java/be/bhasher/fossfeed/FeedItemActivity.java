package be.bhasher.fossfeed;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import be.bhasher.fossfeed.databinding.ActivityFeeditemBinding;
import be.bhasher.fossfeed.ui.home.FeedItem;

public class FeedItemActivity extends AppCompatActivity {
    private ActivityFeeditemBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFeeditemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            FeedItem feedItem = (FeedItem) extras.getSerializable("FeedItem");

            TextView textView = findViewById(R.id.FeedItemText);
            textView.setText(HtmlCompat.fromHtml(feedItem.description, 0));

            TextView titleView = findViewById(R.id.FeedItemTitle);
            titleView.setText(HtmlCompat.fromHtml(feedItem.title, 0));
        }
    }
}
