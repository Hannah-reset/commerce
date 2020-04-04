package cn.tedu.store.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.util.JsonResult;
import cn.tedu.store.vo.CartVO;

@RestController
@RequestMapping("carts")
public class CartController extends BaseController {

	@Autowired 
	private ICartService cartService;
	
	@RequestMapping("add_to_cart")
	public JsonResult<Void> addToCart(Cart cart, HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		cartService.addToCart(cart, uid, username);
		return new JsonResult<>(SUCCESS);
	}

	@GetMapping("/")
    public JsonResult<List<CartVO>> getByUid(
    		HttpSession session) {
    	Integer uid = getUidFromSession(session);
    	List<CartVO> data
    		= cartService.getByUid(uid);
    	return new JsonResult<>(SUCCESS, data);
    }
	
	@RequestMapping("{cid}/add")
	public JsonResult<Integer> add(
		@PathVariable("cid") Integer cid,
		HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		Integer data = cartService.add(cid, uid, username);
		return new JsonResult<>(SUCCESS, data);
	}
	
	@GetMapping("get_by_cids")
	public JsonResult<List<CartVO>> getByCids(
		Integer[] cids, HttpSession session) {
		Integer uid = getUidFromSession(session);
		List<CartVO> data = cartService.getByCids(cids, uid);
		return new JsonResult<>(SUCCESS, data);
	}

}
