//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.easyquery.tag;

import com.zbjdl.common.utils.easyquery.EasyQueryException;
import com.zbjdl.common.utils.easyquery.EasyQueryStack;
import com.zbjdl.common.utils.easyquery.bean.NoResultBean;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

public class NoResultTag extends StrutsBodyTagSupport {
    private Map params = new HashMap();
    private NoResultBean bean;

    public NoResultTag() {
    }

    public int doAfterBody() throws JspException {
        this.params.put("body", this.getBodyContent().getString());
        this.getBodyContent().clearBody();
        return 0;
    }

    public int doEndTag() throws JspException {
        Tag t = findAncestorWithClass(this, ListableTag.class);
        if (t == null) {
            throw new EasyQueryException("no parent listable tag found !!");
        } else {
            this.bean = new NoResultBean();

            try {
                BeanUtils.copyProperties(this.bean, this.params);
            } catch (Exception var3) {
                ;
            }

            EasyQueryStack stack = EasyQueryStack.getEasyQueryStack(this.getStack());
            this.bean.populateParams(stack);
            return 6;
        }
    }
}
