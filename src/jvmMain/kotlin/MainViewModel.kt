import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

class MainViewModel {
    private val viewModelScope = CoroutineScope(Dispatchers.IO)
    private val arguments = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getObject(key: String, clazz: KClass<T>) = arguments[key].takeIf(clazz::isInstance) as T

    fun <T : Any> putObject(key: String, value: T) = arguments.set(key, value)

    fun removeObject(key: String) = arguments.remove(key)

}