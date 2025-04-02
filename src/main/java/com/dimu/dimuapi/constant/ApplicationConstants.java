package com.dimu.dimuapi.constant;

import org.springframework.data.domain.Sort;

public final class ApplicationConstants {
    public static final String JWT_SECRET_DEFAULT_VAL="hbjywuvud6uvjebdlknsiidt";
    public static final String JWT_HEADER = "Authorization";
    public static  final String NUMERIC_CHARS = "0123456789";
    public static final String ALPHANUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABET_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final Sort createdAtSort = Sort.by(Sort.Direction.DESC,"createdAt");
}
