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

import me.finalchild.finalnbt.FinalNBT;
import me.finalchild.finalnbt.TagType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A NBT compound tag. The values' types can differ.
 * To get a custom object, you must use get(String, Class) using the object's class.
 *
 * Actually it's a HashMap<String, Object>.
 */
public class Compound implements Map<String, Object> {

    private HashMap<String, Object> map;

    public Compound() {
        this(new HashMap<>());
    }

    /**
     * Wrap the map. The map's values MUST be NBT standard typed.
     * @param map The map to wrap.
     */
    public Compound(HashMap<String, Object> map) {
        this.map = map;
    }

    public HashMap getHashMap() {
        return map;
    }

    public <T> T get(String key) {
        return (T) map.get(key);
    }

    /**
     * Get a deserialized object.
     * @param key The key associated with the nbt compound tag.
     * @param c The type of the object.
     * @param <T> The type of the object.
     * @return The object deserialized.
     */
    public <T> T get(String key, Class<T> c) {
        return FinalNBT.deserialize(get(key), c);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        if (TagType.fromValue(value) != null) {
            return map.put(key, value);
        } else {
            return map.put(key, FinalNBT.serialize(value));
        }
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

}
