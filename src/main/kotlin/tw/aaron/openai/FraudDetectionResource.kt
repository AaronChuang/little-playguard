package tw.aaron.openai

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.jboss.resteasy.reactive.RestQuery
import java.time.LocalDateTime


@Path("/fraud")
class FraudDetectionResource {

    @Inject
    lateinit var service: FraudDetectionAi

    @Inject
    lateinit var transactions: TransactionRepo

    @Inject
    lateinit var customerRepo: CustomerRepo


    @GET
    @Path("/distance")
    fun detectBasedOnDistance(@RestQuery customerId: Long): String {
        return service.detectDistanceFraudForCustomer(customerId)
    }

    @GET
    @Path("/amount")
    fun detectBaseOnAmount(@RestQuery customerId: Long): String {
        return service.detectAmountFraudForCustomer(customerId)
    }

    @GET
    @Path("/transactions")
    fun list(@RestQuery customerId: Long): List<TransactionRepo.Transaction> {
        return transactions.getTransactionsForCustomer(customerId)
    }

    @GET
    @Path("/verification")
    fun verify(@RestQuery customerId: Long): Double {
        return transactions.getAmountForCustomer(customerId)
    }

    @GET
    @Path("/init")
    fun init() {

        val customers = listOf(
            CustomerRepo.Customer(1, "Emily Johnson", "emily.johnson@mail.com"),
            CustomerRepo.Customer(2, "Michael Anderson", "michael.anderson@mail.com"),
            CustomerRepo.Customer(3, "Olivia Williams", "olivia.williams@mail.com"),
        )
        customerRepo.initData(customers)

        val list = mutableListOf<TransactionRepo.Transaction>()

        for (i in 0..50) {
            val transaction = TransactionRepo.Transaction(
                id = i.toLong(),
                customerId = customers.random().id,
                amount = (0..1000).random().toDouble(),
                time = LocalDateTime.now().minusMinutes((0..20).random().toLong()),
                city = CITIES.random()
            )
            list.add(transaction)
        }
        transactions.initData(list)
    }

    companion object {
        val CITIES = listOf("Paris", "Lyon", "Marseille", "Bordeaux", "Toulouse", "Nantes", "Brest",
            "Clermont-Ferrand", "La Rochelle", "Lille", "Metz", "Strasbourg", "Nancy", "Valence", "Avignon", "Montpellier",
            "Nime", "Arles", "Nice", "Cannes")


        fun getARandomCity(): String {
            return CITIES.random()
        }
    }
}
