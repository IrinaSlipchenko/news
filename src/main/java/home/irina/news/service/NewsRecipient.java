package home.irina.news.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import home.irina.news.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NewsRecipient {

    private List<Post> posts = new ArrayList<>();
    Map<String, String> sources = getSources(); //пролучаем адреса rss из файла с ресурсами

    private SyndFeed readFeed(String url) {
        SyndFeedInput input = new SyndFeedInput();
        try {
            return input.build(new XmlReader(new URL(url)));
        } catch (IOException | FeedException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Считывает из yaml файла адреса rss и возвращает их как значения Map
     */
 //   @PostConstruct  ????????
    private Map<String, String> getSources() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(
                            NewsRecipient.class.getResource("/sources.yml"),
                            SourceList.class)
                    .getSources();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedDelay = 60000)
    private void getPosts() {
        List<Post> resultAllPosts = new ArrayList<>();

        List<SyndFeed> feedList = sources.values()
                .stream()
                .map(this::readFeed)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (SyndFeed feed : feedList) {  // пройти по всем лентам новостей
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                resultAllPosts.add(
                        Post.builder()
                                .title(entry.getTitle())
                                .desc(entry.getDescription() != null
                                        ?
                                        entry.getDescription().getValue()
                                        :
                                        "")
                                .link(entry.getLink())
                                .date(entry.getPublishedDate())
                                .build());
            }
        }
        this.posts = resultAllPosts;
    }

    public List<Post> getAllPosts() {
        return posts;
    }

    /**
     * Вспомогательный класс для десериализации из yaml файла
     */
    static class SourceList {
        Map<String, String> sources;

        public Map<String, String> getSources() {
            return sources;
        }
    }
}
