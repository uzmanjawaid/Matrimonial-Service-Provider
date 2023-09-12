<?php

require("conn.php");
$email = $_POST["email"];
$password = $_POST["password"];
// $status = $_POST["status"];//status

session_start();


$isValidEmail = filter_var($email,FILTER_VALIDATE_EMAIL);
if($conn){
    if($isValidEmail===false){
        echo "This Email is not Valid";
    }
    else{
        $sqlCheckEmail = " SELECT * FROM `users`  WHERE  `email`  LIKE '$email' ";
        $emailQuery = mysqli_query($conn,$sqlCheckEmail);
        if (mysqli_num_rows($emailQuery)>0){
            $sqlLogin = "SELECT * FROM `users`  WHERE  `email`  LIKE '$email' AND `password` LIKE '$password' ";

            $result = $conn->query($sqlLogin);
            if($result->num_rows ==1){
                $row = $result->fetch_assoc();
                if($row['status'] == 'Unblock'){
                    echo json_encode($row);
                }else{
                    echo "blocked";
                }
            }else{
                echo "No Data found";
            }
            
            
        }else{
            echo " This Email not Register";
        }
        
    }
}else{
    echo "Connection ERROR";
}
?>