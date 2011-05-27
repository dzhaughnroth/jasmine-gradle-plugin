describe( "TwoStep", function() {

	var twoStep = new TwoStep();

	beforeEach( function() {
		twoStep = new TwoStep();
	});

	it ( "initially false", function() {
		expect( twoStep.started ).toBe( false );
		expect( twoStep.phaseOneDone ).toBe( false );
		expect( twoStep.phaseTwoDone ).toBe( false );
	} );

        var start;
	it( "Changes state", function() {	
		runs( function() {
			start = new Date().getTime();
                        expect( twoStep.started ).toBe( false );
			twoStep.start();
			expect( twoStep.started ).toBe( true );
		});
		waitsFor( function() { return twoStep.phaseTwoDone; }, "Timeout!", 10000 );
		runs( function() {
                          if ( twoStep.phaseOneTime - twoStep.startTime < 90 ) {
                              throw ("Phase one done too soon." + twoStep.toString() );
                          }
                          if ( twoStep.phaseTwoTime - twoStep.phaseOneTime < 90 ) {
                              throw ("Phase two done too soon." + twoStep.toString() );
                          }
                          expect( twoStep.started ).toBe( true );
                          expect( twoStep.phaseOneDone ).toBe( true );
                          expect( twoStep.phaseTwoDone ).toBe( true );
                          expect( twoStep.startTime ).toBeGreaterThan( 1000000 );
		} );
	} );
} );
