//jQuery time
var current_fs, next_fs, previous_fs; //fieldsets
var left, opacity, scale; //fieldset properties which we will animate
var animating; //flag to prevent quick multi-click glitches

let manualCoinChosen = false; 
let customCoinChosen = false;

function buySellToggle() {
	if (document.getElementById('buy-radio').checked) {
		$('#trade-details-title').html('BUY Trade details'); 
		$("#man-sell-group").insertAfter("#man-buy-group");
	}
	else {
		$('#trade-details-title').html('SELL Trade details'); 
		$("#man-buy-group").insertAfter("#man-sell-group");
	}
}
const checkTradeForm = () => {
    
   	if ($.isNumeric($('#man_quant').val()) && $.isNumeric($('#man_buy_price').val()) && $.isNumeric($('#man_sell_price').val()) && $.isNumeric($('#man_stop_price').val())) {
   		return true; 
   	}
   	return false; 

}

const checkAlgorithmForm = () => {
   	
   	if ($.isNumeric($('#upper_limit').val()) && $.isNumeric($('#lower_limit').val()) && $.isNumeric($('#multiplier').val()) && $.isNumeric($('#fraction').val())) {
   		return true; 
   	}
   	return false; 

}

const checkManualCoin = () => {
	if ($('input[name=strategy]:checked').length > 0) {
	    return true; 
	}
	else {
		return false; 
	}
}

const checkCustomCoin = () => {
	if ($('input[name=strategy_custom]:checked').length > 0) {
	    return true; 
	}
	else {
		return false; 
	}
}

let firstPageCustom = true; 
let firstPageManual = true;

$(".next").click(function(){
	if(animating) return false;
	animating = true;
		
	if (!customChosen) {	
		if (!checkManualCoin() && firstPageManual) {
			alert("Please choose a coin"); 
			animating = false; 
			return; 
		} 
		else {
			$('#final-wallet-manual').html('<h4 class = "fs-finalize-subtitle">' + $("input[name='strategy']:checked").val() + '</h4>');
		}
		
		if (!checkTradeForm() && !firstPageManual) {
			alert("Please fill out all fields with numbers");
			animating = false; 
			return; 
		}
		else {
			$('#details.fs-finalize-title').html("Trade details (" + $("input[name='which-type']:checked").val() + ")"); 
			$('#quantity.fs-finalize-subtitle').append($("#man_quant").val()); 
			$('#buy.fs-finalize-subtitle').append($("#man_buy_price").val()); 
			$('#sell.fs-finalize-subtitle').append($("#man_sell_price").val()); 
			$('#stop.fs-finalize-subtitle').append($("#man_stop_price").val()); 
		}
		
		firstPageManual = false; 
	}
	else {
		if (!checkCustomCoin() && firstPageCustom) {
			alert("Please choose at least one coin"); 
			animating = false; 
			return; 
		} 
		else {
			$('#final-wallet-custom').empty(); 
			$.each($("input[name='strategy_custom']:checked"), function(){            
                $('#final-wallet-custom').append('<h4 class = "fs-finalize-subtitle">' +  $(this).val() + '</h4>');
            });
		}
		
		if (!checkAlgorithmForm() && !firstPageCustom) {
			alert("Please fill out all fields with numbers");
			animating = false; 
			return; 
		}
		else {
            
			$('#upper.fs-finalize-subtitle').append($("#upper_limit").val()); 
			$('#lower.fs-finalize-subtitle').append($("#lower_limit").val()); 
			$('#multiplier.fs-finalize-subtitle').append($("#multiplier").val()); 
			$('#fraction.fs-finalize-subtitle').append($("#fraction").val()); 	
		}
		firstPageCustom = false; 
	}
		
	current_fs = $(this).parent();
	next_fs = $(this).parent().next();
	
	//activate next step on progressbar using the index of next_fs
	$("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
	
	//show the next fieldset
	next_fs.show(); 
	//hide the current fieldset with style
	current_fs.animate({opacity: 0}, {
		step: function(now, mx) {
			//as the opacity of current_fs reduces to 0 - stored in "now"
			//1. scale current_fs down to 80%
			scale = 1 - (1 - now) * 0.2;
			//2. bring next_fs from the right(50%)
			left = (now * 50)+"%";
			//3. increase opacity of next_fs to 1 as it moves in
			opacity = 1 - now;
			current_fs.css({
        'transform': 'scale('+scale+')',
        'position': 'absolute'
      });
			next_fs.css({'left': left, 'opacity': opacity});
		}, 
		duration: 800, 
		complete: function(){
			current_fs.hide();
			animating = false;
		}, 
		//this comes from the custom easing plugin
		easing: 'easeInOutBack'
	});
});

$(".previous").click(function(){
	firstPageCustom = true; 
	firstPageManual = true;
	
	if(animating) return false;
	animating = true;
	
	current_fs = $(this).parent();
	previous_fs = $(this).parent().prev();
		
	//de-activate current step on progressbar
	$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
	
	//show the previous fieldset
	previous_fs.show(); 
	//hide the current fieldset with style
	current_fs.animate({opacity: 0}, {
		step: function(now, mx) {
			//as the opacity of current_fs reduces to 0 - stored in "now"
			//1. scale previous_fs from 80% to 100%
			scale = 0.8 + (1 - now) * 0.2;
			//2. take current_fs to the right(50%) - from 0%
			left = ((1-now) * 50)+"%";
			//3. increase opacity of previous_fs to 1 as it moves in
			opacity = 1 - now;
			current_fs.css({'left': left});
			previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
		}, 
		duration: 800, 
		complete: function(){
			current_fs.hide();
			animating = false;
		}, 
		//this comes from the custom easing plugin
		easing: 'easeInOutBack'
	});
});

$(".submit").click(function(){
	return false;
}); 

$("#ex6").slider();
$("#ex6").on("slide", function(slideEvt) {
	$("#ex6SliderVal").text(slideEvt.value);
});





