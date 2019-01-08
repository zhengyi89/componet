//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.zbjdl.common.utils.event.lang.hessian.ext.AtomSerializationFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerUtils {
    private static SerializerFactory _serializerFactory = null;

    public HessianSerUtils() {
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(bos);
        output.setSerializerFactory(getSerializerFactory());
        output.writeObject(obj);
        output.close();
        return bos.toByteArray();
    }

    public static Object deserialize(byte[] content) throws IOException {
        if (null != content && content.length != 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(content);
            Hessian2Input input = new Hessian2Input(bis);
            input.setSerializerFactory(getSerializerFactory());
            Object obj = input.readObject();
            input.close();
            return obj;
        } else {
            return null;
        }
    }

    public static final SerializerFactory getSerializerFactory() {
        if (_serializerFactory == null) {
            _serializerFactory = new SerializerFactory();
            _serializerFactory.addFactory(new AtomSerializationFactory());
        }

        return _serializerFactory;
    }
}
