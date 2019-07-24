<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $name = $_POST['name'];
    $title = $_POST['title'];
    $content = $_POST['content'];

    require_once 'connect.php';

    $sql = "INSERT INTO forumdata (name, title, content) VALUES 
    ('$name', '$title','$content')";
    
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