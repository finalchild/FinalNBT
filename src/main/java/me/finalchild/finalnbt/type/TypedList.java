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

package me.finalchild.finalnbt.type;

import com.google.gson.*;
import me.finalchild.finalnbt.FinalNBT;
import me.finalchild.finalnbt.TagType;
import me.finalchild.finalnbt.exception.UnsupportedTypeException;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * NBT List tag.
 * @param <T> The type of the ArrayList.
 */
public class TypedList<T> implements List<T> {

    private final TagType<T> type;
    private final List<T> list;

    public TypedList(TagType<T> type) {
        this(type, new ArrayList<>());
    }

    public TypedList(TagType<T> type, List<T> list) {
        this.type = type;
        this.list = list;
    }

    public TagType<T> getType() {
        return type;
    }

    public List<T> getList() {
        return list;
    }

    /**
     * Serializes the elements to NBT Compound tags.
     * @param list The list.
     * @param <T> The type of the list.
     * @return The TypedList of NBT Compound tags.
     */
    public static <T> TypedList<Compound> serialize(List<T> list) {
        return new TypedList<>(TagType.COMPOUND, list.stream().map(FinalNBT::serialize).collect(Collectors.toList()));
    }

    /**
     * Deserializes the elements to the specified type.
     * The element type of this TypedList MUST be Compound.
     * @param type The custom type.
     * @param <U> The custom type.
     * @return The deserialized List.
     */
    public <U> List<U> deserialize(Class<U> type) {
        if (this.type != TagType.COMPOUND) {
            throw new UnsupportedOperationException();
        }
        return list.stream().map(e -> FinalNBT.deserialize((Compound) e, type)).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return list.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public static class TypedListSerializer implements JsonSerializer<TypedList>, JsonDeserializer<TypedList> {

        @Override
        public JsonElement serialize(TypedList src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("type", new JsonPrimitive(src.getType().getId()));
            obj.add("list", context.serialize(src.getList()));
            return obj;
        }

        @Override
        public TypedList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            return new TypedList(TagType.fromId(obj.getAsJsonPrimitive("type").getAsByte()), context.deserialize(obj.get("list"), List.class));
        }
    }
}
