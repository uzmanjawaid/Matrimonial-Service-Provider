<?php

require("conn.php");

if($conn){

    // $currentUserEmail = $_POST["email"];
   
    $sql = "SELECT * FROM users ";

    $result = mysqli_query($conn,$sql);
    if($result->num_rows>0){
        $user = array();

        while ($row = $result->fetch_assoc()) {
            $users[] = $row;
        }
        
        // Return the user data as JSON
        echo json_encode($users);
    }
   

    // while($row = mysqli_fetch_assoc($result)){
    //     $index['Id'] = $row['Id'];
    //     $index['name'] = $row['name'];
    //     $index['email'] = $row['email'];
    //     $index['image'] = $row['image'];
    //     $index['gender'] = $row['gender'];
    //     $index['religion'] = $row['religion'];
    //     $index['caste'] = $row['caste'];
    //     $index['Marital'] = $row['Marital'];
       
    //     array_push($user,$index);
    // }

    // echo json_encode($user);


}else{
    echo  "Connection Error";
}



?>