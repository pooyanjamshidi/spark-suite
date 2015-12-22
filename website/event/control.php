<?php
error_reporting(0);
		include ($_SERVER['DOCUMENT_ROOT']."/class/class.mysql.php");
		if($_POST['ACTIONS']=="add/edit")
		{
			if(!isset($_POST['StartTime'])||!$_POST['StartTime'])
			{
				$info['status']='0';
				$info['msg']='StartTime not null';
				echo json_encode($info);
				exit();
			}
			if(!isset($_POST['EndTime'])||!$_POST['EndTime'])
			{
				$info['status']='0';
				$info['msg']='EndTime not null';
				echo json_encode($info);
				exit();
			  
			}
			if(!isset($_POST['img_url'])||!$_POST['img_url'])
			{
				$info['status']='0';
				$info['msg']='img_url not null';
				echo json_encode($info);
				exit();
			  
			}
			
			if(!isset($_POST['title'])||!$_POST['title'])
			{
				$info['status']='0';
				$info['msg']='title not null';
				echo json_encode($info);
				exit();
			  
			}
			if(!isset($_POST['Location'])||!$_POST['Location'])
			{
				$info['status']='0';
				$info['msg']='Location not null';
				echo json_encode($info);
				exit();
			  
			}
			if(!isset($_POST['campus_selection'])||!$_POST['campus_selection'])
			{
				$info['status']='0';
				$info['msg']='campus_selection not null';
				echo json_encode($info);
				exit();
			  
			}
			if($_POST['id']!="")
				{
				  
						//**********************************************************************************
						//      ADD/EDIT 
						//**********************************************************************************	
						  $sql="update jqcalendar set Subject='".rawurlencode($_POST['title'])."',Location='".
						   rawurlencode($_POST['Location'])."',StartTime='"
						  .date('Y-m-d H:i:s',strtotime($_POST['StartTime']))."',EndTime='".
						  date('Y-m-d H:i:s',strtotime($_POST['EndTime']))."',image_url='".$_POST['img_url']."',file_url='".$_POST['file_url']."' where id=".$_POST['id'];
				}
				else
				{
				  $sql="insert into jqcalendar  (Subject,Location,StartTime,EndTime,image_url,file_url) values('".
				 rawurlencode($_POST['title'])."','".rawurlencode($_POST['Location'])."','".
				 date('Y-m-d H:i:s',strtotime($_POST['StartTime']))."','".date('Y-m-d H:i:s',strtotime($_POST['EndTime']))."','".$_POST['img_url']."','".$_POST['file_url']."')";
				}
				//$info["sql"]=$sql;
				//file_put_contents('d:/a.sql',$sql."++++".$_POST['id']);
				$MyAdd = new mysql();
				$result=$MyAdd->query($sql);
				$info['status']='1';
				echo json_encode($info);
				exit();
		  
		}
		
	    
	//**********************************************************************************
//     SELECT ONE DATA 
//**********************************************************************************
if($_GET['ACTIONS']=="select_item")
{
	$MyAdd = new mysql();

	$sql="select *  from jqcalendar where id=".$_GET['id'];
	$MyAdd->query($sql);
	$rows=$MyAdd->getAll();
		//var_dump($rows);
	while(list($key)=each($rows))
	{
	  $rows[$key]["Subject"]=rawurldecode($rows[$key]["Subject"]);
	   $rows[$key]["Location"]=rawurldecode($rows[$key]["Location"]);
	  $rows[$key]["StartTime"]= date("d F Y",$rows[$key]["StartTime"]);
	  $rows[$key]["EndTime"]= date("d F Y",$rows[$key]["EndTime"]);
	}
	$jasondata=json_encode($rows);
	echo $jasondata;
  
}


//**********************************************************************************
//     uploadimg
//**********************************************************************************
if($_GET['act']=='delete_item'){
	
	$id=$_GET["id"];
	
	$MyAdd = new mysql();
	$sql="delete from latest_news where id=".$id;
	$results=$MyAdd->query($sql);
	if($results=="1")
	{
	  print_r("success");
	}
	else
	{
	  print_r("failed");
	}
	
}
//**********************************************************************************
	//     SELECT DATA 
	//**********************************************************************************
	if($_POST['ACTIONS']=="select"){
		$data = new mysql();
		
		$sql = "SELECT id,name, price, reserved, status FROM event_tickets";
		if($_POST['event_id']!="")
		{
		  $sql = $sql." where event_id='".$_POST['event_id']."' ";
		}
		$sql=$sql." ORDER BY id";
		$data -> query($sql);
		$i = 0;
		$dataArray = array();

		if ($data -> num_rows() > 0){
			while ($data->movenext()) { 

				//$dataArray[$i][] = urldecode($data -> getfield('id'));
				//$dataArray[$i][] = $i+1;
				$dataArray[$i][] = '<span id="title'.urldecode($data -> getfield('id')).'">'.urldecode($data -> getfield('name')).'</span>';
				$dataArray[$i][] = '<span id="price'.urldecode($data -> getfield('id')).'">'.urldecode($data -> getfield('price')).'</span>';
				$dataArray[$i][] = '<span id="reserved'.urldecode($data -> getfield('id')).'">'.urldecode($data -> getfield('reserved')).'</span>';
				$temp = urldecode($data -> getfield('status'));
				if ($temp == 'Y')
					$dataArray[$i][] =  '<i class="icon-ok-sign"></i>';
				else 
					$dataArray[$i][] =  '<i class="icon-remove-sign"></i>';
					
				$dataArray[$i][] = '<button type="button" class="btn btn-default btn-sm" data-toggle="modal" onclick="addTickets('.urldecode($data -> getfield('id')).')" data-target="">
				<i class="icon-tag"></i>
				</button>
				<button type="button" class="btn btn-default btn-sm" onclick="delTickets('.urldecode($data -> getfield('id')).')">
				<i class="icon-trash"></i>
				</button>';
			
				$i++;
			}
		}
		echo json_encode($dataArray);
		
		exit();
	}
	  
	//**********************************************************************************
	//      DELETE  tickets
	//**********************************************************************************

		
		if($_POST['ACTIONS']=='DELETE_ITEM'){
			
			if (!get_magic_quotes_gpc()) {
				$id=addslashes($_POST["id"]);
			} else {
				$id=$_POST["id"];
			}
			
			$MyAdd = new mysql();
			$sql="delete from event_tickets where id=".$id;
			$results=$MyAdd->query($sql);
			if($results=="1"){
			  print_r("success");
			}
			else{
			  print_r("failed");
			}
			
		}
?>