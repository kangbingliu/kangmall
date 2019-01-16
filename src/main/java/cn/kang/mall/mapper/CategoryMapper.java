package cn.kang.mall.mapper;

import java.util.List;

import cn.kang.mall.entiy.Category;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

	List<Category> selectCategoryChildrenByParentId(Integer pagentId);
}