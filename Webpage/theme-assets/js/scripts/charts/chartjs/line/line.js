/*=========================================================================================
    File Name: line.js
    Description: Chartjs simple line chart
    ----------------------------------------------------------------------------------------
    Item Name: Chameleon Admin - Modern Bootstrap 4 WebApp & Dashboard HTML Template + UI Kit
    Version: 1.0
    Author: ThemeSelection
    Author URL: https://themeselection.com/
==========================================================================================*/

// Line chart
// ------------------------------

var email ="";

$("#serachForm").submit(function(e) {
    e.preventDefault();
    let email = document.getElementById("search").value
    
    // $(document).ready(function(){

    $.ajax({
        url: "http://localhost/jee/web/data.php?search=" + email,
        method: "GET",
        success: function(data){
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
    var ctx = $("#line-chart");


    // Chart Data
    var chartData = {
        labels: months,
         datasets: [{
            label: "Very Positive",
            data: veryPositiveData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#228B22",
            pointBorderColor: "#228B22",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Positive",
            data: positiveData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#66CDAA",
            pointBorderColor: "#66CDAA",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Neutral",
            data: neutralData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#FFFF28",
            pointBorderColor: "#FFFF28",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Negative",
            data: negativeData,
            lineTension: 0,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#FA8072",
            pointBorderColor: "#FA8072",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Very Negative",
            data: veryNegativeData,
            lineTension: 0,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#FF4961",
            pointBorderColor: "#FF4961",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }]
    };

    var config = {
        type: 'line',

        // Chart Options
        // options : chartOptions,

        data : chartData
    };

    // Create the chart
    var lineChart = new Chart(ctx, config);

    }, //if error getting data
    error: function(data){
        console.log(data);
    }

    });
    document.getElementById("targetdrop").click()
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
                        veryPositiveData[j] = veryPositive[i].count;
                    }
                }
            }

            for(var i in positive){
                for (var j = 0; j < months.length; j++) {
                    if(positive[i].date===months[j]){
                        positiveData[j] = positive[i].count;
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
    //Get the context of the Chart canvas element we want to select
    var ctx = document.getElementById("line-chart");
    var context = ctx.getContext('2d');
    context.clearRect(0, 0, ctx.width, ctx.height);

    // Chart Data
    var chartData = {
        labels: months,
        datasets: [{
            label: "Very Positive",
            data: veryPositiveData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#228B22",
            pointBorderColor: "#228B22",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Positive",
            data: positiveData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#66CDAA",
            pointBorderColor: "#66CDAA",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Neutral",
            data: neutralData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#FFFF28",
            pointBorderColor: "#FFFF28",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Negative",
            data: negativeData,
            lineTension: 0,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#FA8072",
            pointBorderColor: "#FA8072",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Very Negative",
            data: veryNegativeData,
            lineTension: 0,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#FF4961",
            pointBorderColor: "#FF4961",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }]
    };

    var config = {
        type: 'line',

        // Chart Options
        // options : chartOptions,

        data : chartData
    };

    // Create the chart
    var lineChart = new Chart(ctx, config);

    }, //if error getting data
    error: function(data){
        console.log(data);
    }

    });
    document.getElementById("targetdrop").click()
});