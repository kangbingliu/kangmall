package cn.kang.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.kang.mall.entiy.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

	List<OrderItem> getByOrderNoUserId(@Param("orderNo")Long orderNo, @Param("userId")Integer userId);

	void batchInsert(List<OrderItem> orderItemList);
	
	List<OrderItem> getByOrderNo(Long orderNo);
}