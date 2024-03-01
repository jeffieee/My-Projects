<?php
include '../Includes/dbcon.php';

if (isset($_POST['subjectId']) && isset($_POST['studentId'])) {
    $subjectId = $_POST['subjectId'];
    $studentId = $_POST['studentId'];

    // Query to fetch the subjectName based on subjectId
    $subjectQuery = "SELECT subjectName FROM tblsubjects WHERE Id = '$subjectId'";
    $subjectResult = $conn->query($subjectQuery);

    if ($subjectResult && $subjectResult->num_rows > 0) {
        $subjectRow = $subjectResult->fetch_assoc();
        $subjectName = $subjectRow['subjectName'];

        // Query to fetch the partial grade based on subjectName and studentId
        $query = "SELECT 
            SUM(grade) AS performanceTotal,
            SUM(overallScore) AS performanceOverallScore
            
            FROM tblperformance
            
            WHERE subjectName = '$subjectName' AND studentId = '$studentId'";

        $query1 = "SELECT 
            SUM(grade) AS writtenWorksTotal,
            SUM(overallScore) AS writtenWorksOverallScore
            
            FROM tblwrittenWorks
            
            WHERE subjectName = '$subjectName' AND studentId = '$studentId'";

        $query2 = "SELECT 
            SUM(grade) AS quarterlyTotal,
            SUM(overallScore) AS quarterlyOverallScore
            
            FROM tblquarterly
            
            WHERE subjectName = '$subjectName' AND studentId = '$studentId'";

        $result = $conn->query($query);
        $result1 = $conn->query($query1);
        $result2 = $conn->query($query2);

        if ($result && $result1 && $result2 && $result->num_rows > 0 && $result1->num_rows > 0 && $result2->num_rows > 0) {
            $row = $result->fetch_assoc();
            $performanceTotal = $row['performanceTotal'];
            $performanceOverallScore = $row['performanceOverallScore'];

            $row1 = $result1->fetch_assoc();
            $writtenWorksTotal = $row1['writtenWorksTotal'];
            $writtenWorksOverallScore = $row1['writtenWorksOverallScore'];

            $row2 = $result2->fetch_assoc();
            $quarterlyTotal = $row2['quarterlyTotal'];
            $quarterlyOverallScore = $row2['quarterlyOverallScore'];

            if ($performanceOverallScore != 0 && $writtenWorksOverallScore != 0 && $quarterlyOverallScore != 0) {
                $partial = ($performanceTotal / $performanceOverallScore * 0.4) + ($writtenWorksTotal / $writtenWorksOverallScore * 0.4) + ($quarterlyTotal / $quarterlyOverallScore * 0.2);
                echo $partial *100;
            } else {
                echo "Incomplete";
            }
        } else {
            echo "Incomplete.";
        }
    } else {
        echo '';
    }
} else {
    echo '';
}
?>
