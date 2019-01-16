package cn.kang.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.kang.mall.entiy.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

	List<Cart> selectCartByUserId(Integer userId);

	int selectCartProductCheckedStatusByUserId(Integer userId);

	Cart selectCartByUserIdProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);

	int updateCartByUserIdProductId(@Param("userId")Integer userId, @Param("productId")Integer productId, @Param("count")Integer count);


	int deleteByUserIdProductIds(@Param("userId")Integer userId, @Param("productIdList")List<String> productList);

	int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId")Integer productId,@Param("checked") Integer checked);

	int selectCartCountByUserId(Integer userId);

	List<Cart> selectCheckedCartByUserId(Integer userId);
}



