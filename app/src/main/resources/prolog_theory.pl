% distance(Point1Coordinates, Point2Coordinates, Distance)
distance([], [], 0).
distance([P|Ps], [Q|Qs], D) :- sum_diff_squared(Ps, Qs, Z), K is ((P-Q)**2), D is sqrt(Z+K).
% sum_diff_squared(Point1Coordinates, Point2Coordinates, DiffSumSquared)
sum_diff_squared([], [], 0).
sum_diff_squared([H1|T1], [H2|T2], Z) :- sum_diff_squared(T1, T2, Z2), Z is ((H1-H2)**2)+Z2.

% can_reach(Point1, Point2, Range)
can_reach(P1, P2, Range) :- distance(P1, P2, S), S =< Range.

% reachable_targets(Point, Range, AvailableTargets, ReachableTargets)
reachable_targets(P, Range, [], []).
reachable_targets(P, Range, [ATH|ATT], [ATH|RT]) :- can_reach(P, ATH, Range), !, reachable_targets(P, Range, ATT, RT).
% ignore non-reachable targets with cut (!)
reachable_targets(P, Range, [_|ATT], RT) :- reachable_targets(P, Range, ATT, RT).
% or use the following
% reachable_targets(P, Range, [ATH|ATT], RT) :- \+ can_reach(P, ATH, Range), reachable_targets(P, Range, ATT, RT).

% sort_targets_per_distance(Point, Ilist ,Olist)
sort_targets_per_distance(Point, [], []).
sort_targets_per_distance(Point, [X | Xs], Ys) :- sort_targets_partition(Point, Xs, X, Ls, Bs), sort_targets_per_distance(Point, Ls, LOs), sort_targets_per_distance(Point, Bs, BOs), append(LOs, [X | BOs], Ys).
% partition (Point, Ilist ,Pivot ,Littles ,Bigs)
sort_targets_partition(Point, [] , _, [], []).
sort_targets_partition(Point, [X | Xs], Y, [X | Ls], Bs):- distance(Point, X, XD), distance(Point, Y, YD), XD<YD, !, sort_targets_partition(Point, Xs, Y, Ls, Bs). sort_targets_partition(Point, [X | Xs], Y, Ls, [X | Bs]):- sort_targets_partition(Point, Xs, Y, Ls, Bs).

list_limit([LH|LT], 0, []).
list_limit([LH|LT], 1, [LH]).
list_limit([], N, []).
list_limit([X|T1],N,[X|T2]) :- N >= 0, N1 is (N-1), list_limit(T1,N1,T2), !.

reachable_targets_limit(P, Range, AT, Limit, LT) :- reachable_targets(P, Range, AT, RT), sort_targets_per_distance(P, RT, ST), list_limit(ST, Limit, LT).