package com.cug.service.impl;

import com.cug.common.ServiceResponse;
import com.cug.dao.CategoryMapper;
import com.cug.pojo.Category;
import com.cug.pojo.User;
import com.cug.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2018/9/26 0026.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    private Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public ServiceResponse addCategory(String categoryName, Integer parentId) {
        if(parentId==null || StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("类目为空，添加类目错误");
        }

        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int resultCount=categoryMapper.insert(category);
        if(resultCount>0){
            return ServiceResponse.createBySuccess("添加类目成功");
        }
        return ServiceResponse.createByErrorMessage("添加类目失败");
    }

    @Override
    public ServiceResponse setCategoryName(String categoryName, Integer categoryId) {
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("类目为空，添加类目错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int resultCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount>0){
            return ServiceResponse.createBySuccess("更新类目名字成功");
        }
        return ServiceResponse.createByErrorMessage("更新类目名字失败");
    }

    @Override
    public ServiceResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        if(categoryId==null){
            return ServiceResponse.createByErrorMessage("类目为空，获取类目错误");
        }
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(categoryList.isEmpty()){
            logger.info("未找到其当前分类的子类");

        }
        return ServiceResponse.createBySuccess(categoryList);
    }

    @Override
    public ServiceResponse getAllChildrenCategory(Integer categoryId) {

        Set<Category> categorySet= Sets.newHashSet();
        if(categoryId!=null){
            //findAllChildrenCategory(categorySet,categoryId);
            categorySet=findAllChildrenCategory(categoryId);
            List categoryList= Lists.newArrayList();
            for(Category categoryItem:categorySet){
                categoryList.add(categoryItem.getId());
            }
            if(categoryList.isEmpty()){
                logger.info("未找到其当前分类的子类");
            }
            return ServiceResponse.createBySuccess(categoryList);
        }
        return ServiceResponse.createByErrorMessage("类目为空，获取目录失败");
    }

    /*private void findAllChildrenCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //递归取得类目的所有子节点
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category categoryItem:categoryList){
            findAllChildrenCategory(categorySet,categoryItem.getId());
        }
        return;
    }*/

    //非递归遍历
    private Set<Category> findAllChildrenCategory(Integer categoryId){
        if(categoryId==null){
            return null;
        }
        Set<Category> categorySet=new HashSet<Category>();
        List<Category> categoryList=new ArrayList<Category>();
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }else {
            return categorySet;
        }

        Stack<Category> stack=new Stack<Category>();
        stack.push(category);
        while(!stack.empty()){
            categoryList=categoryMapper.selectCategoryChildrenByParentId(stack.peek().getId());
            stack.pop();
            if(!categoryList.isEmpty()){
                categorySet.addAll(categoryList);
                for (Category categoryItem:categoryList){
                    stack.push(categoryItem);
                }
            }
        }
        return categorySet;
    }
}
