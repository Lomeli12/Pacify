package net.lomeli.pacify.commands;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.pacify.Pacify;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandStatus extends CommandBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args == null || args.length < 1) {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            if (player == null) {
                sender.addChatMessage(new TextComponentTranslation("command.pacify.not_player"));
                return;
            } else
                sender.addChatMessage(new TextComponentTranslation(Pacify.isPlayerPacified(player) ? "command.pacify.status.true" : "command.pacify.status.false", player.getName()));
        } else {
            List<EntityPlayerMP> playerList = Lists.newArrayList();
            List<String> pacified = Lists.newArrayList();
            List<String> armed = Lists.newArrayList();
            for (String name : args) {
                if (Strings.isNullOrEmpty(name)) continue;
                EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(name);
                if (player == null) {
                    sender.addChatMessage(new TextComponentTranslation("command.pacify.invalid_player", name));
                    return;
                }
                playerList.add(player);
            }
            for (EntityPlayerMP playerMP : playerList) {
                if (Pacify.isPlayerPacified(playerMP)) pacified.add(playerMP.getName());
                else armed.add(playerMP.getName());
            }

            StringBuilder out = new StringBuilder();
            for (int i = 0; i < pacified.size(); i++)
                out.append(I18n.format(i == 0 ? "command.pacify.status.true" : "command.pacify.followups", pacified.get(i)));
            if (!pacified.isEmpty() && !armed.isEmpty()) out.append("\n");
            for (int i = 0; i < armed.size(); i++)
                out.append(I18n.format(i == 0 ? "command.pacify.status.false" : "command.pacify.followups", armed.get(i)));
            sender.addChatMessage(new TextComponentString(out.toString()));
        }
    }

    @Override
    public String getCommandName() {
        return "status";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return I18n.format("command.pacify.sub.usage", Pacify.MOD_ID, getCommandName());
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
