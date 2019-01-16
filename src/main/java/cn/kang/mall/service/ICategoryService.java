package cn.kang.mall.service;

import java.util.List;

import cn.kang.mall.common.ServerResponse;

/**
 * 类目管理
 * @author mi
 *
 */
public interface ICategoryService {
	
	/**
	 * 增加类目
	 * @param categoryName
	 * @param parentId
	 * @return
	 */
	ServerResponse addCategory(String categoryName, Integer parentId);
	
	/**
	 * 更新类目名称
	 * @param categoryId
	 * @param categoryName
	 * @return
	 */
	ServerResponse updateCategoryName(Integer categoryId, String categoryName);
	
	/**
	 * 获取该类目下的 子目录  不递归
	 * @param categoryId
	 * @return
	 */
	ServerResponse getChildrenParallelCategory(Integer categoryId);
	
	/**
	 * 获取该类目下的 子目录  递归
	 * @param categoryId
	 * @return
	 */
	ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
