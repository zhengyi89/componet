//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.ParamBean;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class ParamTag extends StrutsBodyTagSupport {
    private Map params = new HashMap();
    private ParamBean bean;

    public ParamTag() {
    }

    public int doEndTag() throws JspException {
        this.bean = new ParamBean();
        EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());

        try {
            BeanUtils.copyProperties(this.bean, this.params);
        } catch (Exception var3) {
            ;
        }

        this.bean.populateParams(stack);
        return 6;
    }

    public void setName(String name) {
        this.params.put("name", name);
    }

    public void setValue(String value) {
        this.params.put("value", value);
    }

    public void setOverwrite(boolean overwrite) {
        this.params.put("preferred", overwrite);
    }

    public void setPreferred(boolean preferred) {
        this.params.put("preferred", preferred);
    }
}
