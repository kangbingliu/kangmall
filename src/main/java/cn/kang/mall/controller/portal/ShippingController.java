package cn.kang.mall.controller.portal;

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
import cn.kang.mall.entiy.Shipping;
import cn.kang.mall.entiy.User;
import cn.kang.mall.service.IShippingService;

/**
 * 
 * @author mi
 *
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {
	@Autowired
    private IShippingService shippingService;
	
	 @RequestMapping("add.do")
	 @ResponseBody
	 public ServerResponse add(HttpSession session,Shipping shipping){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     
	     return shippingService.add(user.getId(),shipping);
	 }
	 /**
	  * 删除
	  * @param session
	  * @param shippingId
	  * @return
	  */
	 @RequestMapping("del.do")
	 @ResponseBody
	 public ServerResponse del(HttpSession session,Integer shippingId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     // 需要userId 避免横向越权
	     return shippingService.del(user.getId(),shippingId);
	 }
	 
	 /**
	  * 更新
	  * @param session
	  * @param shipping
	  * @return
	  */
	 @RequestMapping("update.do")
	 @ResponseBody
	 public ServerResponse update(HttpSession session,Shipping shipping){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     return shippingService.update(user.getId(),shipping);
	 }
	 
	 /**
	  * 查找
	  * @param session
	  * @param shippingId
	  * @return
	  */
	 @RequestMapping("select.do")
	 @ResponseBody
	 public ServerResponse<Shipping> select(HttpSession session,Integer shippingId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     return shippingService.select(user.getId(),shippingId);
	 }
	 
	 
	 @RequestMapping("list.do")
	 @ResponseBody
	 public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
	                                       @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
	                                       HttpSession session){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     
	     return shippingService.list(user.getId(),pageNum,pageSize);
	 }
}









