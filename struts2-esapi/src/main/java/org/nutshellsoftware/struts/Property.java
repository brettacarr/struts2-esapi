package org.nutshellsoftware.struts;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Hex;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.EncryptionException;

import com.opensymphony.xwork2.util.ValueStack;


@StrutsTag(name = "property", tldBodyContent = "empty", tldTagClass = "org.nutshellsoftware.struts.PropertyTag", description = "Print out expression which evaluates against the stack")
public class Property extends Component
{
    private static final Logger log = Logger.getLogger(Property.class);

    public Property(ValueStack stack)
    {
        super(stack);
    }

    private String defaultValue;

    private String value;

    private boolean escapeHtml = true;
    
    private boolean encrypt = false;
    
    private boolean escapeHtmlAttribute = false;

    private boolean escapeJavaScript = false;

    private boolean escapeXml = false;

    private boolean escapeCsv = false;

    @StrutsTagAttribute(description = "The default value to be used if <u>value</u> attribute is null")
    public void setDefault(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @StrutsTagAttribute(description = "Encrypt value", type = "Boolean", defaultValue = "false")
    public void setEncrypt(boolean encrypt)
    {
        this.encrypt = encrypt;
    }
    @StrutsTagAttribute(description = "Deprecated. Use 'escapeHtml'. Whether to escape HTML", type = "Boolean", defaultValue = "true")
    public void setEscape(boolean escape)
    {
        this.escapeHtml = escape;
    }

    @StrutsTagAttribute(description = "Whether to escape HTML", type = "Boolean", defaultValue = "true")
    public void setEscapeHtml(boolean escape)
    {
        this.escapeHtml = escape;
    }
    
    @StrutsTagAttribute(description = "Whether to escape HTML attribute", type = "Boolean", defaultValue = "true")
    public void setEscapeHtmlAttribute(boolean escape)
    {
        this.escapeHtmlAttribute = escape;
    }

    @StrutsTagAttribute(description = "Whether to escape Javascript", type = "Boolean", defaultValue = "false")
    public void setEscapeJavaScript(boolean escapeJavaScript)
    {
        this.escapeJavaScript = escapeJavaScript;
    }

    @StrutsTagAttribute(description = "Value to be displayed", type = "Object", defaultValue = "&lt;top of stack&gt;")
    public void setValue(String value)
    {
        this.value = value;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @StrutsTagAttribute(description = "Whether to escape CSV (useful to escape a value for a column)", type = "Boolean", defaultValue = "false")
    public void setEscapeCsv(boolean escapeCsv)
    {
        this.escapeCsv = escapeCsv;
    }

    @StrutsTagAttribute(description = "Whether to escape XML", type = "Boolean", defaultValue = "false")
    public void setEscapeXml(boolean escapeXml)
    {
        this.escapeXml = escapeXml;
    }

    public boolean start(Writer writer)
    {
        boolean result = super.start(writer);

        String actualValue = null;

        if (value == null)
        {
            value = "top";
        }
        else
        {
            value = stripExpressionIfAltSyntax(value);
        }

        // exception: don't call findString(), since we don't want the
        // expression parsed in this one case. it really
        // doesn't make sense, in fact.
        actualValue = (String) getStack().findValue(value, String.class, throwExceptionOnELFailure);

        try
        {
            if (actualValue != null)
            {
                writer.write(prepare(actualValue));
            }
            else if (defaultValue != null)
            {
                writer.write(prepare(defaultValue));
            }
        }
        catch (IOException e)
        {
            log.info("Could not print out value '" + value + "'", e);
        }

        return result;
    }

    private String prepare(String value)
    {
        if (encrypt)
        {
            return encryptValue(value);
        }
        else
        {
            return escapeValue(value);
        }
    }
    private String escapeValue(String value)
    {
        String result = value;
        if (escapeHtml && !escapeJavaScript && !escapeXml && !escapeCsv && !escapeHtmlAttribute)
        {
            result = ESAPI.encoder().encodeForHTML(result);
        }
        else if (escapeJavaScript)
        {
            result = ESAPI.encoder().encodeForJavaScript(result);
        }
        else if (escapeXml)
        {
            result = ESAPI.encoder().encodeForXML(result);
        }
        else if (escapeCsv)
        {
            result = ESAPI.encoder().encodeForCSS(result);
        }
        else if (escapeHtmlAttribute)
        {
            result = ESAPI.encoder().encodeForHTMLAttribute(result);
        }

        return result;
    }
    private String encryptValue(String value)
    {
        try
        {            
            String encrypted = Hex.encode(ESAPI.encryptor().encrypt(new PlainText(value)).asPortableSerializedByteArray(), false);
                       
            return encrypted;
        }
        catch (EncryptionException e)
        {
            log.warn("Error encrypting hidden field.", e);
        }
        return value;
    }
}
