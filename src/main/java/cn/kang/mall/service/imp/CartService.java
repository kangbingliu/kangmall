package cn.kang.mall.service.imp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ResponseCode;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.Cart;
import cn.kang.mall.entiy.Product;
import cn.kang.mall.mapper.CartMapper;
import cn.kang.mall.mapper.ProductMapper;
import cn.kang.mall.service.ICartService;
import cn.kang.mall.util.BigDecimalUtil;
import cn.kang.mall.util.PropertiesUtil;
import cn.kang.mall.vo.CartProductVo;
import cn.kang.mall.vo.CartVo;
@Service
public class CartService implements ICartService {
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;
	@Override
	public ServerResponse<CartVo> list(Integer userId) {
		 CartVo cartVo = this.getCartVoLimit(userId);
	     return ServerResponse.createBySuccess(cartVo);
	}
	
	@Override
	public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
		// 检验参数
		if(productId == null || count == null){
		     return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		int updated = cartMapper.updateCartByUserIdProductId(userId,productId,count);
		if(updated == 0) {
			return ServerResponse.createByErrorMessage("更新失败");
		}
		return this.list(userId);
	}

	@Override
	public ServerResponse<CartVo> delete(Integer userId, String productIds) {
//		if(StringUtils.isNotBlank(productIds)) {
//			return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
//		}
		List<String> productList = Splitter.on(",").splitToList(productIds);
		if(CollectionUtils.isEmpty(productList)) {
			 return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		cartMapper.deleteByUserIdProductIds(userId,productList);
		return this.list(userId);
	}

	@Override
	public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
		// 检验参数
		if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
		Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
		 if(cart == null){
			 //这个产品不在这个购物车里,需要新增一个这个产品的记录
	         Cart cartItem = new Cart();
	         cartItem.setQuantity(count);
	         cartItem.setChecked(Const.Cart.CHECKED);
	         cartItem.setProductId(productId);
	         cartItem.setUserId(userId);
	         cartMapper.insert(cartItem);
		 }else {
			//这个产品已经在购物车里了.
	        //如果产品已存在,数量相加
	        count = cart.getQuantity() + count;
	        cart.setQuantity(count);
	        cartMapper.updateByPrimaryKeySelective(cart);
		}
		 return this.list(userId);
	}


	
	@Override
	public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, int checked) {
		cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
		return this.list(userId);
	}

	
	
	@Override
	public ServerResponse<Integer> getCartProductCount(Integer userId) {
		int count = cartMapper.selectCartCountByUserId(userId);
		return ServerResponse.createBySuccess(count);
	}

	private CartVo getCartVoLimit(Integer userId) {
		CartVo cartVo = new CartVo();
		List<Cart> cartList = cartMapper.selectCartByUserId(userId);
		List<CartProductVo> cartProductVoList = Lists.newArrayList();
		BigDecimal cartTotalPrice = new BigDecimal("0");
		
		if(CollectionUtils.isNotEmpty(cartList)){
			for(Cart cartItem : cartList){
				CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(userId);
                cartProductVo.setProductId(cartItem.getProductId());
                
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product != null){
                	cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());
                    
                    //判断库存
                    int buyLimitCount = 0;
                    if(product.getStock() >= cartItem.getQuantity()){
                    	//库存充足的时候
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                    	// 购物车中商品数量大于  商品库存
                    	 buyLimitCount = product.getStock();
                    	 cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                         //购物车中更新有效库存   
                    	 Cart cartForQuantity = new Cart();
                         cartForQuantity.setId(cartItem.getId());
                         cartForQuantity.setQuantity(buyLimitCount);
                         cartMapper.updateByPrimaryKeySelective(cartForQuantity);
					}
                    
                    cartProductVo.setQuantity(buyLimitCount);
                    //计算总价  单个商品
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Const.Cart.CHECKED){
                	 //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),
                    										cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
			}
		}
		cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
	}


	// 购物车中商品 是否全部勾选
	private Boolean getAllCheckedStatus(Integer userId) {
		 if(userId == null){
	            return false;
	     }
		 return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
	}
	
	
}
