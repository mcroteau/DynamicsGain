<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<span class="yellow" style="font-weight: 900; font-size:73px;">${count}</span>
<br/><span class="yellow highlight">Homeless</span>

<div id="map" style="margin:0px auto; width: 690px; height: 370px;"></div>

<p>* = data has yet to be entered.</p>

<p>Go +Spirit is an organization that attempts to remove barriers
    for those wishing to help others that are suffering. </p>

<h4>Homelessness by</h4>
<h1>State</h1>
<ul>
    <c:forEach items="${states}" var="state">
        <li><a href="${pageContext.request.contextPath}/states/${state.name}" class="href-dotted" style="line-height: 1.3em;">${state.name} ${state.countZero}</a></li>
    </c:forEach>
</ul>

<script>

    $.ajax({
        url : "${pageContext.request.contextPath}/home/map/data",
        success : success,
        error : error
    })

    function success(data){
        console.log(data)
        var idx = 0;
        var map = new Datamap({
            element: document.getElementById('map'),
            scope: 'usa',
            data: data,
            fills: {
                defaultFill: 'rgba(219,255,211,1.0)',
                defaultFill: 'rgba(249,251,255,1.0)',
                defaultFill: 'rgba(255,255,255,1.0)'
            },
            geographyConfig: {
                //highlightFillColor: 'rgba(84, 175, 255, 1)',
                highlightFillColor: 'rgba(161, 210, 255, 1)',
                highlightFillColor: 'rgba(255,251,117,1.0)',
                highlightFillColor: 'rgba(255,255,255,1.0)',
                borderColor : 'rgba(84,175,255,1)',
                borderColor : 'rgba(84,175,255,1)',
                borderColor : 'rgba(0,0,0,1)',
                borderColor: function(d) {
                    idx++
                    if (idx % 2 == 0) {
                        return '#000000';
                    }
                    if (idx % 3 == 0) {
                        return '#FE4BAF';
                    }
                    if (idx % 5 == 0) {
                        return '#2CF300';
                    }
                    return '#54AEFF';
                    // return '#797979';
                },
                /**
                 * king of pain
                 * i lose you guys
                 * i travel through canada, through with a ough not thru
                 * they make me write everyone and tell them to leave me alone
                 * during canada they piss off Ron, Nettie and Wade
                 * i travel to maryland
                 * drive down south and across the us
                 * they try to get me to walk in front of a moving truck
                 * then a lot of stuff and now we are reconnected...
                 * steve weatherby played NF, im not sure if that was bait or not
                 * i think it was, it was
                 */
                borderWidth: '4px',
                highlightBorderWidth: '5px',
                highlightBorderColor : 'rgba(0,0,0,1)',
                highlightBorderColor : 'rgba(84,175,255,1)',
                popupTemplate: function(geo, d) {
                    return ['<div class="hoverinfo"><strong>',
                        ' ' + geo.properties.name + ' tallied',
                        ': ' + d,
                        '</strong></div>'].join('');
                }
            },
            done: function(datamap) {
                datamap.svg.selectAll('.datamaps-subunit').on('click', function(geography) {
                    window.location = "${pageContext.request.contextPath}/states/" + geography.properties.name.toLowerCase();
                });
            }
        });

        map.labels({'customLabelText': data});
    }


    function error(e){
        console.log(e)
    }


</script>