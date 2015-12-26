<?php
include ($_SERVER ['DOCUMENT_ROOT'] . "/class/class.mysql.php");
include ($_SERVER ['DOCUMENT_ROOT'] . "/class/class.forms.php");

$mysql = new mysql ();
$myforms = new forms ();

session_start ();

// This stops SQL Injection in POST vars
foreach ( $_POST as $key => $value ) {
	$_POST [$key] = mysql_real_escape_string ( $value );
}

// This stops SQL Injection in GET vars
foreach ( $_GET as $key => $value ) {
	$_GET [$key] = mysql_real_escape_string ( $value );
}

// specify the table names
$eventTableName = "groups";
$customerTableName = "customers";



// **********************************************************************************
// EVENT_DELETION
// **********************************************************************************

if ($_POST ['ACTIONS'] == "EVENT_DELETION") {

	if (isset ( $_POST ['ID'] )) {
		
		if (! get_magic_quotes_gpc ()) {//PREVENT SQL INSERTION
			$id = addslashes ( $_POST ['ID'] );
		} else {
			$id = $_POST ['ID'];
		}
	
		
		$MyDelete = new mysql ();
	
		$result = $MyDelete->query ( "DELETE FROM " . $eventTableName . " WHERE id = " . $id);
	
		echo $result;
	
	}
	
}

// **********************************************************************************
// GET Event List
// **********************************************************************************

if ($_REQUEST ['ACTIONS'] == "GET_EVENTS_LIST") {
	
	$result = array ();
	
	$mysql = new mysql ();
	
	$mysql->query ( "SELECT
	id, name, 
	CASE director WHEN '' THEN '&nbsp;' ELSE director END AS director,
	startTime, endTime, active
	FROM " . $eventTableName . ";" );
	
	if ($mysql->num_rows () > 0) {
		while ( $mysql->movenext () ) {
			$arr = array ();
			
			array_push ( $arr, urldecode ( $mysql->getfield ( "id" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "name" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "director" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "startTime" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "endTime" ) ) );
			
			array_push ( $arr, '<button type="button" class="btn btn-default btn-sm" onclick="detail(' . 
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-tag"></i></button>' . '<button type="button" class="btn btn-default btn-sm" onclick="modifyEvent(' . 
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-pencil"></i></button>' . '<button type="button" class="btn btn-default btn-sm" onclick="deleteEvent(' . 
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-trash"></i></button>' );
			
			$isActive = (urldecode ( $mysql->getfield ( "active" ) ) == '1') ? '<input type="checkbox" onClick="makeEventStatus(this)" checked="checked" style="zoom: 140%; margin-bottom: 5px" />' 
					: '<input type="checkbox" onClick="makeEventStatus(this)" style="zoom: 140%; margin-bottom: 5px" />';
			
			array_push ( $arr, $isActive );
			
			array_push ( $result, $arr );
		}
	}
	
	echo json_encode ( $result );
}

// **********************************************************************************
// GET ACTIVE Event
// **********************************************************************************

if ($_REQUEST ['ACTIONS'] == "GET_ACTIVE_EVENTS") {
	
	$result = array ();
	
	$mysql = new mysql ();
	
	$mysql->query ( "SELECT
	id, name, 
	CASE director WHEN '' THEN '&nbsp;' ELSE director END AS director,
	startTime, endTime, active, newUsers 
	FROM " . $eventTableName . " WHERE active='1';" );
	
	if ($mysql->num_rows () > 0) {
		while ( $mysql->movenext () ) {
			$arr = array ();
			
			array_push ( $arr, urldecode ( $mysql->getfield ( "id" ) ) );
			
			//with new customer reminder
			array_push ( $arr, ((urldecode ( $mysql->getfield ( "newUsers" ) ) == '0') || (urldecode ( $mysql->getfield ( "newUsers" ) ) ==  NULL)) ? urldecode ( $mysql->getfield ( "name" ) ) : 
				urldecode ( $mysql->getfield ( "name" ) ) . '<span style="color:red">&nbsp;(' . urldecode ( $mysql->getfield ( "newUsers" ) ) . ')</span>');
			
			array_push ( $arr, urldecode ( $mysql->getfield ( "director" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "startTime" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "endTime" ) ) );
			
			array_push ( $arr, '<button type="button" class="btn btn-default btn-sm" onclick="detail(' . 
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-tag"></i></button>' . '<button type="button" class="btn btn-default btn-sm" onclick="modifyEvent(' . 
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-pencil"></i></button>' . '<button type="button" class="btn btn-default btn-sm" onclick="deleteEvent(' . 
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-trash"></i></button>' );
			
			$isActive = (urldecode ( $mysql->getfield ( "active" ) ) == '1') ? '<input type="checkbox" onClick="makeEventStatus(this)" checked="checked" style="zoom: 140%; margin-bottom: 5px" />' 
					: '<input type="checkbox" onClick="makeEventStatus(this)" style="zoom: 140%; margin-bottom: 5px" />';
			
			array_push ( $arr, $isActive );
			
			array_push ( $result, $arr );
		}
	}
	
	echo json_encode ( $result );
}

// **********************************************************************************
// SET Event to Active or Not
// **********************************************************************************

if ($_REQUEST ['ACTIONS'] == 'SET_EVENT_STATUS') {
	
	$id = $_REQUEST ['ID'];
	$isActive = $_REQUEST ['IS_ACTIVE'];
	
	$mysql = new mysql ();
	
	$result = $mysql->query ( "UPDATE " . $eventTableName . " SET active=" . $isActive . " WHERE id=" . $id );
	
	echo $result;
}

// **********************************************************************************
// EVENT INSERTION AND UPDATE
// **********************************************************************************

if ($_REQUEST ['ACTIONS'] == 'EVENT_INSERTION_AND_UPDATE') {
	
	$result = 0;
	
	if ($_REQUEST ['STATUS'] == 0) { //STATUS = 0 means insert
		$maxId = 0;
		
		$mysql = new mysql ();
		$mysql->query ( "SELECT MAX(id) AS maxId FROM " . $eventTableName . ";" );
		if ($mysql->num_rows () > 0) {
			while ( $mysql->movenext () ) {
				$maxId = $mysql->getfield ( "maxId" );
			}
		}
		
		$eventName = $_REQUEST ['EVENT_NAME'];
		$startTime = $_REQUEST ['START_TIME'];
		$endTime = $_REQUEST ['END_TIME'];
		$director = $_REQUEST ['DIRECTOR'];
		$capacity = $_REQUEST ['CAPACITY'];
		$comments = $_REQUEST ['COMMENTS'];
		$isActive = $_REQUEST ['IS_ACTIVE'];
		
		$insert = new mysql ();
		
		$result = $insert->query ( "INSERT INTO " . $eventTableName . " (id, name, startTime, endTime, director, capacity, comments, active, newUsers)
					 VALUES ('" . ($maxId + 1) . "', '" . $eventName . "', '" . $startTime . "', '" . $endTime . "', '" . $director . "', '" . $capacity . "', '" . $comments . "', '" . $isActive . "', 0)" );
		
		$arr = array ();
		
		$result == 0 ? array_push ( $arr, 0 ) : array_push ( $arr, 1 );// arr[0] = 0 means fail, arr[1] = 1 means success
		array_push( $arr, 0);// arr[1] = 0 means insert, arr[1] = 1 means update
		array_push( $arr, $maxId + 1 );
		
		echo json_encode ( $arr );
		
	} else {  //STATUS > 0 means update, STATUS is the id
		
		$id = $_REQUEST ['STATUS'];
		$eventName = $_REQUEST ['EVENT_NAME'];
		$startTime = $_REQUEST ['START_TIME'];
		$endTime = $_REQUEST ['END_TIME'];
		$director = $_REQUEST ['DIRECTOR'];
		$capacity = $_REQUEST ['CAPACITY'];
		$comments = $_REQUEST ['COMMENTS'];
		$isActive = $_REQUEST ['IS_ACTIVE'];
		
		$update = new mysql();
		$result = $update->query( "UPDATE " . $eventTableName . " SET name = '" . $eventName . "', startTime = '" . $startTime . "', endtime = '" . $endTime . 
				"', director = '" . $director . "', capacity = '" . $capacity . "', comments = '" . $comments . "', active = '" . $isActive .  
				"' WHERE id = " . $id . ";");

		$arr = array ();
		
		$result == 0 ? array_push ( $arr, 0 ) : array_push ( $arr, 1 );// arr[0] = 0 means fail, arr[1] = 1 means success
		array_push( $arr, 1);// arr[1] = 0 means insert, arr[1] = 1 means update
		array_push( $arr, $id);
		
		echo json_encode ( $arr );
	
	}
	
	
}

// **********************************************************************************
// MODIFY EVENT/MODAL RETIREVAL
// **********************************************************************************
if ($_REQUEST ['ACTIONS'] == 'MODAL_RETRIEVAL') {
	
	$arr = array ();
	$mysql = new mysql ();
	
	if (isset ( $_REQUEST ['ID'] )) {
		if (! get_magic_quotes_gpc ()) {
			$id = addslashes ( $_REQUEST ['ID'] );
		} else {
			$id = $_REQUEST ['ID'];
		}
		
		$mysql->query ( "SELECT
						name, startTime, endTime,
						CASE director WHEN '' THEN '&nbsp;' ELSE director END AS director,
						capacity, comments, active
						FROM " . $eventTableName . "
						WHERE id = " . $id . ";" );
		if ($mysql->num_rows () > 0) {
			while ( $mysql->movenext () ) {
				array_push ( $arr, 1 ); // retrieve succeed
				array_push ( $arr, $mysql->getfield ( "name" ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "startTime" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "endTime" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "director" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "capacity" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "comments" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "active" ) ) );
			}
		}
		echo json_encode ( $arr );
	} else {
		array_push ( $arr, 0 ); // retrieve failed
		echo json_encode ( $arr );
	}
}

// **********************************************************************************
// GET EVENT UNPAID CUSTOMERS
// **********************************************************************************
if ($_REQUEST ['ACTIONS'] == 'GET_EVENT_UNPAID_CUSTOMERS') {
	
	$eventId = $_REQUEST['EVENT_ID'];
	
	$result = array ();
	
	$mysql = new mysql ();
	
	$mysql->query ( "SELECT
	id, name, gender, phone, 
	CASE email WHEN '' OR 'null' THEN '&nbsp;' ELSE email END AS email,
	CASE wechat WHEN '' OR 'null' THEN '&nbsp;' ELSE wechat END AS wechat,
	date, paid, comments  
	FROM " . $customerTableName . 
	" WHERE eventId = " . $eventId . 
	" AND paid <> 1 ;" );
	
	if ($mysql->num_rows () > 0) {
		while ( $mysql->movenext () ) {
			$arr = array ();
			
			array_push ( $arr, urldecode ( $mysql->getfield ( "id" ) ) );
			
			if ((urldecode ( $mysql->getfield ( "comments" ) ) == NULL) || (urldecode ( $mysql->getfield ( "comments" ) ) == ''))//if the customer is commented, highlight the name
				array_push ( $arr, urldecode ( $mysql->getfield ( "name" ) ) );
			else
				array_push ( $arr, '<div style = "color:#00868B">' . urldecode ( $mysql->getfield ( "name" ) ) . ' (C)</div>');
			array_push ( $arr, urldecode ( $mysql->getfield ( "gender" ) ) == '1' ? 'M' : ($mysql->getfield ( "gender" )  =='0' ? 'F' : "&nbsp" ));
			
			array_push ( $arr, urldecode ( $mysql->getfield ( "phone" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "email" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "wechat" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "date" ) ) );
			array_push ( $arr, '<button type="button" class="btn btn-default btn-sm" onclick="modifyCustomer(' .
					urldecode ( $mysql->getfield ( "id" ) ) . ", " . 0 . ')"><i class="icon-pencil"></i></button>' . '<button type="button" class="btn btn-default btn-sm" onclick="deleteCustomer(' .
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-trash"></i></button>' );
			$isPaid = (urldecode ( $mysql->getfield ( "paid" ) ) == '1') ? '<input type="checkbox" onClick="makePayment(this)" checked="checked" style="zoom: 140%; margin-bottom: 5px" />'
						: '<input type="checkbox" onClick="makePayment(this)" style="zoom: 140%; margin-bottom: 5px" />';
				
			array_push ( $arr, $isPaid );
				
			array_push ( $result, $arr );
		}
	}
	
	echo json_encode ( $result );
}

// **********************************************************************************
// GET EVENT ALL CUSTOMERS
// **********************************************************************************
if ($_REQUEST ['ACTIONS'] == 'GET_EVENT_ALL_CUSTOMERS') {

	$eventId = $_REQUEST['EVENT_ID'];

	$result = array ();

	$mysql = new mysql ();

	$mysql->query ( "SELECT
	id, name, gender, phone,
	CASE email WHEN '' OR 'null' THEN '&nbsp;' ELSE email END AS email,
	CASE wechat WHEN '' OR 'null' THEN '&nbsp;' ELSE wechat END AS wechat,
	date, paid, comments 
	FROM " . $customerTableName .
			" WHERE eventId = " . $eventId .
			" ;" );

	if ($mysql->num_rows () > 0) {
		while ( $mysql->movenext () ) {
			$arr = array ();

		
			array_push ( $arr, urldecode ( $mysql->getfield ( "id" ) ) );

			if ((urldecode ( $mysql->getfield ( "comments" ) ) == NULL) || (urldecode ( $mysql->getfield ( "comments" ) ) == ''))//if the customer is commented, highlight the name
				array_push ( $arr, urldecode ( $mysql->getfield ( "name" ) ) );
			else
				array_push ( $arr, '<div style = "color:#00868B">' . urldecode ( $mysql->getfield ( "name" ) ) . ' (C)</div>');
			
			array_push ( $arr, urldecode ( $mysql->getfield ( "gender" ) ) == '1' ? 'M' : ($mysql->getfield ( "gender" )  =='0' ? 'F' : "&nbsp" ));
			array_push ( $arr, urldecode ( $mysql->getfield ( "phone" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "email" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "wechat" ) ) );
			array_push ( $arr, urldecode ( $mysql->getfield ( "date" ) ) );
			array_push ( $arr, '<button type="button" class="btn btn-default btn-sm" onclick="modifyCustomer(' .
					urldecode ( $mysql->getfield ( "id" ) ) . ", " . 0 . ')"><i class="icon-pencil"></i></button>' . '<button type="button" class="btn btn-default btn-sm" onclick="deleteCustomer(' .
					urldecode ( $mysql->getfield ( "id" ) ) . ')"><i class="icon-trash"></i></button>' );
					$isPaid = (urldecode ( $mysql->getfield ( "paid" ) ) == '1') ? '<input type="checkbox" onClick="makePayment(this)" checked="checked" style="zoom: 140%; margin-bottom: 5px" />'
							: '<input type="checkbox" onClick="makePayment(this)" style="zoom: 140%; margin-bottom: 5px" />';

			array_push ( $arr, $isPaid );

			array_push ( $result, $arr );
		}
	}

	echo json_encode ( $result );
}

// **********************************************************************************
// CUSTOMER INSERTION AND UPDATE
// **********************************************************************************

if ($_REQUEST ['ACTIONS'] == 'CUSTOMER_INSERTION_AND_UPDATE') {

	$result = 0;
	$eventId = $_REQUEST['EVENT_ID'];

	if ($_REQUEST ['STATUS'] == 0) { //STATUS = 0 means insert
		$maxId = 0;

		$mysql = new mysql ();
		$mysql->query ( "SELECT MAX(id) AS maxId FROM " . $customerTableName . ";" );
		if ($mysql->num_rows () > 0) {
			while ( $mysql->movenext () ) {
				$maxId = $mysql->getfield ( "maxId" );
			}
		}

		$customerName = $_REQUEST ['CUSTOMER_NAME'];
		$gender = 		$_REQUEST ['CUSTOMER_GENDER'];
		$phone =        $_REQUEST ['CUSTOMER_PHONE'];
		$email =   		$_REQUEST ['CUSTOMER_EMAIL'];
		$wechat =  		$_REQUEST ['CUSTOMER_WECHAT'];
		$comments =  	$_REQUEST ['CUSTOMER_COMMENTS'];
		$date =  		$_REQUEST ['CUSTOMER_DATE'];
		$payment =      $_REQUEST ['CUSTOMER_PAYMENT'];
		$isPaid =       $_REQUEST ['IS_PAID'];


		$insert = new mysql ();

		$result = $insert->query ( "INSERT INTO " . $customerTableName . " (id, eventId, name, gender, phone, email, wechat, comments, date, paid, payment) 
					 VALUES ('" . ($maxId + 1) . "', '" . $eventId . "', '" . $customerName .  "', '" . $gender . "', '" . $phone . "', '" . $email . "', '" . $wechat . "', '" . $comments . "', '" . $date . "', '" . $isPaid . "', '" . $payment . "')" );

		$arr = array ();
		
		$result == 0 ? array_push ( $arr, 0 ) : array_push ( $arr, 1 );// arr[0] = 0 means fail, arr[1] = 1 means success
		array_push( $arr, 0);// arr[1] = 0 means insert, arr[1] = 1 means update
		array_push( $arr, $maxId + 1 );
		
		echo json_encode ( $arr );
		
	} else {  //STATUS > 0 means update, STATUS is the id

		$id = $_REQUEST ['STATUS'];
		$customerName = $_REQUEST ['CUSTOMER_NAME'];
		$gender = 		$_REQUEST ['CUSTOMER_GENDER'];
		$phone =        $_REQUEST ['CUSTOMER_PHONE'];
		$email =   		$_REQUEST ['CUSTOMER_EMAIL'];
		$wechat =  		$_REQUEST ['CUSTOMER_WECHAT'];
		$comments =  	$_REQUEST ['CUSTOMER_COMMENTS'];
		$date =  		$_REQUEST ['CUSTOMER_DATE'];
		$payment =      $_REQUEST ['CUSTOMER_PAYMENT'];
		$isPaid =       $_REQUEST ['IS_PAID'];

		$update = new mysql();
		$result = $update->query( "UPDATE " . $customerTableName . " SET name = '" . $customerName . "', gender = '" . $gender . "', phone = '" . $phone .
				"', email = '" . $email . "', wechat = '" . $wechat . "', comments = '" . $comments . "', date = '" . $date . "', paid = '" . $isPaid . "', payment = '" . $payment .
				"' WHERE id = " . $id . ";");
		
		$arr = array ();
		
		$result == 0 ? array_push ( $arr, 0 ) : array_push ( $arr, 1 );// arr[0] = 0 means fail, arr[1] = 1 means success
		array_push( $arr, 1);// arr[1] = 0 means insert, arr[1] = 1 means update
		array_push( $arr, $id);
		
		echo json_encode ( $arr );

	}

}

// **********************************************************************************
// SET CUSTOMER PAYMENT STATUS
// **********************************************************************************

if ($_REQUEST ['ACTIONS'] == 'SET_CUSTOMER_PAYMENT_STATUS') {

	$id = $_REQUEST ['ID'];
	$isPaid = $_REQUEST ['IS_PAID'];

	$mysql = new mysql ();

	$result = $mysql->query ( "UPDATE " . $customerTableName . " SET paid=" . $isPaid . " WHERE id=" . $id );

	echo $result;
}

// **********************************************************************************
// CUSTOMER_DELETION
// **********************************************************************************

if ($_POST ['ACTIONS'] == "CUSTOMER_DELETION") {

	if (isset ( $_POST ['ID'] )) {

		if (! get_magic_quotes_gpc ()) {//PREVENT SQL INSERTION
			$id = addslashes ( $_POST ['ID'] );
		} else {
			$id = $_POST ['ID'];
		}


		$MyDelete = new mysql ();

		$result = $MyDelete->query ( "DELETE FROM " . $customerTableName . " WHERE id = " . $id);

		echo $result;

	}

}

// **********************************************************************************
// MODIFY CUSTOMER DETAILS/MODAL RETIREVAL
// **********************************************************************************
if ($_REQUEST ['ACTIONS'] == 'MODAL_CUSTOMER_DETAILS_RETRIEVAL') {

	$arr = array ();
	$mysql = new mysql ();

	if (isset ( $_REQUEST ['ID'] )) {
		if (! get_magic_quotes_gpc ()) {
			$id = addslashes ( $_REQUEST ['ID'] );
		} else {
			$id = $_REQUEST ['ID'];
		}

		$mysql->query ( "SELECT 
						name, gender, phone, email, wechat, comments, date, paid, payment  
						FROM " . $customerTableName . "
						WHERE id = " . $id . ";" );
		if ($mysql->num_rows () > 0) {
			while ( $mysql->movenext () ) {
				array_push ( $arr, 1 ); // retrieve succeed
				array_push ( $arr, $mysql->getfield ( "name" ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "gender" ) ) == '1' ? '1' : ($mysql->getfield ( "gender" )  =='0' ? '0' : "-1" ));
				array_push ( $arr, urldecode ( $mysql->getfield ( "phone" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "email" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "wechat" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "comments" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "paid" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "payment" ) ) );
				array_push ( $arr, urldecode ( $mysql->getfield ( "date" ) ) );
			}
		}
		echo json_encode ( $arr );
	} else {
		array_push ( $arr, 0 ); // retrieve failed
		echo json_encode ( $arr );
	}
}

// **********************************************************************************
// GET EVENT NAME BY EVENT ID
// **********************************************************************************
if ($_REQUEST ['ACTIONS'] == 'GET_EVENT_NAME_BY_ID') {

	$id = $_REQUEST['EVENT_ID'];
	$mysql = new mysql ();
	$name = "";
	
	$result = $mysql->query ( "SELECT name, startTime
					FROM " . $eventTableName . "
					WHERE id = " . $id . ";" );
	if ($mysql->num_rows () > 0) {
		while ( $mysql->movenext () ) {
			$name = urldecode ( $mysql->getfield ( "name" ) );
			$name = $name . ' (' . urldecode ( $mysql->getfield ( "startTime" ) ) . ')';
		}
	}
	
	if ($result == 0) 
		 echo "error_undefinedId";
	else 
		 echo $name;
	
}

// **********************************************************************************
// CLEAR EVENT NEW USER REMINDER
// **********************************************************************************
if ($_REQUEST ['ACTIONS'] == 'CLEAR_EVENT_NEW_USER_REMINDER') {

	$id = $_REQUEST['EVENT_ID'];
	$mysql = new mysql ();

	$result = $mysql->query ( "UPDATE " . $eventTableName . " SET newUsers = 0 WHERE id=" . $id );
	

	if ($result == 0)
		echo "error_undefinedId";
	else
		echo 1;

}

?>