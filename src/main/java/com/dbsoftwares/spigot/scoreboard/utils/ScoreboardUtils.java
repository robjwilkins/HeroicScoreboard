package com.dbsoftwares.spigot.scoreboard.utils;

import com.dbsoftwares.spigot.scoreboard.HeroicScoreboard;
import com.dbsoftwares.spigot.scoreboard.conditions.ConditionHandler;
import com.dbsoftwares.spigot.scoreboard.config.ScoreboardConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardUtils
{

    private static String[] colorList = { "§0§r", "§1§r", "§2§r", "§3§r", "§4§r",
            "§5§r", "§6§r", "§7§r", "§8§r", "§9§r", "§a§r", "§b§r", "§c§r", "§d§r", "§e§r", "§f§r" };

    public static ScoreboardConfiguration getScoreboardConfigurationFor( final Player player )
    {
        final List<ScoreboardConfiguration> configs = HeroicScoreboard.getInstance().getScoreboardConfigurations()
                .stream()
                .filter( config -> !config.getCondition().equals( "default" ) )
                .collect( Collectors.toList() );

        for ( ScoreboardConfiguration config : configs )
        {
            final ConditionHandler handler = config.getConditionHandler();

            if ( handler.checkCondition( player ) )
            {
                return config;
            }
        }

        final List<ScoreboardConfiguration> defaultConfigs = HeroicScoreboard.getInstance().getScoreboardConfigurations()
                .stream()
                .filter( config -> config.getCondition().equals( "default" ) )
                .collect( Collectors.toList() );

        return defaultConfigs.isEmpty() ? null : MathUtils.getRandomFromList( defaultConfigs );
    }


    public static String[] splitString( final int line, final String str, final ServerVersion version )
    {
        final String[] splitten = new String[3];
        splitten[0] = "";
        splitten[1] = "";
        splitten[2] = "";

        if ( version.isNewerThan( ServerVersion.MINECRAFT_1_13 ) || str.length() < 16 )
        {
            splitten[0] = str;
            splitten[1] = colorList[line];
            return splitten;
        }

        String prefix = str.substring( 0, 16 );
        String suffix = str.substring( 16 );
        String lastColors = ChatColor.getLastColors( prefix );

        if ( prefix.endsWith( String.valueOf( ChatColor.COLOR_CHAR ) ) )
        {
            prefix = prefix.substring( 0, 15 );
            suffix = ChatColor.COLOR_CHAR + suffix;
        }
        if ( !lastColors.isEmpty() )
        {
            // str ends with color char but there is no follow up, so we have to move it to the next str.
            suffix = lastColors + suffix;
        }

        if ( suffix.length() > 16 )
        {
            suffix = suffix.substring( 0, 16 );
        }

        splitten[0] = prefix;
        splitten[1] = colorList[line];
        splitten[2] = suffix;
        return splitten;
    }
}
