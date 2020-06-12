package infrastructure.jsp;

<<<<<<< HEAD
import infrastructure.ApplicationContext;
import infrastructure.currency.CurrencyInfo;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import static web.constant.AttributeConstants.CONTEXT;

=======
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

>>>>>>> 03c5a1b2ec7cfe67a76dc026c7c5641acfc97ed5
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
<<<<<<< HEAD
        ServletContext servletContext = ((PageContext) getJspContext()).getServletContext();
        ApplicationContext context = ((ApplicationContext) servletContext.getAttribute(CONTEXT));
        CurrencyInfo currencyInfo = context.getCurrencyInfo(lang);
        if (currencyInfo == null) {
            context.getCurrencyInfo("en");
        }
        getJspContext().getOut().print(moneyInCents * currencyInfo.getRatioToDollar() + " " + currencyInfo.getCurrencySymbol());
=======
        switch (lang) {
            case "ru": {

                getJspContext().getOut().print(moneyInCents * ruCoefficient + " ₽");
                break;
            }
            default: {
                getJspContext().getOut().print(moneyInCents + " $");
            }
        }
>>>>>>> 03c5a1b2ec7cfe67a76dc026c7c5641acfc97ed5
    }
}
