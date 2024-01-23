package ru.fisenko.news.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fisenko.news.services.NewsService;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    @MockBean
    private NewsService newsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNews_ShouldReturnCreateNewsPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/createNews"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("createNews"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("news"))
                .andDo(print());
    }

    @Test
    void addNews_ShouldRedirectToNewsPage_WhenNewsAddedSuccessfully() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "some image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/news")
                        .file(image)
                        .param("header", "Test Header")
                        .param("content", "Test Content"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/news"))
                .andDo(print());

        verify(newsService, times(1)).saveNews(any(), any());
    }

    @Test
    void addNews_ShouldRedirectToCreateNewsPage_WhenErrorOccurs() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "some image".getBytes());

        // Mocking an error in newsService.saveNews
        doThrow(new RuntimeException("Simulated error")).when(newsService).saveNews(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/news")
                        .file(image)
                        .param("header", "Test Header")
                        .param("content", "Test Content"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/createNews"))
                .andDo(print());

        verify(newsService, times(1)).saveNews(any(), any());
    }
}
