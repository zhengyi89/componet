//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.core.fstore;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QuickRow {
    private QuickLocation key;
    private byte[] value;

    public QuickRow(QuickLocation key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public QuickLocation getKey() {
        return this.key;
    }

    public byte[] getValue() {
        return this.value;
    }

    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        } catch (Exception var2) {
            return super.toString();
        }
    }
}
