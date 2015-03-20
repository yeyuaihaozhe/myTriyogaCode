<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>${title}</title>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/jquery.js"></script>
<script>
	/*
	 * 注意：
	 * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
	 * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
	 * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
	 *
	 * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
	 * 邮箱地址：weixin-open@qq.com
	 * 邮件主题：【微信JS-SDK反馈】具体问题
	 * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
	 */
	var appid = "${appid}";
	var timeStamp = "${timestamp}";
	var nonceStr = "${nonce}";
	var signature = "${signature}";
	wx.config({
		debug : false,
		appId : appid,
		timestamp : timeStamp,
		nonceStr : nonceStr,
		signature : signature,
		jsApiList : [ 'checkJsApi', 'onMenuShareTimeline',
				'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo',
				'hideMenuItems', 'showMenuItems', 'hideAllNonBaseMenuItem',
				'showAllNonBaseMenuItem', 'translateVoice', 'startRecord',
				'stopRecord', 'onRecordEnd', 'playVoice', 'pauseVoice',
				'stopVoice', 'uploadVoice', 'downloadVoice', 'chooseImage',
				'previewImage', 'uploadImage', 'downloadImage',
				'getNetworkType', 'openLocation', 'getLocation',
				'hideOptionMenu', 'showOptionMenu', 'closeWindow',
				'scanQRCode', 'chooseWXPay', 'openProductSpecificView',
				'addCard', 'chooseCard', 'openCard' ]
	});
	wx.ready(function() {

	});
	wx.error(function (res) {
		 alert(res.errMsg);
	});
</script>
<script>
	function is_weixin() {
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == "micromessenger") {
			return true;
		} else {
			return false;
		}
	}
	var isWeixin = is_weixin();
	var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight
			: document.documentElement.clientHeight;

	function loadHtml() {
		var div = document.createElement('div');
		div.id = "remove";
		div.innerHTML = '<p><img id="weixin-tip" src="/live_weixin.png" alt="微信打开"/></p>';
		document.body.appendChild(div);
		$("#remove").click(function() {
			$(this).remove();
		});
	}

	function loadStyleText(cssText) {
		var style = document.createElement('style');
		style.rel = 'stylesheet';
		style.type = 'text/css';
		try {
			style.appendChild(document.createTextNode(cssText));
		} catch (e) {
			style.styleSheet.cssText = cssText; //ie9以下
		}
		var head = document.getElementsByTagName("head")[0]; //head标签之间加上style样式
		head.appendChild(style);
	}
	function runUrl(linkpath) {
		if (is_weixin()) {
			var cssText = "#weixin-tip{position: fixed; left:0; top:0; background: rgba(0,0,0,0.8); filter:alpha(opacity=80); width: 100%; z-index: 100;} #weixin-tip p{text-align: center; margin-top: 10%; padding:0 5%;}";
			loadHtml();
			loadStyleText(cssText);
		} else {
			window.location.assign(linkpath);
		}
	}
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$("img").css({
			"width" : "100%",
			"height" : "auto"
		});
	});
</script>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

img {
	max-width: 100%;
	height: auto;
}
</style>
</head>
<body>
	<div
		style="word-wrap: break-word; word-break: normal; font-size: 20px; border-top: #83a1e5 3px solid">
		<div style="font-family: '黑体'; COLOR: #093b84; font-size: 25px;"
			align="center">
			${title}<br />
			<p style="font-size: 16px;">创建日期：${createdate}</p>
		</div>
		<hr />
		<div style="width: 98%; margin: 0 auto;">${content}</div>
		<hr />

		<c:forEach items="${filesInfo.normalList}" var="link">
			<a href="${link.linkpath }" target="_blank">${link.imagefilename}</a> (${link.filesize })<br />
		</c:forEach>
		<c:if test="${filesInfo.otherSize>0}">
			<hr />其他附件<hr />
			<c:forEach items="${filesInfo.otherList}" var="link">
				<a href="#" onclick="runUrl('${link.linkpath }')">${link.imagefilename}</a> (${link.filesize })<br />
			</c:forEach>
		</c:if>
	</div>
</body>
</html>
