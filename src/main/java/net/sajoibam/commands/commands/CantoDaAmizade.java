package net.sajoibam.commands.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.spec.GuildMemberEditMono;
import discord4j.core.spec.GuildMemberEditSpec;
import net.sajoibam.commands.Command;
import net.sajoibam.utils.ListaAmizade;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class CantoDaAmizade extends Command {
    private ListaAmizade listinha = ListaAmizade.getInstance();

    public CantoDaAmizade() {
        super("amizade", "Coloca duas pessoas para fazer as pazes", new String[] { "ALMIRANTE" });
    }

    @Override
    public void execute(MessageCreateEvent event, String[] args) {
        Optional<Member> pChanel = event.getMember();

        if(pChanel.isEmpty()) {
            sendMessage(event, "Comando nao pode ser feito em canais privados");
            return;
        }

        Member member = pChanel.get();

        if(!hasPermissions(member)){
            sendMessage(event, "Você nao tem o cargo nescessario para esse comando");
            return;
        }

        if (args.length < 2) {
            sendMessage(event, "Siga o formato `!amizade pesssoa1 pessoa2`");
            return;
        }

        Mono<Guild> guild = event.getGuild();
        if(guild.blockOptional().isEmpty()){
            sendMessage(event, "Nao foi possivel encontrar uma guild");
            return;
        }

        guild.subscribe( server -> {
            String pessoa1 = args[0];
            String pessoa2 = args[1];

            if(pessoa1.startsWith("<@") && pessoa1.endsWith(">")){
                String userIdStr = pessoa1.substring(2, pessoa1.length()-1);
                Snowflake userId = Snowflake.of(userIdStr);

                GatewayDiscordClient client = server.getClient();
                User temp = client.getUserById(userId).block();
                if(temp == null){
                    pessoa1 = "foobar";
                }else{
                    pessoa1 = temp.getUsername();
                }
            }

            if(pessoa2.startsWith("<@") && pessoa2.endsWith(">")){
                String userIdStr = pessoa2.substring(2, pessoa2.length()-1);
                Snowflake userId = Snowflake.of(userIdStr);

                GatewayDiscordClient client = server.getClient();
                User temp = client.getUserById(userId).block();
                if(temp == null){
                    pessoa2 = "foobar";
                }else{
                    pessoa2 = temp.getUsername();
                }
            }

            Member membro1 = server.searchMembers(pessoa1, 1).blockFirst();

            if(membro1 == null){
                sendMessage(event,"Usuario " + pessoa1 + " nao encontrado");
                return;
            }

            Member membro2 = server.searchMembers(pessoa2, 1).blockFirst();

            if(membro2 == null){
                sendMessage(event,"Usuario " + pessoa2 + " nao encontrado");
                return;
            }

            if(listinha.jaAdicionado(pessoa1)){
                sendMessage(event, pessoa1 + " ja esta de mau com outra pessoa");
                return;
            }
            if(listinha.jaAdicionado(pessoa2)){
                sendMessage(event, pessoa2 + "ja esta de mau com outra pessoa");
                return;
            }

            VoiceState canal1 = membro1.getVoiceState().block();
            if(canal1 == null){
                sendMessage(event, membro1.getUsername() + " nao esta em canal de voz");
                return;
            }
            VoiceState canal2 = membro2.getVoiceState().block();
            if(canal2 == null){
                sendMessage(event, membro2.getUsername() + " nao esta em canal de voz");
                return;
            }

            sendMessage(event, "continuar logica");

            Snowflake amizade = Snowflake.of("1274029579518083265");

            GuildMemberEditSpec editor = GuildMemberEditSpec.create();

            membro1.edit(spec -> {
                spec.setNewVoiceChannel(amizade);
            });

            listinha.addLista(pessoa1, pessoa2);
        });
    }

    @Override
    public String help(){
        return null;
    }

    private void sendMessage(MessageCreateEvent event, String message){
        event.getMessage().getChannel()
                .block().createMessage(message)
                .block();
    }

    private boolean hasPermissions(Member member) {
        Flux<String> roles = member.getRoles().map(Role::getName);
        Flux<String> requiredRoles = Flux.fromArray(getPermissions());

        Mono<Boolean> hasPermissions =  requiredRoles
                .all(requiredRoleName ->
                        roles.any(userRoleName -> userRoleName.equalsIgnoreCase(requiredRoleName)).block()
                );

        hasPermissions.defaultIfEmpty(false).block();
        return Boolean.TRUE.equals(hasPermissions.block());
    }
}
