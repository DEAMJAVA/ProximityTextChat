package net.deamjava.proximity_text_chat.mixin;

import net.deamjava.proximity_text_chat.ProximityTextChatConfig;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerList.class)
public class PlayerListMixin {


    @Inject(
            method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void proximityChat(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound chatType, CallbackInfo ci) {
        ci.cancel();

        PlayerList self = (PlayerList) (Object) this;
        double range = ProximityTextChatConfig.INSTANCE.getConfig().getChatRange();

        OutgoingChatMessage tracked = OutgoingChatMessage.create(message);

        for (ServerPlayer recipient : self.getPlayers()) {
            if (recipient.getUUID().equals(sender.getUUID())) {
                boolean filtered = sender.shouldFilterMessageTo(recipient);
                recipient.sendChatMessage(tracked, filtered, chatType);
                continue;
            }

            if (recipient.level().dimension() != sender.level().dimension()) continue;

            double dx = sender.getX() - recipient.getX();
            double dy = sender.getY() - recipient.getY();
            double dz = sender.getZ() - recipient.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq <= range * range) {
                boolean filtered = sender.shouldFilterMessageTo(recipient);
                recipient.sendChatMessage(tracked, filtered, chatType);
            }
        }

        self.getServer().logChatMessage(message.decoratedContent(), chatType, null);
    }
}