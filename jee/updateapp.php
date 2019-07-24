<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $email = $_POST['email'];
    $type = $_POST['type'];
    $oremark = $_POST['oremark'];
    $odate = $_POST['odate'];
    $otime = $_POST['otime'];
    $remark = $_POST['remark'];
    $date = $_POST['date'];
    $time = $_POST['time'];

    // $email="l@gmail.com";
    // $type = "Patient";
    // $oremark = "0";
    // $odate ="2019/07/18";
    // $otime="19:46";
    // $remark = "app2";
    // $date ="2019/07/18";
    // $time="19:46";

    require_once 'connect.php';

    $sql = "UPDATE patientappointment 
        SET remark='$remark', appointmentDate='$date', appointmentTime='$time' 
        WHERE email='$email' AND type='$type' AND remark='$oremark' AND 
        appointmentDate='$odate' AND appointmentTime ='$otime' ;";

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