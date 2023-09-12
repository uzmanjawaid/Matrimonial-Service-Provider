<?php
require("conn.php");

$email = $_POST['email'];
$status = $_POST['status'];

if($conn){

    $sql = " UPDATE users SET status = '$status' WHERE email = '$email'";

    if($conn -> query($sql) === TRUE){

    echo "Status updated successfully";
} else {
    echo "Error updating status: " . $conn->error;
}

}else{
    echo "Connection Error";
}
?>