// A typical jasmine spec; See jasmine docs
// Save in a new file and described your tests.
// See SampleSpecRunner.html for the next step.

// A JSLINT directive.

/*global beforeEach : false, describe : false, 
 expect : false, it : false, 
 $ : false, jquery : false,
 globalnamedasdfrzqce : false
 */
describe( "Some feature", 
          function() {
              globalnamedasdfrzqce = function() { return yoo; }; // for jslint to reject in 2 ways

              var something;
              beforeEach( function() {
                              something = { foo: "bar"};
                          } );
              
              it ( "should do something", function() {
                       something.bar = "12";
                       expect( something.foo ).toEqual( "bar" );
                   } );

              it ( "should leave jquery undefined as in noConflict mode", function() {
                       expect( $ ).toBeUndefined();
                       expect( typeof( jquery ) ).toEqual( "undefined" );
                   } );

              describe( "that is broken", function() {
 
                            //Supposed to fail.              
                            it ( "should cause a test to fail if broken", function() {
                                     expect( something.bar ).toBeDefined();
                                 } );

                            //Supposed to fail.              
                            it( "should fail on exceptions", function() {
                                    throw "intentional";
                                } );
                            } );
          } );
