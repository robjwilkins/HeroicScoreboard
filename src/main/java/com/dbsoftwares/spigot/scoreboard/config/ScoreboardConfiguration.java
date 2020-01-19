package com.dbsoftwares.spigot.scoreboard.config;

import com.dbsoftwares.spigot.scoreboard.HeroicScoreboard;
import com.dbsoftwares.spigot.scoreboard.conditions.ConditionHandler;
import com.dbsoftwares.spigot.scoreboard.conditions.NameConditionHandler;
import com.dbsoftwares.spigot.scoreboard.conditions.PingConditionHandler;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScoreboardConfiguration
{

    private final String condition;
    private final ScoreboardTitle title;
    private final List<ScoreboardLine> lines;
    private ConditionHandler conditionHandler;

    public ScoreboardConfiguration( final String condition, final ScoreboardTitle title, final List<ScoreboardLine> lines )
    {
        this.condition = condition;
        this.title = title;
        this.lines = lines;

        if ( condition.toLowerCase().startsWith( "name" ) )
        {
            conditionHandler = new NameConditionHandler( condition );
        }
        else if ( condition.toLowerCase().startsWith( "ping" ) )
        {
            conditionHandler = new PingConditionHandler( condition );
        }
        else if ( !condition.equalsIgnoreCase( "default" ) )
        {
            HeroicScoreboard.getInstance().getLogger().warning( "An invalid scoreboard condition has been entered." );
            HeroicScoreboard.getInstance().getLogger().warning( "The available conditions are: name, ping & default" );
        }
    }

    public ScoreboardConfiguration copy()
    {
        final List<ScoreboardLine> copiedLines = lines.stream().map( ScoreboardLine::copy ).collect( Collectors.toList() );

        return new ScoreboardConfiguration( this.condition, this.title.copy(), copiedLines );
    }
}
