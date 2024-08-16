package net.sajoibam.commands.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import net.sajoibam.commands.Command;
import net.sajoibam.commands.CommandBus;

public class Ping extends Command {

    public Ping() {
        super(
                "ping",
                "Comando teste de bot"
        );
    }

    @Override
    public void execute(MessageCreateEvent event, String[] args){
        event.getMessage().getChannel()
                .block().createMessage("Pong!")
                .block();
    }

    @Override
    public String help(){
        return "Esse é um comando de teste para o bot, não tem funcionalidade real";
    }
}
