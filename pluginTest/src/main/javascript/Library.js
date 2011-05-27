var mylib;

(function() {
     lib = { };

     lib.makeDecimalFormatter = function( places ) {
	 return function( decimal ) {
	     if ( decimal ) {
		 try {
		     return decimal.toFixed( places );		     
		 } catch (x) {
		     return "???";
		 }
	     }
	     else {
		 return "(nil)";
	     }
	 };
     };

     mylib = lib;

})();
