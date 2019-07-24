<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $email = $_POST['email'];
    $type = $_POST['type'];
    $password = $_POST['password'];
    $newPass = $_POST['newPass'];

    //  $email = "l@gmail.com";
    // $type = "Patient";
    // $password = "abc";
    // $newPass = "123";

    $newPass = password_hash($newPass, PASSWORD_DEFAULT);

    require_once 'connect.php';

    if($type==="Patient"){
        $sql = "SELECT * FROM usertable WHERE email='$email' ";
        $sql2 = "UPDATE usertable SET password='$newPass' WHERE email='$email' ";
    } else{
        $sql = "SELECT * FROM caregivertable WHERE email='$email' ";
        $sql2 = "UPDATE caregivertable SET password='$newPass' WHERE email='$email' ";
    }

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['login'] = array();
    $row = mysqli_fetch_assoc($response);    

    if ( password_verify($password, $row['password']) ) {
        
       if(mysqli_query($conn, $sql2)){
            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);
            mysqli_close($conn);

        } else {
            $result['success'] = "0";
            $result['message'] = "error";
            echo json_encode($result);
            mysqli_close($conn);
        }
    }
    else{
        $result['success'] = "-1";
        $result['message'] = "old_password_wrong";
        echo json_encode($result);
        mysqli_close($conn);
    }

}

?>