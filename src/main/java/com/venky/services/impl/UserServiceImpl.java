package com.venky.services.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.venky.binding.LoginForm;
import com.venky.binding.SignupForm;
import com.venky.binding.UnlockForm;
import com.venky.entities.UserDtlsEntity;
import com.venky.repository.UserRepository;
import com.venky.services.UserService;
import com.venky.utils.EmailUtils;
import com.venky.utils.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PwdUtils pwdUtils;

	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private HttpSession session;

	@Override
	public boolean getSignUpForm(SignupForm form) {

		UserDtlsEntity entity = repo.findByEmail(form.getEmail());

		if(entity != null) {
		return false;
		}

		UserDtlsEntity user = new UserDtlsEntity();
		BeanUtils.copyProperties(form, user);

		String pwd = pwdUtils.generatePwd();
		
		String to=form.getEmail();
		
		user.setAccountStatus("Locked");
		user.setPassword(pwd);

		repo.save(user);
		
		String subject="unlock your account";

		StringBuffer body = new StringBuffer("");

		//sb.append("<h1>Hey, "+form.getName()+"</h1>");
		

		body.append("<h3>Unlock your account with this temporary password</h3>");

		body.append("<br>");

		body.append("<p>Temporary password : <B>" + pwd +"</B> </p>");

		body.append("<br>");

		body.append("<a href=\"http://localhost:8080/unlock?mail="+form.getEmail()+"\">Click here to unlock your account </a>");

		/*
		String mailText ="Hey,"+form.getName()+".Your Temporary Password : "+ pwd +
				". Unlock your account with Temporary Password ."+ 
				"http://localhost:8080/unlock?mail="+form.getEmail();
		 */

		emailUtils.sendEmail(to,subject,String.valueOf(body));

		

		return true;
	}

	@Override
	public String unlockAccount(UnlockForm form) {

		UserDtlsEntity user = repo.findByEmail(form.getEmail());

		if(!form.getTempPassword().equals(user.getPassword())) {
			return "Temporary Password is not matching";
		}

		if(!form.getNewPassword().equals(form.getConfirmPassword())) {
			return "Password not matching";
		}

		user.setPassword(form.getNewPassword());
		user.setAccountStatus("Unlocked");

		repo.save(user);

		return "Account unlocked";
	}

	@Override
	public String resetPassword(String email) {
		UserDtlsEntity entity = repo.findByEmail(email);
		if(entity == null) {
			return "Account doesn't exist with this email";
		}

		//String pwd = pwdUtils.generatePwd(6);
		String subject="recovery password";
		String body="your password:"+entity.getPassword();
	
		emailUtils.sendEmail(email, subject, body);
		return "Check your email";
	}

	@Override
	public String loginUser(LoginForm form) {
		UserDtlsEntity entity = repo.findByEmailAndPassword(form.getEmail(),form.getPassword());
		
		if(entity == null) {
			return "Invalid credentials";
		}
		
		if(entity.getAccountStatus().equalsIgnoreCase("locked")) {
			return "Your account need to be unlocked";
		}
		
		session.setAttribute("userID", entity.getUserId());
		
		return "success";
	}

}
