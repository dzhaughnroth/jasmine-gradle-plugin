
describe( "Something in Some.js", function() {

	var something;

	beforeEach( function() {
		something = new Something();
	} );

	it ( "should add values to the base", function() {
		something.baseValue = 12;	
		expect( something.addToBase( 4 ) ).toEqual( 16 );
		expect( something.addToBase( -3 ) ).toEqual( 9 );
		something.baseValue = 3;
		expect( something.addToBase( 4 ) ).toEqual( 7 );
		expect( something.addToBase( -3 ) ).toEqual( 0 );
	} );

	it ( "should have default value of 2", function() {
		expect( something.baseValue ).toEqual( 2 );
	} );

} );
