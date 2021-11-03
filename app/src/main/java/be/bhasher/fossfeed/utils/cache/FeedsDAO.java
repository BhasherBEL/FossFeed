package be.bhasher.fossfeed.utils.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import be.bhasher.fossfeed.ui.home.Feed;

@Dao
public interface FeedsDAO {
    @Query("SELECT * FROM FEED")
    List<Feed> getAll();

    @Insert
    void insert(Feed feed);

    @Update
    void update(Feed feed);

    @Delete
    void delete(Feed feed);
}
