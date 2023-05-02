package com.venky.services;

import java.util.List;

import com.venky.binding.DashboardResponse;
import com.venky.binding.EnquiryForm;
import com.venky.binding.EnquirySearchCriteria;
import com.venky.entities.StudentEnqEntity;

public interface EnquiryService {
	
	List<String> getCourseNames();
	
	List<String> getEnquiryStatus();
	
	DashboardResponse getDashboardData(Integer userId);
	
	Boolean saveEnquiry(EnquiryForm form,Integer userId);
	
	List<StudentEnqEntity> getEnquiries(Integer userId,EnquirySearchCriteria search);
	
	StudentEnqEntity getStudentEnq(Integer eId);

}
