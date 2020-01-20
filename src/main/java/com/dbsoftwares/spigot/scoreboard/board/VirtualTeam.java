package com.dbsoftwares.spigot.scoreboard.board;

import com.dbsoftwares.spigot.scoreboard.packetwrappers.WrapperPlayServerScoreboardTeam;
import com.dbsoftwares.spigot.scoreboard.utils.PacketUtils;
import com.dbsoftwares.spigot.scoreboard.utils.ScoreboardUtils;
import com.dbsoftwares.spigot.scoreboard.utils.ServerVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VirtualTeam
{

    private final int line;
    private final ServerVersion version;
    private final String name;
    private String prefix;
    private String suffix;
    private String currentPlayer;
    private String oldPlayer;

    private boolean prefixChanged, suffixChanged, playerChanged = false;
    private boolean first = true;

    private VirtualTeam( final int line, final ServerVersion version, final String name, final String prefix, final String suffix )
    {
        this.line = line;
        this.version = version;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public VirtualTeam( final int line, final ServerVersion version, final String name )
    {
        this( line, version, name, "", "" );
    }

    public String getName()
    {
        return name;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix( String prefix )
    {
        if ( this.prefix == null || !this.prefix.equals( prefix ) )
        {
            this.prefixChanged = true;
        }
        this.prefix = prefix;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix( String suffix )
    {
        if ( this.suffix == null || !this.suffix.equals( prefix ) )
        {
            this.suffixChanged = true;
        }
        this.suffix = suffix;
    }

    private WrapperPlayServerScoreboardTeam createPacket( int mode )
    {
        boolean newChat = version.isNewerThan( ServerVersion.MINECRAFT_1_13 );

        return PacketUtils.createTeamPacket(
                name,
                (byte) mode,
                /* newChat ? ComponentSerializer.toString( TextComponent.fromLegacyText( name ) ) : */ name,
                (byte) 0,
                "never",
                "always",
                0,
                /* newChat ? ComponentSerializer.toString( TextComponent.fromLegacyText( prefix ) ) : */ prefix,
                /* newChat ? ComponentSerializer.toString( TextComponent.fromLegacyText( suffix ) ) : */ suffix,
                new String[0]
        );
    }

    public WrapperPlayServerScoreboardTeam createTeam()
    {
        return createPacket( 0 );
    }

    public WrapperPlayServerScoreboardTeam updateTeam()
    {
        return createPacket( 2 );
    }

    public WrapperPlayServerScoreboardTeam removeTeam()
    {
        final WrapperPlayServerScoreboardTeam team = PacketUtils.createRemoveTeamPacket( name, (byte) 1 );

        first = true;
        return team;
    }

    public void setPlayer( String name )
    {
        if ( this.currentPlayer == null || !this.currentPlayer.equals( name ) )
        {
            this.playerChanged = true;
        }
        this.oldPlayer = this.currentPlayer;
        this.currentPlayer = name;
    }

    public Iterable<WrapperPlayServerScoreboardTeam> sendLine()
    {
        final List<WrapperPlayServerScoreboardTeam> packets = new ArrayList<>();

        if ( first )
        {
            packets.add( createTeam() );
        }
        else if ( prefixChanged || suffixChanged )
        {
            packets.add( updateTeam() );
        }

        if ( first || playerChanged )
        {
            if ( oldPlayer != null )
            {
                packets.add( addOrRemovePlayer( 4, oldPlayer ) );
            }
            packets.add( changePlayer() );
        }

        if ( first )
        {
            first = false;
        }

        return packets;
    }

    public void reset()
    {
        prefixChanged = false;
        suffixChanged = false;
        playerChanged = false;
        oldPlayer = null;
    }

    public WrapperPlayServerScoreboardTeam changePlayer()
    {
        return addOrRemovePlayer( 3, currentPlayer );
    }

    public WrapperPlayServerScoreboardTeam addOrRemovePlayer( int mode, String playerName )
    {
        return PacketUtils.createTeamPlayersPacket( name, (byte) mode, new String[]{ playerName } );
    }

    public String getCurrentPlayer()
    {
        return currentPlayer;
    }

    public String getValue()
    {
        return getPrefix() + getCurrentPlayer() + getSuffix();
    }

    public void setValue( String value )
    {
        final String[] splitten = ScoreboardUtils.splitString( line, value, version );

        setPrefix( splitten[0] );
        setPlayer( splitten[1] );
        setSuffix( splitten[2] );
    }
}
