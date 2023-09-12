<?php

require("conn.php");

if($conn){

    $sql = "SELECT * FROM feedback ";

    $result = mysqli_query($conn,$sql);
    if($result->num_rows>0){
        $feedback = array();

        while ($row = $result->fetch_assoc()) {
            $feedback[] = $row;
        }
        
        // Return the user data as JSON
        echo json_encode($feedback);
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