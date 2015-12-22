
<script type="text/javascript">
	$(document).ready(function() {
		init();
	});

	function init() {

		loadMain();

		$.post("ajax.php", {
			ACTIONS : "GET_EVENTS_LIST"
		}, function(data) {
			var eventList = eval(data);
			$("#eventsTable").dataTable({
				"data" : eventList
			});
		});

		$.post("ajax.php", {
			ACTIONS : "GET_ACTIVE_EVENTS"
		}, function(data) {
			var eventList = eval(data);
			$("#activeTable").dataTable({
				"data" : eventList
			});
		});

	}

	function makeEventStatus(obj) {
		if ($(obj).attr('checked') == 'checked') 
			if (confirm('Are you sure to change group status to ACTIVE? \nIf click YES, this group will be added to "ACTIVE GROUPS"!'))
				addActiveEvent($($(obj).parent().parent()));
			else
				$(obj).attr('checked', false);
		else
			if (confirm('Are you sure to change group status to INACTIVE? \nIf click YES, this group will be removed from "ACIVE GROUPS"!'))
				removeActiveEvent($($(obj).parent().parent()));
			else
				$(obj).attr('checked', 'checked');
	};

	function addActiveEvent(obj) {
		var idInserted = $($(obj).children()[0]).html(); //get the entry id where the entry need to be inserted
		$.post("ajax.php", {
			ACTIONS : "SET_EVENT_STATUS",
			ID : idInserted,
			IS_ACTIVE : 1
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});

		var t = $('#activeTable').DataTable();
		t.row.add([
								$($(obj).children()[0]).html(),
								$($(obj).children()[1]).html(),
								$($(obj).children()[2]).html(),
								$($(obj).children()[3]).html(),
								$($(obj).children()[4]).html(),
								$($(obj).children()[5]).html(),
								'<input type="checkbox" onClick="makeEventStatus(this)" checked="checked" style="zoom: 140%; margin-bottom: 5px" />' ])
				.draw();

	}

	function removeActiveEvent(obj) {
		var t = $('#activeTable').DataTable();
		var id = $(obj).parent().parent().attr('id'); //get table id
		var idRemoved = $($(obj).children()[0]).html(); //get the entry id where the entry need to be removed

		$.post("ajax.php", {
			ACTIONS : "SET_EVENT_STATUS",
			ID : idRemoved,
			IS_ACTIVE : 0
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});

		if (id == 'eventsTable') {
			var table = $('#activeTable').DataTable();
			var data = table.data();
			$.each(data, function(i, value) {
				if (value[0] == idRemoved) {
					table.row(i).remove().draw(false);
					return false;
				}

			});
		} else {
			var table = $('#activeTable').DataTable();
			var data = table.data();
			$.each(data, function(i, value) {
				if (value[0] == idRemoved) {
					table.row(i).remove().draw(false);
					return false;
				}

			});

			var table = $('#eventsTable').DataTable();
			var data = table.data();
			$.each(data,function(i, value) {
								if (value[0] == idRemoved) {
									var temp = table.row(i).data();
									temp[6] = '<input type="checkbox" onClick="makeEventStatus(this)" style="zoom: 140%; margin-bottom: 5px" />';
									table.row(i).data(temp).draw(false);
									return false;
								}

							});
		}

	}

	function eventModalValidate() {
		var isFilled = true;
		if ($('#eventName').attr('value') == ""
				|| $('#eventName').attr('value') == "null") {
			document.getElementById('eventName').style.borderColor = "red";
			document.getElementById('eventName').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('eventName').style.borderColor = "#d1d1d1";
		}

		if ($('#startTime').attr('value') == ""
				|| $('#startTime').attr('value') == "null") {
			document.getElementById('startTime').style.borderColor = "red";
			document.getElementById('startTime').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('startTime').style.borderColor = "#d1d1d1";
		}

		if ($('#endTime').attr('value') == ""
				|| $('#endTime').attr('value') == "null") {
			document.getElementById('endTime').style.borderColor = "red";
			document.getElementById('endTime').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('endTime').style.borderColor = "#d1d1d1";
		}

		if ($('#director').attr('value') == ""
				|| $('#director').attr('value') == "null") {
			document.getElementById('director').style.borderColor = "red";
			document.getElementById('director').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('director').style.borderColor = "#d1d1d1";
		}

		if ($('#capacity').attr('value') == ""
				|| $('#capacity').attr('value') == "null") {
			document.getElementById('capacity').style.borderColor = "red";
			document.getElementById('capacity').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('capacity').style.borderColor = "#d1d1d1";
		}

		return isFilled;
	}

	function eventCheckAndInsert() {
		var flag_isActive = $('#isActive').attr('checked') == 'checked' ? 1 : 0;

		if (eventModalValidate()) {
			if ($('#status').attr('value') == 0){
				$('#modalEventInsertion').modal('hide');

				$.post("ajax.php", {
					ACTIONS : "EVENT_INSERTION_AND_UPDATE",
					STATUS : $('#status').attr('value'),
					EVENT_NAME : $('#eventName').attr('value'),
					START_TIME : $('#startTime').attr('value'),
					END_TIME : $('#endTime').attr('value'),
					DIRECTOR : $('#director').attr('value'),
					CAPACITY : $('#capacity').attr('value'),
					COMMENTS : $('#comments').attr('value'),
					IS_ACTIVE : flag_isActive
				}, function(array) {
					arr = eval(array);
					if (arr[0] == 0) // arr[0] = 0 means fail, arr[1] = 1 means success
						alert("fail in data updating");
					else {
						updateEventTable(arr[2], flag_isActive); //arr[2] = id

						if (arr[1] == 1) // arr[1] = 0 means insert, arr[1] = 1 means update
							alert('Update successful');
					}
				});
			}
			else
				if (confirm('Do you want to change the customer details?' )){
					$('#modalEventInsertion').modal('hide');

					$.post("ajax.php", {
						ACTIONS : "EVENT_INSERTION_AND_UPDATE",
						STATUS : $('#status').attr('value'),
						EVENT_NAME : $('#eventName').attr('value'),
						START_TIME : $('#startTime').attr('value'),
						END_TIME : $('#endTime').attr('value'),
						DIRECTOR : $('#director').attr('value'),
						CAPACITY : $('#capacity').attr('value'),
						COMMENTS : $('#comments').attr('value'),
						IS_ACTIVE : flag_isActive
					}, function(array) {
						arr = eval(array);
						if (arr[0] == 0) // arr[0] = 0 means fail, arr[1] = 1 means success
							alert("fail in data updating");
						else {
							updateEventTable(arr[2], flag_isActive); //arr[2] = id

							if (arr[1] == 1) // arr[1] = 0 means insert, arr[1] = 1 means update
								alert('update successful');
						}
					});
				}
		}
	}

	function updateEventTable(id, flag_isActive) {
		var found = false;
		var isActive = flag_isActive == 1 ? 'checked = "checked"' : false;
		var table = $('#eventsTable').DataTable();
		var data = table.data();

		$.each(data,function(i, value) {// if the id has already existed in the table, just update the corresponding data
							if (value[0] == id) {
								var temp = table.row(i).data();

								temp[0] = id;
								temp[1] = $('#eventName').attr('value');
								temp[2] = $('#director').attr('value');
								temp[3] = $('#startTime').attr('value');
								temp[4] = $('#endTime').attr('value');
								temp[5] = '<button type="button" class="btn btn-default btn-sm" onclick="detail('
										+ id
										+ ')"><i class="icon-tag"></i></button>'
										+ '<button type="button" class="btn btn-default btn-sm" onclick="modifyEvent('
										+ id
										+ ')"><i class="icon-pencil"></i></button>'
										+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteEvent('
										+ id
										+ ')"><i class="icon-trash"></i></button>';
								temp[6] = '<input type="checkbox" onClick="makeEventStatus(this)"'
										+ isActive
										+ 'style="zoom: 140%; margin-bottom: 5px" />';
								table.row(i).data(temp).draw();
								found = true;
								return false;
							}

						});

		if (found == false) {
			table.row.add([
									id,
									$('#eventName').attr('value'),
									$('#director').attr('value'),
									$('#startTime').attr('value'),
									$('#endTime').attr('value'),
									'<button type="button" class="btn btn-default btn-sm" onclick="detail('
											+ id
											+ ')"><i class="icon-tag"></i></button>'
											+ '<button type="button" class="btn btn-default btn-sm" onclick="modifyEvent('
											+ id
											+ ')"><i class="icon-pencil"></i></button>'
											+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteEvent('
											+ id
											+ ')"><i class="icon-trash"></i></button>',
									'<input type="checkbox" onClick="makeEventStatus(this)"'
											+ isActive
											+ 'style="zoom: 140%; margin-bottom: 5px" />' ])
					.draw();
		}

		if (flag_isActive == 1) {//active, then update activeTable either
			found = false;
			var table = $('#activeTable').DataTable();
			var data = table.data();

			$.each(data,function(i, value) {// if the id has already existed in the table, just update the corresponding data
								if (value[0] == id) {
									var temp = table.row(i).data();

									temp[0] = id;
									temp[1] = $('#eventName').attr('value');
									temp[2] = $('#director').attr('value');
									temp[3] = $('#startTime').attr('value');
									temp[4] = $('#endTime').attr('value');
									temp[5] = '<button type="button" class="btn btn-default btn-sm" onclick="detail('
											+ id
											+ ')"><i class="icon-tag"></i></button>'
											+ '<button type="button" class="btn btn-default btn-sm" onclick="modifyEvent('
											+ id
											+ ')"><i class="icon-pencil"></i></button>'
											+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteEvent('
											+ id
											+ ')"><i class="icon-trash"></i></button>';
									temp[6] = '<input type="checkbox" onClick="makeEventStatus(this)"'
											+ isActive
											+ 'style="zoom: 140%; margin-bottom: 5px" />';
									table.row(i).data(temp).draw();
									found = true;
									return false;
								}

							});

			if (found == false) {
				table.row.add(
								[
										id,
										$('#eventName').attr('value'),
										$('#director').attr('value'),
										$('#startTime').attr('value'),
										$('#endTime').attr('value'),
										'<button type="button" class="btn btn-default btn-sm" onclick="detail('
												+ id
												+ ')"><i class="icon-tag"></i></button>'
												+ '<button type="button" class="btn btn-default btn-sm" onclick="modifyEvent('
												+ id
												+ ')"><i class="icon-pencil"></i></button>'
												+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteEvent('
												+ id
												+ ')"><i class="icon-trash"></i></button>',
										'<input type="checkbox" onClick="makeEventStatus(this)"'
												+ isActive
												+ 'style="zoom: 140%; margin-bottom: 5px" />' ])
						.draw();
			}
		}

		resetModalEventInsertion();
	}

	function resetModalEventInsertion() {
		document.getElementById('eventName').style.borderColor = "#d1d1d1";
		document.getElementById('startTime').style.borderColor = "#d1d1d1";
		document.getElementById('endTime').style.borderColor = "#d1d1d1";
		document.getElementById('director').style.borderColor = "#d1d1d1";
		document.getElementById('capacity').style.borderColor = "#d1d1d1";

		document.getElementById('eventName').placeholder = "";
		document.getElementById('startTime').placeholder = "";
		document.getElementById('endTime').placeholder = "";
		document.getElementById('director').placeholder = "";
		document.getElementById('capacity').placeholder = "";

		//clean modal data
		$('#status').attr('value', 0); //update or insert; if update, value is id; otherwise value is 0 
		$('#eventName').attr('value', '');
		$('#startTime').attr('value', '');
		$('#endTime').attr('value', '');
		$('#director').attr('value', '');
		$('#capacity').attr('value', '');
		$('#comments').attr('value', '');
		$('#isActive').attr('checked', false);
	}

	function insertEvent() {
		resetModalEventInsertion();
		$('#modalEventInsertion').modal('show');
	}

	function modifyEvent(id) {

		$.post("ajax.php", {
			ACTIONS : "MODAL_RETRIEVAL",
			ID : id
		}, function(data) {
			resetModalEventInsertion();
			

			data = eval(data);
			if (data[0] == 1) { //successful

				var flag_isActive = data[7] == 1 ? "checked" : false;

				$('#status').attr('value', id);//aim to update instead of inserting, if update, value is id; otherwise value is 0 

				$('#eventName').attr('value', data[1]);
				$('#startTime').attr('value', data[2]);
				$('#endTime').attr('value', data[3]);
				$('#director').attr('value', data[4]);
				$('#capacity').attr('value', data[5]);
				$('#comments').attr('value', data[6]);
				$('#isActive').attr('checked', flag_isActive);
				
				$('#modalEventInsertion').modal('show');
			} else
				alert("fail in data retireval");
		});
	}

	function deleteEvent(id) {
		if (confirm('Are you sure to delete? If yes, all data/customers associated to this group will be damaged!') == true) {

			$.post("ajax.php", {
				ACTIONS : "EVENT_DELETION",
				ID : id
			}, function(flag) {
				if (flag > 0) {

					//if occurring in activeTable, remove the row
					var table = $('#activeTable').DataTable();
					var data = table.data();
					$.each(data, function(i, value) {
						if (value[0] == id) {
							table.row(i).remove().draw(false);
							return false;
						}

					});

					//if occurring in eventsTable, remove the row
					var table = $('#eventsTable').DataTable();
					var data = table.data();
					$.each(data, function(i, value) {
						if (value[0] == id) {
							table.row(i).remove().draw(false);
							return false;
						}

					});

				} else
					alert("fail in data retireval");
			})
		}
	}

	var eventIndex = 0;// eventId
	//**********************************************************************************
	//     display customers for specified event
	//**********************************************************************************
	function detail(eventId) {

		this.eventIndex = eventId;

		$.post("ajax.php", {
			ACTIONS : "CLEAR_EVENT_NEW_USER_REMINDER",
			EVENT_ID : eventId
		}, function(data) {
			if (data == "error_undefinedId"){
				alert("undefined group id");
				return false;
			}
		});

		$.post("ajax.php", {
			ACTIONS : "GET_EVENT_NAME_BY_ID",
			EVENT_ID : eventId
		}, function(data) {
			if (data == "error_undefinedId"){
				alert("undefined group id");
				return false;
			}
			else
				$(".customerPageTitle").html(data);
		});
		
		$("html,body").animate({//page is displayed from the top 
			scrollTop : 0
		}, 500);

		$('.main').hide();
		$('.customerDetails').show();

		$.post("ajax.php", {
			ACTIONS : "GET_EVENT_UNPAID_CUSTOMERS",
			EVENT_ID : eventId
		}, function(data) {
			var customerList = eval(data);
			$("#unpaidCustomersTable").DataTable().destroy();
			$("#unpaidCustomersTable").dataTable({
				"data" : customerList,
				"order": [[ 6, "desc" ]],
			});
		});

		$.post("ajax.php", {
			ACTIONS : "GET_EVENT_ALL_CUSTOMERS",
			EVENT_ID : eventId
		}, function(data) {
			var customerList = eval(data);
			$("#customersTable").DataTable().destroy();
			$("#customersTable").dataTable({
				"data" : customerList,
				"order": [[ 6, "desc" ]],
			});
		});

	}

	//**********************************************************************************
	//     back to event list
	//**********************************************************************************
	function loadMain() {
		$('.customerDetails').hide();
		$('.main').show();
	}

	//**********************************************************************************
	//     reset/remove the content of the modal
	//**********************************************************************************
	function resetModalCustomerInsertion() {
		document.getElementById('customerName').style.borderColor = "#d1d1d1";
		document.getElementById('customerName').placeholder = "";
		document.getElementById('customerPhone').style.borderColor = "#d1d1d1";
		document.getElementById('customerPhone').placeholder = "";
		document.getElementById('customerDate').style.borderColor = "#d1d1d1";
		document.getElementById('customerDate').placeholder = "";
		document.getElementById('customerPayment').style.borderColor = "#d1d1d1";
		document.getElementById('customerPayment').placeholder = "";

		//clean modal data
		$('#customerStatus').attr('value', '');//update or insert; if update, value is id; otherwise value is 0
		$('#customerName').attr('value', '');
		$('#customerGender').attr('value', '-1');
		$('#customerPhone').attr('value', '');
		$('#customerEmail').attr('value', '');
		$('#customerWechat').attr('value', '');
		$('#customerComments').attr('value', '');
		$('#customerDate').attr('value', '');
		$('#isPaid').attr('checked', false);
		$('#customerPayment').attr('value', '0');

	}

	//**********************************************************************************
	//     insert a new customer (just open the modal)
	//**********************************************************************************
	function insertCustomer() {
		resetModalCustomerInsertion();
		$('#modalCustomerInsertion').modal('show');
	}

	function customerModalValidate() {
		var isFilled = true;
		if ($('#customerName').attr('value') == ""
				|| $('#customerName').attr('value') == "null") {
			document.getElementById('customerName').style.borderColor = "red";
			document.getElementById('customerName').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('customerName').style.borderColor = "#d1d1d1";
		}

		if ($('#customerPhone').attr('value') == ""
				|| $('#customerPhone').attr('value') == "null") {
			document.getElementById('customerPhone').style.borderColor = "red";
			document.getElementById('customerPhone').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('customerPhone').style.borderColor = "#d1d1d1";
		}

		if ($('#customerDate').attr('value') == ""
				|| $('#customerDate').attr('value') == "null") {
			document.getElementById('customerDate').style.borderColor = "red";
			document.getElementById('customerDate').placeholder = "cannot empty";
			isFilled = false;
		} else {
			document.getElementById('customerDate').style.borderColor = "#d1d1d1";
		}

		if ($('#isPaid').attr('checked') == 'checked')
			if ($('#customerPayment').attr('value') == 0){
				document.getElementById('customerPayment').style.borderColor = "red";
				$('#customerPayment').attr('value', '');
				document.getElementById('customerPayment').placeholder = "Amount Required";
				isFilled = false;
			}
			else {
				document.getElementById('customerPayment').style.borderColor = "#d1d1d1";
			}

		return isFilled;
	}

	//**********************************************************************************
	//     insert a new customer to database
	//**********************************************************************************
	function customerCheckAndInsert() {

		var flag_isPaid = $('#isPaid').attr('checked') == 'checked' ? 1 : 0; //whether the customer has already paid 

		if (customerModalValidate()) {

			if ($('#customerStatus').attr('value') == 0){ //$('#customerStatus').attr('value')  == 0 means insertion $('#customerStatus').attr('value') == 1 means update 
				$('#modalCustomerInsertion').modal('hide');
				$.post("ajax.php", {
					ACTIONS : 'CUSTOMER_INSERTION_AND_UPDATE',
					STATUS : $('#customerStatus').attr('value'),
					EVENT_ID : this.eventIndex,
					CUSTOMER_NAME : $('#customerName').attr('value'),
					CUSTOMER_PHONE : $('#customerPhone').attr('value'),
					CUSTOMER_GENDER : $('#customerGender').attr('value'),
					CUSTOMER_EMAIL : $('#customerEmail').attr('value'),
					CUSTOMER_WECHAT : $('#customerWechat').attr('value'),
					CUSTOMER_COMMENTS : $('#customerComments').attr('value'),
					CUSTOMER_DATE : $('#customerDate').attr('value'),
					CUSTOMER_PAYMENT: $('#customerPayment').attr('value'),
					IS_PAID : flag_isPaid
				}, function(array) {
					var arr = eval(array);
					if (arr[0] == 0) // arr[0] = 0 means fail, arr[1] = 1 means success
						alert("Fail in data updating");
					else {
						flag_isPaid = $('#isPaid').attr('checked') == 'checked' ? 1 : 0; //whether the customer has already paid 
						updateCustomerTable(arr[2], flag_isPaid); //arr[2] = id
						

						if (arr[1] == 1) // arr[1] = 0 means insert, arr[1] = 1 means update
							alert('Update successful');
					}
				});
			}
			else //$('#customerStatus').attr('value')  == 0 means insertion $('#customerStatus').attr('value') == 1 means update 
				if (confirm('Do you want to change the customer details?' )){ 
					$('#modalCustomerInsertion').modal('hide');
					$.post("ajax.php", {
						ACTIONS : 'CUSTOMER_INSERTION_AND_UPDATE',
						STATUS : $('#customerStatus').attr('value'),
						EVENT_ID : this.eventIndex,
						CUSTOMER_NAME : $('#customerName').attr('value'),
						CUSTOMER_PHONE : $('#customerPhone').attr('value'),
						CUSTOMER_GENDER : $('#customerGender').attr('value'),
						CUSTOMER_EMAIL : $('#customerEmail').attr('value'),
						CUSTOMER_WECHAT : $('#customerWechat').attr('value'),
						CUSTOMER_COMMENTS : $('#customerComments').attr('value'),
						CUSTOMER_DATE : $('#customerDate').attr('value'),
						CUSTOMER_PAYMENT: $('#customerPayment').attr('value'),
						IS_PAID : flag_isPaid
					}, function(array) {
						var arr = eval(array);
						if (arr[0] == 0) // arr[0] = 0 means fail, arr[1] = 1 means success
							alert("fail in data updating");
						else {
							
							flag_isPaid = $('#isPaid').attr('checked') == 'checked' ? 1 : 0; //whether the customer has already paid 
							if (flag_isPaid == 1){
								//remove entry
								var table = $('#unpaidCustomersTable').DataTable();
								var data = table.data();
								var id = $('#customerStatus').attr('value');
								$.each(data, function(i, value) {
									if (value[0] == id) {
										table.row(i).remove().draw(false);
										return false;
									}

								});
							}
							updateCustomerTable(arr[2], flag_isPaid); //arr[2] = id

							//if (arr[1] == 1) // arr[1] = 0 means insert, arr[1] = 1 means update
								//alert('update successful');
						}
					});
				}
		}
	}

	function updateCustomerTable(id, flag_isPaid) {
		var found = false;
		var isPaid = flag_isPaid == 1 ? 'checked = "checked"' : false;

		var table = $('#customersTable').DataTable();
		
		var data = table.data();

		var customerName;

		if ($('#customerComments').attr('value') == '' || $('#customerComments').attr('value') == 'null')
			customerName = $('#customerName').attr('value');
		else
			customerName = '<div style = "color:#00868B">' + $('#customerName').attr('value') + ' (C)</div>';
		
		$.each(data,function(i, value) {// if the id has already existed in the table, just update the corresponding data
			if (value[0] == id) {
								var temp = table.row(i).data();
								temp[0] = id;
								temp[1] = customerName;
								temp[2] = $('#customerGender').attr('value') == '1' ? 'M' : ($('#customerGender').attr('value') == '0' ? 'F' : '&nbsp;');
								temp[3] = $('#customerPhone').attr('value');
								temp[4] = (($('#customerEmail').attr('value') == "" || $('#customerEmail').attr('value') == "null")) ? "&nbsp;" : $('#customerEmail').attr('value');
								temp[5] = (($('#customerWechat').attr('value') == "" || $('#customerWechat').attr('value') == "null")) ? "&nbsp;" : $('#customerWechat').attr('value');
								temp[6] = $('#customerDate').attr('value');
								temp[7] = '<button type="button" class="btn btn-default btn-sm" onclick="modifyCustomer('
										+ id + ", " + 0
										+ ')"><i class="icon-pencil"></i></button>'
										+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteCustomer('
										+ id
										+ ')"><i class="icon-trash"></i></button>';
								
								temp[8] = '<input type="checkbox" onClick="makePayment(this)"'
										+ isPaid 
										+ 'style="zoom: 140%; margin-bottom: 5px" />';
										
								table.row(i).data(temp).draw();
								found = true;
								return false;
			}

		});

		if (found == false) {
			table.row.add([
			   			id,
			   			customerName,
						$('#customerGender').attr('value') == '1' ? 'M' : ($('#customerGender').attr('value') == '0' ? 'F' : '&nbsp;'),
						$('#customerPhone').attr('value'),
						(($('#customerEmail').attr('value') == "" || $('#customerEmail').attr('value') == "null")) ? "&nbsp;" : $('#customerEmail').attr('value'),
						(($('#customerWechat').attr('value') == "" || $('#customerWechat').attr('value') == "null")) ? "&nbsp;" : $('#customerWechat').attr('value'),
						$('#customerDate').attr('value'),
						'<button type="button" class="btn btn-default btn-sm" onclick="modifyCustomer('
						+ id + ", " + 0
						+ ')"><i class="icon-pencil"></i></button>'
						+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteCustomer('
						+ id
						+ ')"><i class="icon-trash"></i></button>',
						
						'<input type="checkbox" onClick="makePayment(this)"'
							+ isPaid 
							+ 'style="zoom: 140%; margin-bottom: 5px" />'
						]).draw();
		}

		if (flag_isPaid == 0){ //unpaid, then update unpaidCustomersTable either
			var table = $('#unpaidCustomersTable').DataTable();
			
			var data = table.data();

			$.each(data,function(i, value) {// if the id has already existed in the table, just update the corresponding data
				if (value[0] == id) {
									var temp = table.row(i).data();
									temp[0] = id;
									temp[1] = customerName;
									temp[2] = $('#customerGender').attr('value') == '1' ? 'M' : ($('#customerGender').attr('value') == '0' ? 'F' : '&nbsp;');
									temp[3] = $('#customerPhone').attr('value');
									temp[4] = (($('#customerEmail').attr('value') == "" || $('#customerEmail').attr('value') == "null")) ? "&nbsp;" : $('#customerEmail').attr('value');
									temp[5] = (($('#customerWechat').attr('value') == "" || $('#customerWechat').attr('value') == "null")) ? "&nbsp;" : $('#customerWechat').attr('value');
									temp[6] = $('#customerDate').attr('value');
									temp[7] = '<button type="button" class="btn btn-default btn-sm" onclick="modifyCustomer('
											+ id + ", " + 0
											+ ')"><i class="icon-pencil"></i></button>'
											+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteCustomer('
											+ id
											+ ')"><i class="icon-trash"></i></button>';
									
									temp[8] = '<input type="checkbox" onClick="makePayment(this)"'
											+ isPaid 
											+ 'style="zoom: 140%; margin-bottom: 5px" />';
											
									table.row(i).data(temp).draw();
									found = true;
									return false;
								}

				});

			if (found == false) {
				table.row.add([
				   			id,
				   			customerName,
							$('#customerGender').attr('value') == '1' ? 'M' : ($('#customerGender').attr('value') == '0' ? 'F' : '&nbsp;'),
							$('#customerPhone').attr('value'),
							(($('#customerEmail').attr('value') == "" || $('#customerEmail').attr('value') == "null")) ? "&nbsp;" : $('#customerEmail').attr('value'),
							(($('#customerWechat').attr('value') == "" || $('#customerWechat').attr('value') == "null")) ? "&nbsp;" : $('#customerWechat').attr('value'),
							$('#customerDate').attr('value'),
							'<button type="button" class="btn btn-default btn-sm" onclick="modifyCustomer('
							+ id + ", " + 0
							+ ')"><i class="icon-pencil"></i></button>'
							+ '<button type="button" class="btn btn-default btn-sm" onclick="deleteCustomer('
							+ id
							+ ')"><i class="icon-trash"></i></button>',
							
							'<input type="checkbox" onClick="makePayment(this)"'
								+ isPaid 
								+ 'style="zoom: 140%; margin-bottom: 5px" />'
							]).draw();
			}
		}

		resetModalCustomerInsertion();
	}

	function makePayment(obj){
			if ($(obj).attr('checked') == 'checked')
				if (confirm('Are you sure to change payment status to PAID for this customer? \nIf click YES, the customer will be removed from the "Unpaid Customers" table!')){
					//removeUnpaidCustomer($($(obj).parent().parent()));
					$(obj).attr('checked', false);// if cancle the change, should set to unpaid; otherwise the modal will do the work
					
					var id = $($(obj).parent().parent().children()[0]).html(); //get customer id
					modifyCustomer(id, 1); // pop up modal
					
				}
				else
					$(obj).attr('checked', false);
			else
				if (confirm('Are you sure to change payment status to UNPAID for this customer? \nIf click YES, the customer will be added to the "Unpaid Customers" table!'))
					addUnpaidCustomer($($(obj).parent().parent()));
				else
					$(obj).attr('checked', 'checked');
	}

	function addUnpaidCustomer(obj) {
		var idInserted = $($(obj).children()[0]).html(); //get the entry id where the entry need to be inserted

		$.post("ajax.php", {
			ACTIONS : "SET_CUSTOMER_PAYMENT_STATUS",
			ID : idInserted,
			IS_PAID : 0
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});

		var t = $('#unpaidCustomersTable').DataTable();
		t.row.add([
								$($(obj).children()[0]).html(),
								$($(obj).children()[1]).html(),
								$($(obj).children()[2]).html(),
								$($(obj).children()[3]).html(),
								$($(obj).children()[4]).html(),
								$($(obj).children()[5]).html(),
								$($(obj).children()[6]).html(),
								$($(obj).children()[7]).html(),
								'<input type="checkbox" onClick="makePayment(this)" style="zoom: 140%; margin-bottom: 5px" />' ])
				.draw();
	}

	function removeUnpaidCustomer(obj) {
		var id = $(obj).parent().parent().attr('id'); //get table id
		var idRemoved = $($(obj).children()[0]).html(); //get the entry id where the entry need to be removed

		$.post("ajax.php", {
			ACTIONS : "SET_CUSTOMER_PAYMENT_STATUS",
			ID : idRemoved,
			IS_ACTIVE : 1
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});

		//no matter which table the click from, remove the entry in unpaidCustomersTable (as this is to set unpaid->paid, there must be an entry under unpaidCustomersTable)
		var table = $('#unpaidCustomersTable').DataTable();
		var data = table.data();
		$.each(data, function(i, value) {
			if (value[0] == idRemoved) {
				table.row(i).remove().draw(false);
				return false;
			}

		});

		//if click from unpaidCustomersTable, then not only remove then entry in unpaidCustomersTable, also update the entry in customersTable
		if (id == 'unpaidCustomersTable') {
			var table = $('#customersTable').DataTable();
			var data = table.data();
			$.each(data,function(i, value) {
								if (value[0] == idRemoved) {
									var temp = table.row(i).data();
									temp[8] = '<input type="checkbox" onClick="makePayment(this)" checked="checked" style="zoom: 140%; margin-bottom: 5px" />';
									table.row(i).data(temp).draw(false);
									return false;
								}

							});
		}

	}

	function deleteCustomer(id) {
		if (confirm('Are you sure to delete? If yes, all details associated to this customer will be damaged!') == true) {

			$.post("ajax.php", {
				ACTIONS : "CUSTOMER_DELETION",
				ID : id
			}, function(flag) {
				if (flag > 0) {

					//if occurring in unpaidCustomersTable, remove the row
					var table = $('#unpaidCustomersTable').DataTable();
					var data = table.data();
					$.each(data, function(i, value) {
						if (value[0] == id) {
							table.row(i).remove().draw(false);
							return false;
						}

					});

					//if occurring in customersTable, remove the row
					var table = $('#customersTable').DataTable();
					var data = table.data();
					$.each(data, function(i, value) {
						if (value[0] == id) {
							table.row(i).remove().draw(false);
							return false;
						}

					});

				} else
					alert("fail in data retireval");
			})
		}
	}

	function modifyCustomer(id, isForMakingPayment) {

		$.post("ajax.php", {
			ACTIONS : "MODAL_CUSTOMER_DETAILS_RETRIEVAL",
			ID : id
		}, function(data) {
			resetModalCustomerInsertion();

			data = eval(data);
			if (data[0] == 1) { //successful

				var flag_isPaid = data[7] == 1 ? "checked" : false;

				$('#customerStatus').attr('value', id);//aim to update instead of inserting, if update, value is id; otherwise value is 0 

				$('#customerName').attr('value', data[1]);
				$('#customerGender').attr('value', data[2]);
				$('#customerPhone').attr('value', data[3]);
				$('#customerEmail').attr('value', data[4]);
				$('#customerWechat').attr('value', data[5]);
				$('#customerComments').attr('value', data[6]);
				$('#isPaid').attr('checked', flag_isPaid);
				$('#customerPayment').attr('value', data[8]);
				$('#customerDate').attr('value', data[9]);

				if (isForMakingPayment != 1)
					$('#modalCustomerInsertion').modal('show');
				else{
					$('#isPaid').attr('checked', 'checked');
					$('#customerPayment').focus();
					$('#customerPayment').attr('value', '');
					document.getElementById('customerPayment').placeholder = "Amount Required";
					document.getElementById('customerPayment').style.borderColor = "red";
					$('#modalCustomerInsertion').modal('show');
				}
			} else
				alert("fail in data retireval");
		});
	}
</script>