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
var barChart = null;

$("#serachForm").submit(function(e) {
    e.preventDefault();
    email = document.getElementById("search").value
    

    $.ajax({
        url: "http://localhost/jee/web/data3.php?search=" + email,
        method: "GET",
        success: function(data){
            console.log(data);
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
            
            var verySatisfiedData = [0,0,0,0,0,0,0];
            var satisfiedData = [0,0,0,0,0,0,0];
            var neutralData = [0,0,0,0,0,0,0];
            var unsatisfiedData = [0,0,0,0,0,0,0];
            var veryUnsatisfiedData = [0,0,0,0,0,0,0];

            

            for(var i in verySatisfied){
                    index = verySatisfied[i].week - dayToday +1;
                    if (index < 0){
                        index = dayArray.length - dayToday+1+ 
                        parseInt(verySatisfied[i].week);
                    }
                    verySatisfiedData[index] = verySatisfied[i].count;
            }
            

            for(var i in satisfied){
                console.log("day today is "+dayToday);
                console.log("day array length is "+dayArray.length);
                console.log("data day is "+satisfied[i].week);
                    index = satisfied[i].week - dayToday +1;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(satisfied[i].week);
                    }
                    satisfiedData[index] = satisfied[i].count;
            }

            for(var i in neutral){
                    index = neutral[i].week - dayToday +1;
                    if (index < 0){
                        index = dayArray.length - dayToday+1+ 
                        parseInt(neutral[i].week);
                    }
                    neutralData[index] = neutral[i].count;
            }

            for(var i in unsatisfied){
                    index = unsatisfied[i].week - dayToday+1;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(unsatisfied[i].week);
                    }
                    unsatisfiedData[index] = unsatisfied[i].count;
            }

            for(var i in veryUnsatisfied){
                    index = veryUnsatisfied[i].week - dayToday+1;
                    if (index < 0){
                        index = dayArray.length - dayToday+ 1+
                        parseInt(veryUnsatisfied[i].week);
                    }
                    veryUnsatisfiedData[index] = veryUnsatisfied[i].count;
            }


            // console.log(dateData);
            console.log(satisfiedData);
            console.log(neutralData);
            console.log(unsatisfiedData);
        

    //Get the context of the Chart canvas element we want to select
    var ctx = document.getElementById("weekly-chart")
    if (barChart !== null)
        barChart.destroy();
    barChart = new Chart(ctx, {});

    // Chart Options
    

    // Chart Data
    var chartData = {

        labels: dayArray,
        datasets: [{
            label: "Very Satisfied",
            data: veryPositiveData, 
            backgroundColor: "#228B22",
            hoverBackgroundColor: "rgba(34,139,34,.9)",
            borderColor: "transparent"
        }, {
            label: "Satisfied",
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
            label: "Unsatisfied",
            data: negativeData,
            backgroundColor: "#FF7F50",
            hoverBackgroundColor: "rgba(255,127,80,.9)",
            borderColor: "transparent"
        },{ 
            label: "Very Unsatisfied",
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
    barChart = new Chart(ctx, config);


        }, //if error getting data
        error: function(data){
            console.log(data);
        }

    });
    // document.getElementById("targetdrop").click()
});