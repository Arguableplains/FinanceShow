package com.FS.FinanceShow_demo;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class CustomBindingControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new java.beans.PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                } catch (DateTimeParseException e) {
                    setValue(null);
                }
            }
        });
    }
}
