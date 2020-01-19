package com.dbsoftwares.spigot.scoreboard.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScoreboardLine extends ScoreboardIterable
{

    public ScoreboardLine( int interval, List<String> lines )
    {
        super( interval, lines );
    }

    public ScoreboardLine copy()
    {
        return new ScoreboardLine( this.getInterval(), this.getLines() );
    }
}
