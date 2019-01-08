//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.lang.hessian.ext;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import java.io.IOException;

public interface AtomSerialization {
    void writeObjectData(AbstractHessianOutput var1) throws IOException;

    void readObjectData(AbstractHessianInput var1) throws IOException;
}
