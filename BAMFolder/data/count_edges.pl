my %h;

while(<>) {
    chomp;
    my @l=split("\t");
    $h{"$l[0]-$l[1]-$l[5]"}+=$l[4];
}

while(my ($k,$v)=each(%h)) {
    my @l=split("-",$k);
    print join("\t",@l,$v),"\n";
}
