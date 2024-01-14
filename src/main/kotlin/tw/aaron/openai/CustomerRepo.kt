package tw.aaron.openai

import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters.eq
import dev.langchain4j.agent.tool.Tool
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.codecs.pojo.annotations.BsonId
import java.util.logging.Logger

@ApplicationScoped
class CustomerRepo {
    private val logger: Logger = Logger.getLogger(this::class.qualifiedName)

    @Inject
    private lateinit var mongoClient: MongoClient


    @Tool("get the customer name for the given customerId")
    fun getCustomerName(id: Long): String {
        return mongoClient.getDatabase("openai")
            .getCollection("Customer", Customer::class.java)
            .find(eq("_id", id))
            .firstOrNull()?.name ?: "unknown"
    }

    fun initData(list:List<Customer>){
        mongoClient.getDatabase("openai")
            .getCollection("Customer", Customer::class.java)
            .insertMany(list)
    }
    data class Customer(
        @BsonId
        val id:Long,
        val name:String,
        val email:String,
    )
}
