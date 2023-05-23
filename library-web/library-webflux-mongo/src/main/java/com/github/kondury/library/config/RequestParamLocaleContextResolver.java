package com.github.kondury.library.config;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.List;
import java.util.Locale;

@Component(WebHttpHandlerBuilder.LOCALE_CONTEXT_RESOLVER_BEAN_NAME)
public class RequestParamLocaleContextResolver implements LocaleContextResolver {
    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        List<String> lang = exchange.getRequest().getQueryParams().get("lang");
        Locale targetLocale = null;
        if (lang != null && !lang.isEmpty()) {
            targetLocale = Locale.forLanguageTag(lang.get(0));
        }
        if (targetLocale == null) {
            targetLocale = Locale.getDefault();
        }
        return new SimpleLocaleContext(targetLocale);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        throw new UnsupportedOperationException(
                "Cannot change lang query parameter - use a different locale context resolution strategy");
    }
}
