package politech.budget.builder;

import org.springframework.stereotype.Component;
import politech.budget.dto.User;
import politech.budget.dto.UserPost;

@Component
public class UserBuilder {

    public User build(UserPost userPost) {
        User user = new User();
        user.setName(userPost.getName());
        user.setPassword(userPost.getPassword());
        return user;
    }
}
