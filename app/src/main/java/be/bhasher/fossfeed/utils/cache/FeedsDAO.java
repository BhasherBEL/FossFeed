package be.bhasher.fossfeed.utils.cache;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;

import be.bhasher.fossfeed.ui.home.Feed;

@Dao
public interface FeedsDAO {
    @Query("SELECT * FROM Feeds")
    ArrayList<Feed> g
}
