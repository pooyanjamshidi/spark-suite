
	$(document).ready(function(){
		read();
		$('#parameters').DataTable();
				
	});
	
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
	
	var para_name = new Array();
	var para_description = new Array();
	var para_option = new Array();

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
						table_parameter.row.add([str]).draw();

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
