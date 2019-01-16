package cn.kang.mall.service;

import com.github.pagehelper.PageInfo;

import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.Shipping;

public interface IShippingService {
	
	/**
	 *  添加收货地址
	 * @param id
	 * @param shipping
	 * @return
	 */
	ServerResponse add(Integer id, Shipping shipping);
	
	/**
	 * 删除
	 * @param userId
	 * @param shippingId
	 * @return
	 */
	ServerResponse del(Integer userId, Integer shippingId);
	
	/**
	 * 查找
	 * @param id
	 * @param shippingId
	 * @return
	 */
	ServerResponse<Shipping> select(Integer id, Integer shippingId);

	/**
	 * 查找全部  并分页
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo> list(Integer id, int pageNum, int pageSize);
	
	/**
	 * 更新
	 * @param id
	 * @param shipping
	 * @return
	 */
	ServerResponse update(Integer id, Shipping shipping);

}
