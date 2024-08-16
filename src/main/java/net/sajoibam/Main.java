package net.sajoibam;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import net.sajoibam.Secrets;
import net.sajoibam.commands.Command;
import net.sajoibam.commands.CommandBus;
import net.sajoibam.commands.CommandInitializer;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //Padrões do DS
        DiscordClient client = DiscordClient.create(Secrets.getToken());
        GatewayDiscordClient gateway = client.login().block();

        //Classe inicializadora dos comandos, populando o CommandBus
        CommandInitializer.init();
        //CommandBus onde todos os comandos são armazenados
        CommandBus bus = CommandBus.getInstance();

        //Monitorador de menssagens
        gateway.getEventDispatcher().on(MessageCreateEvent.class)
                        .subscribe(event -> {
                            //Pegando o conteudo da mensagem
                            final String message = event.getMessage().getContent();

                            //Confirmando se a msg é um comando
                            if (message.startsWith(Command.PREFIX)) {
                                //Separando o comando de seus argumentos
                                String[] argumentos = message.split(" ");
                                //Buscando se o comando existe
                                Command command = bus.getCommand(argumentos[0].substring(1));
                                if (command != null) {
                                    //Caso exista executando os comandos
                                    command.execute(event, Arrays.copyOfRange(argumentos, 1, argumentos.length));
                                    //O bom de usar a biblioteca array, é que aparentemente caso não exista mais argumentos
                                    //ele simplesmente retorna um array null
                                }
                            }
                        });

        gateway.onDisconnect().block();
    }
}
