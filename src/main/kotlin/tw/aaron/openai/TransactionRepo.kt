package tw.aaron.openai

import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import dev.langchain4j.agent.tool.Tool
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime

@ApplicationScoped
class TransactionRepo {

    @Inject
    lateinit var mongoClient: MongoClient

    @Tool("Get the transaction for a given customer for the last 15 minutes")
    fun getTransactionsForCustomer(customerId:Long):List<Transaction> {
        return mongoClient.getDatabase("openai")
            .getCollection("Transaction", Transaction::class.java)
            .find(
                and(
                    Filters.eq("customerId", customerId),
                    Filters.gt("time", LocalDateTime.now().minusMinutes(15))
                )
            ).toList()
    }

    fun getAmountForCustomer(customerId:Long):Double {
        return mongoClient.getDatabase("openai")
            .getCollection("Transaction", Transaction::class.java)
            .find(
                and(
                    Filters.eq("customerId", customerId),
                    Filters.gt("time", LocalDateTime.now().minusMinutes(15))
                )
            ).sumOf { it.amount }
    }

    @Tool("Get the city for a given transaction id")
    fun getCityForTransaction( transactionId:Long):String {
        return mongoClient.getDatabase("openai")
            .getCollection("Transaction", Transaction::class.java)
            .find(
                    Filters.eq("_id", transactionId),
            ).firstOrNull()?.city ?: "unknown"
    }

    fun initData(list:List<Transaction>){
        mongoClient.getDatabase("openai")
            .getCollection("Transaction", Transaction::class.java)
            .insertMany(list)
    }

    data class Transaction(
        @BsonId
        val id:Long,
        val amount:Double,
        val customerId:Long,
        val city:String,
        val time: LocalDateTime
    )
}
