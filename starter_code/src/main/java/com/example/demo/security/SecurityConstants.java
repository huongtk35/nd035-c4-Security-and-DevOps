/**
 * @author RoseDao
 * @email [huongtk35@gmail.com]
 * @create date 2024-06-23 20:56:37
 * @modify date 2024-06-23 20:56:37
 * @desc [description]
 */
package com.example.demo.security;

public class SecurityConstants {
    public static final String SECRET = "oursecretkey";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";

    public  static final String LOGIN_URL = "/login";
}
