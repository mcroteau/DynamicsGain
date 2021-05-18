<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.goioc.Parakeet" %>

<html>
<head>
    <title><decorator:title default="Akki : Activity Management"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">

<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/packages/normalize.css.css?v=<%=System.currentTimeMillis()%>">--%>
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/packages/cutegrids.css?v=<%=System.currentTimeMillis()%>">--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/default.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/jquery.js"></script>

</head>
<body>

<div class="container">

    <style>
        body{
            padding:0px !important;
            background: #fff;
        }
        .container{
            background: #ffffff;
            margin-left:auto;
            margin-right:auto;
            /*margin-top:10px;*/
            /*width:98%;*/
            padding:0px;
            padding-bottom:100px;
            drop-shadow(0px 6px 40px black)
            margin-top:20px;
        }
        @media screen and (min-width: 690px) {
            .inside-container{
                padding:21px 20px !important;
            }
        }
        @media screen and (max-width: 690px) {
            .inside-container{
                padding:21px 20px !important;
            }
        }
        h1.inside-header{
            margin-top:29px;
        }
        #header-wrapper{
            width:100%;
            border-bottom:solid 1px #e6e8ea;
        }

        .prospects-href{
            font-size:16px;
            font-weight: 900;
            padding:13px 17px;
            background: #efefef;
            text-decoration: none;
            float:left;
            display: inline-block;
        }

        .create-prospect{
            color: #fff;
            background: #222227;
            border-right:solid 1px #000;
        }

        .search-propsects{
            color:#fff;
            background: #4889f6;
            background: #3876de;
        }

        .stats-href{
            color:#fff;
            background:#fffa00;
            background: #f3b607;
        }

        #welcome{
            float:right;
        }

        #welcome,
        #welcome a{
            color:#000;
            font-size:19px;
            font-weight:300;
            padding-top:10px;
            padding-right:10px;
            text-decoration: none;
        }

        #welcome a{
            text-decoration: underline;
        }
    </style>
    <style>
        .sales-activities{
            height:71px;
            overflow-y: scroll;
            border-bottom: solid 1px #e6e8ea;
        }
        .sales-activities .sales-activity{
            float:left;
            padding:15px 30px;
            text-decoration: none;
            font-size:15px;
        }
        .sales-activities .sales-activity span {
            display:block;
        }
        .upcoming-activities{

        }
        .upcoming-activities .sales-activity{
            text-decoration: none;
        }
        .divider{
            border-right:solid 1px #e6e8ea;
        }
        .bottom-divider{
            height:2px;
            border-top: solid 1px #e6e8ea;
        }
    </style>
    <style>
        .primary{
            min-height: 201px;
        }
        .akki-sig-left{
            float:left;
            width:9px;
            margin-top:15px;
        }
        .akki-sig-left span{
            display:block;
        }
        .color{
            height:19px;
        }
        .uno{
            background: #222227;
            background: #000;
        }
        .dos{
            background: #fffa00;
            background: #e24133;
        }
        .tres{
            background: #4889f6;
            background: #f3b607;
        }
        .quatro{
            background: #fff;
        }
        .cinco{
            background: #f3b607;
            background: #4889f6;
        }
        .seies{
            background: #e24133;
            background: #fffa00;
            background: #222227;
        }
        .siete{
            background: #000;
            background: #222227;
            background: #fffa00;
        }
        .akki-sig{
            height:7px;
        }
        .akki-sig .color{
            float:left;
            height:100%;
        }
        .akki-styles .color.text{
            padding:10px 0px 21px 0px;
            text-align: center;
            text-decoration: none;
            vertical-align: middle !important;
            display: block;
        }
        .akki-sig .text span{
            color: #fff;
            font-weight: 900;
            font-size: 16px;
            display: block;
            vertical-align: middle;
            margin-top:20px;
        }
        .plus{
            margin-top:5px !important;
            font-size:30px !important;
            padding-bottom:5px;
        }
        .une{
            width:15px;
            background:#4889f6;
            background: #000;
        }
        .deux{
            width:25px;
            background:#fffa00;
            background:#e24133;
        }
        .deux span{
            color:#fff !important;
        }
        .trois{
            width:15px;
            background:#000;
            background:#f3b607;
        }
        .quatre{
            width:15px;
            background: #222227;
            background: #fff;
        }
        .cinc{
            width:15px;
            background: #4889f6;
        }
        .seis{
            width:15px;
            background:#fffa00;
        }
        .sept{
            width:15px;
            background: #222227;
        }
        .clear{
            clear: both;
        }
    </style>
    <style>
        .row-container{
            display: table;
            width:100%;
        }
        .inside-container{
            padding:21px 49px;
        }
        .inside-container-row{
            padding:21px 49px;
            display:table-cell;
        }
        #prospect-search{
            width:100%;
            color: #4281ea;
        }
        #search-button{
            margin-top:23px;
        }
    </style>
    <style>
        .horizontal-form{
            float:left;
        }
        #prospect-back{
            display:inline-block;
            margin-bottom: 30px;
            clear:both;
        }
        .akki-styles{
            height:43px;
        }
        label{
            display:block;
            margin:20px 0px 0px;
        }
        input[type="text"]{
        }
    </style>

    <div id="header-wrapper">
        <a href="${pageContext.request.contextPath}/prospects/create" class="prospects-href create-prospect">+</a>
        <a href="${pageContext.request.contextPath}/prospects" class="prospects-href search-propsects">+ Prospects</a>
<%--        <a href="${pageContext.request.contextPath}/stats" class="prospects-href stats-href">+ Stats</a>--%>

        <div id="welcome">Hello <a href="${pageContext.request.contextPath}/users/edit/${sessionScope.userId}">${sessionScope.username}</a>!</div>
        <br class="clear"/>
    </div>

    <c:if test="${prospectActivities.size() > 0}">
        <div class="sales-activities">
            <c:forEach items="${prospectActivities}" var="prospectActivity">
                <a href="${pageContext.request.contextPath}/prospects/${prospectActivity.prospectId}" class="sales-activity">
                    <span class="activity-date"><strong>${prospectActivity.prettyTime}</strong> : ${prospectActivity.name}</span>
                    <span class="activity-prospect">${prospectActivity.prospectName}</span>
                </a>
            </c:forEach>
            <br class="clear"/>
        </div>
    </c:if>


    <decorator:body />

    <br class="clear"/>

</body>
</html>