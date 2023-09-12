<?php
require("conn.php");

if ($conn) {
$email = $_GET['email'];

$query = "SELECT * FROM users WHERE email = '$email'";
$result = mysqli_query($conn, $query);

if (mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    $image_path = $row['image'];
    unset($row['image']);
    $row['image'] = "http://192.168.1.102//MatrimonialServiceProvider/Images/" . $image_path;
    echo json_encode($row);
} else {
    echo "No user found";
}

mysqli_close($conn);
}else{
    echo json_encode(array("error" => "Connection error"));
}
?>