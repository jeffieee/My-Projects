<?php

include '../Includes/dbcon.php';

    $cid = intval($_GET['cid']);//

        $queryss=mysqli_query($conn,"select * from tblclassarms where classId=".$cid."");                        
        $countt = mysqli_num_rows($queryss);

         if($countt === 0){
           echo   
                           "<div class='alert alert-danger' role='alert'>
                            No Record Found!
                            </div>";
        }else{
              echo '
        <select required name="classArmId" class="form-control mb-3">';
        echo'<option value="">--Select Section--</option>';
        }
        
        while ($row = mysqli_fetch_array($queryss)) {
        echo'<option value="'.$row['Id'].'" >'.$row['classArmName'].'</option>';
        }
        echo '</select>';
?>
