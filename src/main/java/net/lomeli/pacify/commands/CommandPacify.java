package net.lomeli.pacify.commands;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.List;

import net.lomeli.pacify.Pacify;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class CommandPacify extends CommandBase {
    List<String> commandNames;
    List<CommandBase> modCommands;

    public CommandPacify() {
        super();
        commandNames = Lists.newArrayList();
        modCommands = Lists.newArrayList();
        modCommands.add(new CommandAdd());
        modCommands.add(new CommandRemove());
        modCommands.add(new CommandStatus());
        for (CommandBase cmd : modCommands)
            commandNames.add(cmd.getCommandName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender != null) {
            if (args != null && args.length >= 1) {
                boolean helpFlag = false;
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                    sender.addChatMessage(new TextComponentTranslation(getCommandUsage(sender)));
                    helpFlag = true;
                }
                for (CommandBase commandBase : modCommands) {
                    if (helpFlag)
                        sender.addChatMessage(new TextComponentTranslation(String.format("/%s %s", getCommandName(), commandBase.getCommandName())));
                    else if (commandBase.getCommandName().equalsIgnoreCase(args[0]) && commandBase.checkPermission(server, sender)) {
                        String[] newArgs = new String[args.length - 1];
                        for (int i = 0; i < newArgs.length; i++)
                            newArgs[i] = args[i + 1];
                        commandBase.execute(server, sender, newArgs);
                    }
                }
            } else sender.addChatMessage(new TextComponentTranslation(getCommandUsage(sender)));
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (sender != null && args != null) {
            if (args.length == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commandNames);
            else if (args.length >= 2) {
                for (CommandBase command : modCommands) {
                    if (command.getCommandName().equalsIgnoreCase(args[0]))
                        return command.getTabCompletionOptions(server, sender, args, pos);
                }
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandName() {
        return Pacify.MOD_ID;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return I18n.translateToLocalFormatted("command.pacify.base.usage", getCommandName());
    }
}
