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
    

    $.ajax({
        url: "http://localhost/jee/web/data2.php?search=" + email,
        method: "GET",
        success: function(data){
            console.log(data);
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
                        index = dayArray.length - dayToday+1+ 
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
                        index = dayArray.length - dayToday+1+ 
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


            // console.log(dateData);
            console.log(positiveData);
            console.log(neutralData);
            console.log(negativeData);
        

    //Get the context of the Chart canvas element we want to select
    var ctx = $("#weekly-chart");

    // Chart Options
    

    // Chart Data
    var chartData = {

        labels: dayArray,
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