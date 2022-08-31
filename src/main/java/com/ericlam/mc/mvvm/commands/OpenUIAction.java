package com.ericlam.mc.mvvm.commands;

import org.eldependenci.mvvm.viewmodel.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenUIAction {

    Class<? extends ViewModel> view();

    String[] props() default {};

}
