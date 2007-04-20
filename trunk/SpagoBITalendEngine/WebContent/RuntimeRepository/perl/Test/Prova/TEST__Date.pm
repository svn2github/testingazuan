use Exporter;
use Time::Local;

use vars qw(@EXPORT @ISA);
@ISA = qw(Exporter);
@EXPORT = qw(
    getDate
    getRandomDate
    datestringISOtoFr
    datestringISOtoUS
);


# getDate : return the current datetime with the given display format
#
# format : (optional) string representing the wished format of the
#          date. This string contains fixed strings and variables related
#          to the date. By default, the format string is DD/MM/CCYY. Here
#          is the list of date variables:
#
#    + CC for century
#    + YY for year
#    + MM for month
#    + DD for day
#    + hh for hour
#    + mm for minute
#    + ss for second
sub getDate {
    my ($format) = @_;
    $format = 'DD/MM/CCYY' if not defined $format;

    my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) =
        localtime(time);

    my %fields = (
        CC => int(($year + 1900) / 100),
        YY => $year % 100,
        MM => $mon + 1,
        DD => $mday,
        hh => $hour,
        mm => $min,
        ss => $sec
    );

    %fields = map {$_ => sprintf('%02u', $fields{$_})} keys %fields;

    foreach my $field (keys %fields) {
        $format =~ s/$field/$fields{$field}/g;
    }

    return $format;
}

sub getRandomDate {
    my %params = @_;

    my ($year, $month, $day);

    my $regex = '(\d{4})-(\d{2})-(\d{2})';

    ($year, $month, $day) = ($params{min} =~ m{$regex});
    my $min = timelocal(0, 0, 0, $day, $month-1, $year);

    ($year, $month, $day) = ($params{max} =~ m{$regex});
    my $max = timelocal(0, 0, 0, $day, $month-1, $year);

    my $diff = $max - $min;

    {
        my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) =
            localtime($min + int rand $diff);

        return sprintf(
            '%4u-%02u-%02u',
            $year + 1900,
            $mon + 1,
            $mday,
        );
    }
}

sub datestringISOtoFr {
    my ($datestring) = @_;

    my ($year, $month, $day) = ($datestring =~ m/(\d{4})-(\d{2})-(\d{2})/);

    return $day.'/'.$month.'/'.$year;
}

sub datestringISOtoUS {
    my ($datestring) = @_;

    my ($year, $month, $day) = ($datestring =~ m/(\d{4})-(\d{2})-(\d{2})/);

    return $month.'/'.$day.'/'.$year;
}

1;

