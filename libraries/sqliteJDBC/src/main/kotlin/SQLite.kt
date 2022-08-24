import java.nio.file.Path
import java.sql.DriverManager


class SQLite(private val library: Path): SQLDataInterface {
    private val connection by lazy {
        Class.forName("org.sqlite.JDBC") // 初始化 Sqlite 驱动类
        DriverManager.getConnection("jdbc:sqlite:$library")
    }

    fun open() = "${connection.metaData.driverName}\t${connection.metaData.driverVersion}"

    /**
     * 执行给定的 SQL 语句, 该语句返回单个 ResultSet 对象.
     * @param sql – 要发送到数据库的 SQL 语句，通常是静态 SQL SELECT 语句
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    fun <T> executeDQLorDCL(sql: String): SQLResult<T> {
        return runCatching {
            connection.createStatement().use { statement ->
                statement.executeQuery(sql).let { resultSet ->
                    val resultList = mutableListOf<T>()
                    while(resultSet.next()){
                        val result:T by resultSet
                        resultList.add(result)
                    }
                    SQLResult(null, resultList)
                }
            }
        }.onFailure {
            return SQLResult("SQL执行异常:${it.message}\nSQL:$sql", listOf())
        }.getOrThrow()
    }

    /**
     * 执行给定的 SQL 语句, 它可能是 INSERT, UPDATE 或 DELETE 语句或不返回任何内容的 SQL 语句, 例如 SQL DDL 语句.
     * @param sql SQL数据操作语言(DML)语句, 例如  INSERT, UPDATE 或 DELETE;
     * 或是不返回任何内容的 SQL 语句, 例如 DDL 语句.
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    fun executeDMLorDDL(sql: String):String {
        return runCatching {
            connection.createStatement().use { it.executeUpdate(sql) }
        }.onFailure {
            return "SQL执行异常:${it.message}\nSQL:$sql"
        }.getOrThrow().let { "SQL执行成功:${it}行数据受影响" }
    }

    fun close(){
        connection.close()
    }
}

