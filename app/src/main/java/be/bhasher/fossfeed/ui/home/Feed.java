package be.bhasher.fossfeed.ui.home;

import java.io.Serializable;
import java.util.ArrayList;

public class Feed extends ArrayList<FeedItem> implements Serializable {
    public String title;
    public String link;
    public String description;
    public String lastBuild;
}
