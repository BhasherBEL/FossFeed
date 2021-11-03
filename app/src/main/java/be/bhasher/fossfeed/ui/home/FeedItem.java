package be.bhasher.fossfeed.ui.home;

public class FeedItem {
    public final String title;
    public final String subtitle;
    public final String link;
    public final String imageUrl;

    public FeedItem(String title, String subtitle, String link, String imageUrl){
        this.title = title;
        this.subtitle = subtitle;
        this.link = link;
        this.imageUrl = imageUrl;
    }
}
