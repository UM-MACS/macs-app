<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $email = $_POST['email'];
    // $email="l@gmail.com";

    require_once 'connect.php';

    $sql = "SELECT * FROM patientAppointment WHERE email='$email' ";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['remark'] = array();
    $result['appointmentDate'] = array();
    $result['appointmentTime'] = array();
    $i = 0;
    
     if ($response) {
        
        while($row = mysqli_fetch_assoc($response)){
           $result['remark'][$i] = $row['remark'];
           $result['appointmentDate'][$i] = $row['appointmentDate'];
           $result['appointmentTime'][$i] = $row['appointmentTime'];
           $i++;
        }

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

            mysqli_close($conn);

    }

    else{
        $result['success'] = "-1";
        $result['message'] = "error";
        echo json_encode($result);
        mysqli_close($conn);
    }

}

?>