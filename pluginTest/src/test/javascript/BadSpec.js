
describe( "Something in Some.js NOT ", function() {

	var something;

	beforeEach( function() {
		something = new Something();
	} );

	it ( "should fail in these cases", function() {
		something.baseValue = 12;	
		expect( something.addToBase( 4 ) ).toEqual( 15 );
		expect( something.addToBase( -3 ) ).toEqual( 10 );
	} );

	it ( "should not be the end of mathematics", function() {
		expect( 2+2 ).toEqual( 4 );
	} );

	describe( "nested spec", function() {		

		it( "Tests can fail too", function() {
			throw "Foo";
		} );
		it ( "can partailly pass", function() {
			expect( something.baseValue ).toEqual( 2 );
		} );
	} );


} );
