package cn.kang.mall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ResponseCode;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.User;
import cn.kang.mall.service.ICartService;
import cn.kang.mall.vo.CartVo;

/**
 * 购物车模块
 * @author mi
 *
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
	@Autowired
	private ICartService cartService;
	
	/**
	 *  查询  购物车
	 * @param session
	 * @return
	 */
	@RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		
		return cartService.list(user.getId());
	}
	
	/**
	 * 购物车中  增加商品
	 * @param session
	 * @param count
	 * @param productId
	 * @return
	 */
	@RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
		return cartService.add(user.getId(),productId,count);
	}
	
	/**
	 * 更新购物车
	 * @param session
	 * @param count
	 * @param productId
	 * @return
	 */
	 @RequestMapping("update.do")
	 @ResponseBody
	 public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user ==null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
	     }
	     return cartService.update(user.getId(),productId,count);
	 }
	 
	 /**
	  * 删除
	  * @param session
	  * @param productIds
	  * @return
	  */
	  @RequestMapping("delete_product.do")
	  @ResponseBody
	  public ServerResponse<CartVo> deleteProduct(HttpSession session,String productIds){
		  User user = (User)session.getAttribute(Const.CURRENT_USER);
		  if(user ==null){
		       return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
		   }
		  return cartService.delete(user.getId(),productIds);
	  }
	  
	  /**
	   * 全选
	   * @param session
	   * @return
	   */
	  @RequestMapping("select_all.do")
	  @ResponseBody
	  public ServerResponse<CartVo> selectAll(HttpSession session){
		  User user = (User)session.getAttribute(Const.CURRENT_USER);
		  if(user ==null){
		       return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
		   }
		  return cartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
	  }
	  
	  /**
	   * 全部  不选
	   * @param session
	   * @return
	   */
	  @RequestMapping("un_select_all.do")
	  @ResponseBody
	  public ServerResponse<CartVo> unSelectAll(HttpSession session){
		  User user = (User)session.getAttribute(Const.CURRENT_USER);
		  if(user ==null){
		       return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
		   }
		  return cartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
	  } 
	  
	  /**
	   * 选中
	   * @param session
	   * @param productId
	   * @return
	   */
	  @RequestMapping("select.do")
	  @ResponseBody
	  public ServerResponse<CartVo> select(HttpSession session,Integer productId){
		  User user = (User)session.getAttribute(Const.CURRENT_USER);
		  if(user ==null){
		       return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
		   }
		  return cartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
	  }
	  
	  /**
	   * 取消选中
	   * @param session
	   * @param productId
	   * @return
	   */
	  @RequestMapping("un_select.do")
	  @ResponseBody
	  public ServerResponse<CartVo> unSelect(HttpSession session,Integer productId){
		  User user = (User)session.getAttribute(Const.CURRENT_USER);
		  if(user ==null){
		       return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
		   }
		  return cartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
	  }
	  /**
	   * 获取购物车商品数量
	   * @param session
	   * @return
	   */
	  @RequestMapping("get_cart_product_count.do")
	  @ResponseBody
	  public ServerResponse<Integer> getCartProductCount(HttpSession session){
		  User user = (User)session.getAttribute(Const.CURRENT_USER);
		  if(user ==null){
		       return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
		   }
		 return cartService.getCartProductCount(user.getId());
	  }
}













