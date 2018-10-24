package com.cug.service;

import com.cug.common.ServiceResponse;
import com.cug.pojo.Category;
import com.cug.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2018/9/26 0026.
 */
public interface ICategoryService {

    ServiceResponse addCategory(String categoryName, Integer parentId);

    ServiceResponse setCategoryName(String categoryName, Integer categoryId);

    ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServiceResponse getAllChildrenCategory(Integer categoryId);
}
