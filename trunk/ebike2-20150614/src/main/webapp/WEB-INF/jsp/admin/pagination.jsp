<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="list-pagination">
	<ul class="pagination center">
		<li <c:if test="${pager.curPage==1}">class="disabled"</c:if> ><a  <c:if test="${pager.curPage!=1}">href="${domain}?page=1"</c:if> >首页</a></li>
		<li <c:if test="${pager.startPage==pager.curPage}">class="disabled"</c:if> ><a  <c:if test="${pager.startPage!=pager.curPage}">href="${domain}?page=${pager.curPage-1}"</c:if> >&laquo;</a></li>
		<c:forEach  varStatus="status" begin="${pager.startPage}" end="${pager.endPage}" step="1">
			<li <c:if test="${status.index==pager.curPage}">class="active"</c:if> ><a href="${domain}?page=${status.index}">${status.index}</a></li>	
		</c:forEach>	
		<c:choose>
			<c:when test="${pager.totalPages == '0'}">
				<li class="disabled"><a>&raquo;</a></li>	    		
				<li class="disabled"><a>尾页</a></li>	
			</c:when>
			<c:otherwise>
				<li <c:if test="${pager.endPage==pager.curPage}">class="disabled"</c:if> ><a <c:if test="${pager.endPage!=pager.curPage}">href="${domain}?page=${pager.curPage+1}"</c:if>>&raquo;</a></li>	    		
				<li <c:if test="${pager.curPage==pager.totalPages}">class="disabled"</c:if> ><a <c:if test="${pager.curPage!=pager.totalPages}">href="${domain}?page=${pager.totalPages}"</c:if>>尾页</a></li>	    		
			</c:otherwise>
		</c:choose>		
	</ul>
</div>