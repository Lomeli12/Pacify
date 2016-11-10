package net.lomeli.pacify.commands;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.pacify.Pacify;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class CommandRemove extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args == null || args.length < 1) {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (player == null) {
                sender.addChatMessage(new TextComponentTranslation("command.pacify.not_player"));
                return;
            } else {
                sender.addChatMessage(new TextComponentTranslation("command.pacify.remove.one", player.getName()));
                Pacify.setPacifiedState(player, false);
            }
        } else {
            List<EntityPlayerMP> playerList = Lists.newArrayList();
            for (String name : args) {
                if (Strings.isNullOrEmpty(name)) continue;
                EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(name);
                if (player == null) {
                    sender.addChatMessage(new TextComponentTranslation("command.pacify.invalid_player", name));
                    return;
                } else if (!Pacify.isPlayerPacified(player)) {
                    sender.addChatMessage(new TextComponentTranslation("command.pacify.remove.already", player.getName()));
                    return;
                }
                playerList.add(player);
            }
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < playerList.size(); i++) {
                EntityPlayerMP player = playerList.get(i);
                out.append(I18n.translateToLocalFormatted(i == 0 ? "command.pacify.remove.one" : "command.pacify.followups", player.getName()));
                Pacify.setPacifiedState(player, false);
            }
            sender.addChatMessage(new TextComponentString(out.toString()));
        }
    }

    @Override
    public String getCommandName() {
        return "remove";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return I18n.translateToLocalFormatted("command.pacify.sub.usage", Pacify.MOD_ID, getCommandName());
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
