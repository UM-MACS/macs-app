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
        url: "http://localhost/jee/web/data2.php?search=" + email,
        method: "GET",
        success: function(data){
            data = JSON.parse(data)
            var result =[];
            var veryPositive =[];
            var positive = [];
            var neutral = [];
            var negative = [];
            var veryNegative =[];

            veryPositive = data[0];
            positive = data[1];
            neutral = data[2];
            negative = data[3];
            veryNegative = data[4];

            var day = new Date();
            var dayToday = (day.getDay() + 1) % 7;
            console.log("Day today is "+dayToday);
            var dayArray =["Sunday","Monday","Tueday","Wednesday","Thursday",
            "Friday","Saturday"];
            dayArray = dayArray.slice(dayToday).concat(dayArray.slice(0, dayToday));
            console.log(dayArray);
            
            var veryPositiveData = [0,0,0,0,0,0,0];
            var positiveData = [0,0,0,0,0,0,0];
            var neutralData = [0,0,0,0,0,0,0];
            var negativeData = [0,0,0,0,0,0,0];
            var veryNegativeData = [0,0,0,0,0,0,0];

            

            for(var i in veryPositive){
                    index = veryPositive[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(veryPositive[i].week);
                    }
                    veryPositiveData[index] = veryPositive[i].count;
            }
            

            for(var i in positive){
                    index = positive[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(positive[i].week);
                    }
                    positiveData[index] = positive[i].count;
            }

            for(var i in neutral){
                    index = neutral[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(neutral[i].week);
                    }
                    neutralData[index] = neutral[i].count;
            }

            for(var i in negative){
                    index = negative[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(negative[i].week);
                    }
                    negativeData[index] = negative[i].count;
            }

            for(var i in veryNegative){
                    index = veryNegative[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(veryNegative[i].week);
                    }
                    veryNegativeData[index] = veryNegative[i].count;
            }
        

    //Get the context of the Chart canvas element we want to select
    var ctx = $("#weekly-line-chart");


    // Chart Data
    var chartData = {
        labels: dayArray,
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

