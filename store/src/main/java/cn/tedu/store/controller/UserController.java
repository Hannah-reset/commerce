package cn.tedu.store.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.store.controller.ex.FileEmptyException;
import cn.tedu.store.controller.ex.FileSizeException;
import cn.tedu.store.controller.ex.FileTypeException;
import cn.tedu.store.controller.ex.FileUploadIOException;
import cn.tedu.store.controller.ex.FileUploadStateException;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.util.JsonResult;

/**
 * 处理用户数据相关请求的控制器类
 */
@RestController
@RequestMapping("users")
public class UserController extends BaseController {
	
	private static final long AVATAR_MAX_SIZE = 2 * 1024 * 1024;
	private static final List<String> AVATAR_CONTENT_TYPES = new ArrayList<>();
	static {
		AVATAR_CONTENT_TYPES.add("image/jpeg");
		AVATAR_CONTENT_TYPES.add("image/png");
	}
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("reg")
	public JsonResult<Void> reg(User user) {
		userService.reg(user);
		return new JsonResult<Void>(SUCCESS);
	}
	
	@RequestMapping("login")
	public JsonResult<User> login(
		String username, String password,
		HttpSession session) {
		User user = userService.login(username, password);
		session.setAttribute("uid", user.getUid());
		session.setAttribute("username", user.getUsername());
		return new JsonResult<>(SUCCESS, user);
	}
	
	@RequestMapping("change_password")
	public JsonResult<Void> changePassword(
		@RequestParam("old_password") String oldPassword, 
		@RequestParam("new_password") String newPassword, 
		HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		userService.changePassword(uid, username, oldPassword, newPassword);
		return new JsonResult<>(SUCCESS);
	}
	
	@GetMapping("get_info")
	public JsonResult<User> getByUid(
			HttpSession session) {
		Integer uid = getUidFromSession(session);
		User data = userService.getByUid(uid);
		return new JsonResult<>(SUCCESS, data);
	}
	
	@RequestMapping("change_info")
	public JsonResult<Void> changeInfo(
		User user, HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		user.setUid(uid);
		user.setUsername(username);
		userService.changeInfo(user);
		return new JsonResult<>(SUCCESS);
	}
	
	@PostMapping("change_avatar")
	public JsonResult<String> changeAvatar(
		HttpServletRequest request, 
		@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new FileEmptyException(
				"上传失败！请选择有效的文件！");
		}
		
		if (file.getSize() > AVATAR_MAX_SIZE) {
			throw new FileSizeException(
				"上传失败！不允许使用超过" + (AVATAR_MAX_SIZE / 1024) + "KB的文件！");
		}
		
		if (!AVATAR_CONTENT_TYPES.contains(file.getContentType())) {
			throw new FileTypeException(
				"上传失败！仅允许使用以下类型的图片文件：" + AVATAR_CONTENT_TYPES);
		}

		//获取项目的绝对路径
		String dirPath = request.getServletContext().getRealPath("upload");
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String originalFilename = file.getOriginalFilename();
		String suffix = "";
		int beginIndex = originalFilename.lastIndexOf(".");
		if (beginIndex != -1) {
			suffix = originalFilename.substring(beginIndex);
		}
		String filename = UUID.randomUUID().toString() + suffix;

		File dest = new File(dir, filename);
		try {
			file.transferTo(dest);
		} catch (IllegalStateException e) {
			throw new FileUploadStateException(
				"上传失败！请检查原文件是否存在并可以被访问！");
		} catch (IOException e) {
			throw new FileUploadIOException(
				"上传失败！读出数据时出现未知错误！");
		}

		String avatar = "/upload/" + filename;
		HttpSession session = request.getSession();
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		userService.changeAvatar(uid, username, avatar);

		JsonResult<String> jr = new JsonResult<>();
		jr.setState(SUCCESS);
		jr.setData(avatar);
		return jr;
	}

}










