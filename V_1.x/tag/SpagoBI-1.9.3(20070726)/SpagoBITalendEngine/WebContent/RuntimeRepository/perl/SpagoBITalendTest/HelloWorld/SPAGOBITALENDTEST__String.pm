use Exporter;

use vars qw(@EXPORT @ISA);
@ISA = qw(Exporter);
@EXPORT = qw(
    formatString
    getAsciiRandomString
    getRandomString
    getHexRandomString
);

# formatString return the input string formatted as requested. This function
# takes a hash as input. We have 4 different cases:
#
# 1. "asis => 1", the string is returned "as is"
#
# 2. the string is shorter than the requested size, "align" parameter can be
# set to 'R', 'L' or 'C' (center).
#
# 3. the string is longer than the requested size, "keep" parameter can be
# 'All', 'Right', 'Left' or 'Middle', depending on you want to keep all
# letters, only right letters, only left letters or only middle letters.
#
# 4. the length of the string is the requested size
#
# You can change the padding char with the "padding_char" parameter.
#
# Examples:
# formatString(string => 'foobar', size => 10, align => 'L');
# formatString(string => 'foobar', size => 3, keep => 'M');
# formatString(string => 'foobar', size => 20, padding_char => '0');
sub formatString {
    my %params = @_;

    my %default = (
        keep         => 'A',
        aligne       => 'L',
        padding_char => ' ',
    );

    foreach my $key (keys %default) {
        $params{$key}  = $default{$key} if not defined $params{$key};
    }

    for (qw/keep align/) {
        $params{$_} = uc substr($params{$_}, 0, 1);
    }

    my $len = length $params{string};

    if (defined $params{asis} and $params{asis}) {
        return $params{string};
    }

    if ($len < $params{size}) {
        if ($params{align} eq 'R') {
            return
                ($params{padding_char} x ($params{size} - $len))
                .$params{string}
            ;
        }

        if ($params{align} eq 'L' ) {
            return
                $params{string}
                .($params{padding_char} x ($params{size} - $len))
            ;
        }

        if ($params{align} eq 'C' ) {
            my $space = int(($params{size} - $len) / 2);

            return
                ($params{padding_char} x $space)
                .$params{string}
                .($params{padding_char} x ($params{size} - ($space + $len)))
            ;
        }
    }

    if ($len > $params{size}) {
        if ($params{keep} eq 'A') {
            return $params{string};
        }

        if ($params{keep} eq 'R') {
            return substr(
                $params{string},
                $len - $params{size},
                $params{size}
            );
        }

        if ($params{keep} eq 'L') {
            return substr($params{string}, 0, $params{size});
        }

        if ($params{keep} eq 'M') {
            my $start = int(($len - $params{size}) / 2);
            return substr($params{string}, $start, $params{size});
        }
    }

    return $params{string};
}

sub getRandomString {
    my ($length, $letters) = @_;

    my $string = '';
    for (1..$length) {
        $string.= $letters->[int rand scalar @$letters];
    }

    return $string;
}

sub getAsciiRandomString {
    my ($length) = @_;

    return getRandomString(
        $length,
        ['a'..'z', 'A'..'Z', 0..9]
    );
}

sub getHexRandomString {
    my ($length) = @_;

    return getRandomString(
        $length,
        ['a'..'f', 0..9]
    );
}

1;
