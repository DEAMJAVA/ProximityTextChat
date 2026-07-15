package net.deamjava.proximity_text_chat.mixin;

import net.deamjava.proximity_text_chat.ProximityTextChatConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.Team.Visibility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Redirect(
            method = "die",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V",
                    ordinal = 0
            )
    )
    private void proximityDeathMessage(PlayerList playerList, Component deathMessage, boolean overlay) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        double range = ProximityTextChatConfig.INSTANCE.getConfig().getDeathMessageRange();

        Team team = self.getTeam();

        for (ServerPlayer recipient : playerList.getPlayers()) {
            if (team != null) {
                Visibility vis = team.getDeathMessageVisibility();
                if (vis == Visibility.HIDE_FOR_OTHER_TEAMS && recipient.getTeam() != team) continue;
                if (vis == Visibility.HIDE_FOR_OWN_TEAM && recipient.getTeam() == team) continue;
            }

            if (recipient.getUUID().equals(self.getUUID())) {
                recipient.sendSystemMessage(deathMessage);
                continue;
            }

            if (recipient.level().dimension() != self.level().dimension()) continue;

            double dx = self.getX() - recipient.getX();
            double dy = self.getY() - recipient.getY();
            double dz = self.getZ() - recipient.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq <= range * range) {
                recipient.sendSystemMessage(deathMessage);
            }
        }
    }
}