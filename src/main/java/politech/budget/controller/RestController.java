package politech.budget.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import politech.budget.builder.UserBuilder;
import politech.budget.dto.User;
import politech.budget.dto.UserPost;
import politech.budget.service.Dao;

@RequiredArgsConstructor
@Controller
public class RestController {

    private final Dao dao;
    private final UserBuilder userBuilder;


    @CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    User put(@RequestBody UserPost user) {
        User user1 = dao.postUser(userBuilder.build(user));
        System.out.println("------" + user1.getId());
        return user1;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String get(@PathVariable("id") long id) throws JsonProcessingException {
        User user = dao.getUser(id);
        System.out.println("====" + user.toString() + "====");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(user);
    }


}
