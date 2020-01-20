package com.dbsoftwares.spigot.scoreboard.config;

import com.dbsoftwares.spigot.scoreboard.utils.Utils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ScoreboardIterable
{

    private final int interval;
    private final List<String> lines;

    private int idx = 0;
    private int stayTime;

    public void reduceStayTime()
    {
        if ( stayTime > 0 )
        {
            stayTime--;
        }
    }

    public boolean canRun()
    {
        return interval > 0 && stayTime <= 0;
    }

    public String next( final Player player )
    {
        if ( lines.isEmpty() )
        {
            return "";
        }
        if ( idx >= lines.size() )
        {
            idx = 0;
        }
        stayTime = interval;

        return Utils.format( player, lines.get( idx++ ) );
    }
}
