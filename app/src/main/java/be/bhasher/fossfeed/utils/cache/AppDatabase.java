package be.bhasher.fossfeed.utils.cache;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.ui.home.Feed;

@Database(entities = {Feed.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedsDAO feedsDAO();

    private static final AppDatabase db = Room.databaseBuilder(MainActivity.context, AppDatabase.class, "fossfeed_db").build();
    private static final FeedsDAO feedsDAO = db.feedsDAO();

    public static FeedsDAO getInstance(){
       return feedsDAO;
    }
}
