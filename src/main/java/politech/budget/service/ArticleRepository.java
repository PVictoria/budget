package politech.budget.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import politech.budget.dto.Article;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query("SELECT a FROM Article a")
    List<Article> getAll();

    @Query("DELETE FROM Article a WHERE a.name = :name")
    @Modifying
    void deleteArticleByName(@Param("name") String name);

    @Query("SELECT a FROM Article a WHERE a.name = :name")
    Article findArticleByName(@Param("name") String name);

}
