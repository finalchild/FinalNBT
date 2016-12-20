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

import me.finalchild.finalnbt.exception.DepthException;
import me.finalchild.finalnbt.type.Compound;
import me.finalchild.finalnbt.type.TypedList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Standard NBT tag types.
 * @param <T> The type of the data.
 */
public abstract class TagType<T> {

    public static final TagType<Void> END = new TagType<Void>((byte) 0, Void.class) {
        @Override
        public Void readValue(DataInputStream stream) {
            return null;
        }

        @Override
        public void writeValue(DataOutputStream stream, Void value) {
        }
    };
    public static final TagType<Byte> BYTE = new TagType<Byte>((byte) 1, Byte.class) {
        @Override
        public Byte readValue(DataInputStream stream) throws IOException {
            return stream.readByte();
        }

        @Override
        public void writeValue(DataOutputStream stream, Byte value) throws IOException {
            stream.writeByte(value);
        }
    };
    public static final TagType<Short> SHORT = new TagType<Short>((byte) 2, Short.class) {
        @Override
        public Short readValue(DataInputStream stream) throws IOException {
            return stream.readShort();
        }

        @Override
        public void writeValue(DataOutputStream stream, Short value) throws IOException {
            stream.writeShort(value);
        }
    };
    public static final TagType<Integer> INT = new TagType<Integer>((byte) 3, Integer.class) {
        @Override
        public Integer readValue(DataInputStream stream) throws IOException {
            return stream.readInt();
        }

        @Override
        public void writeValue(DataOutputStream stream, Integer value) throws IOException {
            stream.writeInt(value);
        }
    };
    public static final TagType<Long> LONG = new TagType<Long>((byte) 4, Long.class) {
        @Override
        public Long readValue(DataInputStream stream) throws IOException {
            return stream.readLong();
        }

        @Override
        public void writeValue(DataOutputStream stream, Long value) throws IOException {
            stream.writeLong(value);
        }
    };
    public static final TagType<Float> FLOAT = new TagType<Float>((byte) 5, Float.class) {
        @Override
        public Float readValue(DataInputStream stream) throws IOException {
            return stream.readFloat();
        }

        @Override
        public void writeValue(DataOutputStream stream, Float value) throws IOException {
            stream.writeFloat(value);
        }
    };
    public static final TagType<Double> DOUBLE = new TagType<Double>((byte) 6, Double.class) {
        @Override
        public Double readValue(DataInputStream stream) throws IOException {
            return stream.readDouble();
        }

        @Override
        public void writeValue(DataOutputStream stream, Double value) throws IOException {
            stream.writeDouble(value);
        }
    };
    public static final TagType<Byte[]> BYTE_ARRAY = new TagType<Byte[]>((byte) 7, Byte[].class) {
        @Override
        public Byte[] readValue(DataInputStream stream) throws IOException {
            int length = stream.readInt();
            Byte[] value = new Byte[length];
            for (int i = 0; i < length; i ++) {
                value[i] = stream.readByte();
            }
            return value;
        }

        @Override
        public void writeValue(DataOutputStream stream, Byte[] value) throws IOException {
            stream.writeInt(value.length);
            for (Byte b : value) {
                stream.writeByte(b);
            }
        }
    };
    public static final TagType<String> STRING = new TagType<String>((byte) 8, String.class) {
        @Override
        public String readValue(DataInputStream stream) throws IOException {
            return stream.readUTF();
        }

        @Override
        public void writeValue(DataOutputStream stream, String value) throws IOException {
            stream.writeUTF(value);
        }
    };
    public static final TagType<TypedList> LIST = new TagType<TypedList>((byte) 9, TypedList.class) {
        @Override
        public TypedList readValue(DataInputStream stream) throws IOException {
            int depth = FinalNBT.getDepth(stream);
            if (depth > 511) {
                throw new DepthException();
            }
            FinalNBT.setDepth(stream, depth + 1);

            TagType type = TagType.read(stream);
            assert type != null;
            int length = stream.readInt();
            TypedList value = new TypedList(type);
            for (int i = 0; i < length; i ++) {
                value.add(type.readValue(stream));
            }
            FinalNBT.setDepth(stream, depth);
            return value;
        }

        @Override
        public void writeValue(DataOutputStream stream, TypedList value) throws IOException {
            TagType type = value.getType();
            type.write(stream);
            stream.writeInt(value.size());
            for (Object e : value) {
                type.writeValue(stream, e);
            }
        }
    };
    public static final TagType<Compound> COMPOUND = new TagType<Compound>((byte) 10, Compound.class) {
        @Override
        public Compound readValue(DataInputStream stream) throws IOException {
            int depth = FinalNBT.getDepth(stream);
            if (depth > 511) {
                throw new DepthException();
            }
            FinalNBT.setDepth(stream, depth + 1);

            Compound value = new Compound();
            NamedTag tag;
            while ((tag = NamedTag.read(stream)).getValue() != null) {
                value.put(tag.getName(), tag.getValue());
            }
            FinalNBT.setDepth(stream, depth);
            return value;
        }

        @Override
        public void writeValue(DataOutputStream stream, Compound value) throws IOException {
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                new NamedTag(entry.getKey(), entry.getValue()).write(stream);
            }
            new NamedTag("", null).write(stream);
        }
    };
    public static final TagType<Integer[]> INT_ARRAY = new TagType<Integer[]>((byte) 11, Integer[].class) {
        @Override
        public Integer[] readValue(DataInputStream stream) throws IOException {
            int length = stream.readInt();
            Integer[] value = new Integer[length];
            for (int i = 0; i < length; i ++) {
                value[i] = stream.readInt();
            }
            return value;
        }

        @Override
        public void writeValue(DataOutputStream stream, Integer[] value) throws IOException {
            stream.writeInt(value.length);
            for (Integer i : value) {
                stream.writeInt(i);
            }
        }
    };

    private final byte id;
    private final Class targetClass;

    private TagType(final byte id, final Class targetClass) {
        this.id = id;
        this.targetClass = targetClass;
    }

    public final byte getId() {
        return id;
    }

    public final Class getTargetClass() {
        return targetClass;
    }

    public static TagType fromId(final byte id) {
        switch (id) {
            case 0:
                return END;
            case 1:
                return BYTE;
            case 2:
                return SHORT;
            case 3:
                return INT;
            case 4:
                return LONG;
            case 5:
                return FLOAT;
            case 6:
                return DOUBLE;
            case 7:
                return BYTE_ARRAY;
            case 8:
                return STRING;
            case 9:
                return LIST;
            case 10:
                return COMPOUND;
            case 11:
                return INT_ARRAY;
            default:
                return null;
        }
    }

    public static TagType fromClass(final Class c) {
        if (c == Void.class) {
            return END;
        } else if (c == Byte.class) {
            return BYTE;
        } else if (c == Short.class) {
            return SHORT;
        } else if (c == Integer.class) {
            return INT;
        } else if (c == Long.class) {
            return LONG;
        } else if (c == Float.class) {
            return FLOAT;
        } else if (c == Double.class) {
            return DOUBLE;
        } else if (c == Byte[].class) {
            return BYTE_ARRAY;
        } else if (c == String.class) {
            return STRING;
        } else if (c == TypedList.class) {
            return LIST;
        } else if (c == Compound.class) {
            return COMPOUND;
        } else if (c == Integer[].class) {
            return INT_ARRAY;
        } else {
            return null;
        }
    }

    public static TagType fromValue(Object value) {
        if (value == null) {
            return END;
        } else {
            return fromClass(value.getClass());
        }
    }

    public static TagType read(DataInputStream stream) throws IOException {
        return fromId(stream.readByte());
    }

    public void write(DataOutputStream stream) throws IOException {
        stream.writeByte(getId());
    }

    public abstract T readValue(DataInputStream stream) throws IOException;

    public abstract void writeValue(DataOutputStream stream, T value) throws IOException;

}
