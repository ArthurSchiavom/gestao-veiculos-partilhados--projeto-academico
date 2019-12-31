package lapr.project.controller;

import lapr.project.data.registers.Company;
import lapr.project.utils.InvalidFileDataException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterPOIController {
    private final Company company;

    private static final int POI_LATITUDE = 0;
    private static final int POI_LONGITUDE = 1;
    private static final int POI_ELEVATION = 2;
    private static final int POI_DESCRIPTION = 3;

    public RegisterPOIController(Company company) {
        this.company = company;
    }

    /**
     * @return the number of users added
     */
    public int registerPOIs(List<String[]> parsedData, String fileName) throws SQLException, InvalidFileDataException {
        String[] line = parsedData.get(0);
        if (line.length != 4 || !line[0].equals("latitude") || !line[1].equals("longitude") || !line[2].equals("elevation")
                || !line[3].equals("poi description"))
            throw new InvalidFileDataException("Header is different from expected");

        List<Double> lat = new ArrayList<>();
        List<Double> lon = new ArrayList<>();
        List<Integer> elev = new ArrayList<>();
        List<String> desc = new ArrayList<>();

        int i = 0;
        try {
            for (i = 1; i < parsedData.size(); i++) {
                line = parsedData.get(i);
                if (line.length == 1 && line[0].isEmpty())
                    continue;

                if(line[POI_ELEVATION].isEmpty()){
                    lat.add(Double.parseDouble(line[POI_LATITUDE]));
                    lon.add(Double.parseDouble(line[POI_LONGITUDE]));
                    elev.add(0); // in the input folder it says if there's no elevation, to assume that it is 0
                    desc.add(line[POI_DESCRIPTION]);
                }else {
                    lat.add(Double.parseDouble(line[POI_LATITUDE]));
                    lon.add(Double.parseDouble(line[POI_LONGITUDE]));
                    elev.add(Integer.parseInt(line[POI_ELEVATION]));
                    desc.add(line[POI_DESCRIPTION]);
                }

            }
        } catch (NumberFormatException e) {
            throw new InvalidFileDataException("Invalid data at non-commented, non-empty line number " + i + " of the file " + fileName);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileDataException("Not all columns are present at non-commented, non-empty line " + i + " of the file " + fileName);
        }
        try {
            return company.getPoiRegister().insertPOIs(lat,lon,elev,desc);
        } catch (SQLException e) {
            throw new SQLException("Failed to write data to the database");
        }
    }
}
