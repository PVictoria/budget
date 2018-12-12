package politech.budget.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    private String name;
    private String password;
}
