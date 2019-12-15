package lapr.project.ui;

import lapr.project.bootstrap.Bootstrap;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.shutdown.Shutdown;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Main {

    /**
     * Logger class.
     */
    private static final Logger LOGGER = Logger.getLogger("MainLog");

    /**
     * Private constructor to hide implicit public one.
     */
    private Main() {

    }

    /**
     * Application main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Bootstrap.boot();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return;
        }
        DataHandler dh = Company.getInstance().getDataHandler();
        try {
            Company.getInstance().getVehicleRegister().addEletricScooter(1, 61, 30, 1, 1, 1, true, ElectricScooterType.URBAN, "PT001", 1, 75);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.INFO, "test");
        Shutdown.shutdown();


//        CalculatorExample calculatorExample = new CalculatorExample();
//        int value = calculatorExample.sum(3, 5);
//
//        if (LOGGER.isLoggable(Level.INFO))
//            LOGGER.log(Level.INFO, String.valueOf(value));
//
//        //load database properties
//
//        try {
//            Properties properties =
//                    new Properties(System.getProperties());
//            InputStream input = new FileInputStream("target/classes/application.properties");
//            properties.load(input);
//            input.close();
//            System.setProperties(properties);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        //Initial Database Setup
//        DataHandler dh = new DataHandler();
//        dh.scriptRunner("target/test-classes/demo_jdbc.sql");
//
//        System.out.println("\nVerificar se existe Sailor 100...");
//        try {
//            Sailor.getSailor(100);
//            System.out.println("Nunca deve aparecer esta mensagem");
//        } catch (IllegalArgumentException ex) {
//            System.out.println(ex.getMessage());
//        }
//        System.out.println("\nAdicionar Sailor ...");
//
//
//        long sailorID = 100;
//        String sailorName = "Popeye";
//        long sailorRating = 11;
//        int sailorAge = 85;
//
//        Sailor sailor = new Sailor(sailorID, sailorName);
//        sailor.setAge(sailorAge);
//        sailor.setRating(sailorRating);
//        sailor.save();
//
//        System.out.println("\t... Sailor Adicionado.");
//
//        System.out.println("\nVerificar se existe Sailor 100...");
//        try {
//            sailor = Sailor.getSailor(100);
//            System.out.println("\nSailor 100 existe...: " + sailor.getName());
//        } catch (IllegalArgumentException ex) {
//            System.out.println(ex.getMessage());
//        }

    }
}

