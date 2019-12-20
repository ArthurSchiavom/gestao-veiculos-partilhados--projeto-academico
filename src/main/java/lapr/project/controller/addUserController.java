package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.data.registers.UsersRegister;

import java.sql.SQLException;
import java.util.List;

public class addUserController {

    private Company company;
    private UsersRegister usersRegister;

    public addUserController(Company company) {
        this.company = company;
        usersRegister = company.getUsersRegister();
    }

    /**
     * @return the number of users added
     */
    public int addUsers(List<String> email, List<String> username, List<Integer> height, List<Integer> weight, List<Character> gender, List<String> creditCardNumber, List<Float> cyclingAvgSpeed) throws SQLException {
        return usersRegister.insertClients(email,username,height,weight,gender,creditCardNumber,cyclingAvgSpeed);
    }
}
