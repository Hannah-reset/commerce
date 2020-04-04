package cn.tedu.store.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tedu.store.entity.Goods;
import cn.tedu.store.mapper.GoodsMapper;
import cn.tedu.store.service.IGoodsService;

/**
 * 处理商品数据的业务层实现类
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public List<Goods> getHotList() {
		return findHotList();
	}
	
	@Override
	public Goods getById(Long id) {
		return findById(id);
	}

	/**
	 * 获取热销商品列表
	 * 
	 * @return 热销商品列表
	 */
	private List<Goods> findHotList() {
		return goodsMapper.findHotList();
	}
	
	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情，如果没有匹配的数据，则返回null
	 */
	private Goods findById(Long id) {
		return goodsMapper.findById(id);
	}

	
}
