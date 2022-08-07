package home.irina.news.model;

import lombok.Value;

import java.util.List;

@Value
public class SearchParam {
    int hours;
    List<String> keywords;
}
