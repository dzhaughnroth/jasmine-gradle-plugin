/*global format:true*/

(function() {
    
     var reverse = function( x ) {
	 var result = ""; 
	 var i;
	 for( i = x.length - 1; i >= 0; i-- ) {
	     result += x.charAt( i );	    
	 }
	 return result;
     };
     
     var isPalindrome = function( x ) {
	 var last = x.length - 1;
	 var i;
	 for( i = 0; i < last / 2; i++) {
	     if ( x.charAt( i ) !== x.charAt( last - i ) ) {
		 return false;
	     }
	 }
	 return true;
     };
     
     format = {
	 reverse: reverse,
	 isPalindrome: isPalindrome
     };
     
 }());

