<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
:root {
--color-white: #fff; 
--color-black: #333; 
--color-gray: #75787b; 
--color-gray-light: #bbb; 
--color-gray-disabled: #e8e8e8; 
--color-green: #53a318; 
--color-green-dark: #383; 
--font-size-small: .75rem; 
--font-size-default: .875rem;
}
												
.progress_bar {
display: flex;
justify-content: space-between;
list-style: none;
padding: 0;
margin: 0 0 1rem 0;
}
												
												.progress_bar li {
													flex: 2;
													position: relative;
													padding: 0 0 14px 0;
													font-size: var(--font-size-default);
													line-height: 1.5;
													color: var(--color-green);
													font-weight: 600;
													white-space: nowrap;
													overflow: visible;
													min-width: 0;
													text-align: center;
													border-bottom: 2px solid var(--color-gray-disabled);
												}
												
												.progress_bar li:first-child, .progress_bar li:last-child {
													flex: 1;
												}
												
												.progress_bar li:last-child {
													text-align: right;
												}
												
												.progress_bar li:before {
													content: "";
													display: block;
													width: 8px;
													height: 8px;
													background-color: var(--color-gray-disabled);
													border-radius: 50%;
													border: 2px solid var(--color-white);
													position: absolute;
													left: calc(50% - 6px);
													bottom: -7px;
													z-index: 3;
													transition: all .2s ease-in-out;
												}
												
												.progress_bar li:first-child:before {
													left: 0;
												}
												
												.progress_bar li:last-child:before {
													right: 0;
													left: auto;
												}
												
												.progress_bar span {
													transition: opacity .3s ease-in-out;
												}
												
												.progress_bar li:not(.is_active) span {
													opacity: 0;
												}
												
												.progress_bar .is_complete:not (:first-child):after, .progress_bar .is_active:not(:first-child):after {
													
													content: "";
													display: block;
													width: 100%;
													position: absolute;
													bottom: -2px;
													left: -50%;
													z-index: 2;
													border-bottom: 2px solid var(- -color-green);
												}
												
												.progress_bar li:last-child span {
													width: 200%;
													display: inline-block;
													position: absolute;
													left: -100%;
												}
												
												.progress_bar .is_complete:last-child:after, .progress_bar .is_active:last-child:after
													{
													width: 200%;
													left: -100%;
												}
												
												.progress_bar .is_complete:before {
													background-color: var(--color-green);
												}
												
												.progress_bar .is_active:before, .progress_bar li:hover:before,
													.progress_bar .is-hovered:before {
													background-color: var(--color-white);
													border-color: var(--color-green);
												}
												
												.progress_bar li:hover:before, .progress_bar .is-hovered:before {
													transform: scale(1.33);
												}
												
												.progress_bar li:hover span, .progress_bar li.is-hovered span {
													opacity: 1;
												}
												
												.progress_bar:hover li:not (:hover) span {
													opacity: 0;
												}
												.progress_bar .has-changes {
													opacity: 1 !important;
												}


</style>

<ol class="progress_bar">
	<li class="is_complete"><span>요청</span></li>
	<li class="is_complete"><span>접수</span></li>
	<li class="is_complete"><span>개발</span></li>
	<li class="is_complete"><span>테스트</span></li>
	<li class="is_complete"><span>유저테스트</span></li>
	<li class="is_complete"><span>배포</span></li>
	<li class="is_active"><span>완료</span></li>
</ol>









