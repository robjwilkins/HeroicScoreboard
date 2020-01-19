package com.dbsoftwares.spigot.scoreboard.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import com.dbsoftwares.spigot.scoreboard.HeroicScoreboard;
import com.dbsoftwares.spigot.scoreboard.board.Scoreboard;
import com.dbsoftwares.spigot.scoreboard.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.concurrent.ThreadPoolExecutor;

@CommandAlias("heroicscoreboard|hs|heroicsb|hsb")
public class HeroicScoreboardCommand extends BaseCommand
{

    @HelpCommand
    public static void onHelp( CommandSender sender, CommandHelp help )
    {
        sender.sendMessage( Utils.c( "&4&lHeroicScoreboard &8» &6Help Reference:" ) );
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("heroicscoreboard.reload")
    public void onReload( final CommandSender sender )
    {
        HeroicScoreboard.getInstance().load( true );
        sender.sendMessage( Utils.c( "&4&lHeroicScoreboard &8» &6The configuration has been reloaded successfully!" ) );
    }

    @Subcommand("stats")
    @CommandPermission("heroicscoreboard.stats")
    public void onStats( final CommandSender sender )
    {
        final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Scoreboard.THREAD_POOL;

        if ( threadPool == null )
        {
            sender.sendMessage( Utils.c( "&4&lHeroicScoreboard &8» &6Async or thread pool is not enabled!" ) );
            return;
        }

        sender.sendMessage( Utils.c( "&4&lHeroicScoreboard &8» &6Thread Pool Statistics:" ) );
        sender.sendMessage( Utils.c( "&eActive Threads: &b" + threadPool.getActiveCount() ) );
        sender.sendMessage( Utils.c( "&eAvailable Threads: &b" + threadPool.getPoolSize() ) );
        sender.sendMessage( Utils.c( "&eMax Threads: &b" + threadPool.getMaximumPoolSize() ) );
        sender.sendMessage( Utils.c( "&eActive Tasks: &b" + (threadPool.getTaskCount() - threadPool.getCompletedTaskCount()) ) );
        sender.sendMessage( Utils.c( "&eCompleted Tasks: &b" + threadPool.getCompletedTaskCount() ) );
        sender.sendMessage( Utils.c( "&eTotal Tasks: &b" + threadPool.getTaskCount() ) );
    }
}
