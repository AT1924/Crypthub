

<!-- MultiStep Form -->
<div style = "display: block" id = "manual-form" class="row justify-content-center">
	<h2 style = "margin-top: 40px" class = "pb-3 text-center">Manual Trade</h2>
    <div>
        <form id="msform" class = "my-5">
            <!-- progressbar -->
            <ul id="progressbar">
           		<li class="active">Choose coin</li>
                <li>Trade details</li>
                <li>Finalize</li>
            </ul>
            <!-- fieldsets -->
            <fieldset id = "first-manual">
                <h2 class="fs-title">Wallet information</h2>
                <h3 class="fs-subtitle">Choose the coin to pair with ETH</h3>
               	<div id="manual-preloader">
				  <div class="loader"></div>
				</div>
				
                <div class = "assets-container">
					  <ul style = "display: none" id="assets-strategy-manual"> </ul>
				</div>
                <input type="button" name="next" style = "display: none" class="wallet-next-btn next action-button site-btn sb-gradients" value="Next"/>
            </fieldset>      
            <fieldset>
                <h2 id = "trade-details-title" class="fs-title">BUY Trade details</h2>
                <h3 class="fs-subtitle">Let us know what you want in your trade</h3>
                
                <div id = "buy-sell-chooser">
					  <div> 
					  	  <div style = "display: inline" class="form-group mx-3">
						  	<input type = "radio" onclick="javascript:buySellToggle();" value ="Buy"  name = "which-type" style = "width: auto" id = "buy-radio" checked> <label class = "strategy-form-label" for="buy-radio">Buy</label>
						  </div>  
						  <div style = "display: inline" class="form-group mx-3">
						   <input type = "radio" onclick="javascript:buySellToggle();" value = "Sell"  name = "which-type" style = "width: auto" id = "sell-radio"> <label class = "strategy-form-label" for="sell-radio">Sell</label>  
						  </div>  
					  </div>
				</div>
				
				<div id = "man-quantity-group" class="form-group">
				    <label class = "strategy-form-label" for="man_quant">Quantity </label>
				    <a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="This is the quantity of the coin you chose early that you will buy or sell"><i class="ti-info-alt popover-icon-strategy"></i></a>
				    <input type="text" id="man_quant" name="quantity" placeholder="Enter quantity of coin you want to sell"/>
				</div>
				
				<div id = "man-buy-group" class="form-group">
					<label class = "strategy-form-label" for="man_buy_price">Buy price </label>
					<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="This is the price that we will try to buy the coin at"><i class="ti-info-alt popover-icon-strategy"></i></a>
					<input type="text" id="man_buy_price" name="buy" placeholder="Enter the price you want to buy it for"/>
				</div>
				
				<div id = "man-sell-group" class="form-group">
					<label class = "strategy-form-label" for="man_sell_price">Sell price </label>
					<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="This is the price that we will try to sell the coin at"><i class="ti-info-alt popover-icon-strategy"></i></a>
					<input type="text" id="man_sell_price" name="sell" placeholder="Enter the price you want to sell it at"/>
				</div>
					
				<div id = "man-stop-group" class="form-group">
					<label class = "strategy-form-label" for="man_stop_price">Stop price </label>
					<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="This is the price that we will stop trading at"><i class="ti-info-alt popover-icon-strategy"></i></a>
                	<input type="text" id="man_stop_price" name="stop" placeholder="Enter the price you want to give up at"/>
                </div>
                
                <input type="button" name="previous" class="previous action-button-previous site-btn sb-gradients" value="Previous"/>
                <input type="button" name="next" class="next action-button site-btn sb-gradients" value="Next"/>
            </fieldset>
            <fieldset>
                <h2 class="fs-title">Finalize trade</h2>
                <h3 class="fs-subtitle">Whenever your ready</h3>
                <div>
	                <h3 class="fs-finalize-title">Coin chosen</h3>
	                <ul id = "final-wallet-manual"> </ul>
	                
	                <h3 id = "details" class="fs-finalize-title">Trade Details</h3>
	                <h4 id = "quantity" class="fs-finalize-subtitle">Quantity: </h4>
	                <h4 id = "buy" class="fs-finalize-subtitle">Buy price: </h4>
	                <h4 id = "sell" class="fs-finalize-subtitle">Sell price: </h4>
	                <h4 id = "stop" class="fs-finalize-subtitle"> Stop price: </h4>
                </div>
                <input type="button" name="previous" class="previous action-button-previous site-btn sb-gradients" value="Previous"/>
                <input type="button" data-toggle="modal" data-target="#are-you-sure-manual" name="start-trade" class="action-button site-btn sb-gradients" value="Start trading!"/>

            </fieldset>
        </form>
    </div>
</div>
<!-- /.MultiStep Form -->

<!-- MultiStep Form -->
<div style = "display: none" id = "custom-form" class="justify-content-center">
	<h2 style = "margin-top: 40px" class = "text-center">Automatic Trade</h2>
    <div>
        <form id="msform" class = "my-5">
            <!-- progressbar -->
            <ul id="progressbar">
           		<li class="active">Choose coin</li>
                <li>Algorithm details</li>
                <li>Finalize</li>
            </ul>
            <!-- fieldsets -->
            <fieldset>
                <h2 class="fs-title">Wallet information</h2>
                <h3 class="fs-subtitle">Choose the coins you want the automatic algorithm to track</h3>
	        	<div id="custom-preloader">
				  <div class="loader"></div>
				</div>
                <div class = "assets-container">
					  <ul style = "display: none" id="assets-strategy-custom"></ul>
				</div>
                <input type="button" name="next" style = "display: none" class="next action-button site-btn sb-gradients wallet-next-btn" value="Next"/>
            </fieldset>   
            <fieldset>
                <h2 class="fs-title">Algorithm details</h2>
                <h3 class="fs-subtitle">Tell us what you want from this algorithm</h3>
                
                <div class="form-group">
                	<label class = "strategy-form-label" for="upper_limit">Upper limit (RSI)</label>
                	<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="The upper limit on the RSI is a number between 0 and 100 "><i class="ti-info-alt popover-icon-strategy"></i></a>
                	
               		<input type="text" id="upper_limit" name="upper-limit" placeholder="Enter the RSI value at which you want to sell"/>
                </div>
                
                <div class="form-group">
                	<label class = "strategy-form-label"  for="lower_limit">Lower limit (RSI) </label>
                	<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="The lower limit on the RSI is a number between 0 and 100 "><i class="ti-info-alt popover-icon-strategy"></i></a>
                	<input type="text" id="lower_limit" name="lower-limit" placeholder="Enter the RSI value at which you want to buy"/>
                </div>
                
                <div class="form-group">
                	<label class = "strategy-form-label" for="multiplier">Multiplier</label>
                	<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="The multiplier is the standard deviation"><i class="ti-info-alt popover-icon-strategy"></i></a>
               		<input type="text" id="multiplier" name="multiplier" placeholder="Enter your standard deviation multiplier"/>
                </div>
                
                <div class="form-group">
                	<label class = "strategy-form-label" for="man_stop_price">Fraction</label>
                	<a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="The fraction is how much of the coins you entered you want to sell"><i class="ti-info-alt popover-icon-strategy"></i></a>
                	<input type="text" id="fraction" name="fraction" placeholder="Enter the fraction of coins you want to sell"/>
                </div>
				
                <input type="button" name="previous" class="previous action-button-previous site-btn sb-gradients" value="Previous"/>
                <input type="button" name="next" class="next action-button site-btn sb-gradients" value="Next"/>
            </fieldset>
            <fieldset>
                <h2 class="fs-title">Finalize trade</h2>
                <h3 class="fs-subtitle">Whenever your ready</h3>
                <div>
	                <h3 class="fs-finalize-title">Coin chosen</h3>
	                <ul id = "final-wallet-custom"> </ul>
	                
	                <h3 id = "algorithm" class="fs-finalize-title">Algorithm Details</h3>
	                <h4 id = "upper" class="fs-finalize-subtitle">Upper limit: </h4>
	                <h4 id = "lower" class="fs-finalize-subtitle">Lower limit: </h4>
	                <h4 id = "multiplier" class="fs-finalize-subtitle">Multiplier: </h4>
	                <h4 id = "fraction" class="fs-finalize-subtitle">Fraction: </h4>
                </div>
                <input type="button" name="previous" class="previous action-button-previous site-btn sb-gradients" value="Previous"/>
                <input type="button" data-toggle="modal" data-target="#are-you-sure-custom" name="start-trade" class="action-button site-btn sb-gradients" value="Start trading!"/>
            </fieldset>
        </form>
    </div>
</div>
<!-- /.MultiStep Form -->

<!-- Modal -->
<div class="modal fade" id="are-you-sure-custom" tabindex="-1" role="dialog" aria-labelledby="modal-dialog-title" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modal-dialog-title">Are you sure?</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      	<p> You are about to start an automatic service that will make buy and sell orders . You cannot undo this action </p>
        <form id = "update-keys-form">
          	<button type="button" class="btn btn-secondary" data-dismiss="modal" id = "custom-submit">Yes I am!</button>
        	<button type="button" class="btn btn-primary" data-dismiss="modal" id = "not-ready-to-trade">Not yet</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Modal -->
<div class="modal fade" id="are-you-sure-manual" tabindex="-1" role="dialog" aria-labelledby="modal-dialog-title" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modal-dialog-title">Are you sure?</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      	<p> You are about to trade real money. You cannot undo this action </p>
        <form id = "update-keys-form">
          	<button type="button" class="btn btn-secondary" data-dismiss="modal" id = "manual-submit">Yes I am!</button>
        	<button type="button" class="btn btn-primary" data-dismiss="modal" id = "not-ready-to-trade">Not yet</button>
        </form>
      </div>
    </div>
  </div>
</div>

