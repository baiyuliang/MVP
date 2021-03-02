package com.byl.mvp.utils;

import java.io.IOException;

import okhttp3.RequestBody;
import okio.Buffer;

public class TextUtil {

    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
