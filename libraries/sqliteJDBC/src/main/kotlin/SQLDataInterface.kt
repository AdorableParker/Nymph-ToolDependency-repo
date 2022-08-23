import java.sql.ResultSet
import kotlin.reflect.KProperty

interface SQLDataInterface {
    @Suppress("UNCHECKED_CAST")
    operator fun <T, E> ResultSet.getValue(thisRef: E, property: KProperty<*>): T = getObject(property.name) as T
}