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
package org.spongepowered.common.data.processor.data.entity;

import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableSittingData;
import org.spongepowered.api.data.manipulator.mutable.entity.SittingData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeSittingData;
import org.spongepowered.common.data.processor.common.AbstractEntitySingleDataProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;

import java.util.Optional;

public class SittingDataProcessor extends AbstractEntitySingleDataProcessor<EntityTameable, Boolean, Value<Boolean>, SittingData, ImmutableSittingData> {

    public SittingDataProcessor() {
        super(EntityTameable.class, Keys.IS_SITTING);
    }

    @Override
    protected boolean set(EntityTameable entity, Boolean value) {
        entity.setSitting(value);
        return true;
    }

    @Override
    protected Optional<Boolean> getVal(EntityTameable entity) {
        return Optional.of(entity.isSitting());
    }

    @Override
    protected ImmutableValue<Boolean> constructImmutableValue(Boolean value) {
        return ImmutableSpongeValue.cachedOf(Keys.IS_SITTING, false, value);
    }

    @Override
    protected SittingData createManipulator() {
        return new SpongeSittingData();
    }

    @Override
    public DataTransactionResult remove(DataHolder dataHolder) {
        return DataTransactionBuilder.failNoData();
    }
}
