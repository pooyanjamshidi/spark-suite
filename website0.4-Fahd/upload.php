<?php
	$dir = "jar/";
	move_uploaded_file($_FILES["jar"]["tmp_name"], "jar/". $_FILES["jar"]["name"]);
?>