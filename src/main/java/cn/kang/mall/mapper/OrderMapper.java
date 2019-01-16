package cn.kang.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.kang.mall.entiy.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

	Order selectByUserIdAndOrderNo(@Param("userId")Integer userId, @Param("orderNo")Long orderNo);

	Order selectByOrderNo(Long orderNo);
	
	List<Order> selectByUserId(Integer userId);

	List<Order> selectAllOrder();
}



