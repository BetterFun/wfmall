package com.cug.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class Const {
    public static final String CURRENT_USER="currentUser";

    public static final String EMAIL="email";

    public static final String USERNAME="username";

    public interface Role{
        final int ROLE_CUSTOMER=0;  //普通用户
        final int ROLE_ADMIN=1;  //管理员
    }

    public interface ProductListOrderBy{
        final Set<String> PRICE_ASC_DESC= Sets.newHashSet("price_asc","price_desc");
    }

    public interface Cart{
        public final int CHECKED=1;
        public final int UN_CHECK=0;

        public String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
        public String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
    }
}
