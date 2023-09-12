<?php

require("conn.php");
$name = $_POST["name"];
$username = $_POST["username"];
$message = $_POST["message"];
$email = $_POST["email"];

if($conn){

    $sql_feedback = "INSERT INTO `feedback` (`name`,`username`,`message`,`email`) VALUES (
        '$name','$username','$message','$email')";

    if(mysqli_query($conn,$sql_feedback)){
    echo "Feedback Add Successfully";
}else{
    echo "Failed to Add..";
}

}else{
    echo"connection ERROR..";
}

?>