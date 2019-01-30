/*
 * Copyright (c) 2017 Castle
 */

package io.castle.android.queue;

import com.squareup.tape2.ObjectQueue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import io.castle.android.Utils;

class GsonConverter<T> implements ObjectQueue.Converter<T> {
    private final Class<T> type;

    GsonConverter(Class<T> type) {
        this.type = type;
    }

    @Override public T from(byte[] bytes) {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
        return Utils.getGsonInstance().fromJson(reader, type);
    }

    @Override public void toStream(T object, OutputStream bytes) throws IOException {
        Writer writer = new OutputStreamWriter(bytes);
        Utils.getGsonInstance().toJson(object, writer);
        writer.close();
    }
}
