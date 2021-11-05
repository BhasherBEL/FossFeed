package be.bhasher.fossfeed.utils.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import be.bhasher.fossfeed.ui.home.FeedItem;

@Dao
public interface ItemsDAO {
    @Query("SELECT * FROM FEEDITEMS")
    List<FeedItem> getAll();

    @Query("SELECT * FROM FEEDITEMS ORDER BY timestamp DESC LIMIT :i")
    List<FeedItem> getAllByTime(int i);

    @Query("SELECT COUNT(*)>0 FROM FEEDITEMS/*,FEEDCHANNELS*/ WHERE timestamp=:timestamp AND FEEDITEMS.title=:title/* AND FEEDCHANNELS.title=:channelTitle*/")
    boolean has(long timestamp, String title/*, String channelTitle*/);

    @Insert
    void insert(FeedItem feeditem);

    @Update
    void update(FeedItem feeditem);

    @Delete
    void delete(FeedItem feeditem);
}