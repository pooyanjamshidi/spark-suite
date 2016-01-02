
	$(document).ready(function(){
		read();
		$('#parameters').DataTable();
		loadParaOptions();
				
	});

	var para_name = new Array();
	var para_description = new Array();
	var para_option = new Array();
	var para_name_selected = "";

	function loadParaOptions(){
		var input = '<input type="text" class="form-control" id="para_options_input0">';
		var button = '<button type="button" class="btn btn-info" id="para_options_button0"' 
				+ '" onClick="addParaOptionInput(0)">+</button>';	
		var table_paraOption = $('#para_options').DataTable();
		
		table_paraOption = $('#para_options').DataTable().destroy();
		$('#para_options').DataTable( {
			"paging":   false,
			"info":     false,
			"searching":   false,
		} );

		table_paraOption = $('#para_options').DataTable().clear().draw();
		
		table_paraOption.row.add([input, button]).draw();
	}
	
	function showOptions(obj){
		if (obj == 1){

			$('#block1').show();
			$('#block2').hide();
			$('#block3').hide();
			$('#block4').hide();
			$('#block5').hide();
		}
		else if (obj == 2){
		
			$('#block2').show();
			$('#block1').hide();
			$('#block3').hide();
			$('#block4').hide();
			$('#block5').hide();
		}
		else if (obj == 3){
		
			$('#block3').show();
			$('#block1').hide();
			$('#block2').hide();
			$('#block4').hide();
			  $('#block5').hide();
		}
		else if (obj == 4){
	
			$('#block4').show();
			$('#block1').hide();
			$('#block2').hide();
			$('#block3').hide();
			$('#block5').hide();
		}
		else if (obj == 5){
			$('#block=5').show();
			$('#block1').hide();
			$('#block2').hide();
			$('#block3').hide();
			$('#block5').hide();
		}
	}

// Lance au depart cette fonction permet de lire dans le fichier config.ini pour initialiser la DataTable.
	function read(){
		var table_parameter = $('#parameters').DataTable();
	 	$.ajax({
			url: 'config.ini',
			dataType: 'text',
			success: function(data) { 
				var arr_line = data.split("\n");
				var counter = -1;
				for (var i = 0; i < arr_line.length; i++){
					
					var arr_row = arr_line[i].split(",", 2);
					
					
					if (arr_row[0].length > 1){
						counter++;

						//parameter name
						var str = "<span onClick='showOptions(" + counter + ")'>" + arr_row[0] + "</span>";
						var option = '<input type="checkbox" id="isParaSelected' + counter + '" style="zoom: 140%; margin-bottom: 5px" onClick="paraSelected(' + counter + ')"/>';
						table_parameter.row.add([str, option]).draw();

						//parameter description
						var start = arr_row[0].length + arr_row[1].length + 2;
						var stop = arr_line.length - 1;
						var description = arr_line[i].substring(start, stop);

						para_name[counter] = arr_row[0];
						para_option[counter] = arr_row[1];
						para_description[counter] = description;
					}
				}
			}
		});
	
	}
	
	function showOptions(id){
		$('#para_name').text(para_name[id]);
		

		
		if (para_option[id]== 0){//boolean
			
			var table_value = $('#values').DataTable().destroy();
			$('#values').DataTable( {
				"paging":   false,
				"info":     false,
				"searching":   false,
			} );
			var table_value = $('#values').DataTable().clear().draw();
			

			var option_true = '<input type="checkbox" id="value_isTrue" checked="checked" style="zoom: 140%; margin-bottom: 5px" />';
			var option_false = '<input type="checkbox" id="value_isFalse" style="zoom: 140%; margin-bottom: 5px" />';
			table_value.row.add(["True", option_true]).draw();
			table_value.row.add(["False", option_false]).draw();
		}
		else{
			addValueInput(0);
		}
	}

	function addValueInput(counter){
		var table_value = $('#values').DataTable();;
		
		if (counter == 0){
			
			table_value = $('#values').DataTable().destroy();
			$('#values').DataTable( {
				"paging":   false,
				"info":     false,
				"searching":   false,
			} );

			table_value = $('#values').DataTable().clear().draw();
		}
		else{
			var temp_button = "#value_button" + counter;
			$(temp_button).prop('disabled', true);
		}
			
		counter++;
		if (counter < 5){//5 attamps maximum
			var input = '<input type="text" class="form-control" id="value_input' + counter + '">';
			var button = '<button type="button" class="btn btn-success" id="value_button' + counter 
				+ '" onClick="addValueInput(' + counter + ')">+</button>';
			table_value.row.add([input,button]).draw();
		}
		else{
			var input = '<input type="text" class="form-control" id="value_input' + counter + '">';
			var text = 'Max';
			table_value.row.add([input,text]).draw();
		}
	}

	function addParaOptionInput(counter){
		var table_paraOption = $('#para_options').DataTable();
		
		
		var temp_button = "#para_options_button" + counter;
		$(temp_button).prop('disabled', true);
		
			
		counter++;
		if (counter < 5){//5 attamps maximum
			var input = '<input type="text" class="form-control" id="para_options_input' + counter + '">';
			var button = '<button type="button" class="btn btn-info" id="para_options_button' + counter 
				+ '" onClick="addParaOptionInput(' + counter + ')">+</button>';
			table_paraOption.row.add([input,button]).draw();
		}
		else{
			var input = '<input type="text" class="form-control" id="para_options_input' + counter + '">';
			var text = 'Max';
			table_paraOption.row.add([input,text]).draw();
		}
	}

	/*
	var table_value; 
		if (counter == 0){
			table_value = $('#values').DataTable().destroy();
	
		}
		else{
			table_value = $('#values').DataTable();
			var temp_button = "#value_button" + (counter - 1);
			$(temp_button).prop('disabled', true);
		}

		var button = "#value_button" + counter;
		counter++;
		if (counter < 3){//5 attamps maximum
			var input = '<input type="text" class="form-control" id="value_input' + counter + '">';
			var button = '<button type="button" class="btn btn-success" id="value_button' + counter + '" onClick="addValueInput(' + counter + ')">+</button>';
			table_value.row.add([input,button]).draw();
	}
	*/
	function page_submit(){
		var para_name = getParameterName();

		if (para_name == "")
			alert("NO PARAMETER SELECTED");
		else
			alert("PARAMETER PASSED:" + para_name);
		
		
		$.post("http://129.31.208.95", {
			ACTIONS : "PASS_PARAMETERS_NAME",
			PARAMETERS : para_name
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});
	}

	function getParameterName(){
		return para_name_selected;
	}

	function paraSelected(id){

		para_id = "#isParaSelected" + id;
		if ($(para_id).prop('checked'))
			(para_name_selected == "") ? para_name_selected = para_name[id] : para_name_selected = para_name_selected + ", " + para_name[id];
	}

	function addObj(obj) {
		var idInserted = $($(obj).children()[0]).html(); //get the entry id where the entry need to be inserted

		$.post("http://129.31.208.95", {
			ACTIONS : "TEST_ADD",
			ID : idInserted
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});

		var t = $('#table_value').DataTable();
		t.row.add([
								$($(obj).children()[0]).html(),
								$($(obj).children()[1]).html(),
								$($(obj).children()[2]).html(),
								$($(obj).children()[3]).html(),
								$($(obj).children()[4]).html(),
								$($(obj).children()[5]).html(),
								$($(obj).children()[6]).html(),
								$($(obj).children()[7]).html(),
								'<input type="checkbox" onClick="make(this)" style="zoom: 140%; margin-bottom: 5px" />' ])
				.draw();
	}

	function update(id, flag) {
		var found = false;
		var isPaid = flag == 1 ? 'checked = "checked"' : false;

		var table = $('#table_value').DataTable();
		
		var data = table.data();

		var para_name;

		if ($('#table_value').attr('value') == '' || $('#table_value').attr('value') == 'null')
			para_name = $('#table_value').attr('value');
		else
			para_name = '<div style = "color:#00868B">' + $('#para_name').attr('value') + ' (C)</div>';
		
		$.each(data,function(i, value) {// if the id has already existed in the table, just update the corresponding data
			if (value[0] == id) {
								var temp = table.row(i).data();
								temp[0] = id;
								temp[1] = para_name;
								temp[2] = $('#table_value').attr('value') == '1' ? 'M' : ($('#table_value').attr('value') == '0' ? 'F' : '&nbsp;');
								temp[3] = $('#table_value').attr('value');
								temp[4] = (($('#table_value').attr('value') == "" || $('#table_value').attr('value') == "null")) ? "&nbsp;" : $('#table_value').attr('value');
								temp[5] = (($('#table_value').attr('value') == "" || $('#table_value').attr('value') == "null")) ? "&nbsp;" : $('#table_value').attr('value');
								temp[6] = $('#table_value').attr('value');
								temp[7] = '<button type="button" class="btn btn-default btn-sm" onclick="modify('
										+ id + ", " + 0
										+ ')"><i class="icon-pencil"></i></button>'
										+ '<button type="button" class="btn btn-default btn-sm" onclick="input_delete('
										+ id
										+ ')"><i class="icon-trash"></i></button>';
								
								temp[8] = '<input type="checkbox" onClick="make(this)"'
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
			   			para_name,
						$('#table_value').attr('value') == '1' ? 'M' : ($('#table_value').attr('value') == '0' ? 'F' : '&nbsp;'),
						$('#table_value').attr('value'),
						(($('#table_value').attr('value') == "" || $('#table_value').attr('value') == "null")) ? "&nbsp;" : $('#table_value').attr('value'),
						(($('#table_value').attr('value') == "" || $('#table_value').attr('value') == "null")) ? "&nbsp;" : $('#table_value').attr('value'),
						$('#table_value').attr('value'),
						'<button type="button" class="btn btn-default btn-sm" onclick="modify('
						+ id + ", " + 0
						+ ')"><i class="icon-pencil"></i></button>'
						+ '<button type="button" class="btn btn-default btn-sm" onclick="input_delete('
						+ id
						+ ')"><i class="icon-trash"></i></button>',
						
						'<input type="checkbox" onClick="make(this)"'
							+ isPaid 
							+ 'style="zoom: 140%; margin-bottom: 5px" />'
						]).draw();
		}
	}

	function make(){
		//will be done later

	}

	function input_delete(){
		var t = $('#table_value').DataTable();
		var id = $(obj).parent().parent().attr('id'); //get table id
		var idRemoved = $($(obj).children()[0]).html(); //get the entry id where the entry need to be removed

		$.post("http://129.31.208.95", {
			ACTIONS : "TEST_DELETE",
			ID : idRemoved,
			IS_ACTIVE : 0
		}, function(data) {
			if (data == 0)
				alert("fail in data updating");
		});

		var para_id = 'isParaSelected' + id;

		if (id == (para_id)) {
			var table = $('#table_value').DataTable();
			var data = table.data();
			$.each(data, function(i, value) {
				if (value[0] == idRemoved) {
					table.row(i).remove().draw(false);
					return false;
				}

			});
		} else {
			var table = $('#table_value').DataTable();
			var data = table.data();
			$.each(data, function(i, value) {
				if (value[0] == idRemoved) {
					table.row(i).remove().draw(false);
					return false;
				}

			});

			var table = $('#table_value').DataTable();
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

	function validation(){
		//will be done later
	}
