package io.mercury.persistence.h2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.dbutils.RowProcessor;

@FunctionalInterface
public interface ResultSetProcessor extends RowProcessor {

	
	
	@Override
	default Object[] toArray(ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException("Unsupport toArray(ResultSet) function");
	}

	@Override
	default Map<String, Object> toMap(ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException("Unsupport toMap(ResultSet) function");
	}

	@Override
	default <T> T toBean(ResultSet rs, Class<? extends T> type) throws SQLException {
		throw new UnsupportedOperationException("Unsupport toArray(ResultSet, Class) function");
	}

}
