package lapr.project.data;

import java.sql.SQLException;

public interface SQLOperation<T> {
    T executeOperation() throws SQLException;
}
