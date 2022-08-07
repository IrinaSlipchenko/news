package home.irina.news.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class Post implements Comparable<Post>{
    String title;
    String desc;
    String link;
    Date date;

    @Override
    public int compareTo(Post o) {
        return o.getDate().compareTo(this.getDate());
    }
}
