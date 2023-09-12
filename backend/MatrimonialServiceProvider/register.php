<?php

require("conn.php");
$fullname = $_POST["name"];
$email = $_POST["email"];
$password = $_POST["password"];
$gender = $_POST["gender"];
$religion = $_POST["religion"];
$caste = $_POST["caste"];
$Marital = $_POST["Marital"];
$salary = $_POST["salary"];
$Occupation = $_POST["Occupation"];
$Mobile = $_POST["Mobile"];
$City = $_POST["City"];
$image = $_POST["image"];
$status = $_POST["status"];

$taget_dir = "Images/";  
$imageStore = rand()."_".time().".jpeg";
$taget_dir= $taget_dir."/".$imageStore;
file_put_contents($taget_dir,base64_decode($image));

$isValidEmail = filter_var($email,FILTER_VALIDATE_EMAIL);
if($conn){


    if(strlen($password)>40 || strlen($password)<6 ){
        echo "Password must be less then 40 and more then 6 charachters";
    }else if($isValidEmail == false){
        echo "This email is not Valid";
    }
    else{
        //check username query
        $sqlCheckUsername = "SELECT * FROM `users`  WHERE  `name`  LIKE '$fullname'";

        $usernameQuery = mysqli_query($conn,$sqlCheckUsername);

        //check email
        $sqlCheckEmail = "SELECT * FROM `users`  WHERE  `email`  LIKE '$email'";

        $emailQuery = mysqli_query($conn,$sqlCheckEmail);

        if(mysqli_num_rows($usernameQuery)>0){
            echo "User Name Already used, type another name";
        }else if(mysqli_num_rows($emailQuery)>0){
            echo "Email Already Register, type another Email";
        }else{
            $sql_register = "INSERT INTO `users` (`name`,`email`,`password`,`gender`,`religion`,`caste`,`Marital`,
            `salary`,`Occupation`,`Mobile`,`City`,`image`,`status`) VALUES (
                '$fullname','$email','$password','$gender','$religion','$caste','$Marital','$salary','$Occupation','$Mobile'
                ,'$City','$imageStore','$status')";
             if(mysqli_query($conn,$sql_register)){
                echo "Successfully Registered";
            }else{
                echo "Failed to Registered";
            }
        }
    }

}else{
    echo"connection ERROR";
}
?>