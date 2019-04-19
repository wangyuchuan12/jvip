package com.zjmxdz.context;


import com.zjmxdz.domain.TbaseUserinfo;

public abstract class UserContext {
    private static ThreadLocal<TbaseUserinfo> userinfos = new ThreadLocal<>();

    public static void set(TbaseUserinfo userinfo) {
        userinfos.set(userinfo);
    }

    public static TbaseUserinfo get() {
        return userinfos.get();
    }

    public static void clear() {
        userinfos.set(null);
        userinfos.remove();
    }
}
