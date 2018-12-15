package politech.budget.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import politech.budget.dto.Operation;

import java.util.Date;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface OperationsRepository extends JpaRepository<Operation, Integer> {

    @Query("SELECT o FROM Operation o WHERE o.userId = :userId")
    List<Operation> findOperationsByUserId(@Param("userId") Integer userId);

    @Query("SELECT o FROM Operation o WHERE o.userId = :userId AND o.articleId = :articleId")
    List<Operation> findOperationsByUserIdAndArticleId(@Param("userId") Integer userId,
                                                       @Param("articleId") Integer articleId);

    @Query("SELECT o FROM Operation o WHERE o.userId = :userId AND o.createDate BETWEEN :startTime AND :endTime")
    List<Operation> findOperationsByUserIdAndCreateDate(@Param("userId") Integer userId,
                                                        @Param("startTime") Date startTime,
                                                        @Param("endTime") Date endTime);

    @Query("SELECT o FROM Operation o WHERE o.userId = :userId AND o.articleId = :articleId AND o.createDate BETWEEN :startTime AND :endTime")
    List<Operation> findOperationsByUserIdAAndArticleIdAndCreateDate(@Param("userId") Integer userId,
                                                                     @Param("articleId") Integer articleId,
                                                                     @Param("startTime") Date startTime,
                                                                     @Param("endTime") Date endTime);
}
