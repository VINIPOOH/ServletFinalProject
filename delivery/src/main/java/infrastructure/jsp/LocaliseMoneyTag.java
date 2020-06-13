package infrastructure.jsp;

import infrastructure.ApplicationContext;
import infrastructure.currency.CurrencyInfo;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import static web.constant.AttributeConstants.CONTEXT;

public class LocaliseMoneyTag extends SimpleTagSupport {
    private long moneyInCents;
    private String lang;

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
    public void doTag() throws IOException {
        ServletContext servletContext = ((PageContext) getJspContext()).getServletContext();
        ApplicationContext context = ((ApplicationContext) servletContext.getAttribute(CONTEXT));
        CurrencyInfo currencyInfo = context.getCurrencyInfo(lang);
        if (currencyInfo == null) {
            getJspContext().getOut().print(moneyInCents + " $");
        }
        getJspContext().getOut().print(moneyInCents * currencyInfo.getRatioToDollar() + " " + currencyInfo.getCurrencySymbol());
    }
}
