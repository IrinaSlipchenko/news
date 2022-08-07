package home.irina.news.model;

import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.Date;

@Value
public class Post {
    String title;
    String desc;
    String link;
    Date date;
}
