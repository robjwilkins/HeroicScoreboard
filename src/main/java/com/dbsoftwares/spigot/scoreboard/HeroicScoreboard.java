package com.dbsoftwares.spigot.scoreboard;

import co.aikar.commands.PaperCommandManager;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.spigot.scoreboard.board.Scoreboard;
import com.dbsoftwares.spigot.scoreboard.commands.HeroicScoreboardCommand;
import com.dbsoftwares.spigot.scoreboard.config.ScoreboardConfiguration;
import com.dbsoftwares.spigot.scoreboard.config.ScoreboardLine;
import com.dbsoftwares.spigot.scoreboard.config.ScoreboardTitle;
import com.dbsoftwares.spigot.scoreboard.listeners.PlayerListener;
import com.dbsoftwares.spigot.scoreboard.utils.ScoreboardUtils;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HeroicScoreboard extends JavaPlugin
{
    @Getter
    private List<Scoreboard> scoreboards = Collections.synchronizedList( Lists.newArrayList() );
    @Getter
    private IConfiguration configuration;
    @Getter
    private List<ScoreboardConfiguration> scoreboardConfigurations = Lists.newArrayList();

    public static HeroicScoreboard getInstance()
    {
        return HeroicScoreboard.getPlugin( HeroicScoreboard.class );
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable()
    {
        final File config = new File( getDataFolder(), "config.yml" );

        if ( !config.exists() )
        {
            IConfiguration.createDefaultFile( getResource( "config.yml" ), config );
        }

        this.configuration = IConfiguration.loadYamlConfiguration( config );
        super.getServer().getPluginManager().registerEvents( new PlayerListener(), this );

        final PaperCommandManager commandManager = new PaperCommandManager( this );
        commandManager.enableUnstableAPI( "help" );
        commandManager.registerCommand( new HeroicScoreboardCommand() );

        load( false );
    }

    public Scoreboard getScoreboard( final Player player )
    {
        return scoreboards.stream().filter( scoreboard -> scoreboard.getPlayer().equals( player ) ).findFirst().orElse( null );
    }

    public void load( boolean reload )
    {
        try
        {
            configuration.reload();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        scoreboardConfigurations.clear();
        for ( ISection section : configuration.getSectionList( "scoreboards" ) )
        {
            final String condition = section.getString( "condition" );
            final ScoreboardTitle title = new ScoreboardTitle(
                    section.getInteger( "title.interval" ),
                    Collections.synchronizedList( section.getStringList( "title.frames" ) )
            );
            final List<ScoreboardLine> lines = Lists.newArrayList();

            for ( ISection line : section.getSectionList( "lines" ) )
            {
                lines.add( new ScoreboardLine(
                        line.getInteger( "interval" ),
                        Collections.synchronizedList( line.getStringList( "frames" ) )
                ) );
            }

            scoreboardConfigurations.add( new ScoreboardConfiguration( condition, title, lines ) );
        }

        if ( reload )
        {
            Lists.newArrayList( scoreboards ).forEach( Scoreboard::destroy );

            for ( Player player : Bukkit.getOnlinePlayers() )
            {
                final ScoreboardConfiguration config = ScoreboardUtils.getScoreboardConfigurationFor( player );

                if ( config == null )
                {
                    return;
                }

                new Scoreboard( player, config ).create();
            }
        }
    }
}
