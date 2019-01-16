package cn.kang.mall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ResponseCode;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.User;
import cn.kang.mall.service.IOrderService;
import cn.kang.mall.service.IUserService;
import cn.kang.mall.vo.OrderVo;

/**
 * 管理员订单模块
 * @author mi
 *
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManagerController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> orderList(HttpSession session, 
			 									@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
	                                            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     } 
	     if(userService.checkAdminRole(user).isSuccess()){
	          //填充我们增加产品的业务逻辑
	          return orderService.manageList(pageNum,pageSize);
	      }else{
	          return ServerResponse.createByErrorMessage("无权限操作");
	      }
	}
	/**
	 * 获取 订单详情
	 * @param session
	 * @param orderNo
	 * @return
	 */
	 @RequestMapping("detail.do")
	 @ResponseBody
	 public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     } 
	     if(userService.checkAdminRole(user).isSuccess()){
	          //填充我们增加产品的业务逻辑
	    	 return orderService.manageDetail(orderNo);
	      }else{
	          return ServerResponse.createByErrorMessage("无权限操作");
	      }
	 }
	
	 /**
	  * 搜索订单  等等
	  * @param session
	  * @param orderNo
	  * @param pageNum
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("search.do")
	 @ResponseBody
	 public ServerResponse<PageInfo> orderSearch(HttpSession session, 
	    										Long orderNo,
	    										@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
	                                            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     } 
	     if(userService.checkAdminRole(user).isSuccess()){
	          //填充我们增加产品的业务逻辑
	    	 return orderService.manageSearch(orderNo,pageNum,pageSize);
	      }else{
	          return ServerResponse.createByErrorMessage("无权限操作");
	      }
		 
	 }
	 
	 /**
	  *  商品发货
	  * @param session
	  * @param orderNo
	  * @return
	  */
	 @RequestMapping("send_goods.do")
	 @ResponseBody
	 public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     } 
	     if(userService.checkAdminRole(user).isSuccess()){
	          //填充我们增加产品的业务逻辑
	    	 return orderService.manageSendGoods(orderNo);
	      }else{
	          return ServerResponse.createByErrorMessage("无权限操作");
	      }
		 
	 }
}










