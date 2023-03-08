<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<body>
		
		<nav class="navbar navbar-expand topbar mb-3 static-top" style="background-color: #d7dde4;">

			<!-- Topbar Navbar -->
			<ul class="navbar-nav ml-auto">

				<!-- Nav Item - Alerts -->
				<c:if test="${member.mid == null}">
				
				<li class="nav-item dropdown no-arrow mx-1">
					<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/login" role="button" style="color: #5C6B7A;">
						<i class="fas fa-sign-in-alt"></i>
						Login
					</a>
				</li>
				<li class="nav-item dropdown no-arrow mx-1">
					<a class="nav-link dropdown-toggle" href="${pageContext.request.contextPath}/customer/register" role="button" style="color: #5C6B7A;">
						<i class="fas fa-user-plus"></i>
						SignUp
					</a>
					
				</li>
				</c:if>
				<c:if test="${member.mid != null}">
				<!-- Nav Item - Alerts -->
				<li class="nav-item dropdown no-arrow mx-1">
					<a class="nav-link dropdown-toggle" href="#" id="alertsDropdown" role="button"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<i class="fas fa-bell fa-fw"></i>
						<!-- Counter - Alerts -->
						<span class="badge badge-danger badge-counter">${fn:length(newAlertList)}</span>
					</a>
					<!-- Dropdown - Alerts -->
					<div class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in"
						aria-labelledby="alertsDropdown">
						<h6 class="dropdown-header">
							신규 내역 알림
						</h6>	
						<if test="${newAlertList==null}">
							<div class="m-3">신규 내역이 없습니다.</div>
						</if>				
						<c:forEach var="newAlert" items="${newAlertList}">
							<a class="dropdown-item d-flex align-items-center" href="#">
								<div class="mr-3">
									<div>
										<c:if test="${newAlert.statusNo == 1}">
											<span class="badge badge-warning">접수중</span>
										</c:if>										
										<c:if test="${newAlert.statusNo == 2}">
											<span class="badge badge-warning">접수완료</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 3}">
											<span class="badge badge-danger">개발재검토</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 5}">
											<span class="badge badge-primary">개발완료</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 7}">
											<span class="badge badge-success">테스트완료</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 9}">
											<span class="badge badge-info">품질검토완료</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 11}">
											<span class="badge badge-secondary">배포완료</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 12}">
											<span class="badge badge-danger">반려</span>
										</c:if>
										<c:if test="${newAlert.statusNo == 13}">
											<span class="badge badge-dark">완료</span>
										</c:if>														
									</div>
								</div>
								<div>
									<div class="small text-gray-500"><fmt:formatDate value="${newAlert.reqDate}" pattern="yyyy-MM-dd"/></div>
									<span class="font-weight-bold">${newAlert.reqTitle}</span>
								</div>
							</a>
						</c:forEach>
					</div>
				</li>
				<li class="nav-item dropdown no-arrow mx-1">
					<a class="nav-link dropdown-toggle" data-toggle="modal" data-target="#logoutModal" role="button" style="color: #5C6B7A;">
						<i class="fas fa-sign-out-alt"></i>
						Logout
					</a>
					
				</li>
				</c:if>
				

				<div class="topbar-divider d-none d-sm-block"></div>

				<!-- Nav Item - User Information -->
				<li class="nav-item dropdown no-arrow">
					<div class="nav-link dropdown-toggle" id="userDropdown">
						<img class="img-profile rounded-circle" src="${pageContext.request.contextPath}/customer/mypage/${sessionScope.member.mid}" >
						<a href="${pageContext.request.contextPath}/customer/mypage">
							<span class="ml-3 mr-3 d-lg-inline font-weight-bold " style="color: #5C6B7A;">${sessionScope.member.mname} 님</span>
						</a>
					</div>
				</li>
			</ul>
		</nav>
		<!-- Logout Modal-->
	    <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
	        aria-hidden="true">
	        <div class="modal-dialog" role="document">
	            <div class="modal-content">
	                <div class="modal-header">
	                    <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
	                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">×</span>
	                    </button>
	                </div>
	                <div class="modal-body">로그아웃을 원하시면 버튼을 눌러주세요.</div>
	                <div class="modal-footer">
	                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
	                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/logout">Logout</a>
	                </div>
	            </div>
	        </div>
	    </div>
	</body>
</html>


