package politech.budget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import politech.budget.builder.UserBuilder;
import politech.budget.dto.*;
import politech.budget.service.Dao;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class RestController {

    private final Dao dao;
    private final UserBuilder userBuilder;

    /*
     * User
     */
    @CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User postUser(@RequestBody UserPost user) {
        return dao.postUser(userBuilder.build(user));
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id) {
        return dao.getUser(id);
    }

    /*
     * Article
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Article> getArticles() {
        return dao.getArticles();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/article", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Article postArticle(@RequestBody Article article) {
        return dao.postArticle(article);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/article", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteArticle(@RequestBody Article article) {
        dao.deleteArticle(article.getName());
    }

    /*
     * Operations
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Operation> getOperations(@RequestBody User user) {
        return dao.findOperationsByUserId(user.getId());
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/article", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Operation> getOperationsArticle(@RequestBody UserArticle userArticle) {
        return dao.findOperationsByUserIdAndArticle(userArticle.getUserName(), userArticle.getArticleName());
    }



    /*
     * Balance
     */


    /*
     * Statistics
     */

}
