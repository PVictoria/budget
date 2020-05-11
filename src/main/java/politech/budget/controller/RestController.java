package politech.budget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import politech.budget.builder.UserBuilder;
import politech.budget.dto.*;
import politech.budget.service.Dao;

import java.util.List;
import java.util.Set;

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
    @RequestMapping(value = "/operation/{userId}/article/{articleName}/monthYear/{monthYear}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OperationGet> getOperationsArticleBetweenDates(@PathVariable("userId") Integer userId,
                                                               @PathVariable("articleName") String articleName,
                                                               @PathVariable("monthYear") String monthYear) {
        return dao.findOperationsByUserIdAndArticleIdAndCreationTime(userId, articleName, monthYear);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/operation/{userId}/monthYear/{monthYear}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<OperationGet> getOperationsBetweenDates(@PathVariable("userId") Integer userId,
                                                        @PathVariable("monthYear") String monthYear) {
        return dao.findOperationsByUserIdAndCreationTime(userId, monthYear);
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
    public List<Balance> getBalanceByUserName(@PathVariable("userId") Integer userId) {
        return dao.findBalanceByUserName(userId);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/balance/{monthYear}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Balance postArticle(@PathVariable("monthYear") String monthYear, @RequestBody Balance balance) {
        return dao.postBalance(monthYear, balance);
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

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/statistics/line/{userId}/{time}/article/{articleName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LineChart> getLineStatistics(@PathVariable("userId") Integer userId,
                                             @PathVariable("time") String time,
                                             @PathVariable("articleName") String articleName) {
        return dao.getLineChartStatistics(userId, time, articleName);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/statistics/pie/{userId}/{monthYear}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<PieChart> getPieStatistics(@PathVariable("userId") Integer userId,
                                          @PathVariable("monthYear") String monthYear) {
        return dao.getPieChartStatistics(userId, monthYear);
    }

}
