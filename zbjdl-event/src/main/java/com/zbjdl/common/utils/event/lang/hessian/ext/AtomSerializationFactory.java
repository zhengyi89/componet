//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.hessian.ext;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

public class AtomSerializationFactory extends AbstractSerializerFactory {
    public AtomSerializationFactory() {
    }

    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        return AtomSerialization.class.isAssignableFrom(cl) ? new AtomSerializer(cl) : null;
    }

    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        return AtomSerialization.class.isAssignableFrom(cl) ? new AtomDeserializer(cl) : null;
    }
}
