package politech.budget.controller;

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

    @RequestMapping(value = "/user/create/{name}/{password}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void put(@PathVariable("name") String name,
                    @PathVariable("password") String password) {
        userService.put(name, password);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String get(@PathVariable("id") long id) {
        User user = userService.getUser(id);
        return user.toString();
    }
}
