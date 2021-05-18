<html>
<head>
    <title>Search Prospects</title>
</head>
<body>


    <div class="inside-container">

        <form action="${pageContext.request.contextPath}/prospects/search" method="get">
            <h2>Search Prospects: </h2>

            <p>${prospectCount} Prospects available</p>

            <input type="text" name="q" placeholder="Frank's Wine & Dine" id="prospect-search"/>

            <input type="submit" value="Search Prospects" class="button super" id="search-button" style="float: right"/>
            <br class="clear"/>
        </form>
    </div>


</body>
</html>
