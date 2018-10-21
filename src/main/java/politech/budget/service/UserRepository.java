package politech.budget.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import politech.budget.dto.User;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM budget.users u WHERE u.id = ?;", nativeQuery = true)
    User findUserById(@Param("id") Long id);

    @Modifying
    @Query(value = "insert into budget.users (name, password) values (?, ?);", nativeQuery = true)
    void put(@Param("name") String name,
             @Param("password") String password);
}
