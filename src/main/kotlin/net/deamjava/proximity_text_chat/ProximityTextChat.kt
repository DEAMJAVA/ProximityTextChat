package net.deamjava.proximity_text_chat

import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ProximityTextChat : ModInitializer {
	const val MOD_ID = "proximity-text-chat"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		ProximityTextChatConfig.init(FabricLoader.getInstance().configDir.toFile())
		LOGGER.info("Proximity Text Chat initialized (chat range: ${ProximityTextChatConfig.config.chatRange}, death range: ${ProximityTextChatConfig.config.deathMessageRange})")
	}
}