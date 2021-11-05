package be.bhasher.fossfeed.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import be.bhasher.fossfeed.utils.DownloadImage;

public class DownloadImageView extends DownloadImage {
    private final ImageView imageView;

    public DownloadImageView(ImageView imageView){
        super();
        this.imageView = imageView;
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
