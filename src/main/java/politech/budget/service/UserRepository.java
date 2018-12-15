package politech.budget.service;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import politech.budget.dto.User;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u  WHERE id = :id ")
    User findUserById(@Param("id") Integer id);

    @Query(value = "SELECT u FROM User u  WHERE u.name = :name AND u.password = :password")
    User findUserByNameAndPassword(@Param("name") String name,
                                   @Param("password") String password);

    @Query(value = "SELECT u FROM User u WHERE u.name = :name")
    User findUserByName(@Param("name") String name);
}
