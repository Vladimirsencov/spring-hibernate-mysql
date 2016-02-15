package org.javarush.test.controller;

import org.apache.log4j.Logger;
import org.javarush.test.domain.User;
import org.javarush.test.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Handles and retrieves person request
 */

@Controller
@RequestMapping("/main")
public class MainController {

    protected static Logger logger = Logger.getLogger("controller");
    private final int PERSON_PER_PAGE = 3;
    private String firstName;
    private String searchFromSQL;
    private int pageNumber = 1;

    @Resource(name = "userService")
    private UserService userService;

    /**
     * Handles and retrieves all persons and show it in a JSP page
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getPersons(Model model) {

        // Retrieve all users by delegating the call to UserService
        List<User> users;

        if ((firstName == null || firstName.isEmpty()) && (searchFromSQL == null || searchFromSQL.isEmpty())) {
            logger.debug("Received request to show all users");
            users = userService.getAll();
            final int from = (pageNumber - 1) * PERSON_PER_PAGE;
            final int to = pageNumber * PERSON_PER_PAGE <= users.size() ? pageNumber * PERSON_PER_PAGE : users.size();
            List<User> personsOnPage = userService.getAll().subList(from, to);
            model.addAttribute("users", personsOnPage);
        } else if (firstName != null && !firstName.isEmpty()) {
            assert searchFromSQL == null || searchFromSQL.isEmpty();
            logger.debug("Received request to show  users by name");
            users = userService.getAllByName(firstName);
            if (users.size() < PERSON_PER_PAGE) pageNumber = 1;
            final int from = (pageNumber - 1) * PERSON_PER_PAGE;
            final int to = pageNumber * PERSON_PER_PAGE <= users.size() ? pageNumber * PERSON_PER_PAGE : users.size();
            List<User> personsOnPage = userService.getAllByName(firstName).subList(from, to);
            model.addAttribute("users", personsOnPage);
        } else {
            assert firstName == null || firstName.isEmpty();
            logger.debug("Received request to show  users by SQL");
            users = userService.getAllBySQL(searchFromSQL);
            if (users.size() < PERSON_PER_PAGE) pageNumber = 1;
            final int from = (pageNumber - 1) * PERSON_PER_PAGE;
            final int to = pageNumber * PERSON_PER_PAGE <= users.size() ? pageNumber * PERSON_PER_PAGE : users.size();
            List<User> personsOnPage = userService.getAllBySQL(searchFromSQL).subList(from, to);
            model.addAttribute("users", personsOnPage);
        }
        // Attach attributes to the Model
        model.addAttribute("firstName", firstName);
        model.addAttribute("pages", pageNumber(users, PERSON_PER_PAGE));
        model.addAttribute("searchFromSQL", searchFromSQL);
        model.addAttribute("pageNumber", pageNumber);
        // This will resolve to /WEB-INF/jsp/userspage.jsp
        return "userspage";
    }

    /**
     * Retrieves the add page
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
        logger.debug("Received request to show add page");

        // Create new User and add to model
        // This is the formBackingOBject
        model.addAttribute("userAttribute", new User());

        // This will resolve to /WEB-INF/jsp/addpage.jsp
        return "addpage";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("personAttribute") User user) {
        logger.debug("Received request to add new user");

        // The "personAttribute" model has been passed to the controller from the JSP
        // We use the name "personAttribute" because the JSP uses that name

        // Call UserService to do the actual adding
        userService.add(user);

        // This will resolve to /WEB-INF/jsp/addedpage.jsp
        return "addedpage";
    }
    @RequestMapping(value = "/users/setName", method = RequestMethod.GET)
    public String getNameForSearch(Model model) {
        logger.debug("Received request to show add page");

        // Create new User and add to model
        // This is the formBackingOBject
        model.addAttribute("userFirstNameAttribute", new User());

        // This will resolve to /WEB-INF/jsp/addpage.jsp
        return "setname";
    }

    @RequestMapping(value = "/users/setSQL", method = RequestMethod.GET)
    public String getSQLForSearch(Model model) {
        logger.debug("Received request to show add page");

        // Create new User and add to model
        // This is the formBackingOBject
        model.addAttribute("personSQLAttribute", new User());

        // This will resolve to /WEB-INF/jsp/addpage.jsp
        return "sql";
    }

    @RequestMapping(value = "/users/setSQL", method = RequestMethod.POST)
    public String setSQLForSearch(@ModelAttribute("userSQLAttribute") User user, Model model) {
        logger.debug("Received request to add new user");

        // The "personAttribute" model has been passed to the controller from the JSP
        // We use the name "personAttribute" because the JSP uses that name

        // Call UserService to do the actual adding

        searchFromSQL = user.getName();
        model.addAttribute("searchFromSQL", searchFromSQL);
        firstName = "";
        // This will resolve to /WEB-INF/jsp/addedpage.jsp
        return "aftersql";
    }

    @RequestMapping(value = "/users/setName", method = RequestMethod.POST)
    public String setNameForSearch(@ModelAttribute("personFirstNameAttribute") User user, Model model) {
        logger.debug("Received request to add new user");

        // The "personAttribute" model has been passed to the controller from the JSP
        // We use the name "personAttribute" because the JSP uses that name

        // Call UserService to do the actual adding
        firstName = user.getName();
        model.addAttribute("name", firstName);
        searchFromSQL = "";
        // This will resolve to /WEB-INF/jsp/addedpage.jsp
        return "setednamepage";
    }

    /**
     * Adds a new user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     *
     * @return the name of the JSP page
     */


    /**
     * Deletes an existing person by delegating the processing to UserService.
     * Displays a confirmation JSP page
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) Integer id,
                         Model model) {

        logger.debug("Received request to delete existing person");

        // Call UserService to do the actual deleting
        userService.delete(id);

        // Add id reference to Model
        model.addAttribute("id", id);

        // This will resolve to /WEB-INF/jsp/deletedpage.jsp
        return "deletedpage";
    }

    /**
     * Retrieves the edit page
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam(value = "id", required = true) Integer id,
                          Model model) {
        logger.debug("Received request to show edit page");

        // Retrieve existing User and add to model
        // This is the formBackingOBject
        model.addAttribute("userAttribute", userService.get(id));

        // This will resolve to /WEB-INF/jsp/editpage.jsp
        return "editpage";
    }

    /**
     * Edits an existing user by delegating the processing to UserService.
     * Displays a confirmation JSP page
     *
     * @return the name of the JSP page
     */
    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public String saveEdit(@ModelAttribute("userAttribute") User user,
                           @RequestParam(value = "id", required = true) Integer id,
                           Model model) {
        logger.debug("Received request to update user"+id);

        // The "userAttribute" model has been passed to the controller from the JSP
        // We use the name "personAttribute" because the JSP uses that name

        // We manually assign the id because we disabled it in the JSP page
        // When a field is disabled it will not be included in the ModelAttribute
        user.setId(id);

        // Delegate to UserService for editing
        userService.edit(user);

        // Add id reference to Model
        model.addAttribute("id", id);

        // This will resolve to /WEB-INF/jsp/editedpage.jsp
        return "editedpage";
    }

    private List<Integer> pageNumber(List<User> allUser, int usersPerPage) {
        int size = allUser.size();
        if (size != 0) {
            int counter = size % usersPerPage == 0 ? size / usersPerPage : size / usersPerPage + 1;
            List<Integer> numberList = new ArrayList<Integer>(counter);
            for (int i = 1; i <= counter; ++i) {
                numberList.add(i);
            }
            return numberList;
        } else {
            return new ArrayList<Integer>(Collections.singletonList(1));
        }
    }

    @RequestMapping(value = "/users/page", method = RequestMethod.GET)
    public String getPage(@RequestParam(value = "id", required = true) Integer id,
                          Model model) {
        pageNumber = id;
        model.addAttribute("pageNumber", id);
        return "numberPage";
    }
}
