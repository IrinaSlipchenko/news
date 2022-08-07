package home.irina.news.service;


import home.irina.news.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsFilter {

    long millisecondsInOneHour = 3_600_000L;

    private final NewsRecipient newsRecipient;


    public List<Post> searchByParam(int hours, List<String> keywordsOriginal) {
        List<String> keywords = keywordsOriginal
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        Date limitDate = Date.from(new Date().toInstant().minusMillis(hours * millisecondsInOneHour));

        List<Post> allPosts = newsRecipient.getAllPosts();

         return allPosts
                .stream()
                .filter(post -> post.getDate().after(limitDate))
                .filter(post -> isKeywordsInPost(post.getTitle(), post.getDesc(), keywords))
                .sorted()
                .collect(Collectors.toList());
    }

    private boolean isKeywordsInPost(String titleOriginal, String descOriginal, List<String> keywords) {
        String title = titleOriginal.trim().toLowerCase();
        String desc = descOriginal.trim().toLowerCase();
        for (String word : keywords) {
            if (title.contains(word) || desc.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
