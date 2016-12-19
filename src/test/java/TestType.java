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

import me.finalchild.finalnbt.serialize.NBTSerializable;
import me.finalchild.finalnbt.type.Compound;
import me.finalchild.finalnbt.type.TypedList;

public class TestType implements NBTSerializable {
    TestType2 nested_compound_test;
    TypedList<Compound> listTest_compound;
    TypedList<Long> listTest_long;
    byte byteTest;
    double doubleTest;
    float floatTest;
    int intTest;
    long longTest;
    short shortTest;
    String stringTest;
    byte[] byteArrayTest;

    @Override
    public Compound serialize() {
        Compound nbt = new Compound();
        nbt.put("nested compound test", nested_compound_test);
        nbt.put("listTest (compound)", listTest_compound);
        nbt.put("listTest (long)", listTest_long);
        nbt.put("byteTest", byteTest);
        nbt.put("doubleTest", doubleTest);
        nbt.put("floatTest", floatTest);
        nbt.put("intTest", intTest);
        nbt.put("longTest", longTest);
        nbt.put("shortTest", shortTest);
        nbt.put("stringTest", stringTest);
        nbt.put("byteArrayTest", byteArrayTest);
        return nbt;
    }

    public static TestType deserialize(Compound nbt) {
        TestType tt = new TestType();
        tt.nested_compound_test = nbt.get("nested compound test", TestType2.class);
        tt.listTest_compound = nbt.get("listTest (compound)");
        tt.listTest_long = nbt.get("listTest (long)");
        tt.byteTest = nbt.get("byteTest");
        tt.doubleTest = nbt.get("doubleTest");
        tt.floatTest = nbt.get("floatTest");
        tt.intTest = nbt.get("intTest");
        tt.longTest = nbt.get("longTest");
        tt.shortTest = nbt.get("shortTest");
        tt.stringTest = nbt.get("stringTest");
        tt.byteArrayTest = nbt.get("byteArrayTest");
        return tt;
    }
}
