package com.buuz135.togetherforever.command;

import com.buuz135.togetherforever.api.*;
import com.buuz135.togetherforever.api.command.SubCommandAction;
import com.buuz135.togetherforever.api.data.DefaultPlayerInformation;
import com.buuz135.togetherforever.api.data.TogetherRegistries;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ForceSyncCommand extends SubCommandAction {

    public ForceSyncCommand() {
        super("forcesync");
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] args) {
        try {
            EntityPlayerMP senderPlayer = CommandBase.getCommandSenderAsPlayer(sender);
            ITogetherTeam togetherTeam = TogetherForeverAPI.getInstance().getPlayerTeam(senderPlayer.getUniqueID());
            if (togetherTeam != null) {
                for (IPlayerInformation playerInformation : togetherTeam.getPlayers()) {
                    EntityPlayerMP playerMP = playerInformation.getPlayer();
                    if (playerMP != null && !senderPlayer.getUniqueID().equals(playerMP.getUniqueID())) {
                        sender.sendMessage(new TextComponentString(TextFormatting.GOLD + "Trying to sync data from " + playerMP.getName() + " please don't hurt yourself in the process!"));
                        for (ISyncAction<?, ? extends IOfflineSyncRecovery> action : TogetherRegistries.getSyncActions()) {
                            action.syncJoinPlayer(DefaultPlayerInformation.createInformation(senderPlayer), playerInformation);
                        }
                    }
                }
                return true;
            }
        } catch (PlayerNotFoundException e) {
            sender.sendMessage(new TextComponentTranslation(e.getLocalizedMessage(), e.getErrorObjects()));
        }
        return false;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInfo() {
        return "Forces a sync from another player of the team";
    }
}
