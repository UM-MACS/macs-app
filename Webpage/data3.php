<?php
//setting header to json
// header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

//database
define('DB_HOST', 'localhost');
define('DB_USERNAME', 'root');
define('DB_PASSWORD', '');
define('DB_NAME', 'masoccdata');

//get connection
$mysqli = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

if(!$mysqli){
  die("Connection failed: " . $mysqli->error);
}

$email = $_GET["search"];

//query to get data from the table
// $email = 'james@gmail.com';
$verySatisfied = 'Verysatisfied';
$satisfied = 'Satisfied';
$neutral = 'Neutral';
$unsatisfied = 'Unsatisfied';
$veryUnsatisfied = 'Veryunsatisfied';

$query = sprintf("
	SELECT WEEKDAY(date) AS week,
		   analysis,
		   COUNT(*) AS count 
	  FROM eventAssessment
   	 WHERE email='$email' AND date BETWEEN DATE_ADD(CURDATE(), INTERVAL -6 DAY) AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND analysis='$verySatisfied'
   	 GROUP BY week, analysis" );

$query2 = sprintf("
	SELECT WEEKDAY(date) AS week,
		   analysis,
		   COUNT(*) AS count 
	  FROM eventAssessment
   	 WHERE email='$email' AND date BETWEEN DATE_ADD(CURDATE(), INTERVAL -6 DAY) 
   	 AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND analysis='$satisfied'
   	 GROUP BY week, analysis");

$query3 = sprintf("
	SELECT WEEKDAY(date) AS week,
		   analysis,
		   COUNT(*) AS count 
	  FROM eventAssessment
   	 WHERE email='$email' AND date BETWEEN DATE_ADD(CURDATE(), INTERVAL -6 DAY) AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND analysis='$neutral'
   	 GROUP BY week, analysis");

$query4 = sprintf("
	SELECT WEEKDAY(date) AS week,
		   analysis,
		   COUNT(*) AS count 
	  FROM eventAssessment
   	 WHERE email='$email' AND date BETWEEN DATE_ADD(CURDATE(), INTERVAL -6 DAY) AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND analysis='$unsatisfied'
   	 GROUP BY week, analysis");

$query5 = sprintf("
	SELECT WEEKDAY(date) AS week,
		   analysis,
		   COUNT(*) AS count 
	  FROM eventAssessment
   	 WHERE email='$email' AND date BETWEEN DATE_ADD(CURDATE(), INTERVAL -6 DAY) AND DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND analysis='$veryUnsatisfied'
   	 GROUP BY week, analysis");

//execute query
$result = $mysqli->query($query);
$result2 = $mysqli->query($query2);
$result3 = $mysqli->query($query3);
$result4 = $mysqli->query($query4);
$result5 = $mysqli->query($query5);

$data = array();
foreach ($result as $row) {
  $data[] = $row;
}

$data2 = array();
foreach ($result2 as $row) {
  $data2[] = $row;
}

$data3 = array();
foreach ($result3 as $row) {
  $data3[] = $row;
}

$data4 = array();
foreach ($result4 as $row) {
  $data4[] = $row;
}

$data5 = array();
foreach ($result5 as $row) {
  $data5[] = $row;
}
// $result->close();
// $mysqli->close();
// // //now print the data
print json_encode(array($data,$data2,$data3,$data4,$data5));