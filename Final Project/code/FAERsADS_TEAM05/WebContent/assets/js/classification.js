/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global _ */

/**
 * 
 */
$(function () {
    var drugname = [];
    var route = [];
    var doseunit = [];
    var doseform = [];
    var dosefreq = [];
    var manufacturer = [];
    var reaction = [];
    var y = [];
    var type = [];
    var score = [];
    var options = {
        chart: {
            renderTo: 'outcomechart',
            type: 'column'
        },
        title: {
            text: 'Drug Outcome Probability'
        },
        xAxis: {
            categories: type
        },
        series: {
            data: y
        },
        yAxis: {
            title: {
                text: 'percentages'
            }
        }
    };
    var chart = new Highcharts.Chart(options);
    $.ajax({
        type: "GET",
        url: "outcome.htm",
        contentType: "application/json",
        dataType: 'json',
        success: function (response) {
            for (var key in response) {
                drugname.push(response[key].drugname);
                route.push(response[key].route);
                doseunit.push(response[key].doseunit);
                doseform.push(response[key].doseform);
                dosefreq.push(response[key].dosefrequency);
                manufacturer.push(response[key].manufacturer);
                reaction.push(response[key].reaction);
            }
            uniquedrugs = _.uniq(drugname);
            var options = $("#drugname");
            $.each(uniquedrugs, function (item) {
                options.append($("<option />").val(uniquedrugs[item]).text(uniquedrugs[item]));
            });

            uniqueroute = _.uniq(route);
            var options = $("#route");
            $.each(uniqueroute, function (item) {
                options.append($("<option />").val(uniqueroute[item]).text(uniqueroute[item]));
            });

            uniquedoseunit = _.uniq(doseunit);
            var options = $("#doseunit");
            $.each(uniquedoseunit, function (item) {
                options.append($("<option />").val(uniquedoseunit[item]).text(uniquedoseunit[item]));
            });

            uniquedoseform = _.uniq(doseform);
            var options = $("#doseform");
            $.each(uniquedoseform, function (item) {
                options.append($("<option />").val(uniquedoseform[item]).text(uniquedoseform[item]));
            });

            uniquedosefreq = _.uniq(dosefreq);
            var options = $("#dosefreq");
            $.each(uniquedosefreq, function (item) {
                options.append($("<option />").val(uniquedosefreq[item]).text(uniquedosefreq[item]));
            });

            uniquemanufacturer = _.uniq(manufacturer);
            var options = $("#mfndetails");
            $.each(uniquemanufacturer, function (item) {
                options.append($("<option />").val(uniquemanufacturer[item]).text(uniquemanufacturer[item]));
            });

            uniquereaction = _.uniq(reaction);
            var options = $("#reactionlist");
            $.each(uniquereaction, function (item) {
                options.append($("<option />").val(uniquereaction[item]).text(uniquereaction[item]));
            });




//                event.preventDefault();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        },
        headers: {
            Accept: "application/json"
        }
    });


    $("#classify").submit(function (event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "apicall.htm",
            data: {
                drugname: $("#drugname").val(),
                reactionlist: $("#reactionlist").val(),
                doseform: $("#doseform").val(),
                doseunit: $("#doseunit").val(),
                dosefreq: $("#dosefreq").val(),
                mfndetails: $("#mfndetails").val(),
                route: $("#route").val(),
                doseamt: $("#doseamt").val(),
            },
            contentType: "application/json",
            dataType: 'json',
            success: function (response) {
                $("#jsonresult").empty();
                $("#jsonresult").css("display", "block");
                $("#classification").css("display", "block");
                $("#jsonresult").append($("<div>").text(response));
                var obj = JSON.parse(response);




                for (var key in obj.Results.outcomeresult.value.ColumnNames)
                    type.push(obj.Results.outcomeresult.value.ColumnNames[key]);
                type.pop();
                for (var key in obj.Results.outcomeresult.value.Values)
                    score.push(obj.Results.outcomeresult.value.Values);
                console.log(type);

                for (var i = 0; i < 7; i++) {
                    y.push(parseFloat(score[0][0][i]) * 100);
                }
                showChart(y, type);


            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);

            }

        });



    });
    function getObjects(obj, key, val) {
        var retv = [];

        if (jQuery.isPlainObject(obj))
        {
            if (obj[key] === val) // may want to add obj.hasOwnProperty(key) here.
                retv.push(obj);

            var objects = jQuery.grep(obj, function (elem) {
                return (jQuery.isArray(elem) || jQuery.isPlainObject(elem));
            });

            retv.concat(jQuery.map(objects, function (elem) {
                return getObjects(elem, key, val);
            }));
        }

        return retv;
    }

    function showChart(data, categories) {
        console.log(data);
        console.log(categories);
        console.log("in chart");
        $("#outcomechart").highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: 'Drug Outcome Probability'
            },
            xAxis: {
                categories: ['CA','DE','DS','HO','LT','OT','RI']
            },
            series: [{
                    name:"Outcome",
                    data: data
                }],
            yAxis: {
                title: {
                    text: 'percentages'
                }
            }
        });
        $("#hlabels").css("display", "block");
    }
});

