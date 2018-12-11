package politech.budget.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import politech.budget.dto.User;
import politech.budget.service.UserService;

@Controller
public class RestController {

    private final UserService userService;

    @Autowired
    public RestController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/user/create/{name}/{password}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void put(@PathVariable("name") String name,
                    @PathVariable("password") String password) {
        userService.put(name, password);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String get(@PathVariable("id") long id) throws JsonProcessingException {
        User user = userService.getUser(id);
        System.out.println("====" + user.toString() + "====");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(user);
    }
}
