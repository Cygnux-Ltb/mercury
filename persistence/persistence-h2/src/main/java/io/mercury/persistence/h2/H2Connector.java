package io.mercury.persistence.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.mercury.common.sys.SysProperties;

public class SqliteConnect {

	/**
	 * Connect to a sample database
	 */
	public static void connect() {

		File dbFile = new File(SysProperties.USER_HOME, "sqlite.db");
		String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
		System.out.println(url);

		final HikariConfig config = new HikariConfig();
		config.setDriverClassName("org.sqlite.JDBC");
		config.setJdbcUrl(url);
		config.setConnectionTestQuery("SELECT 0");
		config.setMaxLifetime(60000);
		config.setIdleTimeout(45000);
		config.setMaximumPoolSize(30);
		// URL Parameters
		try (
				// create hikari data source
				HikariDataSource dataSource = new HikariDataSource(config);
				// create a connection to the database
				Connection conn = dataSource.getConnection()) {
			System.out.println("Connection to SQLite has been established -> " + conn.toString());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		connect();
	}

}
