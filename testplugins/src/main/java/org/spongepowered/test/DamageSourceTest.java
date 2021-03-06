/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.test;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Plugin(id = "damagesourcetest", name = "Damage Source Test", description = "A plugin to test damage sources")
public final class DamageSourceTest {

    private final Set<UUID> activated = new HashSet<>();

    @Listener
    public void onInit(GameInitializationEvent event) {
        Sponge.getCommandManager().register(this,
                CommandSpec.builder()
                        .executor((src, args) -> {
                            if (src instanceof Player) {
                                final UUID uuid = ((Player) src).getUniqueId();
                                if (this.activated.contains(uuid)) {
                                    this.activated.remove(uuid);
                                    src.sendMessage(Text.of("You have deactivated damage source analysis."));
                                } else {
                                    this.activated.add(uuid);
                                    src.sendMessage(Text.of("You have activated damage source analysis."));
                                }
                                return CommandResult.success();
                            }
                            throw new CommandException(Text.of(TextColors.RED, "You must be a player to execute this command!"));
                        })
                        .build(),
                "dstest");

        final DamageSource damageSource = DamageSource.builder()
                .type(DamageTypes.CUSTOM)
                .exhaustion(5)
                .scalesWithDifficulty()
                .build();

        Sponge.getCommandManager().register(this,
                CommandSpec.builder()
                        .executor((src, args) -> {
                            if (src instanceof Player) {
                                final Player player = (Player) src;
                                player.damage(args.<Double>getOne("damage").orElse(2.0), damageSource);
                                player.sendMessage(Text.of("You have damaged yourself with the custom damage source."));
                                return CommandResult.success();
                            }
                            throw new CommandException(Text.of(TextColors.RED, "You must be a player to execute this command!"));
                        })
                        .arguments(GenericArguments.doubleNum(Text.of("damage")))
                        .build(),
                "dsdamage");
    }

    @Listener(order = Order.POST)
    public void onPlayerDamage(DamageEntityEvent event, @Getter("getTargetEntity") Player player, @Root DamageSource source) {
        player.sendMessage(Text.of(TextColors.BLUE, "You have been damaged by the following source for " + event.getFinalDamage()));
        player.sendMessage(Text.of(TextColors.GOLD, "======================================="));
        player.sendMessage(Text.of(TextColors.GOLD, "Damage type: ", TextColors.GRAY, source.getType().getName()));
        player.sendMessage(Text.of(TextColors.GOLD, "Affects creative: ", TextColors.GRAY, source.doesAffectCreative()));
        player.sendMessage(Text.of(TextColors.GOLD, "Exhaustion: ", TextColors.GRAY, source.getExhaustion()));
        player.sendMessage(Text.of(TextColors.GOLD, "Absolute: ", TextColors.GRAY, source.isAbsolute()));
        player.sendMessage(Text.of(TextColors.GOLD, "Bypassing armor: ", TextColors.GRAY, source.isBypassingArmor()));
        player.sendMessage(Text.of(TextColors.GOLD, "Explosive: ", TextColors.GRAY, source.isExplosive()));
        player.sendMessage(Text.of(TextColors.GOLD, "Magic: ", TextColors.GRAY, source.isMagic()));
        player.sendMessage(Text.of(TextColors.GOLD, "Scaled by difficulty: ", TextColors.GRAY, source.isScaledByDifficulty()));
    }

}
