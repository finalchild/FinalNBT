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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.finalchild.finalnbt.FinalNBT;
import me.finalchild.finalnbt.NamedTag;
import me.finalchild.finalnbt.type.TypedList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        NamedTag<TestType> nbt = FinalNBT.readNBT(new FileInputStream("bigtest.nbt"), TestType.class);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TypedList.class, new TypedList.TypedListSerializer())
                .setPrettyPrinting().create();
        String origin = gson.toJson(nbt.getValue());
        System.out.println(origin);

        FinalNBT.writeNBT(new FileOutputStream("bigtestcopied.nbt"), nbt, TestType.class);
        /*
        Map<String, Object> nbtFromJson = gson.fromJson(origin, new TypeToken<Map<String, Object>>(){}.getType());
        String doubleConverted = gson.toJson(nbtFromJson);
        System.out.println(doubleConverted);
         */
    }

}
