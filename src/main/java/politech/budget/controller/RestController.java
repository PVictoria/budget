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
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User getUser(@RequestBody User user) {
        return dao.getUser(user);
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
    @RequestMapping(value = "/article/{articleName}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteArticle(@PathVariable("articleName") String articleName) {
        dao.deleteArticle(articleName);
    }

    /*
     * Operations
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OperationGet> getOperations(@PathVariable("userId") Integer userId) {
        return dao.findOperationsByUserId(userId);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userId}/article/{articleName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OperationGet> getOperationsArticle(@PathVariable("userId") Integer userId, @PathVariable("articleName") String articleName) {
        return dao.findOperationsByUserIdAndArticle(userId, articleName);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userId}/article/{articleName}/date/{date}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OperationGet> getOperationsArticleBetweenDates(@PathVariable("userId") Integer userId,
                                                               @PathVariable("articleName") String articleName,
                                                               @PathVariable("date") String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = format.parse(date);
        return dao.findOperationsByUserIdAndArticleIdAndCreationTime(userId, articleName, date1);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userId}/date/{date}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OperationGet> getOperationsBetweenDates(@PathVariable("userId") Integer userId,
                                                        @PathVariable("date") String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);
        return dao.findOperationsByUserIdAndCreationTime(userId, date1);
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Operation postOperation(@RequestBody OperationPost operationPost) {
        return dao.postOperation(operationPost);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{operationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteOperation(@PathVariable Integer operationId) {
        dao.deleteOperation(operationId);
    }

    /*
     * Balance
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance/{userId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Balance> getBalaceByUserName(@PathVariable("userId") Integer userId) {
        return dao.findBalanceByUserName(userId);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Balance postArticle(@RequestBody Balance balance) {
        return dao.postBalance(balance);
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteArticle(@PathVariable("userId") Integer userId) {
        dao.deleteBalance(userId);
    }

    /*
     * Statistics
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/statistics/bar/{userId}/{time}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<BarChart> getBarStatistics(@PathVariable("userId") Integer userId,
                                           @PathVariable("time") String time) {
        return dao.getBarChartStatistics(userId, time);
    }

}
