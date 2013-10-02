package org.nutshellsoftware.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

public class PropertyTag extends ComponentTagSupport
{
    private static final long serialVersionUID = 435308349113743852L;

    private String defaultValue;

    private String value;

    private boolean escapeHtml = true;
    
    private boolean encrypt = false;

    private boolean escapeJavaScript = false;

    private boolean escapeXml = false;

    private boolean escapeCsv = false;

    private boolean escapeHtmlAttribute = false;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
    {
        return new Property(stack);
    }

    protected void populateParams()
    {
        super.populateParams();

        Property tag = (Property) component;
        tag.setDefault(defaultValue);
        tag.setValue(value);
        tag.setEscape(escapeHtml);
        tag.setEscapeHtml(escapeHtml);
        tag.setEscapeJavaScript(escapeJavaScript);
        tag.setEscapeXml(escapeXml);
        tag.setEscapeCsv(escapeCsv);
        tag.setEscapeHtmlAttribute(escapeHtmlAttribute);
        tag.setEncrypt(encrypt);
    }

    public void setDefault(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public void setEscape(boolean escape)
    {
        this.escapeHtml = escape;
    }

    public void setEscapeHtml(boolean escapeHtml)
    {
        this.escapeHtml = escapeHtml;
    }
    
    public void setEncrypt(boolean encrypt)
    {
        this.encrypt = encrypt;
    }

    public void setEscapeJavaScript(boolean escapeJavaScript)
    {
        this.escapeJavaScript = escapeJavaScript;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public void setEscapeCsv(boolean escapeCsv)
    {
        this.escapeCsv = escapeCsv;
    }

    public void setEscapeXml(boolean escapeXml)
    {
        this.escapeXml = escapeXml;
    }

    public void setEscapeHtmlAttribute(boolean escapeHtmlAttribute)
    {
        this.escapeHtmlAttribute = escapeHtmlAttribute;
    }
}
