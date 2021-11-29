package be.bhasher.fossfeed;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.squareup.picasso.Picasso;

import be.bhasher.fossfeed.databinding.ActivityFeeditemBinding;
import be.bhasher.fossfeed.ui.home.FeedItem;
import be.bhasher.fossfeed.ui.home.OnSwipeListener;
import be.bhasher.fossfeed.utils.DownloadImage;
import be.bhasher.fossfeed.utils.DownloadImageView;

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

            TextView titleView = findViewById(R.id.FeedItemTitle);
            titleView.setText(HtmlCompat.fromHtml(feedItem.title, 0));

            TextView textView = findViewById(R.id.FeedItemText);

            ImageGetter imageGetter = new ImageGetter(getResources(), textView);

            textView.setText(HtmlCompat.fromHtml(feedItem.description, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null));

            ImageView imageView = findViewById(R.id.FeedItemImage);

            if(feedItem.imageUrl != null) Picasso.get().load(feedItem.imageUrl).into(imageView);;

            TextView subtitleView = findViewById(R.id.FeedItemSubtitle);
            subtitleView.setText(feedItem.getSubtitle());

            Button backButton = findViewById(R.id.feeditem_backbutton);
            backButton.setOnClickListener(v -> onBackPressed());

            Button openButton = findViewById(R.id.feeditem_openbutton);
            openButton.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feedItem.link))));

            feedItem.markAsRead();
        }
    }
}

class DownloadImageInHtml extends DownloadImage {
    private final ImageGetter.BitmapDrawablePlaceHolder holder;
    private final Resources resources;
    private final TextView textView;

    public DownloadImageInHtml(ImageGetter.BitmapDrawablePlaceHolder holder, Resources resources, TextView textView){
        this.holder = holder;
        this.resources = resources;
        this.textView = textView;
    }

    @Override
    public void onPostExecute(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(resources, bitmap);

        float ratio = ((float) drawable.getIntrinsicWidth()) / ((float) drawable.getIntrinsicHeight());

        drawable.setBounds(0, 0, textView.getWidth(), (int) (textView.getWidth() / ratio));
        holder.setDrawable(drawable);
        holder.setBounds(00, 0, textView.getWidth(), (int) (textView.getWidth() / ratio));
        textView.refreshDrawableState();
        textView.setText(textView.getText());
    }
}

class ImageGetter  implements Html.ImageGetter{
    Resources resources;
    TextView textView;

    public ImageGetter(Resources resources, TextView textView){
        this.resources = resources;
        this.textView = textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        BitmapDrawablePlaceHolder holder = new BitmapDrawablePlaceHolder();
        new DownloadImageInHtml(holder, resources, textView).execute(source);
        return holder;
    }

    static class BitmapDrawablePlaceHolder extends BitmapDrawable{
        private Drawable drawable = null;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if(drawable != null) drawable.draw(canvas);
        }

        public void setDrawable(Drawable drawable){
            this.drawable = drawable;
        }
    }
}