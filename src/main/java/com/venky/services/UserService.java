package com.venky.services;

import com.venky.binding.LoginForm;
import com.venky.binding.SignupForm;
import com.venky.binding.UnlockForm;

public interface UserService {

	
	
	public String unlockAccount(UnlockForm form);
	
	public String resetPassword(String email);
	
	public String loginUser(LoginForm form);

	public boolean getSignUpForm(SignupForm form);

}
