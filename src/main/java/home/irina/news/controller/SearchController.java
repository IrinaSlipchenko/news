package home.irina.news.controller;

import home.irina.news.model.Post;
import home.irina.news.model.SearchParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import home.irina.news.service.NewsFilter;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://sergey-oreshkin.github.io/")
public class SearchController {

    private final NewsFilter newsFilter;

    @PostMapping("/")
    public List<Post> search(@RequestBody SearchParam searchParam){
        return newsFilter.searchByParam(searchParam.getHours(), searchParam.getKeywords());
    }
}
