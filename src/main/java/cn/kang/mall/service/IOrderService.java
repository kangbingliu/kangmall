package cn.kang.mall.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.vo.OrderVo;

public interface IOrderService {
	
	/**
	 * 支付  
	 * @param orderNo
	 * @param userId
	 * @param path
	 * @return
	 */
	ServerResponse pay(Long orderNo,Integer userId,String path);
	
	/**
	 *  校验通过后的处理
	 * @param params
	 * @return
	 */
	ServerResponse aliCallback(Map<String,String> params);
	
	/**
	 * 查看订单状态
	 * @param id
	 * @param orderNo
	 * @return
	 */
	ServerResponse<Boolean> queryOrderStatus(Integer userId, Long orderNo);
	
	/**
	 * 创建订单 
	 * @param userId
	 * @param shippingId
	 * @return
	 */
	ServerResponse createOrder(Integer userId, Integer shippingId);
	
	/**
	 * 取消订单
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	ServerResponse cancel(Integer userId, Long orderNo);
	
	/**
	 * 获取订单商品
	 * @param id
	 * @return
	 */
	ServerResponse getOrderCartProduct(Integer id);
	
	/**
	 * 获取订单详情
	 * @param id
	 * @param orderNo
	 * @return
	 */
	ServerResponse getOrderDetail(Integer userId, Long orderNo);
	
	/**
	 * 查询订单
	 * @param id
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse getOrderList(Integer userId, int pageNum, int pageSize);
	
	/**
	 * 管理员查询订单
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo> manageList(int pageNum, int pageSize);
	
	/**
	 * 管理查询订单详情
	 * @param orderNo
	 * @return
	 */
	ServerResponse<OrderVo> manageDetail(Long orderNo);
	
	/**
	 * 管理员  搜索订单
	 * @param orderNo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);
	
	/**
	 * 
	 * @param orderNo
	 * @return
	 */
	ServerResponse<String> manageSendGoods(Long orderNo);
}
