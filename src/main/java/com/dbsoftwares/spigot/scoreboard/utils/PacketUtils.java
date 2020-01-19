package com.dbsoftwares.spigot.scoreboard.utils;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.dbsoftwares.spigot.scoreboard.packetwrappers.WrapperPlayServerScoreboardDisplayObjective;
import com.dbsoftwares.spigot.scoreboard.packetwrappers.WrapperPlayServerScoreboardObjective;
import com.dbsoftwares.spigot.scoreboard.packetwrappers.WrapperPlayServerScoreboardScore;
import com.dbsoftwares.spigot.scoreboard.packetwrappers.WrapperPlayServerScoreboardTeam;

import java.util.Arrays;

public class PacketUtils
{

    public static WrapperPlayServerScoreboardObjective createScoreboardObjectivePacket( final String name, final String value, final String type, final byte action )
    {
        final WrapperPlayServerScoreboardObjective objective = new WrapperPlayServerScoreboardObjective();

        objective.setName( name );
        objective.setMode( action );

        if ( action != 1 )
        {
            objective.setDisplayName( value );
            objective.setHealthDisplay( WrapperPlayServerScoreboardObjective.HealthDisplay.valueOf( type.toUpperCase() ) );
        }

        return objective;
    }

    public static WrapperPlayServerScoreboardDisplayObjective createScoreboardDisplayPacket( final byte position, final String name )
    {
        final WrapperPlayServerScoreboardDisplayObjective display = new WrapperPlayServerScoreboardDisplayObjective();

        display.setPosition( position );
        display.setScoreName( name );

        return display;
    }

    public static WrapperPlayServerScoreboardTeam createTeamPacket(
            final String name,
            final byte mode,
            final String displayName,
            final byte friendlyFire,
            final String nameTagVisibility,
            final String collisionRule,
            final int color,
            final String prefix,
            final String suffix,
            final String[] players
    )
    {
        final WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();

        team.setName( name );
        team.setMode( mode );

        if ( mode == 0 || mode == 2 )
        {
            team.setDisplayName( displayName );
            team.setPrefix( prefix );
            team.setSuffix( suffix );
            team.setPackOptionData( friendlyFire );
            team.setNameTagVisibility( nameTagVisibility );

            if ( ServerVersion.search().isNewerThan( ServerVersion.MINECRAFT_1_9 ) )
            {
                team.setCollisionRule( collisionRule );
            }
            team.setColor( color );
        }

        if ( mode == 0 || mode == 3 || mode == 4 )
        {
            team.setPlayers( Arrays.asList( players ) );
        }

        return team;
    }

    public static WrapperPlayServerScoreboardTeam createTeamPlayersPacket( final String name, final byte action, final String[] players )
    {
        final WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();

        team.setName( name );
        team.setMode( action );
        team.setPlayers( Arrays.asList( players ) );

        return team;
    }

    public static WrapperPlayServerScoreboardTeam createRemoveTeamPacket( final String name, final byte action )
    {
        final WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();

        team.setName( name );
        team.setMode( action );

        return team;
    }

    public static WrapperPlayServerScoreboardScore createScoreboardScorePacket( final String itemName, final byte action, final String scoreboardName, final int value )
    {
        final WrapperPlayServerScoreboardScore score = new WrapperPlayServerScoreboardScore();

        score.setScoreName( itemName );
        score.setObjectiveName( scoreboardName );
        score.setScoreboardAction( action == 0 ? EnumWrappers.ScoreboardAction.CHANGE : EnumWrappers.ScoreboardAction.REMOVE );
        score.setValue( value );

        return score;
    }
}
