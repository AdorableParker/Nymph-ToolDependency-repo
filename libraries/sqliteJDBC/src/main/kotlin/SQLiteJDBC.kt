import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager


class SQLiteJDBC(private val library: Path) {

    val connection: Connection by lazy {
        Class.forName("org.sqlite.JDBC") // 初始化 Sqlite 驱动类
        DriverManager.getConnection("jdbc:sqlite:$library")
    }

    /**
     * 初始化 Sqlite 驱动类,连接数据库,输出驱动程序的版本信息
     */
    fun open() = "${connection.metaData.driverName}\t${connection.metaData.driverVersion}"

    /**
     * 执行给定的 SQL 语句, 该语句返回单个 ResultSet 对象.
     * @param safeSQL – 要发送到数据库的 SQL 语句，通常是静态 SQL SELECT 语句
     * @param values – 要传入 SQL 语句的参数
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    inline fun <reified T> safeExecuteDQLorDCL(safeSQL: String, vararg values: Any): SQLResult<T> = runCatching {
        connection.prepareStatement(safeSQL).use { stmt ->
            // 设置参数
            values.forEachIndexed { index, value ->
                when (value) {
                    is String -> stmt.setString(index + 1, value)
                    is Long -> stmt.setLong(index + 1, value)
                    is Double -> stmt.setDouble(index + 1, value)
                    is Int -> stmt.setInt(index + 1, value)
                    is Boolean -> stmt.setBoolean(index + 1, value)
                }
            }
            stmt.executeQuery().let { resultSet ->
                val resultList = mutableListOf<T>()
                val initFunc = T::class.java.getDeclaredConstructor(*listOf(Map::class.java).toTypedArray())
                    .apply { isAccessible = true }
                while (resultSet.next()) {
                    val nameList = T::class.members.map { it.name }
                    val row = mutableMapOf<String, Any?>()
                    for (i in 1..resultSet.metaData.columnCount) {
                        if (resultSet.metaData.getColumnName(i) in nameList) {
                            row[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
                        }
                    }
                    initFunc.newInstance(row).let(resultList::add)
                }
                SQLResult(null, resultList)
            }
        }
    }.onFailure {
        return SQLResult("SQL执行异常:${it.message}\nSQL:$safeSQL", listOf())
    }.getOrThrow()


    /**
     * 执行给定的 SQL 语句, 该语句返回单个 ResultSet 对象.
     * @param sql – 要发送到数据库的 SQL 语句，通常是静态 SQL SELECT 语句
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    inline fun <reified T> executeDQLorDCL(sql: String): SQLResult<T> = runCatching {
        connection.createStatement().use { statement ->
            statement.executeQuery(sql).let { resultSet ->
                val resultList = mutableListOf<T>()
                val initFunc = T::class.java.getDeclaredConstructor(*listOf(Map::class.java).toTypedArray())
                    .apply { isAccessible = true }
                while (resultSet.next()) {
                    val nameList = T::class.members.map { it.name }
                    val row = mutableMapOf<String, Any?>()
                    for (i in 1..resultSet.metaData.columnCount) {
                        if (resultSet.metaData.getColumnName(i) in nameList) {
                            row[resultSet.metaData.getColumnName(i)] = resultSet.getObject(i)
                        }
                    }
                    initFunc.newInstance(row).let(resultList::add)
                }
                SQLResult(null, resultList)
            }
        }
    }.onFailure {
        return SQLResult("SQL执行异常:${it.message}\nSQL:$sql", listOf())
    }.getOrThrow()

    /**
     * 执行给定的 SQL 语句, 该语句返回单个 ResultSet 对象.
     * @param sql – 要发送到数据库的 SQL 语句，通常是静态 SQL SELECT 语句
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    inline fun <reified T> executeDQLorDCL(sql: () -> String): SQLResult<T> = executeDQLorDCL(sql.invoke())

    /**
     * 执行给定的 SQL 语句, 它可能是 INSERT, UPDATE 或 DELETE 语句或不返回任何内容的 SQL 语句, 例如 SQL DDL 语句.
     * @param sql SQL数据操作语言(DML)语句, 例如  INSERT, UPDATE 或 DELETE;
     * 或是不返回任何内容的 SQL 语句, 例如 DDL 语句.
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    fun executeDMLorDDL(sql: String): String {
        return runCatching {
            connection.createStatement().use { it.executeUpdate(sql) }
        }.onFailure {
            return "SQL执行异常:${it.message}\nSQL:$sql"
        }.getOrThrow().let { "SQL执行成功:${it}行数据受影响" }
    }

    /**
     * 执行给定的 SQL 语句, 它可能是 INSERT, UPDATE 或 DELETE 语句或不返回任何内容的 SQL 语句, 例如 SQL DDL 语句.
     * @param sql SQL数据操作语言(DML)语句, 例如  INSERT, UPDATE 或 DELETE;
     * 或是不返回任何内容的 SQL 语句, 例如 DDL 语句.
     * @return 返回执行语句后受影响的行数或是异常的原因
     */
    fun executeDMLorDDL(sql: () -> String): String = executeDMLorDDL(sql.invoke())

    /**
     * 关闭 SQL 链接
     */
    fun close() {
        connection.close()
    }
}



