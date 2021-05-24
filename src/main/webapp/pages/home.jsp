<div style="position:relative">
    <div id="map" style="position: relative; width: 690px; height: 370px;"></div>
</div>

<p>Go +Spirit is an organization that attempts to remove barriers
    for those wishing to help others that are suffering. </p>


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
                },
                borderWidth: '3px',
                highlightBorderWidth: '3px',
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
                    console.log(geography.properties);
                });
            }
        });

        map.labels({'customLabelText': data});
    }


    function error(e){
        console.log(e)
    }


</script>