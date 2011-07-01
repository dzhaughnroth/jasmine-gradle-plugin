// A typical jasmine spec; See jasmine docs

// For JSLINT
/*global beforeEach : false, describe : false, 
 expect : false, it : false, xit : false,
 format : false
 */
describe( "Format", 
          function() {
              it ( "should be defined", function() {
		       expect( format ).toBeDefined();		       
		   } );
              describe( "Reverse", function() {
			    it ( "should reverse simple strings", function() {
				     var q = "stuff";
				     expect( format.reverse( q ) ).toEqual( "ffuts" );
				 } );
			    xit ( "should handle null", function() {
				     expect( format.reverse( null ) ).toBeNull();
				     expect( format.reverse( undefined ) ).toBeUndefined();
				 } );
			} );
	      describe( "isPalindrome", function() {
			    it ( "should detect simple palindromes", function() {
				     expect( format.isPalindrome( "kayak" )).toBeTruthy();
				     expect( format.isPalindrome( "foo" )).toBeFalsy();
			    } );
			    xit( "should ignore caps and punctuation", function() {
				     expect( format.isPalindrome( "Dogma: I am God" ) )
					 .toBeTrue();
				 } );
			    xit ( "should handle null", function() { 
				 expect( format.isPalindrome( null ) ).toBeTruthy();
				 expect( format.isPalindrome( undefined ) ).toBeTruthy();
				 } );
			} );
          } );
