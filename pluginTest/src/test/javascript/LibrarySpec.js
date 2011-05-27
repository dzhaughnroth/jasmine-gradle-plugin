
describe( "Library", 
  function() {
      var two, four;
      beforeEach( function() {
		      two = mylib.makeDecimalFormatter( 2 );
		      four = mylib.makeDecimalFormatter( 4 );		      
		  } );
      it ( "should format numbers", function() {
               expect( two( 3.3 ) ).toEqual( "3.30" );
               expect( two( 3.34732 )).toEqual( "3.35" );
               expect( four( 10 )).toEqual( "10.0000" );
               expect( four( 3.34732 )).toEqual( "3.3473" );
           } );
      it ( "should deal with non numbers", function() {
                       expect( two() ).toEqual( "(nil)" );
               expect( two("sdfkj")).toEqual( "???" );
           } );
  } 
);