package ru.fisenko.news.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.fisenko.news.models.News;
import ru.fisenko.news.services.NewsService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class NewsController {
    @Value("${upload.path}")
    private String uploadPath;
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Endpoint to serve image files by filename
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        try {
            Resource file = new UrlResource(filePath.toUri());

            if (file.exists() && file.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getFilename())
                        .body(file);
            } else {
                logger.error("Image not found: " + filename);
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            logger.error("Error to load image", e);
            return ResponseEntity.status(500).build();
        }
    }

    // Endpoint to display a paginated list of news
    @GetMapping("/news")
    public String news(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<News> newsPage = newsService.findAll(pageable);
        model.addAttribute("newsPage", newsPage);
        return "news";
    }

    // Endpoint to display the form for creating news
    @GetMapping("/createNews")
    public String createNews(Model model) {
        model.addAttribute("news", new News());
        return "createNews";
    }

    // Endpoint to handle the form submission for adding news
    @PostMapping("/news")
    public String addNews(@ModelAttribute("news") @Valid News news,
                          BindingResult bindingResult,
                          @RequestParam("image") MultipartFile image,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "createNews";
        try {
            newsService.saveNews(news, image);
            return "redirect:/news";
        } catch (Exception e) {
            logger.error("An error occurred while adding news", e);
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while adding news.");
            return "redirect:/createNews";
        }

    }
}
