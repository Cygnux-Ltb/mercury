package io.mercury.persistence.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class H2Connector {

	private String EmbeddedMemoryMode = "jdbc:h2:mem:";

	private String EmbeddedLocalModeProtocol = "jdbc";

	private static final String DriverClass = "org.h2.Driver";
	
	public String getEmbeddedPrivateMemoryMode() {
		return EmbeddedMemoryMode;
	}

	public String getEmbeddedNamedMemoryMode(@Nonnull String dbName) {
		return EmbeddedMemoryMode + dbName;
	}

	public void get() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("");
		config.setDriverClassName(DriverClass);
		DataSource dataSource = new HikariDataSource(config);
		

	}

	/**
	 * 以嵌入式(本地)连接方式连接H2数据库
	 */
	private static final String JDBC_URL = "jdbc:h2:file:~/h2-file/user";

	/**
	 * 使用TCP/IP的服务器模式(远程连接)方式连接H2数据库(推荐)
	 */
	// private static final String JDBC_URL =
	// "jdbc:h2:tcp://127.0.0.1/~/h2-file/user";

	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public static void main(String[] args) throws Exception {

		// jdbc:h2:~/test
		// 连接位于用户目录下的test数据库
		// jdbc:h2:file:/data/sample

		// TODO Auto-generated method stub
		Class.forName(DriverClass);
		Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
		Statement statement = conn.createStatement();
//		statement.execute("DROP TABLE IF EXISTS USER_INF");
//		statement.execute("CREATE TABLE USER_INF(id INTEGER PRIMARY KEY, name VARCHAR(100), sex VARCHAR(2))");
//
//		statement.executeUpdate("INSERT INTO USER_INF VALUES(1, 'tom', '男') ");
//		statement.executeUpdate("INSERT INTO USER_INF VALUES(2, 'jack', '女') ");
//		statement.executeUpdate("INSERT INTO USER_INF VALUES(3, 'marry', '男') ");
//		statement.executeUpdate("INSERT INTO USER_INF VALUES(4, 'lucy', '男') ");

		ResultSet resultSet = statement.executeQuery("select * from USER_INF");

		while (resultSet.next()) {
			System.out.println(
					resultSet.getInt("id") + ", " + resultSet.getString("name") + ", " + resultSet.getString("sex"));
		}

		statement.close();
		conn.close();
	}

}
