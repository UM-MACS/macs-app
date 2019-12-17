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
var lineChart = null;

$("#serachForm").submit(function(e) {
    e.preventDefault();
    let email = document.getElementById("search").value
    
    // $(document).ready(function(){

    $.ajax({
        url: "http://localhost/jee/web/data3.php?search=" + email,
        method: "GET",
        success: function(data){
            data = JSON.parse(data)
            var result =[];
            var verySatisfied =[];
            var satisfied = [];
            var neutral = [];
            var unsatisfied = [];
            var veryUnsatisfied =[];

            verySatisfied = data[0];
            satisfied = data[1];
            neutral = data[2];
            unsatisfied = data[3];
            veryUnsatisfied = data[4];

            var day = new Date();
            var dayToday = (day.getDay() + 1) % 7;
            console.log("Day today is "+dayToday);
            var dayArray =["Sunday","Monday","Tueday","Wednesday","Thursday",
            "Friday","Saturday"];
            dayArray = dayArray.slice(dayToday).concat(dayArray.slice(0, dayToday));
            console.log(dayArray);
            
            var veryUnsatisfiedData = [0,0,0,0,0,0,0];
            var satisfiedData = [0,0,0,0,0,0,0];
            var neutralData = [0,0,0,0,0,0,0];
            var unsatisfiedData = [0,0,0,0,0,0,0];
            var veryUnsatisfiedData = [0,0,0,0,0,0,0];

            

            for(var i in verySatisfied){
                    index = verySatisfied[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(verySatisfied[i].week);
                    }
                    verySatisfiedData[index] = verySatisfied[i].count;
            }
            

            for(var i in satisfied){
                    index = satisfied[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(satisfied[i].week);
                    }
                    satisfiedData[index] = satisfied[i].count;
            }

            for(var i in neutral){
                    index = neutral[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(neutral[i].week);
                    }
                    neutralData[index] = neutral[i].count;
            }

            for(var i in unsatisfied){
                    index = unsatisfied[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(unsatisfied[i].week);
                    }
                    unsatisfiedData[index] = unsatisfied[i].count;
            }

            for(var i in veryUnsatisfied){
                    index = veryUnsatisfied[i].week - dayToday;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(veryUnsatisfied[i].week);
                    }
                    veryUnsatisfiedData[index] = veryUnsatisfied[i].count;
            }
        

    //Get the context of the Chart canvas element we want to select
    var ctx = document.getElementById("weekly-line-chart");
    if (lineChart !== null)
        lineChart.destroy();
    lineChart = new Chart(ctx, {});

    // Chart Data
    var chartData = {
        labels: dayArray,
        datasets: [{
            label: "Very Satisfied",
            data: verySatisfiedData,
            fill: false,
            borderDash: [5, 5],
            borderColor: "#228B22",
            pointBorderColor: "#228B22",
            pointBackgroundColor: "#FFF",
            pointBorderWidth: 2,
            pointHoverBorderWidth: 2,
            pointRadius: 4,
        }, {
            label: "Satisfied",
            data: satisfiedData,
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
            label: "Unsatisfied",
            data: unsatisfiedData,
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
            label: "Very Unsatisfied",
            data: veryUnsatisfiedData,
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
    lineChart = new Chart(ctx, config);

    }, //if error getting data
    error: function(data){
        console.log(data);
    }

    });
    document.getElementById("targetdrop").click()
});

