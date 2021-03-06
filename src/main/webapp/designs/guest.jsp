<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Dynamics +Gain : <decorator:title default="End Homelessness Movement"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,900&display=swap');
    </style>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/packages/grid.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/default.css?v=<%=System.currentTimeMillis()%>">

    <script src="${pageContext.request.contextPath}/assets/js/packages/d3.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/packages/topo.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/data.maps.js"></script>

    <style>

        a.gospirit{
            color:#000;
            font-size:47px;
            font-style: italic;
            text-decoration: none;
            line-height: 1.0em;
        }
        a.gospirit .plus{

        }
        a.gospirit span{
            font-size: 47px;
            line-height: 1.0em;
        }
        .spirit{
            /*background: rgb(255,75,176);*/
            color: rgb(84,175,255);
            /*background: linear-gradient(101deg, rgba(255,107,185,1) 0%, rgb(255, 107, 185) 19%, rgba(253,254,3,1) 26%, rgba(44,244,0,1) 28%, rgba(44,244,0,1) 49%, rgba(84,175,255,1) 54%, rgba(84,175,255,1) 72%, rgba(0,0,0,1) 72%);*/
            /*-webkit-background-clip: text;*/
            /*-moz-background-clip: text;*/
            /*-webkit-text-fill-color: transparent;*/
            /*-moz-text-fill-color: transparent;*/
        }
        .signature{
            background: rgb(255,75,176);
            background: linear-gradient(101deg, rgba(255,75,176,1) 0%, rgba(255,75,176,1) 19%, rgba(253,254,3,1) 26%, rgba(44,244,0,1) 28%, rgba(44,244,0,1) 49%, rgba(84,175,255,1) 54%, rgba(84,175,255,1) 72%, rgba(0,0,0,1) 72%);
        }
        p{
            font-family: Roboto;
            margin:0px;
        }

        #map{
            cursor: pointer;
            cursor: hand;
        }
        #guest-navigation a{
            text-decoration: none;
            display:block;
            clear:both;
            color:#000;
            font-family: Roboto;
            font-weight:700;
            font-size:21px;
            line-height:1.5em;

        }
        #footer-navigation{
            margin:30px 0px 200px 0px;
        }
        .center{
            text-align: center;
        }
        label{
            display:block;
            clear:both;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="row">

        <div class="col-sm-12">

            <div id="header">
                <div style="float:left">
                    <a href="${pageContext.request.contextPath}/" class="gospirit">Dynamics<span class="plus"></span><br/>
                        <span class="spirit">Gain+</span></a>
<%--                    <a href="${pageContext.request.contextPath}" class="gospirit">Go<span class="plus">+</span><br/>--%>
<%--                        <span class="spirit">Spirit</span></a>--%>
                    <p style="font-size: 16px;">End Homelessness Movement.</p>
                </div>
                <div style="float:left;">
                    <parakeet:isAuthenticated>
                        &nbsp;<strong class="highlight" style="font-family: roboto-slab-semibold !important">Signed in <a href="${pageContext.request.contextPath}/" class="href-dotted">My Profile</a></strong>&nbsp
                        &nbsp;<a href="${pageContext.request.contextPath}/signout" class="href-dotted">Signout</a>&nbsp;
                    </parakeet:isAuthenticated>
                </div>
                <div style="float:right; text-align: right" id="guest-navigation">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                    <a href="${pageContext.request.contextPath}/donate">Give</a>
                    <a href="${pageContext.request.contextPath}/organizations">Organizations</a>
                    <a href="${pageContext.request.contextPath}/about">About</a>
                    <parakeet:isAnonymous>
                        <a href="${pageContext.request.contextPath}/signin">Signin</a>
                    </parakeet:isAnonymous>
                    </div>
            </div>

            <br class="clear"/>

            <div id="guest-content" class="center">
                <decorator:body />
            </div>

        </div>

    </div>
</div>

<script async src="https://www.googletagmanager.com/gtag/js?id=G-EF5QWVVWFK"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'G-EF5QWVVWFK');
</script>

</body>
</html>