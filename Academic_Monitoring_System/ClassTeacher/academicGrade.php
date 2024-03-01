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
    $sessionId = $rrw['sessionId']; // Assign sessionId
} else {
    // Handle the case where no data was retrieved
    $termId = 0; // Set a default value for termId
    $sessionName = ""; // Set a default value for sessionName
}

//------------------------SAVE--------------------------------------------------

if (isset($_POST['save'])) {
    $subjectId = $_POST['subjectId']; // Get the selected subject ID
    $studentId = $_POST['studentName']; // Modify to match your actual column name
    $scoreGrade = $_POST['scoreGrade']; // Modify to match your actual column name

    $partial = $_POST['partial'];

    // Check if the student is already graded for this subject, term, and session
    $checkQuery = mysqli_query($conn, "SELECT * FROM tblgrades WHERE subjectId = '$subjectId' AND studentId = '$studentId' AND termId = '$termId'");

    if (!$checkQuery) {
        // Handle query execution error
        $statusMsg = "<div class='alert alert-danger' style='margin-right:700px;'>Error: " . mysqli_error($conn) . "</div>";
    } else {
        // Check if a record was found
        $existingRecord = mysqli_fetch_array($checkQuery);
        if ($existingRecord) {
            $statusMsg = "<div class='alert alert-danger' style='margin-left:700px;'>This student is already graded for the selected subject, term, and session!</div>";
        } else {
            // Insert the data into the tblgrades table
            $insertQuery = mysqli_query($conn, "INSERT INTO tblgrades(subjectId, studentId, partial, grade, termId, sessionId, isGraded) 
            VALUES ('$subjectId', '$studentId',$partial, '$scoreGrade', '$termId', '$sessionId', '1')");

            if ($insertQuery) {
                $statusMsg = "<div class='alert alert-success'  style='margin-left:700px;'>Created Successfully!</div>";
            } else {
                $statusMsg = "<div class='alert alert-danger' style='margin-right:700px;'>An error occurred while inserting data: " . mysqli_error($conn) . "</div>";
            }
        }
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

            $('select[name="subjectId"], select[name="studentName"]').on('change', function () {
                var subjectId = $('select[name="subjectId"]').val();
                var studentId = $('select[name="studentName"]').val();

                fetchPartialGrade(subjectId, studentId);
            });

            // Automatically click the studentName dropdown after form submission
            <?php if (isset($_POST['save'])) { ?>
                $(document).ready(function () {
                    $('select[name="studentName"]').select2('open');
                });
            <?php } ?>
        });

        function confirmSave() {
            return confirm("Are you sure you want to save the grade for the selected student?");
        }

        function fetchPartialGrade(subjectId, studentId, termId) {
            $.ajax({
                type: 'POST',
                url: 'fetchPartialGrade.php',
                data: {
                    subjectId: subjectId,
                    studentId: studentId,
                },
                success: function (data) {
                    $('#partialGrade').val(data);
                }
            });
        }

         // Export to Excel button click event
    $("#exportBtn").on("click", function () {
        // Get table data as an array of arrays
        var tableData = [
            ["Subject", "Student Name", "Partial Grade", "Score Grade"],
            // ... (populate the data from the table)
        ];

        // Populate table data from the HTML table
        $("#dataTable tbody tr").each(function () {
            var rowData = [];
            $(this).find("td").each(function () {
                rowData.push($(this).text());
            });
            tableData.push(rowData);
        });

        // Create a new workbook and add a worksheet
        var ws = XLSX.utils.aoa_to_sheet(tableData);
        var wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, "Sheet1");

        // Save the workbook as an Excel file
        var fileName = "quarterly_grades_data.xlsx";
        XLSX.writeFile(wb, fileName);
    });
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
                                     <button class="btn btn-success" id="exportBtn">Export to Excel</button>
                                </div>
                                <div class="card-body">
                                    <!-- Search and Range Filter Form -->
                                    <form method="post" action="">
                                        <div class="form-group row mb-4">
                                            <div class="col-xl-3">
                                                <label class="form-control-label">Select Subject<span class="text-danger ml-2">*</span></label>
                                                <?php
                                                    $query = "SELECT * FROM tblsubjects WHERE classId IN (SELECT classId FROM tblclassteacher WHERE Id = '".$_SESSION['userId']."')";
                                                    $rs = $conn->query($query);
                                                    $num = $rs->num_rows;
                                                    if ($num > 0) {
                                                        echo '<select class="form-control" name="subjectId" required>';
                                                        echo '<option value="">--Select Subject Name--</option>';
                                                        while ($row = $rs->fetch_assoc()) {
                                                            $selected = ($row['Id'] == $_POST['subjectId']) ? 'selected' : '';
                                                            echo '<option value="'.$row['Id'].'" '.$selected.'>'.$row['subjectName'].'</option>';
                                                        }
                                                        echo '</select>';
                                                    }
                                                ?>
                                            </div>
                                            <div class="col-xl-4">
                                                <label class="form-control-label">Select Students<span class="text-danger ml-2">*</span></label>
                                                <?php
                                                    $qry = "SELECT * FROM tblstudents WHERE classId = '$_SESSION[classId]' AND classArmId = '$_SESSION[classArmId]' ORDER BY lastName ASC";
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
                                            <div class="col-xl-2">
                                                <label class="form-control-label">Partial Grade<span class="text-danger ml-2">*</span></label>
                                                <input type="text" id="partialGrade" class="form-control" name="partial" placeholder="" readonly required>
                                            </div>
                                            <script>
                                               $(document).ready(function () {
                                                $('.select2').select2();

                                                $('select[name="subjectId"], select[name="studentName"]').on('change', function () {
                                                    var subjectId = $('select[name="subjectId"]').val();
                                                    var studentId = $('select[name="studentName"]').val();
                                                    
                                                    fetchPartialGrade(subjectId, studentId);
                                                });
                                            });
                                            </script>
                                            <div class="col-xl-2">
                                                <label class="form-control-label">Grade<span class="text-danger ml-2">*</span></label>
                                                <input type="text" class="form-control" name="scoreGrade" placeholder="Enter Grade" required>
                                            </div>

                                        </div>
                                        <div class="form-group row mb-3">
                                            <!-- Add more form fields here if needed -->
                                        </div>
                                        <button type="submit" name="save" class="btn btn-primary">Save</button>
                                    </form>

                                    <!-- Display Data Table -->
                                    <div class="table-responsive mt-4">
                                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Subject</th>
                                                    <th>Student Name</th>
                                                    <th>Partial Grade</th>
                                                    <th>Score Grade</th>
                                                    <!-- Add more columns if needed -->
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <?php
                                                // Fetch data from tblGrades and display in the table
                                                $gradesQuery = "SELECT tblGrades.*, tblsubjects.subjectName, tblstudents.firstName, tblstudents.lastName
                                                                FROM tblGrades
                                                                INNER JOIN tblsubjects ON tblGrades.subjectId = tblsubjects.Id
                                                                INNER JOIN tblstudents ON tblGrades.studentId = tblstudents.admissionNumber
                                                                WHERE tblGrades.termId = '$termId'";
                                                $gradesResult = $conn->query($gradesQuery);

                                                while ($grade = $gradesResult->fetch_assoc()) {
                                                    echo "<tr>";
                                                    echo "<td>{$grade['subjectName']}</td>";
                                                    echo "<td>{$grade['firstName']} {$grade['lastName']}</td>";
                                                    echo "<td>{$grade['partial']}</td>";
                                                    echo "<td>{$grade['grade']}</td>";
                                                    // Add more columns if needed
                                                    echo "</tr>";
                                                }
                                                ?>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <a class="scroll-to-top
 rounded" href="#page-top">
            <i class="fas fa-angle-up"></i>
        </a>

        <script src="js/ruang-admin.min.js"></script>
         <script src="../vendor/jquery/jquery.min.js"></script>
        <script src="../vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="../vendor/jquery-easing/jquery.easing.min.js"></script>
        <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script src="../vendor/datatables/dataTables.bootstrap4.min.js"></script>

        <script>
            $(document).ready(function () {
                // Initialize Select2
                $('.select2').select2();

                // Initialize DataTable
                $('#dataTable').DataTable();

                function confirmSave() {
                    var confirmation = confirm("Are you sure you want to save the data?");
                    return confirmation;
                }
            });
        </script>
    </body>

    </html>
