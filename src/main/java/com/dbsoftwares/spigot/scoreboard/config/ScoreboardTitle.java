package com.dbsoftwares.spigot.scoreboard.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScoreboardTitle extends ScoreboardIterable
{

    public ScoreboardTitle( int interval, List<String> lines )
    {
        super( interval, lines );
    }

    public ScoreboardTitle copy()
    {
        return new ScoreboardTitle( this.getInterval(), this.getLines() );
    }
}
