package talend::dbOp;

use Exporter;
use Carp;

# use constant true  => 1;
# use constant false => 0;
# use constant null  => undef;

use vars qw(@EXPORT @ISA);

@ISA = qw(Exporter);

@EXPORT = qw(
    getTableCreationQuery
    getConnectionString
);

sub getTableCreationQuery {
    my %param = @_;

#     use Data::Dumper;
#     print '[getTableCreationQuery] parameters :', "\n";
#     print Dumper(\%param);

    # In $param{schema}, each column looks like this:
    #
    # {
    #     name    => 'shop_code',
    #     key     => true,
    #     type    => 'int',
    #     dbtype  => 'INTEGER',
    #     len     => null,
    #     precision => null,
    #     null    => false,
    #     default => '',
    #     comment => '',
    # }

    my $query;
    my $column_num = 1;
    my @key_names = map { $_->{name} } grep { $_->{key} } @{ $param{schema} };

    if ($param{dbtype} eq 'mysql') {
        # MySQL creation table statement example
        #
        # CREATE TABLE `sales_copy` (
        #   `shop_code` int(11) NOT NULL,
        #   `ean` char(13) NOT NULL,
        #   `sales` int(11) default NULL,
        #   `quantity` int(11) default NULL,
        #   primary key(shop_code, ean)
        # );
        $query = 'CREATE TABLE `'.$param{tablename}.'` ('."\n";

        foreach my $column_href (@{ $param{schema} }) {
            if (lc $column_href->{type} eq 'string') {
                if (not defined $column_href->{len}
                    or $column_href->{len} == -1) {
                    $column_href->{len} = 255;
                }
            }

            if ($column_num++ > 1) {
                $query.= ', ';
            }

            $query.= '`'.$column_href->{name}.'`';
            $query.= ' '.$column_href->{dbtype};

            if (defined $column_href->{len} and $column_href->{len} != -1) {
                $query.= ' (';
                $query.= $column_href->{len};

                if (grep /^$column_href->{type}$/, qw/float double/) {
                    # REAL, DOUBLE, FLOAT, DECIMAL, NUMERIC
                    $query.= ','.$column_href->{precision};
                }

                $query.= ')';
            }

            if (not $column_href->{null}) {
                $query.= ' NOT NULL';
            }

            if ($column_href->{default} != '') {
                $query.= " default '".$column_href->{default}."'";
            }

            if ($column_href->{comment} != '') {
                $query.= sprintf(" COMMENT '%s'", $column_href->{comment});
            }

            $query.= "\n";

            $column_num++;
        }

        if (@key_names) {
            $query.= sprintf(
                ", PRIMARY KEY(%s)\n",
                join(
                    ',',
                    @key_names
                )
            );
        }

        $query.= ')';
    }
    elsif ($param{dbtype} eq 'mssql') {
        # Microsoft SQL Server creation table statement example
        #
        # CREATE TABLE sales_copy (
        #   shop_code integer      NOT NULL,
        #   ean       char(13)     NOT NULL,
        #   sales     decimal(5,2) default NULL,
        #   quantity  integer      default NULL,
        #
        #   primary key(shop_code, ean)
        # );
        $query = 'CREATE TABLE '.$param{tablename}.' ('."\n";

        foreach my $column_href (@{ $param{schema} }) {
            if (lc $column_href->{type} eq 'string') {
                if (not defined $column_href->{len}
                    or $column_href->{len} == -1) {
                    $column_href->{len} = 255;
                }
            }

            if ($column_num++ > 1) {
                $query.= ', ';
            }

            $query.= ''.$column_href->{name}.'';
            $query.= ' '.$column_href->{dbtype};

            if (lc $column_href->{type} eq 'string'
                or lc $column_href->{dbtype} eq 'decimal') {
                $query.= ' (';
                $query.= $column_href->{len};

                if (lc $column_href->{dbtype} eq 'decimal') {
                    $query.= ','.$column_href->{precision};
                }

                $query.= ')';
            }

            if (not $column_href->{null}) {
                $query.= ' NOT NULL';
            }

            if ($column_href->{default} != '') {
                $query.= " DEFAULT '".$column_href->{default}."'";
            }

            $query.= "\n";

            $column_num++;
        }

        if (@key_names) {
            $query.= sprintf(
                ", PRIMARY KEY(%s)\n",
                join(
                    ',',
                    @key_names
                )
            );
        }

        $query.= ')';
    }
    elsif ($param{dbtype} eq 'postgresql') {
        # PostgreSQL creation table statement example
        #
        # CREATE TABLE sales_copy (
        #   shop_code integer NOT NULL,
        #   ean char(13) NOT NULL,
        #   sales integer default NULL,
        #   quantity integer default NULL
        #   primary key(shop_code, ean)
        # );
        $query = 'CREATE TABLE '.$param{tablename}.' ('."\n";

        foreach my $column_href (@{ $param{schema} }) {
            if (lc $column_href->{type} eq 'string') {
                if (not defined $column_href->{len}
                    or $column_href->{len} == -1) {
                    $column_href->{len} = 255;
                }
            }

            if ($column_num++ > 1) {
                $query.= ', ';
            }

            $query.= $column_href->{name};
            $query.= ' '.$column_href->{dbtype};

            if (grep /^$column_href->{type}$/, qw/String char float double/
                and defined $column_href->{len}
                and $column_href->{len} != -1
            ) {
                $query.= '(';
                $query.= $column_href->{len};

                if (grep /^$column_href->{type}$/, qw/float double/
                    and defined $column_href->{precision}
                    and $column_href->{precision} != -1
                ) {
                    $query.= ','.$column_href->{precision};
                }

                $query.= ')';
            }

            if (not $column_href->{null}) {
                $query.= ' NOT NULL';
            }

            if ($column_href->{default} ne '') {
                $query.= " default '".$column_href->{default}."'";
            }

            $query.= "\n";

            $column_num++;
        }

        if (@key_names) {
            $query.= sprintf(
                ", PRIMARY KEY(%s)\n",
                join(
                    ',',
                    @key_names
                )
            );
        }

        $query.= ')';
    }
    elsif ($param{dbtype} eq 'oracle') {
        # Oracle creation table statement example
        #
        # create table sales
        # (
        #   shop_code number(9) not null,
        #   ean       varchar2(13) not null,
        #   sales     number(5,2),
        #   quantity  number(9),
        #   constraint sales_pk primary key (shop_code, ean)
        # )
        $query = 'CREATE TABLE '.$param{tablename}.' ('."\n";

        foreach my $column_href (@{ $param{schema} }) {
            if (lc $column_href->{type} eq 'string') {
                if (not defined $column_href->{len}
                    or $column_href->{len} == -1) {
                    $column_href->{len} = 255;
                }
            }

            if ($column_num++ > 1) {
                $query.= ', ';
            }

            $query.= ''.$column_href->{name}.'';
            $query.= ' '.$column_href->{dbtype};

            if (lc $column_href->{dbtype} eq 'varchar2'
                or lc $column_href->{dbtype} eq 'number') {
                $query.= ' (';
                $query.= $column_href->{len};

                if (lc $column_href->{type} eq 'float') {
                    $query.= ','.$column_href->{precision};
                }

                $query.= ')';
            }

            if (not $column_href->{null}) {
                $query.= ' NOT NULL';
            }

            if ($column_href->{default} != '') {
                $query.= " DEFAULT '".$column_href->{default}."'";
            }

            $query.= "\n";

            $column_num++;
        }

        if (@key_names) {
            $query.= sprintf(
                ", CONSTRAINT %s_pk PRIMARY KEY(%s)\n",
                $param{tablename},
                join(
                    ',',
                    @key_names
                )
            );
        }

        $query.= ')';
    }

#     use Data::Dumper;
#     print Dumper($schema);
#     print $query; exit();

    return $query;
}

sub getConnectionString {
    my %param = @_;

    my $connectionString;

    if ($param{driver} eq 'mysql') {
         $connectionString = sprintf(
            'DBI:mysql:database=%s;host=%s;port=%s',
            $param{dbname},
            $param{dbhost},
            $param{dbport},
        );
    }
    if ($param{driver} eq 'Pg') {
         $connectionString = sprintf(
            'DBI:Pg:dbname=%s;host=%s;port=%s;',
            $param{dbname},
            $param{dbhost},
            $param{dbport},
        );
    }
    elsif ($param{driver} eq 'odbc') {
        $connectionString = sprintf(
            'DBI:ODBC:%s',
            $param{dbname},
        );
    }
    elsif ($param{driver} eq 'oracle') {
        $param{dbhost} = undef if $param{dbhost} eq '';
        $param{dbport} = undef if $param{dbport} eq '';

        if (defined $param{dbhost} and defined $param{dbport}) {
            $connectionString = sprintf(
                'dbi:Oracle:host=%s;sid=%s;port=%s',
                $param{dbhost},
                $param{dbname},
                $param{dbport},
            );
        }
        else {
            $connectionString = sprintf(
                'DBI:Oracle:%s',
                $param{dbname},
            );
        }
    }

    return $connectionString;
}

1;
