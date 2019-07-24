<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $email = $_POST['email'];
    $type = $_POST['type'];
    $remark = $_POST['remark'];
    $date = $_POST['date'];
    $time = $_POST['time'];
    // $email="l@gmail.com";
    // $remark = "app3";
    // $date ="2023/04/25";
    // $time="18:23";

    require_once 'connect.php';

    $sql = "DELETE FROM patientappointment WHERE email='$email' AND type=
    '$type' AND remark='$remark' AND appointmentDate='$date' AND appointmentTime ='$time'; ";
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