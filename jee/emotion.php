<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $email = $_POST['email'];
    $type = $_POST['type'];
    $date = $_POST['date'];
    $expression = $_POST['expression'];

    require_once 'connect.php';

    $sql = "INSERT INTO userEmotionData (email, type, date, expression) VALUES ('$email', '$type','$date','$expression')";
    
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