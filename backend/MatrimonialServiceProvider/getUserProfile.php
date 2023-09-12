<?php
require("conn.php");

if ($conn) {
    $userEmail = $_GET['userEmail'];

    // Query to fetch the user's profile image URL based on the email
    $sql = "SELECT image FROM users WHERE email = '$userEmail'";
    $result = mysqli_query($conn, $sql);

    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        $profileImage = $row['image'];

// Build the full URL to the image
        $imageURL = "http://192.168.1.102/MatrimonialServiceProvider/Images/" . $profileImage;

        // Return the profile image URL as a JSON response

        echo json_encode(array("imageURL" => $imageURL));
    } else {
        // User not found or profile image not available
        // Return an appropriate error response
        echo json_encode(array("error" => "User not found or profile image not available"));
    }
} else {
    // Connection error
    echo json_encode(array("error" => "Connection error"));
}
?>






