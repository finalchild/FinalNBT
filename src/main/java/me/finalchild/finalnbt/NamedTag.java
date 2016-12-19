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
import me.finalchild.finalnbt.type.Compound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A value with a name.
 * @param <T> The value's type.
 */
public class NamedTag<T> {

    private final String name;
    private final T value;

    public NamedTag(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    /**
     * Read a NamedTag with a standard tag type from the DataInputStream.
     * @param stream The stream to read from. A GZIPped stream is recommended.
     * @param <T> A standard tag type.
     * @return A NamedTag with a standard tag type.
     * @throws IOException IOException.
     */
    public static <T> NamedTag<T> read(DataInputStream stream) throws IOException {
        TagType type = TagType.read(stream);
        assert type != null;
        String name;
        if (type == TagType.END) {
            name = "";
        } else {
            name = stream.readUTF();
        }
        T value = (T) type.readValue(stream);
        return new NamedTag<>(name, value);
    }

    /**
     * Read a NamedTag with a non-standard tag type(custom class) from the DataInputStream.
     * @param stream The stream to read from. A GZIPped stream is recommended.
     * @param c A non-standard tag type.
     * @param <T> A non-standard tag type.
     * @return A NamedTag with a non-standard tag type.
     * @throws IOException IOException.
     */
    public static <T> NamedTag<T> read(DataInputStream stream, Class<T> c) throws IOException {
        NamedTag<Compound> tag = NamedTag.read(stream);
        return new NamedTag<>(tag.getName(), FinalNBT.deserialize(tag.getValue(), c));
    }

    /**
     * Write this NameTag with a standard tag type to the DataOutputStream.
     * @param stream The stream to write to. A GZIPped stream is recommended.
     * @throws IOException IOException.
     */
    public void write(DataOutputStream stream) throws IOException {
        TagType type = TagType.fromValue(getValue());
        if (type == null) {
            throw new UnsupportedTypeException(getValue().getClass());
        }
        type.write(stream);
        if (type != TagType.END) {
            stream.writeUTF(getName());
        }
        type.writeValue(stream, getValue());
    }

    /**
     * Write this NameTag with a non-standard tag type(custom class) to the DataOutputStream.
     * @param stream The stream to write to. A GZIPped stream is recommended.
     * @param c A non-standard tag type.
     * @throws IOException IOException.
     */
    public void write(DataOutputStream stream, Class<T> c) throws IOException {
        NamedTag<Compound> tag = new NamedTag<>(getName(), FinalNBT.serialize(getValue(), c));
        tag.write(stream);
    }

}
