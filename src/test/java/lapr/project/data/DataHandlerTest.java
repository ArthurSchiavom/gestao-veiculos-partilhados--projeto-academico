package lapr.project.data;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

class DataHandlerTest {


    @Test
    public void testConnection() {
/*

        DataHandler dh = null;
        try {

            // URL da BD a usar...
            // ... do servidor local.
            String jdbcUrl = "jdbc:oracle:thin://@vsrvbd1.dei.isep.ipp.pt:1521/pdborcl";
            // ... do servidor do DEI.
            //String jdbcUrl = "jdbc:oracle:thin://@vsrvbd1.dei.isep.ipp.pt:1521/pdborcl";

            // Credenciais do utilizador da BD.
            String username = "LAPR3_G50";
            String password = "qwerty";

            dh = new DataHandler(jdbcUrl, username, password);

            System.out.println("\nEstabelecer a ligação à BD...");
            dh.openConnection();
            System.out.println("\t... Ligação obtida.");
            String currentDir = System.getProperty("user.dir");
            System.out.println("Current dir using System:" + currentDir);
            String current = new java.io.File(".").getCanonicalPath();
            System.out.println("Current dir:" + current);

            //URL url = Thread.currentThread().getContextClassLoader().getResource("demo_jdbc.sql");
            InputStream stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("demo_jdbc.sql");
            //File cvsTestFile = TestUtil.GetDocFromResource(getClass(), "MyTestFile.cvs");

            dh.scriptRunner("target/test-classes/demo_jdbc.sql");

            // TESTE 1
            String cor = "red";
            System.out.println("\nBarcos " + cor);
            ResultSet rSet = dh.getBoats(cor);
            while (rSet.next()) {
                System.out.printf("%5d %s %n", rSet.getInt("bid"), rSet.getString("bname"));
            }

            rSet = dh.getSailor(100);
            if (rSet != null) {
                dh.removeSailor(100);
                System.out.println("\nMarinheiro removido.");
            }

            // TESTE 3
            System.out.println("\nAdicionar Sailor ...");
            dh.addSailor(100, "Popeye", 11, 85);
            System.out.println("\t... Sailor Adicionado.");

            // TESTE 2
            rSet = dh.getSailor(100);
            if (rSet != null) {
                dh.removeSailor(100);
                System.out.println("\nMarinheiro removido.");
            }

        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            String mensagem = dh.closeAll();
            if (!mensagem.isEmpty())
                System.out.println(mensagem);
            System.out.println("\nTerminada a ligação à BD.");
        }
*/
    }
}