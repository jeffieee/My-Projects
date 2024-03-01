<?php
error_reporting(0);
include '../Includes/dbcon.php';
include '../Includes/session.php';

$statusMsg = ""; // Initialize $statusMsg

// Retrieve termId and sessionName from the database
$query = "SELECT tblterm.Id AS termId, tblterm.termName, tblsessionterm.sessionName, tblsessionterm.id AS sessionId
FROM tblsessionterm
JOIN tblterm ON tblsessionterm.termId = tblterm.Id
WHERE tblsessionterm.isActive = 1";

$rs = $conn->query($query);
$num = $rs->num_rows;
$rrw = $rs->fetch_assoc(); // Fetch the data

if ($num > 0) {
    // Data was fetched Successfully

    $termId = $rrw['termId']; // Assign termId
    $sessionId = $rrw['sessionId']; // Assign sessionName
} else {
    // Handle the case where no data was retrieved
    $termId = 0; // Set a default value for termId
    $sessionName = ""; // Set a default value for sessionName
}


if (isset($_POST['save'])) {
    // Get the selected student's admission number
    $admissionNumber = $_POST['studentName'];


    // Get the remarks from the form
    $remarks = mysqli_real_escape_string($conn, $_POST['remarks']);

    // Check if the admission number is not empty
    if (!empty($admissionNumber)) {
        // Create the INSERT query for saving the remarks
        $insertQuery = "INSERT INTO tblremarks (admissionNumber,    remarks, termId) 
                        VALUES ('$admissionNumber', '$remarks', '$termId')";

        // Execute the query
        if ($conn->query($insertQuery) === TRUE) {
            $statusMsg = '<div class="alert alert-success">Remarks saved successfully!</div>';
        } else {
            $statusMsg = '<div class="alert alert-danger">Error saving remarks: ' . $conn->error . '</div>';
        }
    } else {
        $statusMsg = '<div class="alert alert-warning">Please select a student before saving remarks.</div>';
    }
}

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="img/logo/attnlg.jpg" rel="icon">
    <?php include 'includes/title.php';?>
    <link href="../vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css" rel="stylesheet" />
    <link href="css/ruang-admin.min.css" rel="stylesheet">
    <script src="../vendor/jquery/jquery.min.js"></script>
    <script src="../vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="../vendor/jquery-easing/jquery.easing.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>
    <script>
        function classArmDropdown(str) {
            if (str == "") {
                document.getElementById("txtHint").innerHTML = "";
                return;
            } else {
                if (window.XMLHttpRequest) {
                    xmlhttp = new XMLHttpRequest();
                } else {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xmlhttp.onreadystatechange = function () {
                    if (this.readyState == 4 && this.status == 200) {
                        document.getElementById("txtHint").innerHTML = this.responseText;
                    }
                };
                xmlhttp.open("GET", "ajaxClassArms2.php?cid=" + str, true);
                xmlhttp.send();
            }
        }

        $(document).ready(function () {
            $('.select2').select2();
        });

        function confirmSave() {
            return confirm("Are you sure you want to save the remarks for the selected student?");
        }
    </script>
</head>

<body id="page-top">
    <div id="wrapper">
        <?php include "Includes/sidebar.php";?>
        <div id="content-wrapper" class="d-flex flex-column">
            <div id="content">
                <?php include "Includes/topbar.php";?>
                <div class="container-fluid" id="container-wrapper">
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800"><?php echo $rrw['termName']. ' / ' .$rrw['sessionName']; ?></h1>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="./">Home</a></li>
                            <li class="breadcrumb-item active" aria-current="page">Quarterly Grades</li>
                        </ol>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="card mb-4">
                                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                    <h6 class="m-0 font-weight-bold text-primary">Quarterly Grades</h6>
                                    <?php echo $statusMsg; ?>
                                </div>
                                <div class="card-body">
                                    <form method="post" onsubmit="return confirmSave();">
                                        <div class="form-group row mb-3">
                                            
                                            <div class="col-xl-5">
                                            <label class="form-control-label">Select Students<span class="text-danger ml-2">*</span></label>
                                            <?php
                                                $qry = "SELECT * FROM tblstudents where classId = '$_SESSION[classId]' and classArmId = '$_SESSION[classArmId]' ORDER BY firstName ASC";
                                                $result = $conn->query($qry);
                                                $num = $result->num_rows;
                                                if ($num > 0) {
                                                    echo '<select required name="studentName" class="form-control mb-3 select2">';
                                                    echo '<option value="">--Select Student--</option>';
                                                    while ($rows = $result->fetch_assoc()) {
                                                        echo '<option value="'.$rows['admissionNumber'].'">'.$rows['firstName'].' '.$rows['lastName'].'</option>';
                                                    }
                                                    echo '</select>';
                                                }
                                            ?>
                                        </div>
                                        <div class="col-xl-12 mt-3"><!-- Added margin-top (mt-3) for spacing -->
                                                                                       <textarea class="form-control" name="remarks" placeholder="Enter Remarks" required></textarea>
                                        </div>

                                        </div>
                                        <div class="form-group row mb-3">
                                            <!-- Add more form fields here if needed -->
                                        </div>
                                        <button type="submit" name="save" class="btn btn-primary">Save</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <a class="scroll-to-top rounded" href="#page-top">
            <i class="fas fa-angle-up"></i>
        </a>

        <script src="js/ruang-admin.min.js"></script>
    </body>

    </html>
