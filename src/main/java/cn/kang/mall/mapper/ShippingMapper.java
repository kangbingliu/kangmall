package cn.kang.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.kang.mall.entiy.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);

	Shipping selectByShippingIdUserId(@Param("userId")Integer userId, @Param("shippingId")Integer shippingId);

	List<Shipping> selectByUserId(Integer userId);

	int updateByShipping(Shipping shipping);
}