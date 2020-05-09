$(document).ready(() => {
	fillWalletStrategy();
	
	params = parseURLParams(window.location.href); 
	if (params.type[0] === "custom") {
		$('#manual-form').css('display', 'none');
		$('#custom-form').css('display', 'block');
		customChosen = true;
	}
	else {
		$('#manual-form').css('display', 'block');
		$('#custom-form').css('display', 'none');
		customChosen = false;
	}


	$('#custom-submit').click(conductCustomTrade);
	$('#manual-submit').click(conductManualTrade);
	
	$('[data-toggle="popover"]').popover({
	  trigger: 'focus'
	});
});
