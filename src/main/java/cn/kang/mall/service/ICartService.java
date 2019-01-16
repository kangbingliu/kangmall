package cn.kang.mall.service;

import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.vo.CartVo;

/**
 * 购物车模块  接口
 * @author mi
 *
 */
public interface ICartService {
	
	/**
	 * 查询购物车
	 * @param id
	 * @return
	 */
	ServerResponse<CartVo> list(Integer userId);
	
	/**
	 * 购物车中  增加商品
	 * @param id
	 * @param productId
	 * @param count
	 * @return
	 */
	ServerResponse<CartVo> add(Integer id, Integer productId, Integer count);
	
	/**
	 * 更新 购物车
	 * @param id
	 * @param productId
	 * @param count
	 * @return
	 */
	ServerResponse<CartVo> update(Integer id, Integer productId, Integer count);
	
	/**
	 * 删除
	 * @param id
	 * @param productIds
	 * @return
	 */
	ServerResponse<CartVo> delete(Integer id, String productIds);
	
	/**
	 *  选中商品  取消选中  全选  反选
	 * @param id
	 * @param productId
	 * @param checked
	 * @return
	 */
	ServerResponse<CartVo> selectOrUnSelect(Integer id, Integer productId, int checked);
	
	/**
	 * 获取购物车商品数量
	 * @param id
	 * @return
	 */
	ServerResponse<Integer> getCartProductCount(Integer id);

}
