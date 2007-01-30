use Exporter;

use vars qw(@EXPORT @ISA);

@ISA = qw(Exporter);
@EXPORT = qw(
    printDemoRoutine
    demo2Routine
);

sub printDemoRoutine {
    print " ####################################################################\n";
    print " ########################## printDemoRoutine ########################\n";
    print " ####################################################################\n";
    print " ##\n";
    print " ## On your left, expand the node Code/Routines and edit \"demo 0.1\"\n";
    print " ##\n";
}


sub demo2Routine {
    my($inputValue) = @_;
    
    if($inputValue < 100) {
    	return "(" . $inputValue . " < 100)";
    } else {
    	return "(" . $inputValue . " >= 100)";    
    }
}

1;
