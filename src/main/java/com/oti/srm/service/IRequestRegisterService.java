package com.oti.srm.service;

import java.util.List;

import com.oti.srm.dto.Pager;
import com.oti.srm.dto.Request;

public interface IRequestRegisterService {
	public enum RequestResult {
		SUCCESS, FAIL;
	}
	public static final int REQUEST_SUCCESS = 0;
	public static final int REQUEST_FAIL = 1;
	
	public int request(Request request);

	public List<Request> getRequestList(Request request, Pager pager);

	public int getPresentStep(int rno);
	
	
	
}
