package com.slop.slopbackend.utility;

public class CustomValidation {
    public static <T extends Enum<T>> boolean isValidEnum(String str, Class<T> enumClass) {
        try {
            Enum.valueOf(enumClass, str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
