<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $contact = $_POST['contact'];
    $age = $_POST['age'];
    $relationship = $_POST['relationship'];

    $password = password_hash($password, PASSWORD_DEFAULT);

    require_once 'connect.php';

    $sql = "INSERT INTO caregivertable (name, email, password, contactNo, age, relationship) VALUES 
    ('$name', '$email', '$password','$contact','$age','$relationship')";
    
    $sql2 = "SELECT * FROM usertable WHERE email='$email' ";
    $response = mysqli_query($conn, $sql2);
    if ( mysqli_num_rows($response) === 1 ) {
        $result["success"] = "-1";
        $result["message"] = "email_registered";

        echo json_encode($result);
        mysql_close($conn);
    }

    else{
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
}

?>