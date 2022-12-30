package ch.mixin.islandgenerator.main;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FinishGenerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String name;

    public FinishGenerEvent(String name) {
        this.name = name;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getWorldName() {
        return name;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
