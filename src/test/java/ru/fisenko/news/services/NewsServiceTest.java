package ru.fisenko.news.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import ru.fisenko.news.models.News;
import ru.fisenko.news.repositories.NewsRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private NewsService newsService;

    @Test
    void findAll_ShouldReturnPageOfNews() {
        List<News> newsList = new ArrayList<>();
        newsList.add(new News());
        newsList.add(new News());
        Pageable pageable = Pageable.unpaged();
        Page<News> newsPage = new PageImpl<>(newsList);
        when(newsRepository.findAll(pageable)).thenReturn(newsPage);

        Page<News> result = newsService.findAll(pageable);

        assertEquals(newsPage, result);
        verify(newsRepository, times(1)).findAll(pageable);
    }

    @Test
    void saveNews_WithValidData_ShouldSaveNewsAndImage() {
        News news = new News();
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "some image".getBytes());

        newsService.saveNews(news, image);

        assertNotNull(news.getData());
        assertNotNull(news.getImage_path());
        verify(newsRepository, times(1)).save(news);
        verify(imageStorageService, times(1)).saveImage(image, news.getImage_path());
    }

    @Test
    void saveNews_withEmptyImage_saveNewsWithoutImage() {
        News news = new News();
        MockMultipartFile emptyImage = new MockMultipartFile("emptyImage", new byte[0]);

        newsService.saveNews(news, emptyImage);

        assertNotNull(news.getData());
        assertNull(news.getImage_path());
        verify(newsRepository, times(1)).save(news);
        verify(imageStorageService, never()).saveImage(any(), any());
    }
}