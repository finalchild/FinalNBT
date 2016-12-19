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

import java.util.Map;

/**
 * Represents an object that may be serialized to the NBT format.
 *
 * These objects MUST implement a static method "deserialize" that accepts a single Compound and returns the class.
 *
 * To serialize a custom object to the NBT format, you MUST either make the object implement NBTSerializble, or register a NBTSerializer of the object on FinalNBT
 */
public interface NBTSerializable {

    /**
     * Creates a Compound representation of this class.
     *
     * This class must provide a method to restore this class, as defined in the ConfigurationSerializable interface javadocs.
     * @return Compound containing the current state of this class
     */
    Compound serialize();

}
