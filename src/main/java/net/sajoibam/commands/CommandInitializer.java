package net.sajoibam.commands;

import net.sajoibam.commands.commands.CantoDaAmizade;
import net.sajoibam.commands.commands.Ping;

public class CommandInitializer {

    //Inicializando os comandos
    public static void init(){
        CommandBus bus = CommandBus.getInstance();

        bus.registerCommand(new Ping());
        bus.registerCommand(new CantoDaAmizade());
    }
}
