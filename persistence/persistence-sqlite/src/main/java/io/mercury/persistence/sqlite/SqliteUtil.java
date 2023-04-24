package io.mercury.persistence.sqlite;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.sys.SysProperties;
import io.mercury.common.util.StringSupport;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static io.mercury.common.file.FileUtil.mkdirInHome;

public final class SqliteUtil {

    private static final Logger log = Log4j2LoggerFactory.getLogger(SqliteUtil.class);

    public static final String JdbcProtocol = "jdbc:sqlite:";

    public static String getSqliteUrlInHome(@Nonnull String dir, @Nonnull String dbName) {
        if (!dbName.endsWith(".db")) {
            dbName = dbName + ".db";
        }
        mkdirInHome(dir);
        return JdbcProtocol + StringSupport.fixPath(SysProperties.USER_HOME) + StringSupport.fixPath(dir) + dbName;
    }

    public static <T> List<T> query(@Nonnull Connection connection, @Nonnull String sql,
                                    @Nonnull ResultSetProcessor processor, Class<T> type) throws SQLException {
        Asserter.nonNull(connection, "connection");
        try (// create a database connection
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            return processor.toBeanList(rs, type);
        } catch (SQLException e) {
            log.error("error message -> {}", e.getMessage(), e);
            throw e;
        }
    }

    private SqliteUtil() {
    }

    public static void main(String[] args) {

        System.out.println(SqliteUtil.getSqliteUrlInHome("sqlite-file", "example"));

    }

}
