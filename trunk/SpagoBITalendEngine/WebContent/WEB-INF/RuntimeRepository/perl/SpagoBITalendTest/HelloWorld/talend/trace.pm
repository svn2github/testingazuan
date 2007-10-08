package talend::trace;

use strict;

use Exporter;
use vars qw(@EXPORT @ISA);
use IO::Socket::INET;

@ISA = qw(Exporter);

@EXPORT = qw(
    StartTrace
    SendTrace
);

my $__InternalTraceSocket = undef;

sub StartTrace {
    my ($port, $host) = @_;

    $| = 1;

    print "Connecting to talendStudio on port ", $port, " ...";

    while (not $__InternalTraceSocket) {
        $__InternalTraceSocket = IO::Socket::INET->new(
            PeerAddr => $host,
            PeerPort => $port,
            Proto => 'tcp',
        )
            or print "\nconnection to port ", $port, "  failed - try again...";

        sleep 1;
    }

    print "connected.\n\n";
}


sub SendTrace {
    my ($id, $line_number, $row_aref, $colnames_aref) = @_;

    our $__InternalStatSocket;

    if (not $__InternalTraceSocket) {
        print '[SendTrace] Trace socket not define yet', "\n";
        return;
    }

    my $separator = '|';

    my $message = $id.$separator;
    $message.= $line_number.$separator;

    foreach my $i (0 .. @$row_aref - 1) {
        if (defined $colnames_aref) {
            if (defined $colnames_aref->[$i]) {
                $message.= $colnames_aref->[$i].'=';
            }
        }
        $message.= $row_aref->[$i];
        $message.= $separator;
    }

    $message.= chr(13).chr(10);

    $__InternalTraceSocket->send($message);
}

1;
