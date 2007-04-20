package talend::runstat;

use threads;
use threads::shared;
use strict;
use Exporter;
use vars qw(@EXPORT @ISA);

use IO::Socket::INET;
use Time::HiRes qw(gettimeofday tv_interval);

@ISA = qw(Exporter);

@EXPORT = qw(
    ThreadStat
    FlushStat
    ConnectStat
    SendStat
    UpdateStat
    StartThreadStat
    StopThreadStat
);


my %_rsStart;
my %_rsStop;
my %_rsUpdate;
my %_rsLine;

share(%_rsStart);
share(%_rsStop);
share(%_rsUpdate);
share(%_rsLine);


my $__InternalStatSocket;
my $__RunStatPort = 3334;
my $__RefreshTime = 1; # en seconde

our $thread_stat;

sub StartThreadStat {
    $| = 1;
    $thread_stat = threads->create("ThreadStat","argument")
        or die "can't create thread dedicated to statistics";

    return 1;
}

sub StopThreadStat {
    UpdateStat('__MAIN', 0, 3);
    $thread_stat->join;

    return 1;
}


sub ConnectStat {
    $| = 1;

    print "Connecting to talendStudio on port $__RunStatPort...";

    $__InternalStatSocket = IO::Socket::INET->new(
        PeerAddr => '127.0.0.1',
        PeerPort => $__RunStatPort,
        Proto => 'tcp',
    );

    while (!$__InternalStatSocket) {
        print "\nconnection to port $__RunStatPort failed - try again...";
        $__InternalStatSocket = IO::Socket::INET->new(
            PeerAddr => '127.0.0.1',
            PeerPort => $__RunStatPort,
            Proto => 'tcp',
    );
  }

  print "connected.\n\n";
}


sub SendStat {
  my ($message) = @_;

  if (!$__InternalStatSocket) {
     return;
  }

  $message .=  chr(13).chr(10);
  $__InternalStatSocket->send($message);
}


sub ThreadStat {
  print "ThreadStat started...\n";

  ConnectStat;

  while (1)
  {
    foreach my $curlinkid (keys %_rsUpdate)
    {
      if (($_rsUpdate{$curlinkid} == 1) || ($_rsUpdate{$curlinkid} == 2))
      {
	my @ar1 = split(';', $_rsStart{$curlinkid});
	my @ar2;

	if ($_rsUpdate{$curlinkid} == 1) {
	  @ar2 = gettimeofday;
	}
	else {
	  @ar2 = split(';',  $_rsStop{$curlinkid});
	}



        my $duration = tv_interval \@ar1, \@ar2;

        # performance is now calculated by the server
        # my $perf = ($_rsLine{$curlinkid} / ($duration + 0.001));
        # $perf = sprintf("%.2f", $perf);

        $duration = int(1000 * $duration);
        # $duration = sprintf("%.2f", $duration);

        my $message = $curlinkid.'|'.$_rsLine{$curlinkid}.'|'.$duration;

        if ($_rsUpdate{$curlinkid} == 2) {
            $message.= '|stop';
        }
        elsif ($_rsLine{$curlinkid} == 0) {
            $message.= '|start';
        }

	SendStat($message);
	$_rsUpdate{$curlinkid} = 0;
      }
    }

    if ($_rsUpdate{'__MAIN'} == 3) {
      print "ThreadStat stoped.\n";
      last;
    }
    sleep $__RefreshTime;
  }
}



sub FlushStat {


}

# flush :
#    rien ou 1 : maj
#    2         : stop sur un lien
#    3         : flush global (cf. Main/StopProcess)

sub UpdateStat {
  my ($linkid, $linenb, $flush) = (@_);

  $flush = 1 if (($flush != 2) && ($flush != 3));
  $_rsLine{$linkid} +=  $linenb;
  $_rsUpdate{$linkid} = $flush;

  if (exists($_rsStart{$linkid})) {
    if ($flush == 2) {
      $_rsStop{$linkid} = join(';', gettimeofday)
    }
  }
  else {
    $_rsStart{$linkid} = join(';', gettimeofday);
    $_rsStop{$linkid} = $_rsStart{$linkid};
  }
}

1;
