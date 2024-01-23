package ru.fisenko.news.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisenko.news.models.News;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
    Page<News> findAll(Pageable pageable);
}
