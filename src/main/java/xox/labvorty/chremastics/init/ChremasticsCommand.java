package xox.labvorty.chremastics.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xox.labvorty.chremastics.data.currency.PlayerCoinPurse;

import java.util.Collection;

public class ChremasticsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("chremastics")
                        .requires(source -> source.hasPermission(4))

                        .then(Commands.literal("balance")

                                .then(Commands.literal("set")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                                                        .executes(context -> {
                                                            Collection<ServerPlayer> players =
                                                                    EntityArgument.getPlayers(
                                                                            context,
                                                                            "targets"
                                                                    );

                                                            long amount =
                                                                    LongArgumentType.getLong(
                                                                            context,
                                                                            "amount"
                                                                    );

                                                            for (ServerPlayer player : players) {
                                                                PlayerCoinPurse.setBalance(player, amount);
                                                            }

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(
                                                                            "Set balance of "
                                                                                    + players.size()
                                                                                    + " player(s) to "
                                                                                    + amount
                                                                    ),
                                                                    true
                                                            );

                                                            return players.size();
                                                        })
                                                )
                                        )
                                )

                                .then(Commands.literal("add")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("amount", LongArgumentType.longArg(1))
                                                        .executes(context -> {
                                                            Collection<ServerPlayer> players =
                                                                    EntityArgument.getPlayers(
                                                                            context,
                                                                            "targets"
                                                                    );

                                                            long amount =
                                                                    LongArgumentType.getLong(
                                                                            context,
                                                                            "amount"
                                                                    );

                                                            for (ServerPlayer player : players) {
                                                                PlayerCoinPurse.add(player, amount);
                                                            }

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(
                                                                            "Added "
                                                                                    + amount
                                                                                    + " to "
                                                                                    + players.size()
                                                                                    + " player(s)"
                                                                    ),
                                                                    true
                                                            );

                                                            return players.size();
                                                        })
                                                )
                                        )
                                )

                                .then(Commands.literal("remove")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("amount", LongArgumentType.longArg(1))
                                                        .executes(context -> {
                                                            Collection<ServerPlayer> players =
                                                                    EntityArgument.getPlayers(
                                                                            context,
                                                                            "targets"
                                                                    );

                                                            long amount =
                                                                    LongArgumentType.getLong(
                                                                            context,
                                                                            "amount"
                                                                    );

                                                            int changed = 0;

                                                            for (ServerPlayer player : players) {
                                                                if (PlayerCoinPurse.tryRemoveBalance(player, amount)) {
                                                                    changed++;
                                                                }
                                                            }
                                                            final int a = changed;

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(
                                                                            "Removed "
                                                                                    + amount
                                                                                    + " from "
                                                                                    + a
                                                                                    + " player(s)"
                                                                    ),
                                                                    true
                                                            );

                                                            return changed;
                                                        })
                                                )
                                        )
                                )
                        )
        );
    }
}