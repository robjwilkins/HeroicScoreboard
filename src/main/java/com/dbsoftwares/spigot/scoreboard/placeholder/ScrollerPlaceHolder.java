package com.dbsoftwares.spigot.scoreboard.placeholder;

import com.dbsoftwares.spigot.scoreboard.config.ScoreboardIterable;
import com.dbsoftwares.spigot.scoreboard.utils.ServerVersion;
import com.dbsoftwares.spigot.scoreboard.utils.StringReference;
import com.dbsoftwares.spigot.scoreboard.utils.Utils;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ScrollerPlaceHolder extends PlaceHolder
{

    @Override
    public boolean detect( final StringReference reference )
    {
        final String text = reference.getText();

        if ( !text.contains( "scroller" ) )
        {
            return false;
        }

        final Document document = reference.getDocument();
        final Elements elements = document.select( "scroller" );

        return elements.size() >= 1;
    }

    @Override
    public void replace( final Player player, final StringReference reference, final ScoreboardIterable iterable )
    {
        String message = reference.getText();
        final Document document = reference.getDocument();
        final Elements elements = document.select( "scroller" );

        for ( Element element : elements )
        {
            final String text = element.text();
            final int maxWidth = parseInt( element, "max-width", 24, ServerVersion.search().isNewerThan( ServerVersion.MINECRAFT_1_13 ) ? -1 : 40 );
            final int spaces = parseInt( element, "spaces", 10 );

            if ( !iterable.hasData( "SCROLLER_" + text ) )
            {
                iterable.setData( "SCROLLER_" + text, new ScrollingString( text, maxWidth, spaces ) );
            }
            final ScrollingString scroller = iterable.getData( "SCROLLER_" + text );
            scroller.scrollForward();

            message = message.replace( element.outerHtml(), scroller.getScrolled() );
        }

        reference.setText( message );
    }

    private int parseInt( final Element element, final String attr, final int def, final int max )
    {
        final int parsed = parseInt( element, attr, def );

        if ( max < 0 )
        {
            return parsed;
        }
        return Math.min( parsed, max );
    }

    private int parseInt( final Element element, final String attr, final int def )
    {
        if ( element.hasAttr( attr ) )
        {
            try
            {
                return Integer.parseInt( element.attr( attr ) );
            }
            catch ( NumberFormatException e )
            {
                return def;
            }
        }
        return def;
    }

    private static class ScrollingString
    {

        private String original;
        private int width;
        private int position;

        public ScrollingString( final String original, final int width, final int spaces )
        {
            this.original = original + Utils.multiplyCharacters( ' ', spaces );
            if ( width <= 0 )
            {
                throw new IllegalArgumentException( "Width value has to be greater than 0" );
            }
            else if ( this.original.length() < width )
            {
                throw new IllegalArgumentException( "String length has to be greater than the width value" );
            }
            this.width = width;
        }

        public void scrollForward()
        {
            position++;
            if ( position == original.length() )
            {
                reset();
            }
        }

        public void scrollBackward()
        {
            position--;
            if ( position < 0 )
            {
                position = original.length() - 1;
            }
        }

        public void reset()
        {
            position = 0;
        }

        public void append( String s )
        {
            original += s;
        }

        public String getOriginal()
        {
            return this.original;
        }

        public void setOriginal( String original )
        {
            if ( original.length() < width )
            {
                throw new IllegalArgumentException( "String length has to be greater than the width value" );
            }
            this.original = original;
            reset();
        }

        public String getScrolled()
        {
            String lastColors = "";

            if ( position > 1 )
            {
                final String previous = original.substring( 0, position );
                lastColors = ChatColor.getLastColors( previous );
            }

            int width = this.width;
            if ( !lastColors.isEmpty() )
            {
                width -= lastColors.length();
            }
            final int e = position + width;

            return e > original.length()
                    ? lastColors + original.substring( position ) + original.substring( 0, width - (original.length() - position) )
                    : lastColors + original.substring( position, e );
        }

        public int getWidth()
        {
            return this.width;
        }

        public int getPosition()
        {
            return this.position;
        }
    }
}
