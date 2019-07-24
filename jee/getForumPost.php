<?php

// if ($_SERVER['REQUEST_METHOD']=='POST') {

    // $type = $_POST['type'];

    require_once 'connect.php';

    $sql = "SELECT * FROM forumdata ORDER BY id DESC LIMIT 3;";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['name'] = array();
    $result['title'] = array();
    $result['content'] = array();
    $i = 0;
    
     if ($response) {
        
        while($row = mysqli_fetch_assoc($response)){
           $result['name'][$i] = $row['name'];
           $result['title'][$i] = $row['title'];
           $result['content'][$i] = $row['content'];
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

// }

?>