package tw.aaron.openai

import dev.langchain4j.memory.ChatMemory
import dev.langchain4j.memory.chat.ChatMemoryProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import jakarta.annotation.PreDestroy
import jakarta.enterprise.context.RequestScoped
import java.util.concurrent.ConcurrentHashMap


@RequestScoped
class ChatMemoryBean:ChatMemoryProvider {

    private val memories = ConcurrentHashMap<Any, ChatMemory>()

    override fun get(memoryId: Any): ChatMemory {
        return memories.computeIfAbsent(memoryId) {
            MessageWindowChatMemory.builder()
                .maxMessages(20)
                .id(memoryId)
                .build()
        }
    }

    @PreDestroy
    fun close() {
        memories.clear()
    }
}
