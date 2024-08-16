package net.sajoibam.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandBus {
    private static CommandBus instance;
    private Map<String, Command> commands;


    private CommandBus(){
        commands = new HashMap<String, Command>();
    }

    public static CommandBus getInstance(){
        if(instance == null){
            instance = new CommandBus();
        }

        return instance;
    }

    public void registerCommand(Command command){
        commands.put(command.getCommandName(), command);
    }

    public Command getCommand(String commandName){
        return commands.get(commandName);
    }
}
