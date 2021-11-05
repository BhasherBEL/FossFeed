package be.bhasher.fossfeed.utils.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.ui.home.Feed;
import be.bhasher.fossfeed.ui.home.FeedItem;

@Database(entities = {Feed.class, FeedItem.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedsDAO feedsDAO();
    public abstract ItemsDAO itemsDAO();

    private static final AppDatabase db = Room.databaseBuilder(MainActivity.context, AppDatabase.class, "fossfeed_db").build();
    public static final FeedsDAO feedsDAO = db.feedsDAO();
    public static final ItemsDAO itemsDAO = db.itemsDAO();
}
