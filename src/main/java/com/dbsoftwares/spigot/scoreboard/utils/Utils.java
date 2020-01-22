package com.dbsoftwares.spigot.scoreboard.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Utils
{

    public static String getVersionName()
    {
        return Bukkit.getServer().getClass().getPackage().getName().substring( 23 );
    }

    public static String format( final Player player, String message )
    {
        message = setPlaceHolders( player, message );
        message = ChatColor.translateAlternateColorCodes( '&', message );

        return message;
    }

    public static String setPlaceHolders( final Player player, String message )
    {
        if ( Bukkit.getPluginManager().isPluginEnabled( "MVdWPlaceholderAPI" ) )
        {
            message = be.maximvdw.placeholderapi.PlaceholderAPI.replacePlaceholders( player, message );
        }
        if ( Bukkit.getPluginManager().isPluginEnabled( "PlaceholderAPI" ) )
        {
            message = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders( (OfflinePlayer) player, message );
        }
        if ( Bukkit.getPluginManager().isPluginEnabled( "CentrixCore" ) )
        {
            final com.dbsoftwares.ccoreapi.user.User user = com.dbsoftwares.ccoreapi.CCore.getApi().getUser( player.getName() ).orElse( null );

            message = com.dbsoftwares.ccoreapi.placeholders.PlaceHolderAPI.formatMessage( user, message );
        }
        return message;
    }

    public static String c( final String str )
    {
        return ChatColor.translateAlternateColorCodes( '&', str );
    }

    public static String multiplyCharacters( final char character, final int times )
    {
        final StringBuilder builder = new StringBuilder();

        for ( int i = 0; i < times; i++ )
        {
            builder.append( character );
        }

        return builder.toString();
    }

    public static String optionalSubString( final String str, final int maxLength )
    {
        if ( str.length() > maxLength )
        {
            return str.substring( 0, maxLength );
        }
        return str;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T valueOfOr( final String name, T def )
    {
        assert def != null : "Default value cannot be null.";

        return valueOfOr( (Class<T>) def.getClass(), name, def );
    }

    public static <T extends Enum<T>> T valueOfOr( final Class<T> clazz, final String name, T def )
    {
        try
        {
            T value = Enum.valueOf( clazz, name );

            return value == null ? def : value;
        }
        catch ( IllegalArgumentException e )
        {
            return def;
        }
    }
}
