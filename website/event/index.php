<html>
<head>
<title>Events</title>


<?php
include ($_SERVER ['DOCUMENT_ROOT'] . "/header.php");
include ("/script.php");
?>

<style type="text/css">
th,td {
	text-align: center;
}

.lead .icon-list-alt {
	margin: 6px 6px 0 0;
}

.detailData {
	margin-top: 20px;
	clear: both;
}

.errMsg {
	background-color: #ee5f5b;
}

.errMsg td:hover {
	background-color: #ee5f5b;
}

.notHover:hover>td {
	background-color: #ee5f5b !important;
}

.controls input {
	margin-left: -55px !important;
}
</style>

<div class="filler"></div>
<div class="main">
	<div class="row-fluid page-head">

		<h2 class="page-title heading-icon" aria-hidden="true">GROUPS</h2>


	</div>
	<br />


	<button type="button" class="btn btn-primary btn-sm"
		style="float: right;" onClick="insertEvent()">Add new group</button>
	<table width="100%">
		<tr>
			<td style="float: left; text-align: left;"><span class="lead"><strong><i
						class="icon-list-alt"></i>Active Groups</strong> </span></td>
		</tr>
	</table>

	<br /> <br />
	<div id="page-content" class="page-content">
		<!--column -->
		<div class="widget">
			<div class="widget-content">
				<div class="widget-body">
					<table id="activeTable"
						class="display boo-table table-striped table-hover thumb-small bg-blue-light">
						<thead>
							<tr>
								<th scope="col" width="2%">ID</th>
								<th scope="col" width="30%">Name</th>
								<th scope="col" width="13%">Director</th>
								<th scope="col" width="15%">Start Time</th>
								<th scope="col" width="15%">End Time</th>
								<th scope="col" width="22%">&nbsp;</th>
								<th scope="col" width="3%">Active</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<table width="100%">
			<tr>
				<td style="float: left; text-align: left;"><span class="lead"><strong><i
							class="icon-list-alt"></i>All groups</strong> </span></td>
			</tr>
		</table>
		<br />
		<div class="widget">
			<div class="widget-content">
				<div class="widget-body">
					<table id="eventsTable"
						class="display boo-table table-striped table-hover thumb-small bg-blue-light">
						<thead>
							<tr>
									<th scope="col" width="2%">ID</th>
								<th scope="col" width="30%">Name</th>
								<th scope="col" width="13%">Director</th>
								<th scope="col" width="15%">Start Time</th>
								<th scope="col" width="15%">End Time</th>
								<th scope="col" width="22%">&nbsp;</th>
								<th scope="col" width="3%">Active</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>

	</div>
	<!-- end of page-content -->

	<!-- modal_events -->
	<div id="modalEventInsertion" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
		data-backdrop="static" style="z-index: 0; display: none;">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true"> &times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Group Details</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="row-fluid">
							<div class="span12 form-dark">
								<fieldset>
									<!-- insert or update, if update, value is id; otherwise value is 0 -->
									<input id="status" type="text" value="0" style="display: none;">

									<ul class="form-list label-left list-bordered dotted">
										<li class="control-group"><label class="control-label"> Event
												Name<span class="required">*</span>

										</label>
											<div class="controls">
												<input id="eventName" class="span8" type="text">
											</div></li>
										<!-- // form item -->

										<li class="control-group"><label class="control-label"> Start
												Time<span class="required">*</span>

										</label>
											<div class="controls">
												<div class="input-append date form_date1" data-date=""
													data-date-format="dd MM yyyy" data-link-field="dtp_input2"
													data-link-format="yyyy-mm-dd">
													<input size="16" type="text" id="startTime" readonly
														style="height: 30px;"> <span class="add-on"><i
														class="icon-th"></i></span>
												</div>
											</div></li>
										<li class="control-group"><label for="expiry"
											class="control-label"> End Time<span class="required">*</span>

										</label>
											<div class="controls">
												<div class="input-append date form_date2" data-date=""
													data-date-format="dd MM yyyy" data-link-field="dtp_input2"
													data-link-format="yyyy-mm-dd">
													<input size="16" type="text" id="endTime" readonly
														style="height: 30px;"> <span class="add-on"><i
														class="icon-th"></i></span>
												</div>
											</div></li>
										<script type="text/javascript">
										
                                                        $('.form_date1').datetimepicker({
                                                            language:  'en',
                                                            weekStart: 1,
                                                            todayBtn:  1,
                                                            autoclose: 1,
                                                            todayHighlight: 1,
                                                            startView: 2,
                                                            minView: 2,
                                                            forceParse: 0,
                                                            format: 'dd-mm-yyyy'
                                                        });
                                                        
                                                        $('.form_date2').datetimepicker({
                                                            language:  'en',
                                                            weekStart: 1,
                                                            todayBtn:  1,
                                                            autoclose: 1,
                                                            todayHighlight: 1,
                                                            startView: 2,
                                                            minView: 2,
                                                            forceParse: 0,
                                                            format: 'dd-mm-yyyy',
                                                        });

                                                        $('.form_date2').click(function(){
                                                        	$('.form_date2').datetimepicker('setStartDate', $('#startTime').attr('value'));
                                                        });
                                                        
                                                        $('.form_date2 span').click(function(){
                                                        	$('.form_date2').datetimepicker('setStartDate', $('#startTime').attr('value'));
                                                        });
                                                        
     
                                        </script>
										<li class="control-group">
											<table>
												<tbody>
													<tr>
														<td><label>Event Director<span class="required">*</span> <input
																id="director" name="Location" class="span5" type="text"
																style="margin-left: 35px">
														</label></td>
														<td><label>Event Capacity<span class="required">*</span> <input
																id="capacity" name="Location" class="span5" type="text"
																style="margin-left: 35px">
														</label></td>
												
												</tbody>
											</table>

										</li>
										<!-- // form item -->

										<li class="control-group">
											<h4>Comments</h4> <span class="help-block"> Please enter any
												comments for the event, if required </span>
											<div class="btn-group btn-group-justified">
												<textarea id='comments' style="width: 500px; height: 130px;"></textarea>
											</div>
										</li>

										<li class="control-group"><label class="control-label">Active?

										</label>
											<div class="controls">
												<input id="isActive" type="checkbox"
													style="zoom: 140%; margin-bottom: 5px;">
											</div></li>

									</ul>
								</fieldset>
							</div>
						</div>
					</form>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary addNews"
							onClick="eventCheckAndInsert()">Save</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- end of modal -->
</div>
<!-- end of main -->

<div class="customerDetails">
	<div class="row-fluid page-head">
		<h2 class="customerPageTitle page-title heading-icon" aria-hidden="true"></h2>
	</div>
	<br />

	<button type="button" class="btn btn-default" onClick="loadMain()"float:left;">
		<i class="icon-chevron-left"></i>
	</button>

	<button type="button" class="btn btn-primary btn-sm"
		style="float: right;" onClick="insertCustomer()">Add new customer</button>

	<table width="100%">
		<tr>
			<td style="float: left; text-align: left;"><span class="lead"><strong><i
						class="icon-list-alt"></i>Unpaid Customers</strong> </span></td>
		</tr>
	</table>

	<br /> <br />
	<div id="page-content" class="page-content">
		<!--column -->
		<div class="widget">
			<div class="widget-content">
				<div class="widget-body">
					<table id="unpaidCustomersTable"
						class="display boo-table table-striped table-hover thumb-small bg-blue-light"
						width="100%">
						<thead>
							<tr>
								<th scope="col" width="2%">ID</th>
								<th scope="col" width="18%">Name</th>
								<th scope="col" width="2%">Gender</th>
								<th scope="col" width="13%">Phone</th>
								<th scope="col" width="16%">Email</th>
								<th scope="col" width="13%">WeChat</th>
								<th scope="col" width="15%">Date</th>
								<th scope="col" width="15%">&nbsp;</th>
								<th scope="col" width="5%">Paid</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>

		<br />

		<button type="button" class="btn btn-default" onClick="loadMain()"float:left;">
			<i class="icon-chevron-left"></i>
		</button>

		<table width="100%">
			<tr>
				<td style="float: left; text-align: left;"><span class="lead"><strong><i
							class="icon-list-alt"></i>All Customers</strong> </span></td>
			</tr>
		</table>

		<br /> <br />
		<div class="widget">
			<div class="widget-content">
				<div class="widget-body">
					<table id="customersTable"
							class="display boo-table table-striped table-hover thumb-small bg-blue-light"
							width="100%">
						<thead>
							<tr>
								<th scope="col" width="2%">ID</th>
								<th scope="col" width="18%">Name</th>
								<th scope="col" width="2%">Gender</th>
								<th scope="col" width="13%">Phone</th>
								<th scope="col" width="16%">Email</th>
								<th scope="col" width="13%">WeChat</th>
								<th scope="col" width="15%">Date</th>
								<th scope="col" width="15%">&nbsp;</th>
								<th scope="col" width="5%">Paid</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
		<!-- end of page-content -->


		<!-- modal_events -->
		<div id="modalCustomerInsertion" class="modal fade" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
			data-backdrop="static" style="z-index: 0; display: none;">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true"> &times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Customer Details</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal">
							<div class="row-fluid">
								<div class="span12 form-dark">
									<fieldset>
										<!-- insert or update, if update, value is id; otherwise value is 0 -->
										<input id="customerStatus" type="text" value="0"
											style="display: none;">

										<ul class="form-list label-left list-bordered dotted">
											<li class="control-group"><label class="control-label">Customer
													Name<span class="required">*</span>
											</label>
												<div class="controls">
													<input id="customerName" class="span8" type="text">
												</div></li>

											<li class="control-group">
												<table>
													<tbody>
														<tr>
															<td width="61%"><label>Phone<span class="required">*</span><input
																	id="customerPhone" name="Location" class="span8"
																	type="text" style="margin-left: 26px">
															</label></td>
															<td><label><span style="margin-left: 10px">Gender&nbsp;&nbsp;&nbsp;</span><select
																	id="customerGender" style="margin-left: 34px">
																		<option value="-1">Unknow</option>
																		<option value="1">Male</option>
																		<option value="0">Female</option>
																</select></label></td>
														</tr>
													</tbody>
												</table>

											</li>

											<li class="control-group">
												<table>
													<tbody>
														<tr>
															<td width="60%"><label>Email<input id="customerEmail"
																	name="Location" class="span8" type="text"
																	style="margin-left: 35px">
															</label></td>
															<td><label>WeChat<input id="customerWechat"
																	name="Location" class="span6" type="text"
																	style="margin-left: 35px">
															</label></td>
														</tr>
													</tbody>
												</table>

											</li>

											<li class="control-group">
												<h4>Comments</h4> <span class="help-block"> Please enter any
													comments for the customer, if required </span>
												<div class="btn-group btn-group-justified">
													<textarea id='customerComments'
														style="width: 500px; height: 130px;"></textarea>
												</div>
											</li>

											<li class="control-group">
												<table>
													<tbody>
														<tr>
															<td><label class="control-label">Paid? </label></td>
															<td><input id="isPaid" type="checkbox"
																style="zoom: 140%; margin-bottom: 5px; margin-left: -40px;"></td>

															<td><label style="margin-left: 55px">Expected Amount (£)<input id="customerPayment"
																	class="span5" type="text" style="margin-left: 35px">
															</label></td>
														</tr>
													</tbody>
												</table> 
											</li>

											<li class="control-group"><label for="expiry"
												class="control-label">Date<span class="required">*</span>

											</label>
												<div class="controls">
													<div class="input-append date form_date3" data-date=""
														data-date-format="dd MM yyyy" data-link-field="dtp_input2"
														data-link-format="yyyy-mm-dd">
														<input size="16" type="text" id="customerDate" readonly
															style="height: 30px;"> <span class="add-on"><i
															class="icon-th"></i></span>
													</div>
												</div></li>
											<script type="text/javascript">
                                                        $('.form_date3').datetimepicker({
                                                            language:  'en',
                                                            weekStart: 1,
                                                            todayBtn:  1,
                                                            autoclose: 1,
                                                            todayHighlight: 1,
                                                            startView: 2,
                                                            minView: 2,
                                                            forceParse: 0,
                                                            format: 'dd-mm-yyyy',
                                                            pickerPosition: 'top-right'
                                                        });
                                       </script>

										</ul>
									</fieldset>
								</div>
							</div>
						</form>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary addNews"
								onClick="customerCheckAndInsert()">Save</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- end of modal -->
	</div>
	<!-- end of customerDetails -->





<?php
include ($_SERVER ['DOCUMENT_ROOT'] . "/footer.php");
?>







</body>

</html>
