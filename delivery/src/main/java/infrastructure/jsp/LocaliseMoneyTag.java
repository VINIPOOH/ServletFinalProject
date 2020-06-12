package infrastructure.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class LocaliseMoneyTag extends SimpleTagSupport {
    private long moneyInCents;
    private String lang;
    private int ruCoefficient = 80;


    public long getMoneyInCents() {
        return moneyInCents;
    }

    public void setMoneyInCents(long moneyInCents) {
        this.moneyInCents = moneyInCents;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public void doTag() throws JspException, IOException {
        switch (lang) {
            case "ru": {

                getJspContext().getOut().print(moneyInCents * ruCoefficient + " ₽");
                break;
            }
            default: {
                getJspContext().getOut().print(moneyInCents + " $");
            }
        }
    }
}
