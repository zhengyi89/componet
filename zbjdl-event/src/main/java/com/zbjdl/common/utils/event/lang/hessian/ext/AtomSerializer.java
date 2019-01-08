//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.hessian.ext;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import java.io.IOException;

public class AtomSerializer extends AbstractSerializer {
    public AtomSerializer(Class cl) {
    }

    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (null != obj) {
            if (!out.addRef(obj)) {
                Class<?> cl = obj.getClass();
                int ref = out.writeObjectBegin(cl.getName());
                if (ref < -1) {
                    throw new UnsupportedOperationException("不支持这个map方式的写入");
                } else {
                    if (ref == -1) {
                        out.writeClassFieldLength(0);
                        out.writeObjectBegin(cl.getName());
                    }

                    ((AtomSerialization)obj).writeObjectData(out);
                }
            }
        }
    }
}
