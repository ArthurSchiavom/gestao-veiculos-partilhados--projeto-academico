package lapr.project.data;

import java.sql.SQLException;

public interface SQLOperation<T> {
    public T executeOperation() throws SQLException;
}
