package com.oti.srm.controller.srm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.oti.srm.dto.ListFilter;
import com.oti.srm.dto.Member;
import com.oti.srm.dto.Pager;
import com.oti.srm.dto.Request;
import com.oti.srm.dto.RequestProcess;
import com.oti.srm.dto.SelectPM;
import com.oti.srm.dto.StatusHistoryFile;
import com.oti.srm.service.member.IUserRegisterService;
import com.oti.srm.service.srm.IRequestRegisterService;

import lombok.extern.log4j.Log4j2;

/**
 * @author KOSA
 *
 */
/**
 * @author KOSA
 *
 */
/**
 * @author KOSA
 *
 */
@Controller
@Log4j2
@RequestMapping("/customer")
public class RequestController {

	@Autowired
	private IUserRegisterService userRegisterService;
	@Autowired
	private IRequestRegisterService requestService;

	/**
	 * Kang Ji Seong 유저 등록 페이지 조회
	 */
	@GetMapping("/register")
	public String register(Model model) {
		List<System> systemList = userRegisterService.getSystemList();
		model.addAttribute("systemList", systemList);
		return "member/userregister";
	}

	/**
	 * Kang Ji Seong 유저 등록
	 */
	@PostMapping("/register")
	public String register(Member member, Model model) {
		log.info(member.toString());
		String address = member.getPostcode() + "-" + member.getAddr1() + "-" + member.getAddr2();
		member.setAddress(address);
		MultipartFile mfile = member.getMfile();

		try {
			if (mfile != null && !mfile.isEmpty()) {
				log.info(mfile.toString());
				member.setFileName(mfile.getOriginalFilename());
				member.setSavedDate(new Date());
				member.setFileType(mfile.getContentType());
				member.setFileData(mfile.getBytes());

				int result = userRegisterService.register(member);
				if (result == IUserRegisterService.REGISTER_FAIL) {
					return "redirect:/customer/register";
				} else {
					return "redirect:/";
				}

			} else {
				int result = userRegisterService.register(member);
				if (result == IUserRegisterService.REGISTER_FAIL) {
					return "redirect:/customer/register";
				} else {
					return "redirect:/";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registerResult", "FAIL");
			return "redirect:/customer/register";
		}

	}

	/**
	 * Kang Ji Seong 내 정보 조회
	 */
	@GetMapping("/mypage")
	public String myPage(HttpSession session, Model model) {
		// 내 정보 조회
		Member Sessionmember = (Member) session.getAttribute("member");
		Member returnMember = userRegisterService.getUserInfo(Sessionmember.getMid());

		// 주소 변환
		String[] address = returnMember.getAddress().split("-");
		returnMember.setPostcode(Integer.parseInt(address[0]));
		returnMember.setAddr1(address[1]);
		returnMember.setAddr2(address[2]);


		model.addAttribute("returnMember", returnMember);

		return "member/mypage";
	}

	/**
	 * Kang Ji Seong img byte[] 변환 메소드
	 */
	@GetMapping("/mypage/{mid}")
	public ResponseEntity<byte[]> returnImg(@PathVariable String mid) {
		
		Member returnMember = userRegisterService.getUserInfo(mid);
		log.info(returnMember.toString());
		
		if(returnMember.getMfile() == null) {
			
			returnMember = userRegisterService.getUserInfo("가입Test");
			
		}
		HttpHeaders headers = new HttpHeaders();
		String[] fileTypes = returnMember.getFileType().split("/");
		headers.setContentType(new MediaType(fileTypes[0], fileTypes[1]));
		headers.setContentDispositionFormData("attachment", returnMember.getFileName());
		return new ResponseEntity<byte[]>(returnMember.getFileData(), headers, HttpStatus.OK);

		
	}

	/**
	 * Kang Ji Seong 요청 등록 폼 요청
	 */
	@GetMapping("/request")
	public String customerRequest(Member member, Request request, Model model, RequestProcess requestProcess,
			HttpSession session) {

		// 로그인 member 정보는 JSP에서 SessionScope 이용하여 표시
		// 요청 단계 (default 값으로 지정하여 전달)
		request.setStatusName("접수중");
		request.setStatusNo(1);
		requestProcess.setReqType("정규");

		// 시스템 리스트 전달
		List<System> systemList = userRegisterService.getSystemList();

		model.addAttribute("request", request);
		model.addAttribute("requestProcess", requestProcess);
		model.addAttribute("systemList", systemList);

		return "srm/request";
	}

	/**
	 * 요청 등록 폼 작성
	 */
	@PostMapping("/request")
	public String customerRequest(Request request, Model model, HttpSession session,
			@RequestParam("mfile[]") MultipartFile[] files) {
		// 요청 상태값은 1
		request.setStatusNo(1);
		Member member = (Member) session.getAttribute("member");
		request.setClient(member.getMid());
		log.info(request.getSno());

		List<StatusHistoryFile> fileList = new ArrayList<>();

		try {
			if (files != null) {
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						StatusHistoryFile shf = new StatusHistoryFile();
						shf.setFileData(file.getBytes());
						shf.setFileName(file.getOriginalFilename());
						shf.setFileType(file.getContentType());
						fileList.add(shf);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int result = requestService.writeRequest(request, fileList);
		if (result == IRequestRegisterService.REQUEST_SUCCESS) {
			return "redirect:/customer/requestlist";
		} else {
			model.addAttribute("requestResult", "FAIL");
			return "redirect:/customer/request";
		}
	}

	/**
	 * 내 요청 조회
	 * @throws ParseException 
	 */
	@GetMapping("/myrequestlist")
	public String myrequestlist (Request request, Model model, HttpSession session,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "") String date_first,
			@RequestParam(defaultValue = "") String date_last, @RequestParam(defaultValue = "0") int sno,
			@RequestParam(defaultValue = "전체") String req_type, @RequestParam(defaultValue = "0") int statusNo) throws ParseException {
		
		
		// 요청 조회 필터
		List<System> systemList = userRegisterService.getSystemList();
		// 유저 정보 전달
		Member member = (Member) session.getAttribute("member");
		// 유저 id 저장
		request.setMid(member.getMid());
		
		// 전달받은 필터 값 저장 (단계 제외)
		ListFilter listFilter = new ListFilter();
		listFilter.setReqType(req_type);
		listFilter.setDateFirst(date_first);
		listFilter.setDateLast(date_last);
		listFilter.setSno(sno);
		listFilter.setStatusNo(statusNo);
		
		
		
		ListFilter returnList = requestService.dateFilterList(listFilter);
		log.info(returnList.toString());
		
		// 보여줄 행 수 조회
		int totalRows = requestService.getRequestListRows(listFilter, member);
		Pager pager = new Pager(7, 5, totalRows, pageNo);
		List<SelectPM> requestList = requestService.getMyRequestList(request, listFilter, pager, member);
		
		
		// 시스템 리스트 전달
		model.addAttribute("systemList", systemList);
		// 목록 리스트와 페이지 return
		model.addAttribute("requestList", requestList);
		model.addAttribute("pager", pager);
		// filter 전달
		model.addAttribute("listFilter", returnList);
		
		return "srm/myrequestlist";
	}

	/**
	 * 내 업무 목록 조회
	 * 
	 */
	@GetMapping("/requestlist")
	public String requestList(Request request, Model model, HttpSession session,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "") String date_first,
			@RequestParam(defaultValue = "") String date_last, @RequestParam(defaultValue = "0") int sno,
			@RequestParam(defaultValue = "전체") String req_type, @RequestParam(defaultValue = "0") int statusNo) {

		// 필터에 출력할 시스템 리스트 조회
		List<System> systemList = userRegisterService.getSystemList();

		// 전달받은 필터 값 저장
		ListFilter listFilter = new ListFilter();
		listFilter.setReqType(req_type);
		listFilter.setDateFirst(date_first);
		listFilter.setDateLast(date_last);
		listFilter.setSno(sno);
		listFilter.setStatusNo(statusNo);
		
		ListFilter returnList = requestService.dateFilterList(listFilter);
		log.info(returnList.toString());
		
		// 유저 권한 확인
		Member member = (Member) session.getAttribute("member");
		// 유저 id 저장
		request.setMid(member.getMid());
		// 보여줄 행 수 조회
		int totalRows = requestService.getMyWorkRows(listFilter, member);

		Pager pager = new Pager(7, 5, totalRows, pageNo);
		List<SelectPM> requestList = requestService.getMyWorkList(request, listFilter, pager, member);
		
		// 시스템 리스트 전달
		model.addAttribute("systemList", systemList);
		// 목록 리스트와 페이지 return
		model.addAttribute("requestList", requestList);
		model.addAttribute("pager", pager);
		// filter 전달
		model.addAttribute("listFilter", returnList);
		
//		return "srm/requestlist";
		log.info("담당 업무 리스트 수정");
		return "srm/requestlist_re";
	}

	/**
	 * Kang Ji Seong member type 단계 처리 가져오기
	 */
	@PostMapping("/viewstep")
	@ResponseBody
	public int viewStep(Model model, Request request) {
		int result = requestService.getPresentStep(request.getRno());
		return result;
	}
	
	/**
	 * Kang Ji Seong 요청 글 상세보기
	 */
	
	@GetMapping("/requestdetail")
	public String userRequestDetail(int rno, HttpSession session, Model model) {
		log.info("요청번호" + rno);
		Request request = requestService.getRequestDetail(rno);
		List<System> systemList = userRegisterService.getSystemList();
		
		log.info(request.getStatusNo());
		
		model.addAttribute("request", request);
		model.addAttribute("systemList", systemList);
		return "srm/requestdetail";
	}
	
	/**
	 * Kang Ji Seong 요청 글 파일 다운로드
	 */
	@RequestMapping("/requestdetail/filedownload")
	public ResponseEntity<byte[]> filDownload(int fno) {
		
		StatusHistoryFile fileList = requestService.getMyRequestFile(fno);
		final HttpHeaders headers = new HttpHeaders();
		String [] mtypes = fileList.getFileType().split("/");
		headers.setContentType(new MediaType(mtypes[0], mtypes[1]));
		headers.setContentDispositionFormData("attachment", fileList.getFileName());
		
		return new ResponseEntity<byte[]>(fileList.getFileData(), HttpStatus.OK);
	}
	
	
	
	

}
