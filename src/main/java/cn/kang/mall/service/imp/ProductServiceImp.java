package cn.kang.mall.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ResponseCode;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.Category;
import cn.kang.mall.entiy.Product;
import cn.kang.mall.mapper.CategoryMapper;
import cn.kang.mall.mapper.ProductMapper;
import cn.kang.mall.service.ICategoryService;
import cn.kang.mall.service.IProductService;
import cn.kang.mall.util.DateTimeUtil;
import cn.kang.mall.util.PropertiesUtil;
import cn.kang.mall.vo.ProductDetailVo;
import cn.kang.mall.vo.ProductListVo;
@Service
public class ProductServiceImp implements IProductService {
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private CategoryMapper categoryMapper;
	@Override
	public ServerResponse saveOrUpdateProduct(Product product) {
		if(product != null) {
			// 设置 商品  主图
			if(StringUtils.isNotBlank(product.getSubImages())){
				String[] subImageArray = product.getSubImages().split(",");
				if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
			}
			 if(product.getId() != null){
				 // 更新
				 int rowCount = productMapper.updateByPrimaryKey(product);
				 if(rowCount > 0){
	                 return ServerResponse.createBySuccess("更新产品成功");
	             }
	             return ServerResponse.createBySuccess("更新产品失败");
			 }else{
	             int rowCount = productMapper.insert(product);
	             if(rowCount > 0){
	                 return ServerResponse.createBySuccess("新增产品成功");
	             }
	             return ServerResponse.createBySuccess("新增产品失败");
	         }
		}
		return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
	}

	
	@Override
	public ServerResponse setSaleStatus(Integer productId, Integer status) {
		if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
	}


	
	@Override
	public ServerResponse manageProductDetail(Integer productId) {
		if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ServerResponse getProductList(int pageNum, int pageSize) {
		//startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum,pageSize);
        // 默认id 排序  xml中不要加上；  因为pagehelper的使用
        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
		@SuppressWarnings("rawtypes")
		PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
	}

	
	
	@Override
	public ServerResponse searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
	}


	private ProductListVo assembleProductListVo(Product product) {
		 ProductListVo productListVo = new ProductListVo();
	     productListVo.setId(product.getId());
	     productListVo.setName(product.getName());
	     productListVo.setCategoryId(product.getCategoryId());
	     productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
	     productListVo.setMainImage(product.getMainImage());
	     productListVo.setPrice(product.getPrice());
	     productListVo.setSubtitle(product.getSubtitle());
	     productListVo.setStatus(product.getStatus());
	     return productListVo;
	}


	private ProductDetailVo assembleProductDetailVo(Product product) {
		 ProductDetailVo productDetailVo = new ProductDetailVo();
	     productDetailVo.setId(product.getId());
	     productDetailVo.setSubtitle(product.getSubtitle());
	     productDetailVo.setPrice(product.getPrice());
	     productDetailVo.setMainImage(product.getMainImage());
	     productDetailVo.setSubImages(product.getSubImages());
	     productDetailVo.setCategoryId(product.getCategoryId());
	     productDetailVo.setDetail(product.getDetail());
	     productDetailVo.setName(product.getName());
	     productDetailVo.setStatus(product.getStatus());
	     productDetailVo.setStock(product.getStock());
	     // 设置图片 服务器  地址
	     productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
	     // 设置父类目  
	     Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
	     if(category == null){
	         productDetailVo.setParentCategoryId(0);//默认根节点
	     }else{
	         productDetailVo.setParentCategoryId(category.getParentId());
	     }

	     productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
	     productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
	     return productDetailVo;
	}


	
	@Override
	public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
		if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
		Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
	}


	@Override
	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, 
																Integer categoryId, 
																int pageNum,
																int pageSize, 
																String orderBy) {
		if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
		
		List<Integer> categoryIdList = new ArrayList<Integer>();
		if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            
            if(category == null && StringUtils.isBlank(keyword)){
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();    
		}
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
	}
	
	
}
