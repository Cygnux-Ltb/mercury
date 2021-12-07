package io.mercury.persistence.h2;

import static io.mercury.common.file.FileUtil.mkdirInHome;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.lang.Assertor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.StringSupport;

public final class H2Util {

	private static final Logger log = CommonLoggerFactory.getLogger(H2Util.class);

	public static final String JdbcProtocol = "jdbc:sqlite:";

	public static final String getH2UrlInHome(@Nonnull String dir, @Nonnull String dbName) {
		if (!dbName.endsWith(".db")) {
			dbName = dbName + ".db";
		}
		mkdirInHome(dir);
		return JdbcProtocol + StringSupport.fixPath(SysProperties.USER_HOME) + StringSupport.fixPath(dir) + dbName;
	}

	public static final <T> List<T> query(@Nonnull Connection connection, @Nonnull String sql,
			@Nonnull ResultSetProcessor processor, @Nonnull Class<T> type) throws SQLException {
		Assertor.nonNull(connection, "connection");
		try (// create a database connection
				Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(sql);) {
			return processor.toBeanList(rs, type);
		} catch (SQLException e) {
			log.error("error message -> {}", e.getMessage(), e);
			throw e;
		}
	}

	private H2Util() {
		
	}

	public static void main(String[] args) {

		System.out.println(H2Util.getH2UrlInHome("sqlite-file", "example"));

	}

}
