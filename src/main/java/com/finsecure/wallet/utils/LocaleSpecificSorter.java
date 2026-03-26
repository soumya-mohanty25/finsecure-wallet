package com.finsecure.wallet.utils;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocaleSpecificSorter<T> {
    private Field sortFeild = null;
    Locale locale = LocaleContextHolder.getLocale();

    public LocaleSpecificSorter(Class<T> typeParameterClass) {
        this.locale = LocaleContextHolder.getLocale();
        Field[] var2 = typeParameterClass.getDeclaredFields();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Sortable.class) && ((Sortable)field.getAnnotation(Sortable.class)).lang().equals(this.locale.getLanguage())) {
                this.sortFeild = field;
                break;
            }
        }

    }

    public List<T> sort(List<T> list) {
        if (this.sortFeild == null) {
            return list;
        } else {
            Collator collator = Collator.getInstance(this.locale);
            this.sortFeild.setAccessible(true);
            return (List)list.stream().sorted((first, second) -> {
                try {
                    String a = (String)this.sortFeild.get(first);
                    String b = (String)this.sortFeild.get(second);
                    return collator.compare(a, b);
                } catch (IllegalAccessException var6) {
                    throw new RuntimeException("Error", var6);
                }
            }).collect(Collectors.toList());
        }
    }
}
