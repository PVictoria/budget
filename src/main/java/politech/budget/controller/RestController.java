package politech.budget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import politech.budget.builder.UserBuilder;
import politech.budget.dto.*;
import politech.budget.service.Dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/operation/{userName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Operation> getOperations(@PathVariable("userName") String userName) {
        return dao.findOperationsByUserId(userName);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userName}/article/{articleName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Operation> getOperationsArticle(@PathVariable("userName") String userName, @PathVariable("articleName") String articleName) {
        return dao.findOperationsByUserIdAndArticle(userName, articleName);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userName}/article/{articleName}/date/{date}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Operation> getOperationsArticleBetweenDates(@PathVariable("userName") String userName,
                                                            @PathVariable("articleName") String articleName,
                                                            @PathVariable("date") String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = format.parse(date);
        return dao.findOperationsByUserIdAndArticleIdAndCreationTime(userName, articleName, date1);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userName}/date/{date}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Operation> getOperationsBetweenDates(@PathVariable("userName") String userName,
                                                     @PathVariable("date") String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);
        return dao.findOperationsByUserIdAndCreationTime(userName, date1);
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Operation postOperation(@RequestBody Operation operation) {
        return dao.postOperation(operation);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteOperation(@RequestBody Operation operation) {
        dao.deleteOperation(operation.getId());
    }

    /*
     * Balance
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance/{userName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Balance> getBalaceByUserName(@PathVariable("userName") String userName) {
        return dao.findBalanceByUserName(userName);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance/{userName}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Balance postArticle(@RequestBody Balance balance) {
        return dao.postBalance(balance);
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteArticle(@RequestBody Balance balance) {
        dao.deleteBalance(balance);
    }

    /*
     * Statistics
     */

}
