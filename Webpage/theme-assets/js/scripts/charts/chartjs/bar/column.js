/*=========================================================================================
    File Name: column.js
    Description: Chartjs column chart
    ----------------------------------------------------------------------------------------
    Item Name: Chameleon Admin - Modern Bootstrap 4 WebApp & Dashboard HTML Template + UI Kit
    Version: 1.0
    Author: ThemeSelection
    Author URL: https://themeselection.com/
==========================================================================================*/

// Column chart
var email = "";


$("#serachForm").submit(function(e) {
    e.preventDefault();
    email = document.getElementById("search").value
    document.getElementById("yearButton").style.visibility = "visible";
    
    // $(document).ready(function(){

    $.ajax({
        url: "http://localhost/jee/web/data.php?search=" + email,
        method: "GET",
        success: function(data){
            console.log(data);
            data = JSON.parse(data)
            var date =[];
            var veryPositive =[];
            var positive = [];
            var neutral = [];
            var negative = [];
            var veryNegative =[];
            // var email = [];

            date = data[0];
            veryPositive = data[1];
            positive = data[2];
            neutral = data[3];
            negative = data[4];
            veryNegative = data[5];
            // email = data[6];

            // console.log(email);
            console.log(date);
            console.log(positive);
            console.log(neutral);
            console.log(negative);
            var dateData =[];
            var months = ["January","February","March","April","May",
            "June","July","August","September","October","November","December"];
            var veryPositiveData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var positiveData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var neutralData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var negativeData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var veryNegativeData = [0,0,0,0,0,0,0,0,0,0,0,0];

            

            for (var i in date){
                dateData.push(date[i].date);    
            }

            for(var i in veryPositive){
                for (var j = 0; j < months.length; j++) {
                    if(veryPositive[i].date===months[j]){
                        veryPositiveData[j]= veryPositive[i].count;
                    }
                }
            }

            for(var i in positive){
                for (var j = 0; j < months.length; j++) {
                    if(positive[i].date===months[j]){
                        positiveData[j]= positive[i].count;
                    }
                }
            }

            for(var i in neutral){
                for (var j = 0; j < months.length; j++) {
                    if(neutral[i].date===months[j]){
                        neutralData[j]= neutral[i].count;
                    }
                }
            }

            for(var i in negative){
                for (var j = 0; j < months.length; j++) {
                    if(negative[i].date===months[j]){
                        negativeData[j]= negative[i].count;
                    }
                }
            }

            for(var i in veryNegative){
                for (var j = 0; j < months.length; j++) {
                    if(veryNegative[i].date===months[j]){
                        veryNegativeData[j]= veryNegative[i].count;
                    }
                }
            }


            console.log(dateData);
            console.log(positiveData);
            console.log(neutralData);
            console.log(negativeData);
        

    //Get the context of the Chart canvas element we want to select
    var ctx = $("#yearly-chart");

    // Chart Options
    

    // Chart Data
    var chartData = {

        labels: months,
        datasets: [{
            label: "Very Positive",
            data: veryPositiveData, 
            backgroundColor: "#228B22",
            hoverBackgroundColor: "rgba(40,208,148,.9)",
            borderColor: "transparent"
        }, {
            label: "Positive",
            data: positiveData, 
            backgroundColor: "#28D094",
            hoverBackgroundColor: "rgba(40,208,148,.9)",
            borderColor: "transparent"
        }, {
            label: "Neutral",
            data: neutralData,
            backgroundColor: "#FFFF28",
            hoverBackgroundColor: "rgba(255,255,40,.9)",
            borderColor: "transparent"
        },{
            label: "Negative",
            data: negativeData,
            backgroundColor: "#FF7F50",
            hoverBackgroundColor: "rgba(255,73,97,.9)",
            borderColor: "transparent"
        },{ 
            label: "Very Negative",
            data: veryNegativeData,
            backgroundColor: "#FF4961",
            hoverBackgroundColor: "rgba(255,73,97,.9)",
            borderColor: "transparent"
        }]
    };

    var config = {
        type: 'bar',

        data : chartData
    };

    // Create the chart
    var barChart = new Chart(ctx, config);


        }, //if error getting data
        error: function(data){
            console.log(data);
        }

    });
    // document.getElementById("targetdrop").click()
});







//Listen for a click on any of the dropdown items
$(".year li").click(function(e){
    var year = $(this).attr("value");
    console.log(year);
    document.getElementById("yearButton").innerHTML=year;
    e.preventDefault();
    // $(document).ready(function(){

    $.ajax({
        url: "http://localhost/jee/web/data.php?search=" +email+"&year="+year,
        method: "GET",
        success: function(data){
            console.log(data);
            data = JSON.parse(data)
            var date =[];
            var veryPositive =[];
            var positive = [];
            var neutral = [];
            var negative = [];
            var veryNegative =[];
            // var email = [];

            date = data[0];
            veryPositive = data[1];
            positive = data[2];
            neutral = data[3];
            negative = data[4];
            veryNegative = data[5];
            // email = data[6];

            // console.log(email);
            console.log(date);
            console.log(positive);
            console.log(neutral);
            console.log(negative);
            var dateData =[];
            var months = ["January","February","March","April","May",
            "June","July","August","September","October","November","December"];
            var veryPositiveData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var positiveData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var neutralData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var negativeData = [0,0,0,0,0,0,0,0,0,0,0,0];
            var veryNegativeData = [0,0,0,0,0,0,0,0,0,0,0,0];

            

            for (var i in date){
                dateData.push(date[i].date);    
            }

            for(var i in veryPositive){
                for (var j = 0; j < months.length; j++) {
                    if(veryPositive[i].date===months[j]){
                        veryPositiveData[j]= veryPositive[i].count;
                    }
                }
            }

            for(var i in positive){
                for (var j = 0; j < months.length; j++) {
                    if(positive[i].date===months[j]){
                        positiveData[j]= positive[i].count;
                    }
                }
            }

            for(var i in neutral){
                for (var j = 0; j < months.length; j++) {
                    if(neutral[i].date===months[j]){
                        neutralData[j]= neutral[i].count;
                    }
                }
            }

            for(var i in negative){
                for (var j = 0; j < months.length; j++) {
                    if(negative[i].date===months[j]){
                        negativeData[j]= negative[i].count;
                    }
                }
            }

            for(var i in veryNegative){
                for (var j = 0; j < months.length; j++) {
                    if(veryNegative[i].date===months[j]){
                        veryNegativeData[j]= veryNegative[i].count;
                    }
                }
            }


            console.log(dateData);
            console.log(positiveData);
            console.log(neutralData);
            console.log(negativeData);
        

    //Get the context of the Chart canvas element we want to select
    var ctx = $("#yearly-chart");

    // Chart Options
    

    // Chart Data
    var chartData = {

        labels: months,
        datasets: [{
            label: "Very Positive",
            data: veryPositiveData, 
            backgroundColor: "#228B22",
            hoverBackgroundColor: "rgba(40,208,148,.9)",
            borderColor: "transparent"
        }, {
            label: "Positive",
            data: positiveData, 
            backgroundColor: "#66CDAA",
            hoverBackgroundColor: "rgba(40,208,148,.9)",
            borderColor: "transparent"
        }, {
            label: "Neutral",
            data: neutralData,
            backgroundColor: "#FFFF28",
            hoverBackgroundColor: "rgba(255,255,40,.9)",
            borderColor: "transparent"
        },{
            label: "Negative",
            data: negativeData,
            backgroundColor: "#FA8072",
            hoverBackgroundColor: "rgba(255,73,97,.9)",
            borderColor: "transparent"
        },{ 
            label: "Very Negative",
            data: veryNegativeData,
            backgroundColor: "#FF4961",
            hoverBackgroundColor: "rgba(255,73,97,.9)",
            borderColor: "transparent"
        }]
    };

    var config = {
        type: 'bar',

        data : chartData
    };

    // Create the chart
    var barChart = new Chart(ctx, config);


        }, //if error getting data
        error: function(data){
            console.log(data);
        }

    });
    // document.getElementById("targetdrop").click()
});