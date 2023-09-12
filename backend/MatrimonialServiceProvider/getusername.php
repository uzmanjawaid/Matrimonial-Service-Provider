<?php
require("conn.php");

if($conn){
    $query = "SELECT name FROM users";
$result = mysqli_query($conn, $query);

$userNames = array();
while ($row = mysqli_fetch_assoc($result)) {
    $userNames[] = $row['name'];
}

// Return the user names as JSON response
header('Content-Type: application/json');
echo json_encode($userNames);
}else{
    echo json_encode(array("error" => "Connection error"));
}

?>