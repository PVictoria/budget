package politech.budget.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import politech.budget.dto.Balance;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {

    @Query("SELECT b FROM Balance b WHERE b.userId = :userId")
    List<Balance> findBalanceByUserId(@Param("userId") Integer userId);
}
