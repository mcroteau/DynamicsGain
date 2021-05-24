<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<html>
<head>
    <title>Go +Spirit : <decorator:title default="A Movement to End Pain"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,900&display=swap');
    </style>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/packages/grid.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/default.css?v=<%=System.currentTimeMillis()%>">

    <script src="http://cdnjs.cloudflare.com/ajax/libs/d3/3.5.3/d3.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/topojson/1.6.9/topojson.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/data.maps.js"></script>

    <style>
        h1.gospirit{
            font-style: italic;
        }

        h1 .spirit{
            background: rgb(255,75,176);
            background: linear-gradient(101deg, rgba(255,75,176,1) 0%, rgba(255,75,176,1) 19%, rgba(253,254,3,1) 26%, rgba(44,244,0,1) 28%, rgba(44,244,0,1) 49%, rgba(84,175,255,1) 54%, rgba(84,175,255,1) 72%, rgba(0,0,0,1) 72%);
            -webkit-background-clip: text;
            -moz-background-clip: text;
            -webkit-text-fill-color: transparent;
            -moz-text-fill-color: transparent;
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
        }
        #footer-navigation{
            margin:30px 0px 200px 0px;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="row">

        <div class="col-sm-12">

            <div id="header">
                <div style="float:left">
                    <h1 class="gospirit">Go<span class="plus">+</span><br/>
                        <span class="spirit">Spirit</span></h1>
                    <p>A Movement to End Pain.</p>
                </div>
                <div style="float:right; text-align: right" id="guest-navigation">
                    <a href="#">Home</a>
                    <a href="#">Give</a>
                    <a href="#">Organizations</a>
                    <a href="#">About</a>
                </div>
            </div>

            <br class="clear"/>

            <div id="guest-content">
                <decorator:body />

                <div id="footer-navigation">
                    <%if(!request.getServletPath().equals("/home")){%>
                        <a href="${pageContext.request.contextPath}/home" class="href-dotted">Home</a>
                    <%}%>

                    <%if(!request.getServletPath().equals("/about")){%>
                        &nbsp;<a href="${pageContext.request.contextPath}/about" class="href-dotted">About</a>
                    <%}%>

                    <parakeet:isAuthenticated>
                        &nbsp;<strong class="highlight" style="font-family: roboto-slab-semibold !important">Signed in <a href="${pageContext.request.contextPath}/" class="href-dotted">My Profile</a></strong>&nbsp;
                    </parakeet:isAuthenticated>
                    <parakeet:isAnonymous>
                        &nbsp;<a href="${pageContext.request.contextPath}/signin" class="href-dotted">Signin</a>&nbsp;
                    </parakeet:isAnonymous>

                </div>
            </div>

        </div>

    </div>
</div>

</body>
</html>