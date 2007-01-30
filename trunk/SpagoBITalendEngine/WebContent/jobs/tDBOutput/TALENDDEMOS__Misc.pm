use Exporter;

use vars qw(@EXPORT @ISA);
@ISA = qw(Exporter);
@EXPORT = qw(
    $max
    ereplace
    filemaskToRegex
    getBooleanFromString
    mextract
    myf
    npad
    rule1
);

$max = 4000;

sub rule1 {
    my ($addr) = (@_);

    my $extr = mextract($addr);

    return $extr > $max ? 1 : 0;
}


sub myf {
    my ($reg) = (@_);

    $reg =~ m/(\d+)-(\d+)-(\d+)/;

    return "$3:$2:$1";
}

sub mextract {
    my ($addr) = (@_);

    $addr =~ m/(\d+)/;

    return $1;
}


sub npad {
    my ($num, $len) = (@_);

    return sprintf("%0" . $len . "d", $num);
}

sub ereplace {
    my ($pstring, $psubstring, $preplacement, $pnumber, $pbegin) = (@_);

    my $mresult;

    $mresult = $pstring;
    $mresult =~ s/$psubstring/$preplacement/g;

    return $mresult;
}

sub getBooleanFromString {
    my ($string) = @_;

    if (lc($string) eq 'true') {
        return 1;
    }
    else {
        return 0;
    }
}

sub filemaskToRegex {
    my ($filemask) = @_;

    my $pattern = $filemask;

    # *.log will be used as ^.*\.log$
    $pattern =~ s{\.}{\\.}g;
    $pattern =~ s{\*}{.*}g;
    $pattern =~ s{\?}{.?}g;
    $pattern = '^'.$pattern.'$';

    return $pattern;
}

1;
