
	$(document).ready(function(){
		$('#parameters').DataTable();
		$('').DataTable();
		$('#values1').DataTable( {
			"paging":   true,
			"info":     true,
			"searching":   true
		} );
		$('#values2').DataTable( {
			"paging":   true,
			"info":     true,
			"searching":   true
		} );
		$('#values3').DataTable( {
			"paging":   true,
			"info":     true,
			"searching":   true
		} );
		$('#values4').DataTable( {
			"paging":   true,
			"info":     true,
			"searching":   true
		} );
		$('#values5').DataTable( {
			"paging":   true,
			"info":     true,
			"searching":   true
		} );
		
	});
	
	function showOptions(obj){
		if (obj == 1){

			$('#values1').show();
			$('#values2').hide();
			$('#values3').hide();
			$('#values4').hide();
			$('#values5').hide();
		}
		else if (obj == 2){
		
			$('#values2').show();
			$('#values1').hide();
			$('#values3').hide();
			$('#values4').hide();
			$('#values5').hide();
		}
		else if (obj == 3){
		
			$('#values3').show();
			$('#values1').hide();
			$('#values2').hide();
			$('#values4').hide();
			$('#values5').hide();
		}
		else if (obj == 4){
	
			$('#values4').show();
			$('#values1').hide();
			$('#values2').hide();
			$('#values3').hide();
			$('#values5').hide();
		}
		else if (obj == 5){
			$('#values=5').show();
			$('#values1').hide();
			$('#values2').hide();
			$('#values3').hide();
			$('#values5').hide();
		}
	}
