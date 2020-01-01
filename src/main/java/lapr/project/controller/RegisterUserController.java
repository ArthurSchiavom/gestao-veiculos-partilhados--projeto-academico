package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.utils.InvalidFileDataException;
import lapr.project.utils.Utils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterUserController {

    private final Company company;

    private static final int USER_USERNAME = 0;
    private static final int USER_EMAIL = 1;
    private static final int USER_HEIGHT = 2;
    private static final int USER_WEIGHT = 3;
    private static final int USER_CYCLING_AVERAGE_SPEED = 4;
    private static final int USER_VISA = 5;
    private static final int USER_GENDER = 6;
    private static final int USER_PASS_WORD = 7;

    public RegisterUserController(Company company) {
        this.company = company;
    }

    /**
     * @return the number of users added
     */
    public int registerClients(String filePath) throws SQLException, InvalidFileDataException, FileNotFoundException {
        List<String[]> parsedData = Utils.parseDataFileAndValidateHeader(filePath, ";", "#"
                , "username;email;height;weight;cycling average speed;visa;gender;password");
        String[] line;

        List<String> username = new ArrayList<>();
        List<String> email = new ArrayList<>();
        List<Integer> height = new ArrayList<>();
        List<Integer> weight = new ArrayList<>();
        List<Float> cyclingAvgSpeed = new ArrayList<>();
        List<Character> gender = new ArrayList<>();
        List<String> visa = new ArrayList<>();
        List<String> password = new ArrayList<>();

        int i = 0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);

                username.add(line[USER_USERNAME]);
                email.add(line[USER_EMAIL]);
                height.add(Integer.parseInt(line[USER_HEIGHT]));
                weight.add(Integer.parseInt(line[USER_WEIGHT]));
                cyclingAvgSpeed.add(Float.parseFloat(line[USER_CYCLING_AVERAGE_SPEED]));
                visa.add(line[USER_VISA]);
                gender.add(line[USER_GENDER].charAt(0));
                password.add(line[USER_PASS_WORD]);
            }
        } catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + filePath);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + filePath);
        }
        try {
            return company.getUsersAPI().insertClients(email, username, height, weight, gender, visa, cyclingAvgSpeed,password);
        } catch (SQLException e) {
            throw new SQLException("Failed to write data to the database: \n" + e.getMessage());
        }
    }
}
