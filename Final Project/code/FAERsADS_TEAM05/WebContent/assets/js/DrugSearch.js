/* global _ */

/**
 * 
 */
$(function () {
    var drugs = [];
    var reaction = [];
    var drugname = [];
    var reaction = [];
    var reactions = {};
    $.ajax({
        type: "GET",
        url: "search.htm",
        contentType: "application/json",
        dataType: 'json',
        success: function (response) {
            for (var key in response) {
                drugs.push(response[key].drugname);
                reaction.push(response[key].reactions)
            }

            reactions = {
                drugname: drugs,
                reaction: reaction
            };
            uniquedrugs = _.uniq(drugs);
            var options = $("#druglist");
            $.each(uniquedrugs, function (item) {
                options.append($("<option />").val(uniquedrugs[item]).text(uniquedrugs[item]));
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
        },
        headers: {
            Accept: "application/json"
        }
    });


    $("#drugsearchform").submit(function (event) {
        event.preventDefault();
        selectedDrug = $("#druglist").val();
        data=_.toArray(reactions);
        console.log(data[0].length);
        var results=[];
        
        for(var i=0;i<data[0].length;i++){
            if(data[0][i]===selectedDrug)
                results.push(data[1][i]);
        }
        console.log(results);

        console.log("Handler for .submit() called.");
        console.log($("#reactions"));
        $("#reactions").css("display", "block");
        $("#result").empty();
        for (var key=0;key<results.length;key++) {
            $("#result").append($("<h3 />").text(results[key]));

        }



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
});