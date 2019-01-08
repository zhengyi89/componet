//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.hessian.ext;

import com.caucho.hessian.HessianException;
import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import java.io.IOException;
import java.lang.reflect.Constructor;

public class AtomDeserializer extends AbstractDeserializer {
    private Class<?> _atomType;

    public AtomDeserializer(Class<?> cl) {
        try {
            this._atomType = cl;
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public Class<?> getType() {
        return this._atomType;
    }

    public Object readMap(AbstractHessianInput in) throws IOException {
        return null;
    }

    public Object readObject(AbstractHessianInput in, Object[] fieldNames) throws IOException {
        Object obj = this.newInstance(this._atomType);
        ((AtomSerialization)obj).readObjectData(in);
        in.addRef(obj);
        return obj;
    }

    public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {
        Object obj = this.newInstance(this._atomType);
        ((AtomSerialization)obj).readObjectData(in);
        in.addRef(obj);
        return obj;
    }

    private <T> T newInstance(Class<T> type) throws IOException {
        try {
            return type.newInstance();
        } catch (Exception var8) {
            if (var8 instanceof InstantiationException) {
                Constructor<?>[] constructors = type.getConstructors();
                boolean hasZeroArgConstructor = false;
                int i = 0;

                for(int n = constructors.length; i < n; ++i) {
                    Constructor<?> constructor = constructors[i];
                    if (constructor.getParameterTypes().length == 0) {
                        hasZeroArgConstructor = true;
                        break;
                    }
                }

                if (!hasZeroArgConstructor) {
                    throw new HessianException("Class cannot be created (missing no-arg constructor): " + type.getName(), var8);
                }
            }

            throw new HessianException("Error constructing instance of class: " + type.getName(), var8);
        }
    }
}
