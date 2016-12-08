package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.auth.JwtTokenUtil;
import edu.sjsu.LPOS.auth.UserAuthorities;
import edu.sjsu.LPOS.beans.ResponseBean;
import edu.sjsu.LPOS.beans.UserBean;
import edu.sjsu.LPOS.beans.UserRegisterBean;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.EmailService;
import edu.sjsu.LPOS.service.RedisStoreService;
import edu.sjsu.LPOS.service.UserService;
import edu.sjsu.LPOS.util.EncryptionUtil;
import edu.sjsu.LPOS.util.HtmlPageComposer;
import edu.sjsu.LPOS.util.UserBeanUtil;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired private UserService userService;
	@Autowired private EmailService emailService;
	@Autowired private RedisStoreService redisStoreService;
	@Autowired private JwtTokenUtil tokenUtil;
	
	@Value("${mailconfig.mailservice.expireInHours}")
	private int mailLinkExpireInHours;
	@Value("${mailconfig.mailservice.server}")
	private String mailServiceServer;
	
	@Value("${mailconfig.mailservice.port}")
	private int mailServicePort;
	
//	@PreAuthorize("hasRole('')")
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<ResponseBean> createUser(@RequestBody UserRegisterBean userRegisterBean) {
		ResponseBean respBean = new ResponseBean();
		if (userRegisterBean == null 
			|| userRegisterBean.getUsername() == null
			|| userRegisterBean.getPassword() == null 
			|| (userRegisterBean.getUsername().indexOf('@') == -1 && userRegisterBean.getEmail().indexOf('@') == -1)) {
			respBean.setStatus("Failed");
			respBean.setMessage("Incomplete or bad input(either username, password or email.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
		}
//		String authorityString = user.getAuthorities();
//		List<GrantedAuthority> authlist = AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString);
//		if (authlist == null) {
//			respBean.setStatus("Failed");
//			respBean.setMessage("Incomplete or bad input(authority, "
//					+ "should be like \"ROLE_USER\", or \"ROLE_USER,ROLE_ADMIN\" ).");
//			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
//		}
		User existedUser = userService.findUserByUsername(userRegisterBean.getUsername());
		if (existedUser != null) {
			respBean.setStatus("Failed");
			respBean.setMessage("User already existed.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		user.setPassword(EncryptionUtil.digestWithMD5(userRegisterBean.getPassword()));
		user.setUsername(userRegisterBean.getUsername());
		user.setAuthorities("ROLE_waitforverify");
		user.setEmail(userRegisterBean.getEmail());
		userService.saveUser(user);
		User savedUser = userService.findUserByUsername(user.getUsername());
		UserBean userBean = UserBeanUtil.getUserBeanFromUser(savedUser);
		Map<String, Object> map = new HashMap<>();
		map.put("user", userBean);
		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities());
		UserAuthorities userAuthorities = new UserAuthorities(savedUser.getUsername(), authorities);
		String accessToken = tokenUtil.createAccessToken(userAuthorities);
		String refreshToken = tokenUtil.createRefreshToken(userAuthorities);
		map.put("accessToken", accessToken);
		map.put("refreshToken", refreshToken);
		respBean.setStatus("OK");
		respBean.setData(map);
		

    	Random r = new Random();
    	int Low = 100000;
    	int High = 999999;
    	int code = r.nextInt(High-Low) + Low;
    	String codeKey = String.valueOf(code);
    	String page = HtmlPageComposer.registerConfirmation(user,
    				codeKey,mailServiceServer,mailServicePort,mailLinkExpireInHours);
    	emailService.send(user, page);
		redisStoreService.setVerificationCode(codeKey, codeKey, mailLinkExpireInHours, TimeUnit.HOURS);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/userid/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseBean> updateUser(@RequestBody UserRegisterBean userRegisterBean,
			@PathVariable("userId") int userId) {
		ResponseBean respBean = new ResponseBean();
		Map<String, Object> map = new HashMap<>();
		User fetchUser = userService.findUserById(userId);
		if (fetchUser.getUsername().equals(userRegisterBean.getUsername())) {
			respBean.setStatus("Failed");
			respBean.setMessage("Usernames can not be updated.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
		}
		if (userRegisterBean.getPhonenumber() != null) {
			fetchUser.setPhonenumber(userRegisterBean.getPhonenumber());
		}
		
		if (userRegisterBean.getAddress() != null) {
			fetchUser.setAddress(userRegisterBean.getAddress());
		}
		userService.saveUser(fetchUser);
		map.put("user", UserBeanUtil.getUserBeanFromUser(fetchUser));
		respBean.setData(map);
		respBean.setStatus("OK");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	 
	@RequestMapping(value = "/username", method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> getAllUsers() {
		List<User> userlist = (List<User>) userService.listAllUsers();
		List<UserBean> userBeanList = new ArrayList<>();
		for (User user: userlist) {
			userBeanList.add(UserBeanUtil.getUserBeanFromUser(user));
		}
		ResponseBean respBean = new ResponseBean();
		Map<String, Object> map = new HashMap<>();
		map.put("users", userBeanList);
		respBean.setData(map);
		respBean.setStatus("OK");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/userid", method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> getAllUsersFromId() {
		return getAllUsers();
	}
	
	@RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> getUserByUsername(@PathVariable("username") String username) {
		ResponseBean respBean = new ResponseBean();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			respBean.setStatus("Failed");
			respBean.setMessage("User not found.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> map = new HashMap<>();
		UserBean userBean = UserBeanUtil.getUserBeanFromUser(user);
		map.put("user", userBean);
		respBean.setData(map);
		respBean.setStatus("OK");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/userid/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> getUserByUserId(@PathVariable("userId") int userId) {
		ResponseBean respBean = new ResponseBean();
		User user = userService.findUserById(userId);
		if (user == null) {
			respBean.setStatus("Failed");
			respBean.setMessage("User not found.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> map = new HashMap<>();
		UserBean userBean = UserBeanUtil.getUserBeanFromUser(user);
		map.put("user", userBean);
		respBean.setData(map);
		respBean.setStatus("OK");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	
    @RequestMapping(value="/register/confirm", method = RequestMethod.GET)
    public ResponseEntity<ResponseBean> getConfirm (@RequestParam(value = "id") Integer id, @RequestParam(value = "code") Integer code) {
    	User fetchUser = userService.findUserById(id);
    	String codeKey = String.valueOf(code);
    	String codeValue = redisStoreService.getVerificationCode(codeKey);
		ResponseBean respBean = new ResponseBean();
    	if(codeKey.equals(codeValue)) {
    		String[] authorityStrings = fetchUser.getAuthorities().split(",");
    		List<String> authorities = new ArrayList<>();
    		for (String authority: authorityStrings) {
    			if (!authority.equals("ROLE_waitforverify")){
    				authorities.add(authority);
    			}
    		}
    		authorities.add("ROLE_USER");
    		fetchUser.setAuthorities(String.join(",", authorities));
    		userService.saveUser(fetchUser);

    		respBean.setStatus("OK");
    		respBean.setMessage("Verification successfull.");
    		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
    	}
    	respBean.setStatus("Failed");
    	respBean.setStatus("Your account can not be verified.");
    	return new ResponseEntity<ResponseBean>(respBean, HttpStatus.EXPECTATION_FAILED);
    }
    
	
	@RequestMapping(value = "/savecard", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> saveCreditCardInfo (@RequestBody Restaurant restaurant) {
		ResponseDTO response = new ResponseDTO();
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
}

