package ru.fisenko.news.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.fisenko.news.models.News;
import ru.fisenko.news.repositories.NewsRepository;

import java.util.Calendar;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class NewsService {

    private NewsRepository newsRepository;
    private ImageStorageService imageStorageService;

    @Autowired
    public NewsService(NewsRepository newsRepository, ImageStorageService imageStorageService) {
        this.newsRepository = newsRepository;
        this.imageStorageService = imageStorageService;
    }

    //Retrieve a paginated list of news
    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    //Enriches and saves a news
    @Transactional
    public void saveNews(News news, MultipartFile image) {
        news.setData(Calendar.getInstance().getTime());
        if(!image.isEmpty()) news.setImage_path(saveImage(news, image));
        newsRepository.save(news);
    }

    //Generates an image name and saves the associated image file
    private String saveImage(News news, MultipartFile image) {
        SimpleDateFormat formater = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss_SS");
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String image_name = news.getHeader()
                + formater.format(news.getData())
                + "." + extension;
        imageStorageService.saveImage(image, image_name);
        return image_name;
    }

}
