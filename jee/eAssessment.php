<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $email = $_POST['email'];
    $type = $_POST['type'];
    $q1 = $_POST['q1'];
    $q2 = $_POST['q2'];
    $q3 = $_POST['q3'];
    $q4 = $_POST['q4'];
    $q5 = $_POST['q5'];
    $q6 = $_POST['q6'];
    $q7 = $_POST['q7'];
    $q8 = $_POST['q8'];
    // $email="l@gmail.com";
    // $type="Patient";
    // $remark = "app3";
    // $date ="2023/04/25";
    // $time="18:23";

    require_once 'connect.php';

    $sql = "INSERT INTO eventAssessmentTable (email, type, q1, q2, q3, q4,
    q5, q6, q7, q8) 
    VALUES ('$email', '$type', '$q1', '$q2', '$q3', '$q4', '$q5', '$q6',
    '$q7', '$q8')";
        if ( mysqli_query($conn, $sql) ) {
            $result["success"] = "1";
            $result["message"] = "success";

            echo json_encode($result);
            mysqli_close($conn);

        } else {

            $result["success"] = "0";
            $result["message"] = "error";

            echo json_encode($result);
            mysqli_close($conn);
        }
    }

?>