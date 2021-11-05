package be.bhasher.fossfeed.ui.home;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;


@Entity(tableName = "feedchannels")
public class FeedChannel extends ArrayList<FeedItem>{
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "channel_id") public int id;
    @ColumnInfo(name = "channel_title") public String title;
    @ColumnInfo(name = "channel_link") public String link;
    @ColumnInfo(name = "channel_description") public String description;
    @ColumnInfo(name = "channel_lastbuild") public String lastBuild;
}
