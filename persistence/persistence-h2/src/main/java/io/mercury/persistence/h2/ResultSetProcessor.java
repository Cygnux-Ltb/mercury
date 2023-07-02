package io.mercury.persistence.h2;

import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@FunctionalInterface
public interface ResultSetProcessor extends RowProcessor {

    @Override
    default Object[] toArray(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Unsupported toArray(ResultSet) function");
    }

    @Override
    default Map<String, Object> toMap(ResultSet rs) {
        throw new UnsupportedOperationException("Unsupported toMap(ResultSet) function");
    }

    @Override
    default <T> T toBean(ResultSet rs, Class<? extends T> type) {
        throw new UnsupportedOperationException("Unsupported toArray(ResultSet, Class) function");
    }

}
