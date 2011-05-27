function Something() {

	this.baseValue = 2;

	this.addToBase = function( x ) {
		return this.baseValue + x;
	}

	this.ignored = function( ) {
		return "This should not be covered by any test.";
	}

	this.doThrow = function( x ) {
		throw x;
	}

}

log = new function() {
	var count = 0;
	var messages = [];

	this.log = function( msg ) {	
		messages.push( count + " " + new Date().getTime() + ": " +  msg );
		++count;
	}

	this.print = function() {
		alert( messages.join( "\n" ) );
	}
}

function TwoStep() {
    
    var self = this;
    this.phaseOneDone = false;
    this.phaseOneTime = 0;
    this.phaseTwoDone = false;
    this.phaseTwoTime = 0;
    this.startTime = 0;
    this.started = false;
    
    this.start = function() {
        self.startTime = new Date().getTime();
	self.started = true;
	setTimeout( function() {			
                        self.phaseOneTime = new Date().getTime();
			self.phaseOneDone = true;                       
			setTimeout( function() {
                                        self.phaseTwoTime = new Date().getTime();                 
				        self.phaseTwoDone = true;
			            }, 100 );
		    }, 100 );
    };

    this.toString = function() {
        return "S/1/2: " + this.startTime + ", " + this.phaseOneTime + ", " + this.phaseTwoTime;
    };
}