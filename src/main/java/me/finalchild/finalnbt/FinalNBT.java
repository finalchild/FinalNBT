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

package me.finalchild.finalnbt;

import me.finalchild.finalnbt.exception.UnsupportedTypeException;
import me.finalchild.finalnbt.serialize.NBTSerializer;
import me.finalchild.finalnbt.type.Compound;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FinalNBT {

    private static Map<Class, NBTSerializer> serializers = new HashMap<>();
    private static Map<DataInputStream, Integer> depths = new HashMap<>();

    /**
     * Register an NBTSerializer.
     * @param c The type of the NBTSerializer.
     * @param serializer An NBTSerializer.
     * @param <T> The type of the NBTSerializer.
     */
    public static <T> void registerSerializer(Class<T> c, NBTSerializer<T> serializer) {
        serializers.put(c, serializer);
    }

    /**
     * Get a registered NBTSerializer given the type.
     * @param c The type of the NBTSerializer.
     * @param <T> The type of the NBTSerializer.
     * @return The registered NBTSerializer.
     */
    public static <T> NBTSerializer<T> getSerializer(Class<T> c) {
        return serializers.get(c);
    }

    /**
     * Read a named NBT compound tag from the stream using GZIP.
     * @param input The stream to read from. DO NOT put a GZIPped stream. We will do it for you.
     * @return A named NBT compound tag.
     * @throws IOException
     */
    public static NamedTag<Compound> readNBT(InputStream input) throws IOException {
        DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(input)));
        NamedTag<Compound> value = NamedTag.read(stream);
        stream.close();
        return value;
    }

    /**
     * Read an named object from the stream using GZIP.
     * @param input The stream to read from. DO NOT put a GZIPped stream. We will do it for you.
     * @param c The type of the NBT.
     * @param <T> The type of the NBT.
     * @return An named object.
     * @throws IOException IOException.
     */
    public static <T> NamedTag<T> readNBT(InputStream input, Class<T> c) throws IOException {
        DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(input)));
        NamedTag<T> value = NamedTag.read(stream, c);
        stream.close();
        return value;
    }

    /**
     * Write an NBT compopund tag to the stream using GZIP.
     * @param output The stream to write to. DO NOT put a GZIPped stream. We will do it for you.
     * @param nbt An NBT compound tag.
     * @throws IOException IOException.
     */
    public static void writeNBT(OutputStream output, NamedTag<Compound> nbt) throws IOException {
        DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(output)));
        nbt.write(stream);
        stream.close();
    }

    /**
     * Write an object to the stream using GZIP.
     * @param output The stream to write to. DO NOT put a GZIPped stream. We will do it for you.
     * @param nbt An object.
     * @param c The type of the object.
     * @param <T> The type of the object.
     * @throws IOException IOException.
     */
    public static <T> void writeNBT(OutputStream output, NamedTag<T> nbt, Class<T> c) throws IOException {
        DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(output)));
        nbt.write(stream, c);
        stream.close();
    }

    /**
     * Deserialize an NBT compound tag.
     * @param nbt NBT compound tag.
     * @param c The type of the NBT.
     * @param <T> The type of the NBT.
     * @return Deserialized object.
     */
    public static <T> T deserialize(Compound nbt, Class<T> c) {
        NBTSerializer<T> serializer = FinalNBT.getSerializer(c);
        if (serializer != null) {
            return serializer.deserialize(nbt);
        } else {
            try {
                return (T) c.getMethod("deserialize", Compound.class).invoke(null, nbt);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new UnsupportedTypeException(c);
            }
        }
    }

    /**
     * Serialize an object to an NBT compound tag.
     * @param object The object.
     * @param c The type of the object.
     * @param <T> The type of the object.
     * @return Serialized NBT compound tag.
     */
    public static <T> Compound serialize(T object, Class<T> c) {
        NBTSerializer<T> serializer = FinalNBT.getSerializer(c);
        if (serializer != null) {
            return serializer.serialize(object);
        } else {
            try {
                return (Compound) c.getMethod("serialize").invoke(object);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new UnsupportedTypeException(c);
            }
        }
    }

    /**
     * Serialize an object to an NBT tag.
     * @param object The object.
     * @param <T> The type of the object.
     * @return Serialized NBT tag. If the object is already an NBT tag, the object is returned.
     */
    public static <T> Compound serialize(T object) {
        return serialize(object, (Class<T>) object.getClass());
    }

    /**
     * Serialize an object to an NBT tag.
     * @param object The object.
     * @param <T> The type of the object.
     * @return Serialized NBT tag. If the object is already an NBT tag, the object is returned.
     */
    public static <T> Object toTag(T object) {
        return TagType.fromValue(object) != null ? object : serialize(object, (Class<T>) object.getClass());
    }

    /**
     * For internal use only.
     */
    public static int getDepth(DataInputStream stream) {
        if (depths.containsKey(stream)) {
            return depths.get(stream);
        } else {
            return 0;
        }
    }

    /**
     * For internal use only.
     */
    public static void setDepth(DataInputStream stream, int depth) {
        depths.put(stream, depth);
    }

}
