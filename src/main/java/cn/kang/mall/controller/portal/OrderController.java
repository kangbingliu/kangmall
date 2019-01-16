package cn.kang.mall.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ResponseCode;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.User;
import cn.kang.mall.service.IOrderService;

/***
 *  订单 模块  
 * @author mi
 *
 */
@Controller
@RequestMapping("/order/")
public class OrderController {
	private static  final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private IOrderService orderService;
	
	
	/**
	 *  创建订单
	 * @param session
	 * @param shippingId
	 * @return
	 */
	@RequestMapping("create.do")
	@ResponseBody
	public ServerResponse create(HttpSession session, Integer shippingId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.createOrder(user.getId(),shippingId);
	}
		
	/**
	 *  取消订单
	 * @param session
	 * @param orderNo
	 * @return
	 */
	 @RequestMapping("cancel.do")
	 @ResponseBody
	 public ServerResponse cancel(HttpSession session, Long orderNo){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     return orderService.cancel(user.getId(),orderNo);
	 }
	
	 /**
	  * 获取购物车 商品 详情信息 
	  * @param session
	  * @return
	  */
	 @RequestMapping("get_order_cart_product.do")
	 @ResponseBody
	 public ServerResponse getOrderCartProduct(HttpSession session){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     return orderService.getOrderCartProduct(user.getId());
	 }
	 
	 /**
	  * 获取订单详情
	  * @param session
	  * @param orderNo
	  * @return
	  */
	 @RequestMapping("detail.do")
	 @ResponseBody
	 public ServerResponse detail(HttpSession session,Long orderNo){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     
	     return orderService.getOrderDetail(user.getId(),orderNo);
	 }
	 
	 /**
	  *  查询  当前用户 订单
	  * @param session
	  * @param pageNum
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("list.do")
	 @ResponseBody
	 public ServerResponse list(HttpSession session, 
			 					@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, 
			 					@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     return orderService.getOrderList(user.getId(),pageNum,pageSize);
	 
	 }
	 
	 
	 
	/**
	 * 支付接口
	 * @param session
	 * @param orderNo
	 * @param request
	 * @return
	 */
	@RequestMapping("/pay.do")
	@ResponseBody
	public ServerResponse pay(HttpSession session,Long orderNo,HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return ServerResponse.createByErrorMessage("请登录");
		}
		String path = request.getServletContext().getRealPath("upload");
		return orderService.pay(orderNo, user.getId(), path);
	}
	
	/**
	 * 支付宝的  回调   必须返回success  否则会一直轮询回调
	 * @param session
	 * @param orderNo
	 * @param request
	 * @return
	 */
	@RequestMapping("/alipay_callback.do")
	@ResponseBody
	public Object callBack(HttpSession session,Long orderNo,HttpServletRequest request) {
		Map<String,String[]> requestParams = request.getParameterMap();
		Map<String, String> params = Maps.newHashMap();
		Iterator<String> keys = requestParams.keySet().iterator();
		while (keys.hasNext()) {
			String name = (String) keys.next();
			String[]  values = requestParams.get(name);
			String value = "";
			// 拼接
			for(int i = 0 ; i < values.length; i++) {
				value = (i == values.length - 1)?value+values[i] : value+values[i]+",";
			}
			params.put(name, value);
		}
		
		// 查看文档
		LOGGER.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),
																params.get("trade_status"),
																params.toString());
		
		
		// 必须要严格的  检验
		// sign  和 sign_type 两个参数  需要去掉
		params.remove("sign_type");
		
		try {
		boolean checked = AlipaySignature.rsaCheckV2(params, 
									   Configs.getAlipayPublicKey(), // 是支付宝的公钥
									   "utf-8", 
									   Configs.getSignType());
		// TODO  检验参数   如tarde_id   订单总额  sell_id 等等 
		// 只要一个没有通过  就需要忽略  
		// 取消订单等等   给用户提示
		
		if (!checked) {
			// 参数被纂改
			return ServerResponse.createByErrorMessage("非法请求！！");
		}
		
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		// 通过校验      修改订单状态     创建  支付信息等等
		ServerResponse response = orderService.aliCallback(params);
		//  必须  返回success 才不会重复回调
		if(response.isSuccess()) {
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		// 返回failed
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	
	
	/**
	 *   查询接口
	 * @param session
	 * @param orderNo
	 * @param request
	 * @return
	 */
	@RequestMapping("/query_order_pay_status.do")
	@ResponseBody
	public ServerResponse<Boolean> queryOrderStatus(HttpSession session,Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if(user == null) {
			return  ServerResponse.createByErrorMessage("请先登录！");
		}
		ServerResponse response =  orderService.queryOrderStatus(user.getId(),orderNo);
		if(response.isSuccess()) {
			return ServerResponse.createBySuccess(true);
		}
		return ServerResponse.createBySuccess(false);
	}
	
	 
	
}






