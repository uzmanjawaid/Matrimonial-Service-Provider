<?php

require 'conn.php';
require 'PHPMailer/PHPMailer.php';
require 'PHPMailer/SMTP.php';
require 'PHPMailer/Exception.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;


function sendEmail($recipientEmail, $profileLink) {
    $mail = new PHPMailer(true);

    // SMTP configuration
    $mail->isSMTP();
    $mail->Host = 'smtp.gmail.com';  // SMTP server address
    $mail->SMTPAuth = true;
    $mail->Username = 'saithsaab9046@gmail.com';  // SMTP username
    $mail->Password = 'gwexejhelfpgzjxb';  // SMTP password
    $mail->SMTPSecure = 'tls';  // Enable encryption, if required
    $mail->Port = 587;  // SMTP port number

    // Email content
    $mail->setFrom('ali@gmail.com', 'Your Name');  // Sender's email address and name
    $mail->addAddress($recipientEmail);  // Recipient's email address
    $mail->Subject = 'Your Profile Link';  // Email subject
    $mail->Body = 'Here is your profile link: ' . $profileLink;  // Body of the email

    try {
        // Send the email
        $mail->send();
        return true;
    } catch (Exception $e) {
        return false;
    }
}

// Get the recipient email and profile link from the request parameters
if (isset($_POST['email'])) {
    $email = $_POST['email'];
    $useremail = $_POST['email'];
    $profileLink = $_POST['profile_link']; 
    // "http://192.168.190.176/MatrimonialServiceProvider/userProfile.php?email=". $useremail;

    if (sendEmail($email, $profileLink)) {
        echo json_encode(array('message' => 'Email sent successfully'));
    } else {
        echo json_encode(array('message' => 'Failed to send the email'));
    }
} else {
    // Handle the case when 'email' key is not present in the $_POST array
    echo json_encode(array('message' => 'Email is not provided'));
}

mysqli_close($conn);
?>
