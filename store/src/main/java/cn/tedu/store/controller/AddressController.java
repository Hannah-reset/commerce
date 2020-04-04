package cn.tedu.store.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Address;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.util.JsonResult;

@RestController
@RequestMapping("addresses")
public class AddressController extends BaseController {

    @Autowired 
    private IAddressService addressService;

    @RequestMapping("addnew")
	public JsonResult<Void> addnew(
		Address address, HttpSession session) {
    	Integer uid = getUidFromSession(session);
    	String username = getUsernameFromSession(session);
    	addressService.addnew(address, uid, username);
    	return new JsonResult<>(SUCCESS);
	}
    
    @GetMapping("/")
    public JsonResult<List<Address>> getByUid(
    		HttpSession session) {
    	Integer uid = getUidFromSession(session);
    	List<Address> data
    		= addressService.getByUid(uid);
    	return new JsonResult<>(SUCCESS, data);
    }
    
    @RequestMapping("{aid}/set_default")
	public JsonResult<Void> setDefault(
		@PathVariable("aid") Integer aid,
		HttpSession session) {
    	Integer uid = getUidFromSession(session);
    	String username = getUsernameFromSession(session);
    	addressService.setDefault(aid, uid, username);
    	return new JsonResult<>(SUCCESS); 
	}
    
    @RequestMapping("{aid}/delete")
    public JsonResult<Void> delete(
    		@PathVariable("aid") Integer aid,
    		HttpSession session) {
    	Integer uid = getUidFromSession(session);
    	String username = getUsernameFromSession(session);
    	addressService.delete(aid, uid, username);
    	return new JsonResult<>(SUCCESS); 
    }
    
}








