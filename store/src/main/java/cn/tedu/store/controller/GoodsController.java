package cn.tedu.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Goods;
import cn.tedu.store.service.IGoodsService;
import cn.tedu.store.util.JsonResult;

@RestController
@RequestMapping("goods")
public class GoodsController extends BaseController {

	@Autowired
	private IGoodsService goodsService;
	
	@GetMapping("hot")
	public JsonResult<List<Goods>> getHotList() {
		List<Goods> data = goodsService.getHotList();
		return new JsonResult<>(SUCCESS, data);
	}
	
	@GetMapping("{id}/details")
	public JsonResult<Goods> getById(
		@PathVariable("id") Long id) {
		Goods data = goodsService.getById(id);
		return new JsonResult<>(SUCCESS, data);
	}

}
