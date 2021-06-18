<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.goioc.Parakeet" %>

<html>
<head>
    <title><decorator:title default="Dynamics +Gain : Activity Management"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/default.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/jquery.js"></script>
</head>
<body>

<div class="container">

    <style>
        body{
            padding:0px 30px 100px 30px;
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
    </style>
    <style>
        label{
            display:block;
            margin:20px 0px 0px;
        }
        table{
            width:100%;
        }
        tr td{
            font-size:21px;
        }
        #footer{
            margin-top:59px;
        }
        #header-navigation{
            margin-top:10px;
            float:left;
        }
    </style>

    <div id="header-wrapper">
        <div id="header-navigation">
            <a href="${pageContext.request.contextPath}/home" class="href-dotted">Home</a>&nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/admin/towns" class="href-dotted">Towns</a>&nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/admin/organizations" class="href-dotted">Organizations</a>&nbsp;&nbsp;
            <a href="${pageContext.request.contextPath}/admin/ownership/requests" class="href-dotted">Requests</a>&nbsp;&nbsp;
        </div>
        <div id="welcome">Hello <a href="${pageContext.request.contextPath}/users/edit/${sessionScope.userId}">${sessionScope.username}!</a></div>
        <br class="clear"/>
    </div>

    <div id="wrapper">
        <decorator:body />
    </div>

    <div id="footer">
        <a href="${pageContext.request.contextPath}/signout" class="href-dotted">Signout</a>
    </div>

    <br class="clear"/>

</body>
</html>