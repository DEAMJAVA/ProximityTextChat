package net.deamjava.proximity_text_chat

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

data class ProximityConfig(
    val chatRange: Double = 200.0,
    val deathMessageRange: Double = 200.0,
    val showOutOfRangeMessage: Boolean = false
)

object ProximityTextChatConfig {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private lateinit var configFile: File
    var config: ProximityConfig = ProximityConfig()
        private set

    fun init(configDir: File) {
        configFile = File(configDir, "proximity_text_chat.json")
        load()
    }

    fun load() {
        if (!configFile.exists()) {
            save()
            return
        }
        try {
            config = gson.fromJson(configFile.readText(), ProximityConfig::class.java) ?: ProximityConfig()
        } catch (e: Exception) {
            ProximityTextChat.LOGGER.error("Failed to load config, using defaults", e)
            config = ProximityConfig()
        }
    }

    fun save() {
        configFile.parentFile?.mkdirs()
        configFile.writeText(gson.toJson(config))
    }
}