package politech.budget.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import politech.budget.dto.User;
import politech.budget.dto.UserPost;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserBuilderTest {

    @Autowired
    private UserBuilder userBuilder;

    @Test
    public void build() {
        //given
        UserPost userPost = givenUserPost();
        //when
        User user = userBuilder.build(userPost);
        //then
        then(user);
    }

    private UserPost givenUserPost() {
        UserPost userPost = new UserPost();
        userPost.setName("name");
        userPost.setPassword("pass");
        return userPost;
    }

    private void then(User user) {
        assertEquals(user.getName(), "name");
        assertEquals(user.getPassword(), "pass");
    }
}