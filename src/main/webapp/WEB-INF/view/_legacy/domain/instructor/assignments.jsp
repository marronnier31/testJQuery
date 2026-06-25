<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>JSP 실습</title>
<jsp:include page="/WEB-INF/view/common/common_include.jsp" />
</head>
<body>
	<div id="wrap_area">
		<div id="container">
			<ul>
				<li class="lnb"><jsp:include page="/WEB-INF/view/common/lnbMenu.jsp" /></li>
				<li class="contents">
					<div class="content">
						<jsp:include page="/WEB-INF/view/common/header.jsp">
							<jsp:param name="menu1" value="JSP 실습" />
							<jsp:param name="menu2" value="assignments" />
						</jsp:include>

						<p class="conTitle">
							<span>JSP 실습</span>
						</p>

						<div class="container">
							<!-- JSP 화면 실습 영역 -->
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>
