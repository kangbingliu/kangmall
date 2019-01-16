package cn.kang.mall.service;

import com.github.pagehelper.PageInfo;

import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.Product;
import cn.kang.mall.vo.ProductDetailVo;

/**
 * 商品 服务
 * @author mi
 *
 */
public interface IProductService {
	
	/**
	 * 保存 或更新产品
	 * @param product
	 * @return
	 */
	ServerResponse saveOrUpdateProduct(Product product);
	
	/**
	 * 修改产品状态
	 * @param productId
	 * @param status
	 * @return
	 */
	ServerResponse setSaleStatus(Integer productId, Integer status);
	
	/**
	 * 获取商品详细信息
	 * @param productId
	 * @return
	 */
	ServerResponse manageProductDetail(Integer productId);
	
	/**
	 * 获取商品  列表信息  分页
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse getProductList(int pageNum, int pageSize);
	
	/**
	 * 带条件的分页查询
	 * @param productName
	 * @param productId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse searchProduct(String productName, Integer productId, int pageNum, int pageSize);
	
	/**
	 * 获取商品详细信息
	 * @param productId
	 * @return
	 */
	ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
	
	/**
	 * 前台 获取商品分页列表信息
	 * @param keyword
	 * @param categoryId
	 * @param pageNum
	 * @param pageSize
	 * @param orderBy
	 * @return
	 */
	ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize,
			String orderBy);

}
