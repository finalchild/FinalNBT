/*
 * This file is part of finalnbt, licensed under the MIT License (MIT).
 *
 * Copyright (c) Final Child <https://finalchild.me>
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

package me.finalchild.finalnbt.serialize;

import me.finalchild.finalnbt.type.Compound;

/**
 * A serializer/deserializer that serializes/deserializes a type.
 * @param <T> The type to serialize/deserialize.
 */
public interface NBTSerializer<T> {

    /**
     * Serialize the object into a NBT compound tag.
     * @param object The object to serialize.
     * @return A NBT compound tag.
     */
    Compound serialize(T object);

    /**
     * Deserialize the NBT compound tag into an object.
     * @param nbt The NBT compound tag to deserialize.
     * @return An object.
     */
    T deserialize(Compound nbt);

}
