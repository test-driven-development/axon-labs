package org.axonframework.labs.president;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateMember;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class Match {

    @AggregateIdentifier
    private String matchId;
    @AggregateMember
    private Game game;

    public Match() {
        // Required by Axon
    }

    @CommandHandler
    public Match(CreateMatchCommand command) {
        apply(new MatchCreatedEvent(command.getMatchId()));
    }

    @CommandHandler
    public void handle(JoinMatchCommand cmd) {
        apply(new MatchJoinedEvent(matchId));
    }

    @CommandHandler
    public String handle(StartMatchCommand cmd) {
        String gameId = UUID.randomUUID().toString();
        apply(new GameStartedEvent(matchId, gameId));
        return gameId;
    }

    @EventSourcingHandler
    public void on(MatchCreatedEvent event) {
        matchId = event.getMatchId();
    }

    @EventSourcingHandler
    public void on(GameStartedEvent event) {
        game = new Game(matchId, event.getGameId());
    }

}
