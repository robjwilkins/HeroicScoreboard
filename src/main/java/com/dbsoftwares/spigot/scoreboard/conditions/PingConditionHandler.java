package com.dbsoftwares.spigot.scoreboard.conditions;

import com.dbsoftwares.spigot.scoreboard.utils.ReflectionUtils;
import org.bukkit.entity.Player;

public class PingConditionHandler extends ConditionHandler
{
    public PingConditionHandler( final String condition )
    {
        super( condition.replaceFirst( "ping ", "" ) );
    }

    @Override
    public boolean checkCondition( final Player player )
    {
        final String[] args = condition.split( " " );
        final String operator = args[0];
        final int pingArgument;

        try
        {
            pingArgument = Integer.parseInt( args[1] );
        }
        catch ( NumberFormatException e )
        {
            return false;
        }

        final int ping = getPing( player );

        switch ( operator )
        {
            case "<":
                return ping < pingArgument;
            case "<=":
                return ping <= pingArgument;
            case "==":
                return ping == pingArgument;
            case "!=":
                return ping != pingArgument;
            case ">=":
                return ping >= pingArgument;
            case ">":
                return ping > pingArgument;
            default:
                return false;
        }
    }

    private int getPing( final Player player )
    {
        try
        {
            final Object handle = ReflectionUtils.getHandle( player );
            final int ping = (int) handle.getClass().getDeclaredField( "ping" ).get( handle );

            return Math.max( ping, 0 );
        }
        catch ( IllegalAccessException | NoSuchFieldException e )
        {
            return 0;
        }
    }
}