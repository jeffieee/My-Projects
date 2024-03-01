<?php
include '../Includes/dbcon.php';
include '../Includes/session.php';

// Fetch class and class arm details
$query = "SELECT tblclass.className, tblclassarms.classArmName 
    FROM tblclassteacher
    INNER JOIN tblclass ON tblclass.Id = tblclassteacher.classId
    INNER JOIN tblclassarms ON tblclassarms.Id = tblclassteacher.classArmId
    WHERE tblclassteacher.Id = '$_SESSION[userId]'";

$rs = $conn->query($query);
$num = $rs->num_rows;
$rrw = $rs->fetch_assoc();

$admissionNumber = $_SESSION['admissionNumber']; // Assuming you have the admission number in the session

$queryStudent = mysqli_query($conn, "SELECT isChange FROM tblstudents WHERE admissionNumber = '$admissionNumber'");
$studentData = mysqli_fetch_assoc($queryStudent);
$isChange = $studentData['isChange'];

if ($isChange == 0) {
    // Use JavaScript to show a dialog box after login
    echo "<script>
            // Confirm dialog to ask the user if they want to change their password
            if (confirm('For security purposes, it is recommended to change your password. Do you want to change it now?')) {
                // If the user clicks 'OK', redirect to the changePassword.php page
                window.location.href = 'changePassword.php';
            }
          </script>";
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
    <link href="img/logo/blogo.png" rel="icon">
    <title>Dashboard</title>
    <link href="../vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="../vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="css/ruang-admin.min.css" rel="stylesheet">
    <link href="css/ruang-admin.css" rel="stylesheet">
</head>

<body id="page-top">
    <div id="wrapper">
        <!-- Sidebar -->
        <?php include "Includes/sidebar.php"; ?>
        <!-- Sidebar -->
        <div id="content-wrapper" class="d-flex flex-column">
            <div id="content">
                <!-- TopBar -->
                <?php include "Includes/topbar.php"; ?>
                <!-- Topbar -->
                <!-- Container Fluid-->
                <div class="container-fluid" id="container-wrapper">
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                        <h1 class="h3 mb-0 text-gray-800">Student Dashboard / <?php 
                            $query = "SELECT tblterm.Id AS termId, tblterm.termName, tblsessionterm.sessionName, tblsessionterm.id AS sessionId
                            FROM tblsessionterm
                            JOIN tblterm ON tblsessionterm.termId = tblterm.Id
                            WHERE tblsessionterm.isActive = 1";

                            $rs = $conn->query($query);
                            $num = $rs->num_rows;
                            $rrw = $rs->fetch_assoc(); // Fetch the data
                            $termName = $rrw['termName'];

                            echo $termName
                            ?> </h1>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="./">Home</a></li>
                            <li class="breadcrumb-item active" aria-current="page">Dashboard</li>
                        </ol>
                    </div>

                    <div class="row mb-3">
                        <?php

                        $query1 = mysqli_query($conn, "SELECT * from tblstudents where classId = '$_SESSION[classId]' and classArmId = '$_SESSION[classArmId]'");
                        $students = mysqli_num_rows($query1);
                        ?>
                        <div class="col-l-2 col-md-9 mb-4">
                            <div class="card h-100">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-3">
                                            <div class="text-xs font-weight-bold text-uppercase mb-1 text-center"><?php 
                            $query = "SELECT tblterm.Id AS termId, tblterm.termName, tblsessionterm.sessionName, tblsessionterm.id AS sessionId
                            FROM tblsessionterm
                            JOIN tblterm ON tblsessionterm.termId = tblterm.Id
                            WHERE tblsessionterm.isActive = 1";

                            $rs = $conn->query($query);
                            $num = $rs->num_rows;
                            $rrw = $rs->fetch_assoc(); // Fetch the data
                            $termName = $rrw['termName'];

                            echo $termName
                            ?> Average</div>
                                            <div class="container">
                                                <div class="row justify-content-center">
                                                    <div class="col-m-1 align-items-center">
                                                        <?php
                                                        $querySubjects = mysqli_query($conn, "SELECT * FROM tblsubjects WHERE classId = " . $_SESSION['classId']);

                                                        $totalGrades = 0;
                                                        $gradeCount = 0;

                                                        while ($subject = mysqli_fetch_assoc($querySubjects)) {
                                                            $subjectName = $subject['subjectName'];
                                                            $subjectId = $subject['Id'];

                                                            $queryTerm = mysqli_query($conn, "SELECT termId FROM tblsessionterm WHERE isActive = 1");
                                                            $row = mysqli_fetch_assoc($queryTerm);
                                                            $termId = $row['termId'];

                                                            $queryGrades = mysqli_query($conn, "SELECT grade FROM tblgrades WHERE termId = $termId AND subjectId = $subjectId AND studentId = " . $_SESSION['admissionNumber']);

                                                            while ($row1 = mysqli_fetch_assoc($queryGrades)) {
                                                                // Check if the query was successful
                                                                if ($row1['grade']) {
                                                                    // Grade found, accumulate it
                                                                    $totalGrades += $row1['grade'];
                                                                    $gradeCount++;
                                                                }
                                                            }
                                                        }

                                                        // Calculate and display the overall average grade
                                                        if ($gradeCount > 0) {
                                                            $overallAverage = $totalGrades / $gradeCount;
                                                            echo '<span style="font-weight: bold; text-align: center;">' . $overallAverage . '</span>';
                                                        } else {
                                                            // No grades found, display 'NA'
                                                            echo '<span style="font-weight: bold; text-align: center;">NA</span>';
                                                        }
                                                        ?>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="mt-2 mb-0 text-muted text-xs">
                                                <!-- <span class="text-success mr-2"><i class="fas fa-arrow-up"></i> 20.4%</span>
                                                <span>Since last month</span> -->
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Earnings (Annual) Card Example -->


                        <!-- Pending Requests Card Example -->
                        <?php
                        $query1 = mysqli_query($conn, "SELECT * from tblattendance where classId = '$_SESSION[classId]' and classArmId = '$_SESSION[classArmId]' and status = '1 '");
                        $totAttendance = mysqli_num_rows($query1);
                        ?>
                        <div class="col-xl-3 col-md-6 mb-4">
                            <div class="card h-100">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-uppercase mb-1">Student Attendance</div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                                <?php echo $totAttendance; ?>
                                            </div>
                                            <div class="mt-2 mb-0 text-muted text-xs">
                                                <!-- <span class="text-danger mr-2"><i class="fas fa-arrow-down"></i> 1.10%</span>
                                                <span>Since yesterday</span> -->
                                            </div>
                                        </div>
                                        <div class="col-auto">
                                            <i class="fas fa-calendar fa-2x text-warning"></i>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <?php
                        $querySubjects = mysqli_query($conn, "SELECT * FROM tblsubjects WHERE classId = " . $_SESSION['classId']);

                        while ($subject = mysqli_fetch_assoc($querySubjects)) {
                            $subjectName = $subject['subjectName'];
                            $subjectId = $subject['Id'];

                        ?>
                            <div class="col-xl-3 col-md-6  mb-4">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <div class="row align-items-center">
                                            <div class="col mr-2">
                                                <div class="text-xs font-weight-bold text-uppercase mb-1"><?php echo $subjectName; ?></div>
                                                <!-- Display your subject-specific data here, e.g., class percentage -->
                                                <div class="h5 mb-0 font-weight-bold text-gray-800 text-center">
                                                    <?php
                                                    $queryTerm = mysqli_query($conn, "SELECT termId FROM tblsessionterm WHERE isActive = 1");
                                                    $row = mysqli_fetch_assoc($queryTerm);
                                                    $termId = $row['termId'];

                                                    $queryGrade = mysqli_query($conn, "SELECT grade FROM tblgrades WHERE termId = $termId and subjectId= $subjectId and studentId = " . $_SESSION['admissionNumber']);
                                                    $row1 = mysqli_fetch_assoc($queryGrade);

                                                    // Check if the query was successful
                                                    if ($row1) {
                                                        // Grade found, display it
                                                        echo $row1['grade'];
                                                    } else {
                                                        // No grade found, display 'NA'
                                                        echo 'NA';
                                                    }
                                                    ?>
                                                </div>
                                                <div class="mt-2 mb-0 text-muted text-xs">
                                                    <!-- Additional subject-specific data here -->
                                                </div>
                                            </div>
                                            <div class="col-auto">
                                                <!-- Use an appropriate icon for the subject -->
                                                <i class="fas fa-chalkboard fa-2x text-primary"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <?php
                        }
                        ?>
                    </div>

                    <!-- Display remarks for the current student -->
                    <div class="row">
                        <div class="col-lg-12">
                            <?php
                            $queryRemarks = "SELECT * FROM tblremarks WHERE admissionNumber = '$admissionNumber'";
                            $resultRemarks = $conn->query($queryRemarks);

                            while ($rowRemarks = $resultRemarks->fetch_assoc()) {
                                echo '<div class="alert alert-info" role="alert">';
                                echo 'Remarks: ' . $rowRemarks['remarks'];
                                echo '</div>';
                            }
                            ?>
                        </div>
                    </div>

                </div>
                <!---Container Fluid-->
            </div>
            <!-- Footer -->
            <?php include 'includes/footer.php'; ?>

            <!-- Footer -->
        </div>
    </div>

    <!-- Scroll to top -->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>

    <script src="../vendor/jquery/jquery.min.js"></script>
    <script src="../vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="../vendor/jquery-easing/jquery.easing.min.js"></script>
    <script src="js/ruang-admin.min.js"></script>
    <script src="../vendor/chart.js/Chart.min.js"></script>
    <script src="js/demo/chart-area-demo.js"></script>
</body>

</html>
