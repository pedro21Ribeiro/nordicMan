package net.sajoibam.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;

public abstract class Command {
    public static final String PREFIX = "!";
    private String commandName;
    private String commandDescription;
    private boolean requiresOwner;
    private String[] permissions;

    public Command(String commandName, String commandDescription, boolean requiresOwner, String[] permissions) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.requiresOwner = requiresOwner;
        this.permissions = permissions;
    }

    //Comando que precisa que seja owner
    public Command(String commandName, String commandDescription, boolean requiresOwner) {
        this(commandName, commandDescription, requiresOwner, null);
    }
    //Comando que precisa de permissões
    public Command(String commandName, String commandDescription, String[] permissions) {
        this(commandName, commandDescription, false, permissions);
    }

    //Comando universal
    public Command(String commandName, String commandDescription) {
        this(commandName, commandDescription, false);
    }

    public abstract void execute(MessageCreateEvent event, String[] args);

    public abstract String help();

    public String getCommandName() {
        return commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public boolean isRequiresOwner() {
        return requiresOwner;
    }

    public String[] getPermissions() {
        return permissions;
    }

}
