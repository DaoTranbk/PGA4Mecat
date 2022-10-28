# run a single seed with a specific input 
PROG_NAME = rand-init-sbx4-local-search-mut-select10
VERSION = 1.0
PROG = $(PROG_NAME)-$(VERSION).jar
SEED = 15 
SOURCE = lnoenu_100_4_10

BASE_PATH = ./cache/aTuan/test
SOURCE = l190_4_20
pc = 0.7
pm = 0.2

run:
	java -cp target/$(PROG) hust.mso.ga.Main $(SOURCE) $(pc) $(pm) $(BASE_PATH) $(SEED)

compile: 
	mvn package

INPUT = 450 4 20
avg:
	./scripts/run_avg $(INPUT)

# run average with a specific partner
AVG_SOURCE = 500 4 20 
single: 
	./scripts/run_parallel_seed $(AVG_SOURCE)

parse:
	./scripts/parse 4

# run average with the specific q
q = 4
avg_all:
	./scripts/run_avg_all $(q)

avg_quynq:
	./scripts/run-quynq $(q)

heur:
	java -cp target/$(PROG) hust.mso.ga.Run_Heur $(SOURCE) $(SEED) $(pc) $(pm)
